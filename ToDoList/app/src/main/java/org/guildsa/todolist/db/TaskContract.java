package org.guildsa.todolist.db;

// Android ships with an embedded database called SQLite.
import android.provider.BaseColumns;

// TaskContract defines the final variables/constraints that we will use to access the data
// in the database.
public class TaskContract {
    public static final String DB_NAME = "org.guildsa.todolist.db.tasks";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
    }
}
