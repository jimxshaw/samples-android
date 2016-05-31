package me.jimmyshaw.designsupport;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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

    public void showSnackbarWithActionCallback(View view) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Error loading file", Snackbar.LENGTH_SHORT);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mCoordinatorLayout, "File loaded successfully!", Snackbar.LENGTH_SHORT).show();
            }
        }).show();
    }

    public void showSnackbarWithColoredText(View view) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Download failed", Snackbar.LENGTH_SHORT);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();

        // Capture the snackbar's view and then set its new background color.
        View snackbarLayout = snackbar.getView();
        snackbarLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        // Set snackbar message text color.
        TextView textViewMessage = (TextView) snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        textViewMessage.setTextColor(getResources().getColor(R.color.windowBackground));

        // Change the snackbar action button text color - method 1.
        //snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));

        // Change the snackbar action button text color - method 2.
        Button snackbarButton = (Button) snackbarLayout.findViewById(android.support.design.R.id.snackbar_action);
        snackbarButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}
