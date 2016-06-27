package me.jimmyshaw.dropbucketlist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jimmyshaw.dropbucketlist.R;

public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private LayoutInflater mLayoutInflater;

    private List<String> mItems = new ArrayList<>();

    public AdapterDrops(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mItems = generateValues();
    }

    public static ArrayList<String> generateValues() {
        ArrayList<String> dummyValues = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            dummyValues.add("Item: " + i);
        }
        return dummyValues;
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_drop, parent, false);

        DropHolder holder = new DropHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        holder.mTextViewGoal.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class DropHolder extends RecyclerView.ViewHolder {

        TextView mTextViewGoal;

        public DropHolder(View itemView) {
            super(itemView);

            mTextViewGoal = (TextView) itemView.findViewById(R.id.text_view_goal);
        }
    }
}
