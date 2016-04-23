package me.jimmyshaw.applytheme;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if we're running on Android 5.0 or higher.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here.
        }
        else {
            // For API 21 or below, implement this feature without material design.
        }
    }
}
