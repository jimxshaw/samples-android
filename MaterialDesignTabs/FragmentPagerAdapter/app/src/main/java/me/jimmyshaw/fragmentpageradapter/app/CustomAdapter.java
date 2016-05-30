package me.jimmyshaw.fragmentpageradapter.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.jimmyshaw.fragmentpageradapter.fragments.FragmentFive;
import me.jimmyshaw.fragmentpageradapter.fragments.FragmentFour;
import me.jimmyshaw.fragmentpageradapter.fragments.FragmentOne;
import me.jimmyshaw.fragmentpageradapter.fragments.FragmentSix;
import me.jimmyshaw.fragmentpageradapter.fragments.FragmentThree;
import me.jimmyshaw.fragmentpageradapter.fragments.FragmentTwo;

public class CustomAdapter extends FragmentPagerAdapter {

    private final int ITEMS = 6;

    public CustomAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentOne();
            case 1:
                return new FragmentTwo();
            case 2:
                return new FragmentThree();
            case 3:
                return new FragmentFour();
            case 4:
                return new FragmentFive();
            case 5:
                return new FragmentSix();
            default:
                return new FragmentOne();
        }
    }

    @Override
    public int getCount() {
        return ITEMS;
    }
}
