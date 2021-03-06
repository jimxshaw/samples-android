package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mImgPrevButton;
    private ImageButton mImgNextButton;
    private TextView mQuestionTextView;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ISCHEATER = "isCheater";

    private static final int REQUEST_CODE_CHEAT = 0;

    private Question[] mQuestionBank = new Question[] {
            // Question(int textResId, boolean isAnswerTrue, boolean wasAnswerShown)
            new Question(R.string.question_africa, false, false),
            new Question(R.string.question_americas, true, false),
            new Question(R.string.question_asia, true, false),
            new Question(R.string.question_europe, false, false),
            new Question(R.string.question_oceans, true, false)
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater = false;

    private void updateQuestion() {
        //Log.d(TAG, "Updating question text for question #" + mCurrentIndex, new Exception());
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        Question currentQuestion = mQuestionBank[mCurrentIndex];

        boolean answerIsTrue = currentQuestion.isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            currentQuestion.setAnswerShown(true);
            messageResId = R.string.judgement_toast;
        }
        else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        // Toast.makeText(Context context, int resID, int duration)
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    // If an activity is paused or stopped then its onSaveInstanceState method is called. When it's
    // called, the data is saved to the Bundle object. That Bundle object is stashed in our
    // activity's activity records by Android. When our activity is stashed, an Activity object
    // does not exist but the activity record object lives on in Android. Android can reanimate
    // the activity using the activity record when it needs to. Our activity can pass into the
    // stashed state without onDestroy being called. However, we can always rely on onPause and
    // onSaveInstanceState to be called.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_ISCHEATER, mIsCheater);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    // The Android OS calls onCreate(Bundle) method after the activity instance is created but
    // before it is put on the screen. Typically, an activity overrides onCreate to prepare the
    // specifics of its user interface:
    // - inflating widgets and putting them on the screen (in the call to setContentView(int)),
    // - getting references to inflated widgets,
    // - setting listeners on widgets to handle user interaction,
    // - connecting to external model data.

    // It's important for us to understand that we never call onCreate or any other Activity
    // lifecycle methods ourselves. We override them in our activity subclasses and Android calls
    // them at the appropriate time.

    // The @Override annotation is necessary and asks the compiler to ensure that the class
    // actually has the method that we are attempting to override. If we misspelled onCreate with
    // Oncreate, for example, the compile would catch that.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate(Bundle) called");

        // This method inflates a layout and puts it on the screen. When a layout is inflated,
        // each widget in the layout file is instantiated as defined by its attributes. We
        // specify which layout to inflate by passing in the layout's resource id.
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);

                // When we want to hear back from the child activity, we call startActivityForResult.
                // The first parameter is the intent as before. The second is the request code.
                // The request code is a user-defined integer that's sent to the child and then
                // received back by the parent. It's used when an activity starts more than one type
                // of child activity and needs to know who is reporting back. QuizActivity will only
                // ever start one type of child activity, but using a constant for the request code is
                // a best practice that will set us up well for future changes.
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        mImgPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mImgPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;

                if (mCurrentIndex < 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                }

                updateQuestion();
            }
        });

        mImgNextButton = (ImageButton) findViewById(R.id.next_button);
        mImgNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                updateQuestion();
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_ISCHEATER, false);
        }

        updateQuestion();

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

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    // By pressing the Home button, the sequence of onPause() -> onStop() is called. We're telling
    // Android we're going to check something else and might be back. Android pauses and stops our
    // activity but doesn't destroy in case we do return. Occasionally, an activity destruction
    // will occur when Android needs to reclaim memory for example.
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    // By pressing the Back button, the sequence of onPause() -> onStop() -> onDestroy() is
    // called. We've telling Android we're done with this activity and won't need it anymore.
    // Android destroys our activity as a way to conserve the device's limited resources.
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
