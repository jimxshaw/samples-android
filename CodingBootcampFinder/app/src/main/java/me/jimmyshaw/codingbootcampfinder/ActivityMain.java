package me.jimmyshaw.codingbootcampfinder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import me.jimmyshaw.codingbootcampfinder.fragments.FragmentMain;

public class ActivityMain extends FragmentActivity {

    FragmentMain mFragmentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment();

    }

    private void loadFragment() {
        mFragmentMain = (FragmentMain) getSupportFragmentManager()
                                        .findFragmentById(R.id.frame_layout_container_main);

        if (mFragmentMain == null) {
            mFragmentMain = mFragmentMain.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_container_main, mFragmentMain).commit();
        }
    }

}
