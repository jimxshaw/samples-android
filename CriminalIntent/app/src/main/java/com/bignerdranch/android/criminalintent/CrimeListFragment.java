package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    // The RecyclerView's only responsibilities are recycling TextViews and positioning them on the screen.
    // It works with two subclasses, Adapter and ViewHolder.
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    //private int mCrimeAdapterLastClickPosition = -1; // Not used because adapter is refreshing the entire list every time.
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The FragmentManager is responsible for calling Fragment.onCreateOptions(Menu, MenuInflater)
        // when the activity receives its onCreateOptionsMenu(...) callback from the Operating System.
        // We must explicitly tell the FragmentManager that our fragment should receive a call to
        // onCreateOptionsMenu(...). We do this by calling the following method.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        // As soon as we create the RecyclerView, we have to give it the LayoutManager object. It is
        // mandatory. If we forget to give it a LayoutManager, it will crash.
        // RecyclerView's only responsibilities are recycling TextViews and positioning them on screen.
        // It does not do the job of positioning items on the screen itself, instead delegates it to
        // to the LayoutManager. The LayoutManager handles the positioning of items and also defines
        // the scrolling behavior.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // If savedInstanceState exists, we extract the value set to our SAVED_SUBTITLE_VISIBLE key
        // and assign it to our boolean variable.
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    // The purpose of saving our instance state is to save the visibility of our crime count
    // subtitle even when the device changes orientation. 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    // The reason why we override onResume instead of onStart to update the UI is because we cannot
    // assume that our activity will be stopped when another activity is in front of it. Our activity
    // could simply be paused and if our updateUI method were in onStart then our UI will not be reloaded.
    // Within onResume is the safest place to take action to update a fragment's view.
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Menus are managed by callbacks from the Activity class. When the menu is needed, Android calls
    // the Activity onCreateOptionsMenu(menu) method. Since we're implementing a Fragment, Fragment
    // comes with its own set of menu callbacks, which we'll implement here in CrimeListFragment.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // This populates the Menu instance with the items defined in our file.
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);

        // Changes the words in the toolbar item depending on whether or not the number of crimes
        // is shown.
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    // When we click on an action item, this fragment receives a callback to the method
    // onOptionsItemSelect(MenuItem). This method receives an instance of MenuItem that describes the
    // user's selection. Menus often have more than one menu item option. We determine which action
    // item has be selected by checking the ID of the MenuItem and then respond accordingly. This ID
    // corresponds to the ID we assigned to the MenuItem in our menu file.
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_crime:
                // We create a new Crime, add it to CrimeLab and then start an instance of CrimePagerActivity
                // to edit the new Crime.
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    // This subtitle will display the number of crimes in our crimes list and updateSubtitle method
    // will set the subtitle of the toolbar.
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();

        // We generate the subtitle string using getString method, which accepts replacement values
        // for the placeholders in the string resource.
        String subtitle = getResources()
                            .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

//        if (crimeCount < 2) {
//            subtitle = getString(R.string.subtitle_format_single, crimeCount);
//        }
//        else {
//            subtitle = getString(R.string.subtitle_format_many, crimeCount);
//        }

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        // The activity that hosts CrimeListFragment must be cast to AppCompatActivity so that it can
        // have access to the toolbar. The toolbar is known as the actionbar in legacy Android.
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        // We added an if conditional to deal with potential null pointer exceptions.
        if(activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setSubtitle(subtitle);
        }

    }

    private void updateUI() {
        // Get our singleton CrimeLab that holds our list of crimes.
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        // Get that list of crimes and assign it.
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            // Instantiate our CrimeAdapter with the list of crimes passed in.
            mAdapter = new CrimeAdapter(crimes);
            // We use the CrimeAdapter with our RecyclerView
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setCrimes(crimes);
            // Using the adapter's notifyDataSetChanged method means the entire list of crimes would
            // update even though we only changed something on one particular crime. To update the UI
            // solely for the crime that was changed, use the notifyItemChanged method with the adapter
            // position. The later way is more efficient. 
            mAdapter.notifyDataSetChanged();
//            mAdapter.notifyItemChanged(mCrimeAdapterLastClickPosition);
//            mCrimeAdapterLastClickPosition = -1;
        }

        // By adding updateSubtitle to updateUI, when we create a new crime and then return to
        // CrimeListActivity with the Back button, the number of crimes in the subtitle will update.
        updateSubtitle();
    }

    // The ViewHolder's job is small. It does only one thing, which is holding on to a View.
    private class CrimeHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        // The itemView field is our ViewHolder's reason for existing: it holds a reference to the entire
        // View we passed into super(view). A RecyclerView never creates Views by themselves. It
        // always creates ViewHolders, which bring their itemViews along for the ride.
        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        // When given a crime, CrimeHolder will now update the title TextView, date TextView and solved
        // CheckBox to reflect the state of the Crime.
        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            DateFormat mDateFormat = DateFormat.getDateInstance();
            String mFormattedDate = mDateFormat.format(mCrime.getDate());
            mDateTextView.setText(mFormattedDate);
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            //mCrimeAdapterLastClickPosition = getAdapterPosition();
            // Our CrimeHolder will use the newIntent method passed in from CrimeActivity while
            // itself passing in the crime ID of the particular crime clicked from the list of crimes.
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());

            // When pressing a list item in CrimeListFragment we want to start an instance of
            // CrimePagerActivity instead of CrimeActivity.
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            // The activity that starts with this Intent is CrimePagerActivity.
            startActivity(intent);
        }
    }

    // The RecyclerView does not create ViewHolders itself. It needs an adapter. An adapter is a
    // controller object that sits between the RecyclerView and the data set that the RecyclerView
    // should display. Its responsibilities are creating the necessary ViewHolders and binding
    // ViewHolders to data from the model layer. CrimeAdapter will wrap the list of crimes we get
    // from CrimeLab.
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        // The RecyclerView calls onCreateViewHolder when it needs a new View to display an item. In
        // this method, we create the View and wrap it in a ViewHolder. The RecyclerView does not
        // expect that you will hook it up to any data yet.
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // We inflate a layout from with our custom layout called list_item_crime.
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        // The onBindViewHolder method will bind a ViewHolder's view to our model object. The parameters
        // are the ViewHolder and a position in our data set. To bind our View, we use that position
        // to find the right model data. We then update the View to reflect that model data.
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            // The position in this particular case is the index of the Crime in our array. Once we
            // extract that out, we bind that Crime to our View.
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }
}
