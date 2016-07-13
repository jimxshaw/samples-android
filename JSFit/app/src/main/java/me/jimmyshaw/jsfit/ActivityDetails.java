package me.jimmyshaw.jsfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityDetails extends AppCompatActivity {

    public static final String EXTRA_EXERCISE_TYPE = "exercise_type";

    @BindView(R.id.background_details_main)
    LinearLayout mBackgroundDetailsMain;

    @BindView(R.id.text_view_exercise_type_title)
    TextView mExerciseTypeTitle;

    @BindView(R.id.image_view_exercise_type_symbol)
    ImageView mExerciseTypeSymbol;

    private String mExerciseType;

    public static Intent newIntent(Context context, String exerciseType) {
        Intent intent = new Intent(context, ActivityDetails.class);
        intent.putExtra(EXTRA_EXERCISE_TYPE, exerciseType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        mExerciseType = getIntent().getStringExtra(EXTRA_EXERCISE_TYPE);
    }
}
