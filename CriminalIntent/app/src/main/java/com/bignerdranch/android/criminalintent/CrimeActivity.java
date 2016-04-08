package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    // This activity's hosted CrimeFragment needs to display the data for a specific Crime when we
    // click on it in our CrimeListFragment's RecyclerView. We can tell which clicked CrimeFragment
    // to display by passing the crime ID as an Intent extra when CrimeActivity is started.
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        // After creating this explicit intent, we call putExtra and pass in a string key and the
        // value that the key maps to (the crimeId). CrimeListFragment's CrimeHolder ViewHolder
        // internal class will be able to use this Intent.
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    // Our CrimeActivity hosts a CrimeFragment.
    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }

}
