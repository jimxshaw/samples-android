package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class CrimeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_crime);

        // The FragmentManager is responsible for managing our fragments and adding their views to
        // the activity's view hierarchy. It handles two things: a list of fragments and a back
        // stack of fragment transactions.
        // To add a fragment to an activity in code, we make explicit calls to the
        // activity's FragmentManager.
        FragmentManager fm = getSupportFragmentManager();
        // When we retrieve the CrimeFragment from the FragmentManager, we get it by the
        // container view id.
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new CrimeFragment();
            // This code creates and commits a fragment transaction, which are used to add, remove,
            // attach, detach or replace fragments in the fragment list. They're the core of how we
            // use fragments to compose and recompose screens at runtime. The FragmentManager
            // maintains the fragment transaction back stack that we can navigate through.
            // FragmentTransaction methods that configure transactions return a transaction instead
            // of void, meaning we can chain them together. Create a new fragment transaction,
            // include one add operation in it and then commit it.
            // The add method takes a container view id and a fragment. The container view id tells
            // the FragmentManager where in the activity's view the fragment's view should appear
            // and it's used as a unique identifier for a fragment in the FragmentManager's list.
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


}
