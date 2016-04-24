package me.jimmyshaw.designsupport;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Basic Components");

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"FAB has been clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showSimpleSnackbar(View view) {
        Snackbar.make(mCoordinatorLayout, "Hello from simple snackbar!", Snackbar.LENGTH_SHORT).show();
    }
}
