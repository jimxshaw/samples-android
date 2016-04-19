package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// The following class has to be imported in order to let us refer to the String constants in
// CrimeDbSchema.CrimeTable by typing CrimeTable.Columns.UUID instead of the fully qualified notation.
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

// With a database schema defined, we're ready to create the database itself. We'll always follow
// a few basic steps.
// 1) Check to see if the database already exists.
// 2) If it does not, create it and create the tables and initial data it needs.
// 3) If it does, open it up and see what version of our CrimeDbSchema it has. (The logic being we
//      could add or remove things in future versions of this app.)
// 4) If it is an old version, run code to upgrade it to a newer version.

// Android's SQLiteOpenHelper handles all of the above for us.
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // It's essentially this, put our to create the initial database in onCreate and code to handle
    // any upgrades in onUpgrade and it just works.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                    CrimeTable.Columns.UUID + ", " +
                    CrimeTable.Columns.TITLE + ", " +
                    CrimeTable.Columns.DATE + ", " +
                    CrimeTable.Columns.SOLVED + ")"
                    );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
