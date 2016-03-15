package org.guildsa.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

// MainActivity inherits from many super classes of which one is AppCompatActivity. The highest
// super class Activity is one where all sub activity classes inherit.
public class MainActivity extends AppCompatActivity {
    // The @Override Android Studio annotation is not mandatory but is quite useful to use anyway.
    // Its purpose is to tell the compiler that we're overriding a method from a class we're
    // inheriting from. Once the compiler knows this, it can help us identify errors in our code.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // We call super.onCreate(bundleAlias) just in case the super activity class needs to do
        // some configuring prior to our activity kicking off.
        super.onCreate(savedInstanceState);
        // This sets the activity content from a layout resource, R. The resource will be inflated,
        // adding all top-level views to the activity.
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d("MainActivity", "Add a new task");
                return true;

            default:
                return false;
        }
    }
}
