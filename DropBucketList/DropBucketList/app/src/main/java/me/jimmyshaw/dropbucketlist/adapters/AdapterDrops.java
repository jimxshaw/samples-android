package me.jimmyshaw.dropbucketlist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import me.jimmyshaw.dropbucketlist.R;
import me.jimmyshaw.dropbucketlist.models.Drop;

public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

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
        // Since collections always start at position 0, our footer will always be equal to the
        // size of the collection. If the position is less than the collection size then it will
        // be a goal.
        if (mResults == null || position < mResults.size()) {
            return ITEM;
        }
        else {
            return FOOTER;
        }
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_drop, parent, false);

        DropHolder holder = new DropHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        Drop drop = mResults.get(position);
        holder.mTextViewGoal.setText(drop.getGoal());
        Log.d(TAG, "onBindViewHolder: " + position);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        TextView mTextViewGoal;

        public DropHolder(View itemView) {
            super(itemView);

            mTextViewGoal = (TextView) itemView.findViewById(R.id.text_view_goal);
        }
    }
}
