package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Our list of crimes are stored in this CrimeLab singleton. A singleton is a class that allows only
// one instance of itself to be created. A singleton exists as long as the app stays in memory, so
// storing our list in it will keep the crime data available throughout any lifecycle changes in
// our activities and fragments. Keep in mind that singletons will be destroyed when Android removes
// our app from memory. This CrimeLab singleton is not a solution for long-term storage of data but
// does allow our app to have one owner of the crime data and provides a way to easily pass that data
// between controller classes.
public class CrimeLab {
    // A singleton is usually implemented with a private constructor and a get method. If the instance
    // already exists, then the getter simply returns that instance. If the instance doesn't exist yet,
    // then the getter will call the constructor to create it.

    // Android static variables start with a lower case s.
    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        // When we call getWritableDatabase method, CrimeBaseHelper will do the following:
        // 1) Open up data/data/com.bignerdranch.android.criminalintent/databases/crimeBase.db,
        //      creating a new database file if it does not already exist.
        // 2) If this is the first time the database has been created, call onCreate(SQLiteDatabase),
        //      then save out the latest version number.
        // 3) If this is not the first time, check the version number in the database. If the version
        //      number in CrimeOpenHelper is higher, call onUpgrade(SQLiteDatabase, int, int).
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        // List is an interface that ArrayList implements from. If we ever need to switch to another
        // List implementation like LinkedList then we can do so easily.
        //mCrimes = new ArrayList<>();

//        // Pre-populate our list of crimes for testing purposes.
//        for (int i = 0; i < 100; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            // Mark every other crime as solved.
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        // A database cursor always has its finger on a particular place in a query. So to pull the data
        // out of a cursor, we move it to the first element by calling moveToFirst and then reading
        // in row data. Each time we want to advance to a new row, we call moveToNext until finally
        // isAfterLast tells us that our pointer is off the end of the dataset. Last, we close the cursor.
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Columns.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime) {
        // This code doesn't create any files on the file system. It only returns File objects that
        // point to the right locations. It does one check by verifying that there is external storage
        // to save them to. If there's no external storage, a null is returned.
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        // Insert's first argument is the table we want to insert into. The third argument is the data
        // we want to put in. The second argument is called nullColumnHack. In the event when empty
        // ContentValues wants to be inserted, normally this insert would fail, but since we input
        // null as the nullColumnHack, the empty ContentValues would be ignored and a ContentValues
        // with uuid set to null would be inserted instead.
        mDatabase.insert(CrimeTable.NAME, null, contentValues);

        //mCrimes.add(crime);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);

        // update(String table, ContentValues values, String whereClause, String[] whereArgs), the
        // third and fourth arguments are the where clause and where clause arguments as in update
        // the crime where the UUID equals the passed in UUID. The reason why we're instantiating a
        // new string array instead of directly placing the uuidString there is mitigate possible
        // SQL injection attacks because the uuidString could itself contain a query. By using a new
        // string array, then the uuidString would always be treated as a string value.
        mDatabase.update(CrimeTable.NAME, contentValues, CrimeTable.Columns.UUID + " = ? ", new String[]{uuidString});
    }

    public void deleteCrime(Crime crime) {
        String uuidString = crime.getId().toString();

        mDatabase.delete(CrimeTable.NAME, CrimeTable.Columns.UUID + " = ? ", new String[]{uuidString});
    }

    // Writes and updates to databases are done with a class called ContentValues. It's a key-value
    // store class, similar to HashMap or Bundle. However, unlike those two, ContentValues is specifically
    // designed to store the kinds of data SQLite can hold.
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();

        // We use our column names for our keys. The names are important as they specify the columns
        // that we want to insert or update. If we misspell anything, the insert or update will fail.
        // Every column is listed here except for _id, which is automatically created as a unique row ID.
        values.put(CrimeTable.Columns.UUID, crime.getId().toString());
        values.put(CrimeTable.Columns.TITLE, crime.getTitle());
        values.put(CrimeTable.Columns.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Columns.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Columns.SUSPECT, crime.getSuspect());

        return values;
    }

    // Reading in data from SQLite is done using the SQLiteDatabase.query method. It has many overloads.
    // Most of the arguments for the method are those that are typical for a select statement.
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        // The table argument is the table to query. The columns argument names which columns we want
        // values for and what order we want to receive them in. The whereClause and whereArgs operate
        // the same as what takes place in the SQLiteDatabase.update method.
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        // Cursor is the class which represents a 2 dimensional table of any database. When you
        // try to retrieve some data using the query method, then the database will first create
        // a cursor object and return its reference to you.

        return new CrimeCursorWrapper(cursor);
    }
}
