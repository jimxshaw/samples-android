package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

// Within this DatePickerFragment, we'll create and configure an instance of AlertDialog that displays
// a DatePicker widget. DatePickerFragment will be hosted by CrimePagerActivity. The purpose being when
// we page through our Crime ViewPager views and click the date button of that particular Crime, a date
// widget dialog will appear to allow us to set a date.
public class DatePickerFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // We use the AlertDialog.Builder class that provides a nice interface for constructing
        // an AlertDialog instance. We pass in the Context and two AlertDialog.Builder methods to
        // configure our dialog. The setPositiveButton takes in a string constant and an on click
        // listener. A positive button is what the user should press to accept what the dialog presents
        // or to take the dialog's primary action. Negative and neutral are the other two AlertDialog
        // buttons. Finally, we call create method that returns the configured AlertDialog instance.
        return new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.date_picker_title)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .create();
    }
}
