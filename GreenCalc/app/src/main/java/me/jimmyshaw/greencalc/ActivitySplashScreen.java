package me.jimmyshaw.greencalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivitySplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, ActivityCalc.class);
        startActivity(intent);
        finish();
    }
}
