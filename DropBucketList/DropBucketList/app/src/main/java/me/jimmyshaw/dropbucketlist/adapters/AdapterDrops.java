package me.jimmyshaw.dropbucketlist.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import me.jimmyshaw.dropbucketlist.R;
import me.jimmyshaw.dropbucketlist.models.Drop;

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    // ITEM represents the position of a particular goal in the recycler view. The footer, which
    // is also part of the recycler view is always at a position one greater than the last goal's
    // position. If the final goal were at position n then the footer would be at position n+1.
    // What if there were no items to display? Then NO_ITEMS would substitute for n and the footer
    // would still tag behind it as normal.
    public static final int ITEM = 0;
    public static final int NO_ITEMS = 1;
    public static final int FOOTER = 2;

    private static final String TAG = "Jim";

    private AddListener mAddListener;
    private DetailListener mDetailListener;

    private LayoutInflater mLayoutInflater;

    private Realm mRealm;
    private RealmResults<Drop> mResults;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results) {
        mLayoutInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
    }

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener addListener, DetailListener detailListener) {
        mLayoutInflater = LayoutInflater.from(context);
        mRealm = realm;
        mAddListener = addListener;
        mDetailListener = detailListener;
        update(results);
    }

    public void update(RealmResults<Drop> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // We add a conditional to capture the null scenario to prevent null pointer exceptions.
        // Since collections always start at position 0, if the position is less than the collection size
        // then it will be a goal, otherwise it's the footer.
        if (mResults == null || position < mResults.size()) {
            return ITEM;
        }
        else {
            return FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // The viewType parameter is the int that's returned from getItemViewType. We'll use viewType
        // to inflate any number of different layouts depending on which int gets passed in.
        if (viewType == FOOTER) {
            View view = mLayoutInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view, mAddListener);
        }
        else if (viewType == NO_ITEMS) {
            View view = mLayoutInflater.inflate(R.layout.no_items, parent, false);
            return new NoItemsHolder(view);
        }
        else {
            View view = mLayoutInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view, mDetailListener);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // We pass into this method a general view holder. If the view holder is of type DropHolder
        // then get a drop object, by its position, from our collection and set the goal property
        // to the goal text view.
        // If the view holder is another type, like FooterHolder, we do nothing because this would
        // mean our collection has no drop objects to display.
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.setGoal(drop.getGoal());
            dropHolder.setDateDue(drop.getDateDue());
            // Completed goals have a different background color. The isCompleted property of each
            // drop, a boolean, will determine whether a goal is complete. The only way the isCompleted
            // property is set is through this adapter's markAsComplete method being called.
            dropHolder.setBackground(drop.isCompleted());
        }

    }

    @Override
    public int getItemCount() {
        // The results collection does not encompass the footer. We must add 1 to the collection size
        // for the recycler view to show goals and the footer. Using only the collection's size,
        // we'll just see the goals.
        // When the collection is null or empty we return 0 so that the footer won't be shown. What
        // will be shown is the main activity screen with the app logo and add button.
        if (mResults == null || mResults.isEmpty()) {
            return 0;
        }
        else {
            return mResults.size() + 1;
        }
    }

    @Override
    public void onSwipe(int position) {
        // Since deletion is a write command, we have to begin and commit a Realm transaction.
        // We add an if conditional to make sure we don't try to delete the footer.
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).deleteFromRealm();
            mRealm.commitTransaction();
            // Refreshes the data set.
            notifyItemRemoved(position);
        }
    }

    public void markAsComplete(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            // Refreshes the data set.
            notifyItemChanged(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextViewGoal;
        TextView mTextViewDateDue;
        DetailListener mDetailListener;
        Context mContext;
        View mItemView;

        public DropHolder(View itemView, DetailListener listener) {
            super(itemView);
            // We need access to the context in order to get our drawable resources and set the
            // background color of completed row items.
            mContext = itemView.getContext();
            mItemView = itemView;
            itemView.setOnClickListener(this);
            mTextViewGoal = (TextView) itemView.findViewById(R.id.text_view_goal);
            mTextViewDateDue = (TextView) itemView.findViewById(R.id.text_view_date_due);
            mDetailListener = listener;
        }

        public void setGoal(String goal) {
            mTextViewGoal.setText(goal);
        }

        public void setDateDue(long dateDue) {
            // The third parameter of getRelativeTimeSpanString is called minResolution, which is the
            // minimum about of time period that we'd like notify the user. Day is a perfect time
            // period. When a goal is due today, the text will display today.
            mTextViewDateDue.setText(DateUtils.getRelativeTimeSpanString(dateDue,
                    System.currentTimeMillis(),
                    DateUtils.DAY_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL));
        }

        public void setBackground(boolean isCompleted) {
            // Depending on whether or not the row item is complete, a different drawable will be
            // returned. The returned drawable has to be set to a view and that view is the row item's
            // view that's passed in to DropHolder's constructor every time the constructor is called.
            Drawable drawable;
            if (isCompleted) {
                drawable = ContextCompat.getDrawable(mContext, R.color.background_drop_completed);
            }
            else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.background_row_drop);
            }
            mItemView.setBackground(drawable);
        }

        @Override
        public void onClick(View view) {
            mDetailListener.onClick(getAdapterPosition());
        }


    }

    // We must have a view holder to for the scenario when the user applies a filter option but there
    // are no items to display. Without it, the user will see nothing but the main activity home
    // screen with our app symbol. With this view holder, the user will see a message that states
    // there are no items to display with the footer under that message.
    public static class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button mButtonAdd;
        AddListener mListener;

        public FooterHolder(View itemView) {
            super(itemView);

            mButtonAdd = (Button) itemView.findViewById(R.id.button_footer);
            mButtonAdd.setOnClickListener(this);
        }

        public FooterHolder(View itemView, AddListener listener) {
            super(itemView);

            mButtonAdd = (Button) itemView.findViewById(R.id.button_footer);
            mButtonAdd.setOnClickListener(this);

            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            mListener.add();
        }
    }
}
