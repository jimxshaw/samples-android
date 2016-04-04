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

// CrimeFragment is a controller that interacts with model and view objects. Its job is to present
// the details of a specific crime and update those details as the user changes them.
public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    // Fragment.onCreate is public while Activity.onCreate is protected. The reason all the Fragment
    // lifecycle methods are public is because they will be called by whatever activity is hosting
    // the fragment. Similar to an activity, a fragment has a bundle to which it saves and retrieves
    // its state. We can override the onSaveInstanceState(Bundle) method just like with an activity.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
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
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
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
