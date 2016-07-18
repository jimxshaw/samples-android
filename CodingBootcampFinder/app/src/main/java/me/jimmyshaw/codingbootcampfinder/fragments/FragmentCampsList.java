package me.jimmyshaw.codingbootcampfinder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.jimmyshaw.codingbootcampfinder.R;
import me.jimmyshaw.codingbootcampfinder.adapters.CampAdapter;
import me.jimmyshaw.codingbootcampfinder.models.Camp;
import me.jimmyshaw.codingbootcampfinder.services.DataService;

public class FragmentCampsList extends Fragment {

    private RecyclerView mRecyclerView;

    private ArrayList<Camp> mCamps;

    public FragmentCampsList() {
        // Required empty public constructor
    }

    public static FragmentCampsList newInstance() {
        FragmentCampsList fragment = new FragmentCampsList();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCamps = DataService.getInstance().getCampLocationsWithinTenMilesOfZipCode(75025);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camps_list, container, false);

        setupRecyclerView(view);

        return view;
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new CampAdapter(getActivity(), mCamps));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);


    }

}
