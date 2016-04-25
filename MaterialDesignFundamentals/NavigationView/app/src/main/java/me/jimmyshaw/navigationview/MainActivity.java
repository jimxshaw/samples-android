package me.jimmyshaw.navigationview;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TextView mTextViewMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTextViewMenuItem = (TextView) findViewById(R.id.textview_menu_item);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar,
        // @StringRes int openDrawerContentDescRes,
        // @StringRes int closeDrawerContentDescRes)
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(drawerToggle);
        // If the syncState method is not called then the drawer toggle icon AKA "Hamburger icon"
        // either won't synchronize with the drawer layout or the icon won't appear at all.
        drawerToggle.syncState();
    }
}
