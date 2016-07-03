package me.jimmyshaw.dropbucketlist.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.jimmyshaw.dropbucketlist.R;


// This class extends LinearLayout because our custom date picker layout xml file has the foot
// element of LinearLayout.
public class CustomDatePickerView extends LinearLayout implements View.OnTouchListener {

    private TextView mTextViewMonth;
    private TextView mTextViewDay;
    private TextView mTextViewYear;

    private Calendar mCalendar;

    private SimpleDateFormat mSimpleDateFormat;

    // These int variables represent the boundaries of our drawable text views.
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    public CustomDatePickerView(Context context) {
        super(context);
        initialize(context);
    }

    // These last two constructors are needed if we want to create our custom date picker view
    // from xml as opposed to in code.
    public CustomDatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CustomDatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        // The purpose of this initialize method is to use the LayoutInflater to inflate the specific
        // custom date picker layout and bind it to this specific custom date picker class.
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_custom_date_picker_view,
                CustomDatePickerView.this);

        // An alternative to using the default Java calendar would be the JodaTime for Android library.
        mCalendar = Calendar.getInstance();
        // We instantiate a SimpleDateFormat class in order to set how we'd like our months to be displayed.
        // The actual updating of the month format will take place in our updateCalendar method right
        // before we set the text of the month text view.
        mSimpleDateFormat = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        // This special method manages our views after the layout file as been inflated. The work
        // done in this method is considered the final phase of the overall inflation process.
        super.onFinishInflate();

        // We're allowed to use the this keyword because our class extends a LinearLayout, which
        // itself is a view.
        mTextViewMonth = (TextView) this.findViewById(R.id.text_view_month);
        mTextViewDay = (TextView) this.findViewById(R.id.text_view_day);
        mTextViewYear = (TextView) this.findViewById(R.id.text_view_year);

        // To process the user's click of the up or down button on our custom date picker, we have to
        // include an onTouchListener on each widget.
        mTextViewMonth.setOnTouchListener(this);
        mTextViewDay.setOnTouchListener(this);
        mTextViewYear.setOnTouchListener(this);

        // After initializing our widgets above, we call the updateCalendar method with the
        // date parameters passed in after getting them from the calendar member variable.
        // Since our date picker doesn't take into account the hour, minute or second, we'll pass 0s.
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        updateCalendar(month, day, year, 0, 0, 0);
    }

    private void updateCalendar(int month, int day, int year, int hour, int minute, int second) {
        // Our goal's deadline date is in MM/DD/YYYY format. We get an instance of calendar above and set
        // month, day and year with the user input values from the date picker widget. Even though
        // our app isn't exact in specifying the time, we have to set those fields to a value anyway
        // in order to pass in the date to our Drop object as time-in-milliseconds.

        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);

        mTextViewMonth.setText(mSimpleDateFormat.format(mCalendar.getTime()));
        mTextViewDay.setText(String.valueOf(day));
        mTextViewYear.setText(String.valueOf(year));

    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // The passed in view is the view that was touched but which one? That's determined by a
        // switch statement that takes in the view's id.
        switch (view.getId()) {
            case R.id.text_view_month:
                processTouchEvents(mTextViewMonth, motionEvent);
                break;
            case R.id.text_view_day:
                processTouchEvents(mTextViewDay, motionEvent);
                break;
            case R.id.text_view_year:
                processTouchEvents(mTextViewYear, motionEvent);
                break;
        }

        // The boolean specifies whether or not the touch event has been consumed. True for yes or
        // false for no.
        return true;
    }

    private void processTouchEvents(TextView textView, MotionEvent motionEvent) {
        // To contain a touch event to a particular region of the text view drawable, we must take
        // the boundaries and calculate with them accordingly.
        Drawable[] drawables = textView.getCompoundDrawables();

        if (hasTopDrawable(drawables) && hasBottomDrawable(drawables)) {
            Rect boundsTop = drawables[TOP].getBounds();
            Rect boundsBottom = drawables[BOTTOM].getBounds();

            float x = motionEvent.getX();
            float y = motionEvent.getY();

            // Which drawable region was clicked? The top or the bottom?
            // If the top drawable region was clicked, do all the motion event processing for it.
            // The same applies to the bottom drawable region. If the click was to neither region,
            // we reset everything.
            if (topDrawableClicked(x, y)) {

            }
            else if (bottomDrawableClicked(x, y)) {

            }
            else {

            }

        }

    }

    private boolean hasTopDrawable(Drawable[] drawables) {
        return drawables[TOP] != null;
    }

    private boolean topDrawableClicked(float x, float y) {
        int minX, maxX, minY, maxY;

        return false;
    }

    private boolean hasBottomDrawable(Drawable[] drawables) {
        return drawables[BOTTOM] != null;
    }

    private boolean bottomDrawableClicked(float x, float y) {
        int minX, maxX, minY, maxY;

        return false;
    }
}
