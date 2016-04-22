package com.example.android.materialdesigncodelab;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ListContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ListContentAdapter adapter = new ListContentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return recyclerView;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public ListViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
//
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

    public static class ListContentAdapter extends RecyclerView.Adapter<ListViewHolder> {
        // Set number of List in RecyclerView.
        private static final int LENGTH = 18;

        public ListContentAdapter() {

        }

        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            // no-op
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
