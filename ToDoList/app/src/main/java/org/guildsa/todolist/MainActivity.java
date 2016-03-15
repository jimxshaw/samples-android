package org.guildsa.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}
