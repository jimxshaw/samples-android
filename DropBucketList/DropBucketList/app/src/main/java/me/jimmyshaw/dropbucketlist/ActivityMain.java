package me.jimmyshaw.dropbucketlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import me.jimmyshaw.dropbucketlist.adapters.AdapterDrops;
import me.jimmyshaw.dropbucketlist.models.Drop;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "Jim";

    private Toolbar mToolbar;
    private Button mButtonAdd;

    private RecyclerView mRecyclerView;

    private AdapterDrops mAdapterDrops;

    private Realm mRealm;

    private RealmResults<Drop> mResults;

    private View.OnClickListener mButtonAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialogAdd();
        }
    };

    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            Log.d(TAG, "onChange: was called");
            mAdapterDrops.update(mResults);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mButtonAdd = (Button) findViewById(R.id.button_add_a_drop);
        mButtonAdd.setOnClickListener(mButtonAddListener);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_drops);
        mAdapterDrops = new AdapterDrops(this, mResults);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapterDrops);

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

    @Override
    public void onStart() {
        super.onStart();
        mResults.addChangeListener(mRealmChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mResults.removeChangeListener(mRealmChangeListener);
    }

}
