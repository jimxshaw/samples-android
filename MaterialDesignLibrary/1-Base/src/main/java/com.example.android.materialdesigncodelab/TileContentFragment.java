package com.example.android.materialdesigncodelab;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TileContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        TileContentAdapter adapter = new TileContentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        // Set padding for Tiles.
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }

    public static class TileViewHolder extends RecyclerView.ViewHolder {
        public TileViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_tile, parent, false));

//            // Adding Snackbar to action button inside card.
//            Button button = (Button) itemView.findViewById(R.id.action_button);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(v, "Action is pressed", Snackbar.LENGTH_SHORT).show();
//                }
//            });
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
