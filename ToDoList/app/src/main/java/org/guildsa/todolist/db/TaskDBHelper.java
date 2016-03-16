package org.guildsa.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// The SQLiteOpenHelper class will manage database creation and version management. Our TaskDBHelper
// sub class can implement onOpen(), onCreate() and onUpgrade(), which takes care of opening the
// database if it exists, creating it if it doesn't and upgrading it as necessary.
public class TaskDBHelper extends SQLiteOpenHelper {
    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = String.format("CREATE TABLE %s (" +
                                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                        "%s TEXT)", TaskContract.TABLE, TaskContract.Columns.TASK);

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);

        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(sqlDB);
    }
}
