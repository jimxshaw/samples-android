package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

// CrimeFragment is a controller that interacts with model and view objects. Its job is to present
// the details of a specific crime and update those details as the user changes them.
public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;

    // The mCrime object is the Crime retrieved by ID from within onCreate.
    private Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

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

        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);

        setHasOptionsMenu(true);
    }

    // Crime instances get modified in CrimeFragment and will need to be written out when CrimeFragment
    // finishes. This implementation of onPause will update CrimeLab's copy of our Crime.
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        else if (requestCode == REQUEST_CONTACT && data != null) {
            // We create a query that asks for all the display names of the contacts in the returned
            // data. Then we query the contacts database and get a Cursor object to work with.
            // Since we know that the cursor contains only one item, we move to the first item and
            // get it as a string. This string will be the name of the suspect and we use it to set
            // the Crime's suspect and the text of the Choose Suspect button.
            Uri contactUri = data.getData();
            // Specify which fields we want our query to return values for.
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform our query - the contactUri is like a "where" clause here.
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that we actually got results.
                if (c.getCount() == 0) {
                    return;
                }

                // Extract the first column of the first row of data - that's our suspect's name.
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }
            finally {
                c.close();
            }
        }
    }

    private void updateDate() {
        // A date without formatting looks ugly. We use the DateFormat class to format the current date
        // and then set that to the button text.
        DateFormat mDateFormat = DateFormat.getDateInstance();
        String mFormattedDate = mDateFormat.format(mCrime.getDate());
        mDateButton.setText(mFormattedDate);
    }

    private String getCrimeReport() {
        String solvedString = null;

        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        }
        else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
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
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To get a DialogFragment added to the FragmentManager and put on screen, we can call
                // show(FragmentManager, String) or show(FragmentTransaction, String). The string
                // parameter uniquely identifies the DialogFragment in the FragmentManager's list.
                // The different between the two show method is if we pass in a FragmentTransaction,
                // we are responsible for creating and committing that transaction. If we pass a
                // FragmentManager, a transaction will automatically be created and committed for us.
                FragmentManager fragmentManager = getFragmentManager();
                // We retrieve the date of the selected crime and pass it into DatePickerFragment's
                // newInstance method where it will be stored in the arguments bundle that DatePickerFragment
                // itself can access.
                DatePickerFragment datePickerDialog = DatePickerFragment.newInstance(mCrime.getDate());
                // To have CrimeFragment receive the newly selected date back from DatePickerFragment,
                // we make CrimeFragment the target fragment of DatePickerFragment. The setTargetFragment
                // method takes in a fragment and a request code.
                datePickerDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerDialog.show(fragmentManager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property based on whether or not the solved box is checked.
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Here's an implicit intent. With an implicit intent, we describe to the OS what job
//                // we want done. The OS then starts the activity that has advertised itself as
//                // capable of doing that job. If the OS finds more than one capable activity, then
//                // the user is offered choices.
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                // Sometimes the chooser will not show up because we already have a default set for
//                // an identical implicit intent or our device has only a single activity that can
//                // respond to this intent. In this case, we always want a chooser to appear so we
//                // use the createChooser method.
//                i = Intent.createChooser(i, getString(R.string.send_report));

                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .getIntent()
                        .setType("text/plain")
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                        .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));

                intent = Intent.createChooser(intent, getString(R.string.send_report));

                startActivity(intent);
            }
        });

        // Since we started the activity for a result with ACTION_PICK, we'll receive an intent via
        // onActivityResult(). This intent includes a data URI. The URI is a locator that points at
        // the single contact the user picked.
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        // The pickContact intent launches the contacts list app on the user's phone but not all phones
        // will have the contacts app. Our app will crash if the intent is issued but no contacts
        // app exists. The PackageManager knows about all components on the device. It attempts to
        // find an activity that matches the intent. If the search is successful, it returns an
        // instance of ResolveInfo stating which activity it found. If the search returns null, no
        // contacts app exists and we disable the suspect button.
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_delete_crime:
                try {
                    CrimeLab.get(getActivity()).deleteCrime(mCrime);
                    Toast.makeText(getActivity(), "Crime deleted!", Toast.LENGTH_SHORT).show();

                    //TODO: Delete crime works but only when CrimeListFragment's adapter uses notifyDataSetChanged.
                    getActivity().finish();
                }
                catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
