package me.jimmyshaw.dropbucketlist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.RealmResults;
import me.jimmyshaw.dropbucketlist.R;
import me.jimmyshaw.dropbucketlist.models.Drop;

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // ITEM represents the position of a particular goal in the recycler view. The footer, which
    // is also part of the recycler view is always at a position one greater than the last goal's
    // position. If the final goal were at position n then the footer would be at position n+1.
    public static final int ITEM = 0;
    public static final int FOOTER = 1;

    private static final String TAG = "Jim";

    private LayoutInflater mLayoutInflater;

    private RealmResults<Drop> mResults;

    public AdapterDrops(Context context, RealmResults<Drop> results) {
        mLayoutInflater = LayoutInflater.from(context);
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
        // to inflater two different layouts depending on which int gets passed in.
        if (viewType == FOOTER) {
            View view = mLayoutInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view);
        }
        else {
            View view = mLayoutInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view);
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
            dropHolder.mTextViewGoal.setText(drop.getGoal());
        }

    }

    @Override
    public int getItemCount() {
        // The results collection does not encompass the footer. We must add 1 to the collection size
        // for the recycler view to show goals and the footer. Using only the collection's size,
        // we'll just see the goals.
        return mResults.size() + 1;
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        TextView mTextViewGoal;

        public DropHolder(View itemView) {
            super(itemView);

            mTextViewGoal = (TextView) itemView.findViewById(R.id.text_view_goal);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder {

        Button mButtonAdd;

        public FooterHolder(View itemView) {
            super(itemView);

            mButtonAdd = (Button) itemView.findViewById(R.id.button_footer);
        }
    }
}
