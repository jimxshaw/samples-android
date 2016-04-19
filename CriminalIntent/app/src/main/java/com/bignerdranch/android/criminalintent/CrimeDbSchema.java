package com.bignerdranch.android.criminalintent;

// A database schema is a way to logically group objects such as tables, views,
// stored procedures and so on. Think of a schema as a container of objects.
public class CrimeDbSchema {

    // This inner CrimeTable class only exists to define the String constants needed to describe the
    // moving pieces of our table definition.
    public static final class CrimeTable {
        // The first piece is the name of the table in our database, CrimeTable.NAME.
        public static final String NAME = "crimes";

        // The columns, which is also an inner class, are described here.
        public static final class Columns {
            // We refer to the columns by CrimeTable.Columns.TITLE.
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String Date = "date";
            public static final String SOLVED = "solved";
        }
    }
}
