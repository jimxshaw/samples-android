package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

// The only difference between this abstract class and CrimeActivity is an abstract method named
// createFragment that we use to instantiate the fragment. Subclasses of SingleFragmentActivity
// will implement this method to return an instance of the fragment that the activity is hosting.
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fragment);

        // We set the activity's view to be inflated from content_fragment.xml. Then we look for the
        // fragment in the FragmentManager in that container, creating and adding it if it doesn't exist.

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
            fragment = createFragment();
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
    }
}
