package org.guildsa.permutation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText nEdit;
    private EditText rEdit;

    private TextView repeatNoText;
    private TextView repeatYesText;

    // Int field that represents the activity derived from our main activity's intent.
    private final int CALCULATION_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the views by their id from the activity_main.xml, cast them to the appropriate
        // type and assign them to the class's fields.
        nEdit = (EditText) findViewById(R.id.n);
        rEdit = (EditText) findViewById(R.id.r);
        Button btnSubmit = (Button) findViewById(R.id.Submit);
        repeatNoText = (TextView) findViewById(R.id.repeat_no);
        repeatYesText = (TextView) findViewById(R.id.repeat_yes);

        // Event listener for any clicks is attached to our submit button.
        assert btnSubmit != null;
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new intent. It takes in a context and an activity class.
                Intent calculationScreen = new Intent(getApplicationContext(), CalculationActivity.class);

                // Send data from main activity to calculation activity.
                calculationScreen.putExtra("n", nEdit.getText().toString());
                calculationScreen.putExtra("r", rEdit.getText().toString());

                // Start the calculation activity and ignore any results that derive from it.
                //startActivity(calculationScreen);

                // Start the calculation activity and capture the results that derive from it.
                startActivityForResult(calculationScreen, CALCULATION_ACTIVITY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CALCULATION_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Activity.RESULT_OK");

                    // Retrieve the data passed to us.
                    double repeatNoAnswer = data.getDoubleExtra("repeatNoAnswer", 0);
                    double repeatYesAnswer = data.getDoubleExtra("repeatYesAnswer", 0);

                    Log.d(TAG, "Answer without repetition = " + String.valueOf(repeatNoAnswer));
                    Log.d(TAG, "Answer with repetition = " + String.valueOf(repeatYesAnswer));

                    repeatNoText.append(String.valueOf(repeatNoAnswer));
                    repeatYesText.append(String.valueOf(repeatYesAnswer));
                }
                break;
        }
    }
}
