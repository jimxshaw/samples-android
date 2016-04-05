package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

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

        return view;
    }
}
