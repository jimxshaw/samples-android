package me.jimmyshaw.materialdesigntabs.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import me.jimmyshaw.materialdesigntabs.R;
import me.jimmyshaw.materialdesigntabs.tabs.TabsIcons;
import me.jimmyshaw.materialdesigntabs.tabs.TabsText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private Button mBtnTabsText;
    private Button mBtnTabsIcons;
    private Button mBtnTabsScrollable;
    private Button mBtnTabsCustomViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mBtnTabsText = (Button) findViewById(R.id.btnTabsText);
        mBtnTabsIcons = (Button) findViewById(R.id.btnTabsIcons);
        mBtnTabsScrollable = (Button) findViewById(R.id.btnTabsScrollable);
        mBtnTabsCustomViews = (Button) findViewById(R.id.btnTabsCustomView);

        mBtnTabsText.setOnClickListener(this);
        mBtnTabsIcons.setOnClickListener(this);
        mBtnTabsScrollable.setOnClickListener(this);
        mBtnTabsCustomViews.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {
            case R.id.btnTabsText:
                intent = new Intent(MainActivity.this, TabsText.class);
                startActivity(intent);
                break;
            case R.id.btnTabsIcons:
                intent = new Intent(MainActivity.this, TabsIcons.class);
                startActivity(intent);
                break;
            case R.id.btnTabsScrollable:

                break;
            case R.id.btnTabsCustomView:

                break;
            default:

                break;
        }
    }
}
