package me.jimmyshaw.dropbucketlist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import io.realm.Realm;

public class DialogAdd extends DialogFragment {

    private ImageButton mButtonClose;
    private EditText mInputEditText;
    private DatePicker mInputDatePicker;
    private Button mButtonAdd;

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.button_add_it:
                    addAction();
                    break;
            }

            dismiss();
        }
    };

    private void addAction() {
        // Get the value of the goal or task item. Get the time of when it was added.
        String goal = mInputEditText.getText().toString();
        long time = System.currentTimeMillis();

        Realm realm = Realm.getDefaultInstance();
    }

    public DialogAdd() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButtonClose = (ImageButton) view.findViewById(R.id.button_close);
        mButtonClose.setOnClickListener(mButtonClickListener);
        mInputEditText = (EditText) view.findViewById(R.id.edit_text_drop);
        mInputDatePicker = (DatePicker) view.findViewById(R.id.date_picker_view_date);
        mButtonAdd = (Button) view.findViewById(R.id.button_add_it);
        mButtonAdd.setOnClickListener(mButtonClickListener);

    }
}
