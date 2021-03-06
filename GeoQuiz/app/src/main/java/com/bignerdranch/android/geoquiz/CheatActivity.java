package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private TextView mApiLevelTextView;
    private Button mShowAnswer;
    private boolean mIsCheater;

    private static final String TAG = "CheatActivity";
    // Every intent has an extra identified by its key. Extras' keys are placed here.
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    // This static method allows us to create an Intent properly configured with the extras CheatActivity
    // will need. The answerIsTrue argument, a boolean, is put into the intent with a private name using
    // EXTRA_ANSWER_IS_TRUE constant. Using a newIntent() method like this for our activity subclasses
    // will make it easy for other code to properly configure their launching intents.
    // We can put multiple extras on an Intent if needed. If we do, we'll add more arguments to
    // newIntent() to stay consistent with the pattern.
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    // This static method will be called by QuizActivity so that it could determine if the Show
    // Answer button in CheatActivity was clicked and then would process act accordingly.
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    // A boolean representing whether or not the Show Answer button was clicked is passed into
    // this helper method. Base on that argument, an appropriate intent will be set as the result.
    // Later, QuizActivity's will use the set result in its OnActivityResult method to do specific
    // processing.
    private boolean setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);

        return isAnswerShown;
    }

    // CheatActivity's instance state is saved because the result of whether or not the
    // Show Answer button was clicked must persist. Otherwise the user could simply switch this
    // activity's orientation and CheatActivity would reset, which would also reset Show Answer.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN, mIsCheater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        // We use getBooleanExtra to retrieve the value from the extra. The first argument is the
        // name of the extra. The second argument is a default answer if the key is not found.
        // Note that Activity.getIntent() always returns the Intent that started the activity. In
        // our case, it's what we sent when we called startActivity(intent) in QuizActivity.
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mApiLevelTextView = (TextView) findViewById(R.id.api_level_text_view);

        // In order to get the string value of an Android string resource we have to call
        // the context.getString with the resource as argument. 
        String mApiLevelText = CheatActivity.this.getString(R.string.api_level) + " " + String.valueOf(Build.VERSION.SDK_INT);

        try {
            mApiLevelTextView.setText(mApiLevelText);
        }
        catch (NullPointerException ex) {
            Log.d(TAG, "Error: " + ex);
        }

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mIsCheater = setAnswerShownResult(true);

                // Certain animation methods like createCircularReveal requires a certain Android
                // version. We'll check the device's version with an if conditional. If the device
                // meets the API level then show the newer animation, otherwise perform another action.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth() / 2;
                    int cy = mShowAnswer.getHeight() / 2;
                    float radius = mShowAnswer.getWidth();

                    // createCircularReveal(View view, int centerX,  int centerY, float startRadius, float endRadius)
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    // Add a listener on the animation so we'll know when the it completes.
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            // Set the view that will be hidden or shown based on the animation.
                            // Once the animation completes, we'll show the answer and hide the button.
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }

                    });
                    // The animation actually begins here.
                    anim.start();
                }
                else {
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (savedInstanceState != null) {
            mIsCheater = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false);
            boolean resultValue = setAnswerShownResult(mIsCheater);
            Log.d(TAG, "Is Cheater: " + resultValue);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
