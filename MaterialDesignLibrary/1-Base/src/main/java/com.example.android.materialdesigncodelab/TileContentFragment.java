package com.example.android.materialdesigncodelab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TileContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_tile, null);
    }

    public static class TileViewHolder extends RecyclerView.ViewHolder {
        public TileViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_tile, parent, false));
        }
    }

    public static class TileContentAdapter extends RecyclerView.Adapter<TileViewHolder> {
        // Set number of Tile in RecyclerView.
        private static final int LENGTH = 18;

        public TileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TileViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(TileViewHolder holder, int positoin) {
            // no-op
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
