package me.jimmyshaw.dropbucketlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import me.jimmyshaw.dropbucketlist.adapters.AdapterDrops;
import me.jimmyshaw.dropbucketlist.adapters.AddListener;
import me.jimmyshaw.dropbucketlist.adapters.DetailListener;
import me.jimmyshaw.dropbucketlist.adapters.Divider;
import me.jimmyshaw.dropbucketlist.adapters.SimpleTouchCallback;
import me.jimmyshaw.dropbucketlist.models.Drop;
import me.jimmyshaw.dropbucketlist.widgets.DropRecyclerView;

public class ActivityMain extends AppCompatActivity {

    public static final String ARG_ROW_ITEM_POSITION = "row_item_position";

    private Toolbar mToolbar;
    private Button mButtonAdd;

    private DropRecyclerView mRecyclerView;

    private View mEmptyDropsView;

    private AdapterDrops mAdapterDrops;

    private Realm mRealm;

    private RealmResults<Drop> mResults;

    // This specific add listener is only used with the add button that's on this main activity.
    // The below add listener is used when the recycler view appears, which is a separate view
    // altogether.
    private View.OnClickListener mButtonActivityMainAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialogAdd();
        }
    };

    // Implementation of the AddListener interface's add method that will be used in conjunction
    // with our recycler view adapter.
    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    private DetailListener mDetailListener = new DetailListener() {
        @Override
        public void onClick(int position) {
            showDialogDetail(position);
        }
    };

    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            mAdapterDrops.update(mResults);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();

        mEmptyDropsView = findViewById(R.id.empty_drops);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mButtonAdd = (Button) findViewById(R.id.button_add_a_drop);
        mButtonAdd.setOnClickListener(mButtonActivityMainAddListener);

        mRecyclerView = (DropRecyclerView) findViewById(R.id.recycler_view_drops);
        // Add a divider between each row item.
        mRecyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.hideIfEmpty(mToolbar);
        mRecyclerView.showIfEmpty(mEmptyDropsView);
        mAdapterDrops = new AdapterDrops(this, mRealm, mResults, mAddListener, mDetailListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapterDrops);

        // The recycler view adapter is passed in as it implements the SwipeListener interface.
        // The callback is passed into the ItemTouchHelper, which will be attached to our recycler view.
        SimpleTouchCallback simpleTouchCallback = new SimpleTouchCallback(mAdapterDrops);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        setSupportActionBar(mToolbar);
        initBackgroundImage();

    }

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    private void showDialogDetail(int position) {
        // This method is called when the user clicks on a particular row item and wants to mark it
        // as completed in the detail dialog fragment. How does the detail fragment know which row
        // item to mark as completed? That's why we have to pass in to this method the row item's
        // position as a bundle argument. Our detail fragment will get out this bundle argument and
        // process it accordingly.
        DialogDetail dialog = new DialogDetail();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ROW_ITEM_POSITION, position);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Detail");
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
