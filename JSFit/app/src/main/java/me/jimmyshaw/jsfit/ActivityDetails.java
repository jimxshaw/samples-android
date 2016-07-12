package me.jimmyshaw.jsfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ActivityDetails extends AppCompatActivity {

    public static final String EXTRA_EXERCISE_TYPE = "exercise_type";

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

        mExerciseType = getIntent().getStringExtra(EXTRA_EXERCISE_TYPE);
    }
}
