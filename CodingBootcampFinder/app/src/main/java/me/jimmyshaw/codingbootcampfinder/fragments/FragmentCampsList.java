package me.jimmyshaw.codingbootcampfinder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.jimmyshaw.codingbootcampfinder.R;

public class FragmentCampsList extends Fragment {

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camps_list, container, false);

        return view;
    }

}
