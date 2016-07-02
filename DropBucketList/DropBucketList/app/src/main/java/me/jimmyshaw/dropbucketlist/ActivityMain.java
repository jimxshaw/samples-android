package me.jimmyshaw.dropbucketlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import me.jimmyshaw.dropbucketlist.adapters.AdapterDrops;
import me.jimmyshaw.dropbucketlist.adapters.AddListener;
import me.jimmyshaw.dropbucketlist.adapters.CompleteListener;
import me.jimmyshaw.dropbucketlist.adapters.DetailListener;
import me.jimmyshaw.dropbucketlist.adapters.Divider;
import me.jimmyshaw.dropbucketlist.adapters.Filter;
import me.jimmyshaw.dropbucketlist.adapters.ResetListener;
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

    // The instance of this listener will passed into our DetailDialog when ActivityMain calls showDetailDialog.
    // The DetailDialog fragment will use this listener's implementation of onComplete in order
    // to mark the row item at its particular position as complete when the user clicks on the
    // Mark completed button inside the fragment.
    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            // Since marking a row item as complete involves hitting the Realm database, the actual
            // implementation takes place in our recycler view adapter.
            mAdapterDrops.markAsComplete(position);
        }
    };

    private ResetListener mResetListener = new ResetListener() {
        @Override
        public void onReset() {
            AppDropBucketList.saveFilterToSharePreferences(ActivityMain.this, Filter.NONE);
            updateResults(Filter.NONE);
        }
    };

    // This is a change listener that updates the database's data. The RealmResults' addChangeListener
    // is called in the activity's onStart so that data changes will be notified. RealmResults'
    // removeChangeListener must be called in the activity's onStop to prevent memory leaks. It's
    // essentially an observer pattern. Subscribe to the data stream when the activity starts and
    // unsubscribe to the stream when the activity stops.
    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            // Query results are updated in real time.
            mAdapterDrops.update(mResults);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();

        // We pull the filter from shared preferences, if any, and load a new results data set.
        int filterOption = AppDropBucketList.extractFilterFromSharedPreferences(this);
        updateResults(filterOption);

        mResults = mRealm.where(Drop.class).findAllAsync();

        mEmptyDropsView = findViewById(R.id.empty_drops);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mButtonAdd = (Button) findViewById(R.id.button_add_a_drop);
        mButtonAdd.setOnClickListener(mButtonActivityMainAddListener);

        mRecyclerView = (DropRecyclerView) findViewById(R.id.recycler_view_drops);
        // Add a divider between each row item.
        mRecyclerView.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        // Add animations to the recycler view items.
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.hideIfEmpty(mToolbar);
        mRecyclerView.showIfEmpty(mEmptyDropsView);

        mAdapterDrops = new AdapterDrops(this, mRealm, mResults, mAddListener, mDetailListener, mResetListener);
        // This setHasStableIds method is for optimization. It saysing when we provide a view holder,
        // its id is unique and will not change.
        mAdapterDrops.setHasStableIds(true);

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
        // We pass in the CompleteListener and its implemented onComplete method to DialogDetail so
        // that when the user clicks the Mark completed button, the row item at its position can
        // be marked as complete.
        dialog.setCompleteListener(mCompleteListener);
        dialog.show(getSupportFragmentManager(), "Detail");
    }

    private void initBackgroundImage() {
        ImageView imageView = (ImageView) findViewById(R.id.image_view_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(imageView);

    }

    public void updateResults(int filterOption) {
        // A few things are happening here. First, we have to sort asynchronously or the
        // operation would run on the UI thread and tie up the app. Second, we have to assign the
        // returned results back to mResults or otherwise all our recycler view adapter operations
        // would be using old, unsorted data. Third, previously the only other location where
        // we notified our results data set changed is by calling addChangeListener in this
        // activity's onStart but that's not enough. We have to notify immediately when our
        // results changed so the UI can be updated accordingly. Thus we call addChangeListener
        // after when a menu item action takes place.
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_REMAINING:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("dateDue");
                break;
            case Filter.MOST_TIME_REMAINING:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("dateDue", Sort.DESCENDING);
                break;
            case Filter.COMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
        }
        mResults.addChangeListener(mRealmChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // The menu will only appear only if this method returns true, otherwise no menu will be shown.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This method's boolean determines who handles the menu item's action. True means our code,
        // the developer, will handle the action. False means Android will handle the action.
        // We'll assume the item action was handled successfully but if not then the default switch
        // case will return false.
        boolean actionHandled = true;
        int filterOption = Filter.NONE;

        switch (item.getItemId()) {
            case R.id.action_add:
                showDialogAdd();
                break;
            case R.id.action_sort_ascending_date:
                filterOption = Filter.LEAST_TIME_REMAINING;
                break;
            case R.id.action_sort_descending_date:
                filterOption = Filter.MOST_TIME_REMAINING;
                break;
            case R.id.action_complete:
                filterOption = Filter.COMPLETE;
                break;
            case R.id.action_incomplete:
                filterOption = Filter.INCOMPLETE;
                break;
            case R.id.action_none:
                filterOption = Filter.NONE;
            default:
                actionHandled = false;
                break;
        }

        // We update our results data set according to which menu item action was clicked. The default
        // filter is none.
        updateResults(filterOption);
        AppDropBucketList.saveFilterToSharePreferences(this, filterOption);
        return actionHandled;
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
