package me.jimmyshaw.dropbucketlist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class DialogDetail extends DialogFragment {

    public static final String ARG_ROW_ITEM_POSITION = "row_item_position";

    private ImageButton mButtonClose;
    private Button mButtonCompleted;

    // The widgets on the detail dialog can have their click functions dictated by a
    // single on click listener that differentiates between the widgets by their ids.
    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_mark_completed:
                    // TODO: Handle the action to mark the item as completed.
                    break;
                default:
                    break;
            }
            dismiss();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButtonClose = (ImageButton) view.findViewById(R.id.button_close);
        mButtonClose.setOnClickListener(mButtonClickListener);
        mButtonCompleted = (Button) view.findViewById(R.id.button_mark_completed);
        mButtonCompleted.setOnClickListener(mButtonClickListener);

        // The bundle arguments that we're getting is the recycler view row item's position integer.
        // We'll use this position int to be able to mark that particular row item as completed.
        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_ROW_ITEM_POSITION);
            Toast.makeText(getActivity(), "Row item position: " + position, Toast.LENGTH_SHORT).show();
        }

    }
}
