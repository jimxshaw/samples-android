package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CrimeListFragment extends Fragment {

    // The RecyclerView's only responsibilities are recycling TextViews and positioning them on the screen.
    // It works with two subclasses, Adapter and ViewHolder.
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

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

        updateUI();

        return view;
    }

    private void updateUI() {
        // Get our singleton CrimeLab that holds our list of crimes.
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        // Get that list of crimes and assign it.
        List<Crime> crimes = crimeLab.getCrimes();

        // Instantiate our CrimeAdapter with the list of crimes passed in.
        mAdapter = new CrimeAdapter(crimes);
        // We use the CrimeAdapter with our RecyclerView
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    // The ViewHolder's job is small. It does only one thing, which is holding on to a View.
    private class CrimeHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;

        // The itemView field is our ViewHolder's reason for existing: it holds a reference to the entire
        // View we passed into super(view). A RecyclerView never creates Views by themselves. It
        // always creates ViewHolders, which bring their itemViews along for the ride.
        public CrimeHolder(View itemView) {
            super(itemView);

            // This ViewHolder currently maintains a reference to the title TextView. It expects the
            // itemView to be a TextView but if it isn't then it will crash.
            mTitleTextView = (TextView) itemView;
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
            // We inflate a layout from the Android standard library called simple_list_item_1. This
            // contains a single TextView styled to look nice.
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CrimeHolder(view);
        }

        // The onBindViewHolder method will bind a ViewHolder's view to our model object. The parameters
        // are the ViewHolder and a position in our data set. To bind our View, we use that position
        // to find the right model data. We then update the View to reflect that model data.
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            // The position in this particular case is the index of the Crime in our array. Once we
            // extract that out, we bind that Crime to our View by sending its title to our ViewHolder's
            // TextView.
            Crime crime = mCrimes.get(position);
            holder.mTitleTextView.setText(crime.getTitle());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
