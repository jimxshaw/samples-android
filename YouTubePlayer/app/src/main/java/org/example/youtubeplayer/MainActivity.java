package org.example.youtubeplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
                          implements View.OnClickListener
{

    private Button btnPlaySingle;
    private Button btnPlayStandalone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPlaySingle = (Button) findViewById(R.id.btnPlaySingle);
        btnPlayStandalone = (Button) findViewById(R.id.btnStandalone);

        btnPlaySingle.setOnClickListener(this);
        btnPlayStandalone.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        Intent intent = null;

        // MainActivity does not have any YouTube api code because our YouTubeActivity and
        // StandAloneActivity already do that. Instead of repeating ourselves, we'll simply
        // instantiate a new intent that utilize our other activities to connect to YouTube.
        switch(v.getId()) {
            case R.id.btnPlaySingle:
                // Intent(Context packageContext, Class<?> cls)
                intent = new Intent(MainActivity.this, YouTubeActivity.class);
                break;
            case R.id.btnStandalone:
                intent = new Intent(MainActivity.this, StandAloneActivity.class);
                break;
            default:
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
