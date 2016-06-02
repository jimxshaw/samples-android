package me.jimmyshaw.materialdesigntabs.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import me.jimmyshaw.materialdesigntabs.R;
import me.jimmyshaw.materialdesigntabs.fragments.FragmentOne;
import me.jimmyshaw.materialdesigntabs.fragments.FragmentThree;
import me.jimmyshaw.materialdesigntabs.fragments.FragmentTwo;

public class TabsText extends AppCompatActivity {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_text);

        initialize();

        prepareDataResource();
    }

    private void initialize() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tabs with Text Example");
    }

    private void prepareDataResource() {
        addData(new FragmentOne(), "ONE");
        addData(new FragmentTwo(), "TWO");
        addData(new FragmentThree(), "THREE");
    }

    private void addData(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }
}
