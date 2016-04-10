package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

// We need the ability to "page" through our list of crimes when we view a particular CrimeFragment.
// Hence, we'll forgo CrimeActivity for CrimePagerActivity to host CrimeFragment.
public class CrimePagerActivity extends FragmentActivity {

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeID) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        // After finding the ViewPager in the activity's view, we get our data set from CrimeLab, the
        // list of crimes. Next, we get an instance of FragmentManager and set the adapter with
        // FragmentStatePagerAdapter that takes in said FragmentManager. FragmentStatePagerAdapter is
        // our agent managing the conversation with ViewPager. For our agent to do its job with the
        // fragments that getItem(int) returns, it needs to be able to add them to our activity. That's
        // why it needs our FragmentManager.
        // The agent is adding the fragments we return to our activity and helping ViewPager identify
        // the fragments' views so that they can be placed correctly.
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            // This getItem method fetches the Crime instance for the given position in the dataset.
            // It then uses that Crime's ID to create and return a properly configured CrimeFragment.
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });


        // If the following if conditional is not written, the ViewPager shows the first item in its
        // PagerAdapter. To show the crime list item that was selected we have to set the ViewPager's
        // current item to the index of the selected crime. We loop through each crime's ID. When we
        // find the Crime instance whose mId matches the crimeId in the intent extra, set the current
        // item to the index of that Crime.
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
