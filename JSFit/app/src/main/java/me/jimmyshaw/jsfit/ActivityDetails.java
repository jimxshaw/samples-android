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

        updateUI(mExerciseType);
    }

    private void updateUI(String extra) {

        switch (extra) {
            case "Anaerobic":
                mBackgroundDetailsMain.setBackgroundColor(getResources().getColor(R.color.backgroundExertciseAnaerobic));
                mExerciseTypeTitle.setText(R.string.exercise_anaerobic);
                mExerciseTypeSymbol.setImageResource(R.drawable.lift);
                break;
            case "Aerobic":
                mBackgroundDetailsMain.setBackgroundColor(getResources().getColor(R.color.backgroundExerciseAerobic));
                mExerciseTypeTitle.setText(R.string.exercise_aerobic);
                mExerciseTypeSymbol.setImageResource(R.drawable.cardio);
                break;
            case "Other":
                mBackgroundDetailsMain.setBackgroundColor(getResources().getColor(R.color.backgroundExerciseOther));
                mExerciseTypeTitle.setText(R.string.exercise_other);
                mExerciseTypeSymbol.setImageResource(R.drawable.flower);
                break;
        }

    }
}
