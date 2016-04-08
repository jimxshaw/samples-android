package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.UUID;

// CrimeFragment is a controller that interacts with model and view objects. Its job is to present
// the details of a specific crime and update those details as the user changes them.
public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";

    // The mCrime object is the Crime retrieved by ID from within onCreate.
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    // Every fragment instance can have a Bundle object attached to it. This bundle contains key-value
    // pairs that work just like the intent of an Activity. Each pair is known as an argument.
    // To attach the arguments bundle to a fragment, we call Fragment.setArguments(Bundle). Attaching
    // arguments to a fragment must be done after the fragment is created but before it is added to
    // an activity. To hit this narrow window, we add a static method called newInstance to the
    // Fragment class. When the hosting activity needs an instance of this fragment, we have it call
    // newInstance rather than calling the constructor directly. The activity can pass in any required
    // parameters to newInstance that the fragment needs to create its arguments.
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Fragment.onCreate is public while Activity.onCreate is protected. The reason all the Fragment
    // lifecycle methods are public is because they will be called by whatever activity is hosting
    // the fragment. Similar to an activity, a fragment has a bundle to which it saves and retrieves
    // its state. We can override the onSaveInstanceState(Bundle) method just like with an activity.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Our CrimeFragment uses the getArguments method to get the crime ID passed into it by
        // CrimeActivity.
        // After retrieving the ID, we use it to fetch that particular Crime from CrimeLab.
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    // Note that we do not inflate the fragment's view in onCreate. We create and configure the view
    // with onCreateView. The fragment view's layout is inflated and the view is returned to the
    // hosting activity. LayoutInflater and ViewGroup are needed to inflate the layout. The Bundle
    // contains data that onCreateView can use to recreate the view from a saved state.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // We explicitly inflate the fragment's view by calling LayoutInflater.inflate and passing in
        // the layout resource id. The second parameter is our view's parent, which is needed to
        // configure the widgets properly. The third parameter tells the layout inflater whether to
        // add the inflated view to the view's parent. We passed false because we will add the view
        // in the activity's code.
        // inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        // Finding the view and then casting works almost exactly the same within a fragment as
        // within an activity except here we have to fully qualify the code with View.findViewById(int).
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space is intentionally left blank.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // We call toString on the user input's character sequence and then we set the string
                // as the Crime's title.
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space is intentionally left blank.
            }
        });

        // Grab the crime date button by id, cast it, assign it to a variable and then set the text with
        // the date of the crime, which defaults to the current date. The button is disabled for now.
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        // A date without formatting looks ugly. We use the DateFormat class to format the current date
        // and then set that to the button text.
        DateFormat mDateFormat = DateFormat.getDateInstance();
        String mFormattedDate = mDateFormat.format(mCrime.getDate());
        mDateButton.setText(mFormattedDate);
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property based on whether or not the solved box is checked.
                mCrime.setSolved(isChecked);
            }
        });


        return v;
    }



}
