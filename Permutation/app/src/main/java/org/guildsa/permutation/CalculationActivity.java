package org.guildsa.permutation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CalculationActivity extends AppCompatActivity {
    // Declare a few private fields to hold our data.
    private String nString;
    private String rString;

    // This is called when the activity is first created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        TextView nText = (TextView) findViewById(R.id.n);
        TextView rText = (TextView) findViewById(R.id.r);
        Button btnCalculate = (Button) findViewById(R.id.calculate);

        Intent intent = getIntent();

        // Get the n and r data passed to CalculateActivity from MainActivity.
        nString = intent.getStringExtra("n");
        rString = intent.getStringExtra("r");

        // Display the n and r data the user entered from MainActivity.
        nText.append(nString);
        rText.append(rString);

        // Bind an on click listener to our calculate button.
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Parse the n and r, which are currently string, to double.
                double nDouble = Double.parseDouble(nString);
                double rDouble = Double.parseDouble(rString);

                // Call the two permutation methods with our n and r values that have been converted
                // to double type.
                double repeatNoAnswer = withoutRepetition(nDouble, rDouble);
                double repeatYesAnswer = withRepetition(nDouble, rDouble);

                // Package up the calculated data and set a result to send back to
                // MainActivity that called us and close this CalculateActivity.
                Intent resultIntent = new Intent();

                resultIntent.putExtra("repeatNoAnswer", repeatNoAnswer);
                resultIntent.putExtra("repeatYesAnswer", repeatYesAnswer);

                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }
        });

    }

    private double withoutRepetition(double n, double r) {
        return factorial(n)/(factorial(n - r));
    }

    private double withRepetition(double n, double r) {
        return Math.pow(n, r);
    }

    private double factorial(double n) {
        double nRounded = Math.round(n);

        if (nRounded <= 1) {
            return 1;
        }
        else {
            return n * factorial(n - 1);
        }
    }

}
