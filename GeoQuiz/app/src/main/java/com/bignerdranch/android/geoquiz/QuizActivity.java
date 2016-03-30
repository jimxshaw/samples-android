package com.bignerdranch.android.geoquiz;

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
    private ImageButton mImgPrevButton;
    private ImageButton mImgNextButton;
    private TextView mQuestionTextView;
    private static final String TAG = "QuizActivity";

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_europe, false),
            new Question(R.string.question_oceans, true)
    };

    private int mCurrentIndex = 0;

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        }
        else {
            messageResId = R.string.incorrect_toast;
        }

        // Toast.makeText(Context context, int resID, int duration)
        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
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
        // specify wich layout to inflate by passing in the layout's resource id.
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
