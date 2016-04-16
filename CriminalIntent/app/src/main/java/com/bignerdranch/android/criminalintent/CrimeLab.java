package com.bignerdranch.android.criminalintent;

import android.content.Context;

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
    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        // List is an interface that ArrayList implements from. If we ever need to switch to another
        // List implementation like LinkedList then we can do so easily.
        mCrimes = new ArrayList<>();

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
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime) {
        mCrimes.remove(crime);
    }
}
