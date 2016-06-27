package me.jimmyshaw.dropbucketlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import me.jimmyshaw.dropbucketlist.adapters.AdapterDrops;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "Add";

    private Toolbar mToolbar;
    private Button mButtonAdd;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mButtonAdd = (Button) findViewById(R.id.button_add_a_drop);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_drops);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new AdapterDrops(this));

        initBackgroundImage();

    }

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), TAG);
    }

    private void initBackgroundImage() {
        ImageView imageView = (ImageView) findViewById(R.id.image_view_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(imageView);

    }

}
