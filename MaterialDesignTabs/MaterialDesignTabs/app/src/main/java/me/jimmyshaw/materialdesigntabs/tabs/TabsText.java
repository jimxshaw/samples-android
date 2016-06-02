package me.jimmyshaw.materialdesigntabs.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import me.jimmyshaw.materialdesigntabs.R;
import me.jimmyshaw.materialdesigntabs.adapters.TabsTextAdapter;
import me.jimmyshaw.materialdesigntabs.fragments.FragmentOne;
import me.jimmyshaw.materialdesigntabs.fragments.FragmentThree;
import me.jimmyshaw.materialdesigntabs.fragments.FragmentTwo;

public class TabsText extends AppCompatActivity {

    private ViewPager mViewPager;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_text);

        initialize();

        prepareDataResource();

        TabsTextAdapter adapter = new TabsTextAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);

        mViewPager.setAdapter(adapter);
    }

    private void initialize() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tabs with Text Example");

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void prepareDataResource() {
        addData(new FragmentOne(), "ONE");
        addData(new FragmentTwo(), "TWO");
        addData(new FragmentThree(), "THREE");
    }

    private void addData(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mTitleList.add(title);
    }
}
