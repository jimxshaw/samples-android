package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

// Within this DatePickerFragment, we'll create and configure an instance of AlertDialog that displays
// a DatePicker widget. DatePickerFragment will be hosted by CrimePagerActivity. The purpose being when
// we page through our Crime ViewPager views and click the date button of that particular Crime, a date
// widget dialog will appear to allow us to set a date.
public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;

    // CrimeFragment and DatePickerFragment need to pass data between each other. CrimeFragment passes
    // DatePickerFragment the selected crime's date, the user picks a new date, the new date passes
    // back to CrimeFragment, which calls the selected crime's setDate method with the new date
    // passed in. To get data into DatePickerFragment, we are going to store the date in DatePickerFragment's
    // arguments bundle. Creating and setting fragment arguments is usually done in a newInstance method
    // that replaces teh fragment constructor.
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get the date object that's been placed in this DatePickerFragment's bundle by CrimeFragment.
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // The dialog_date layout is a DatePicker widget. We inflate it and assign it to a view.
        // Then we call setView with our AlertDialog.Builder object and pass in said view. This
        // view should appear between the AlertDialog title and the AlertDialog button(s).
        View v = LayoutInflater.from(getActivity())
                                .inflate(R.layout.dialog_date, null);

        // DatePickerFragment needs to initialize the DatePicker using the information held in the Date.
        // DatePicker needs the month, day and year as integers and each has to be retrieved individually,
        // which involves the Calendar object. We use the Date to configure the Calendar. Finally, we
        // retrieve the integers from the configured Calendar and use them as arguments to
        // initialize DatePicker.
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);

        // We use the AlertDialog.Builder class that provides a nice interface for constructing
        // an AlertDialog instance. We pass in the Context and two AlertDialog.Builder methods to
        // configure our dialog. The setPositiveButton takes in a string constant and an on click
        // listener. A positive button is what the user should press to accept what the dialog presents
        // or to take the dialog's primary action. Negative and neutral are the other two AlertDialog
        // buttons. Finally, we call create method that returns the configured AlertDialog instance.
        return new AlertDialog.Builder(getActivity())
                                        .setView(v)
                                        .setTitle(R.string.date_picker_title)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .create();
    }
}
