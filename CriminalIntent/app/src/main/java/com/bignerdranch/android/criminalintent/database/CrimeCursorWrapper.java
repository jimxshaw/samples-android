package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

// A cursor gives raw column data. We extract the data with methods like getString, getLong, getInt.
// Every time we pull a Crime out of a cursor, we need to repeat these methods and that doesn't even
// include the code to create a Crime instance with those values. That's why we have this CursorWrapper
// class to follow the Don't Repeat Yourself (DRY) principle. Also, this class allows us to potentially
// add new methods that operate on the underlying cursor.
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // This getCrime method pulls out relevant column data.
    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Columns.UUID));
        String title = getString(getColumnIndex(CrimeTable.Columns.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Columns.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Columns.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Columns.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }
}
