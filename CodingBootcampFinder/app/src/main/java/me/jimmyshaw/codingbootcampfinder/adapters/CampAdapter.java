package me.jimmyshaw.codingbootcampfinder.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.jimmyshaw.codingbootcampfinder.models.Camp;

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.CampHolder> {

    private ArrayList<Camp> mCamps;

    public CampAdapter(ArrayList<Camp> camps) {
        mCamps = camps;
    }

    @Override
    public CampHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CampHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mCamps.size();
    }

    public class CampHolder extends RecyclerView.ViewHolder {

        public CampHolder(View itemView) {
            super(itemView);
        }
    }
}
