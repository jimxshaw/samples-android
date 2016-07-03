package me.jimmyshaw.dropbucketlist.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import me.jimmyshaw.dropbucketlist.R;


// This class extends LinearLayout because our custom date picker layout xml file has the foot
// element of LinearLayout.
public class CustomDatePickerView extends LinearLayout {

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
    }

}
