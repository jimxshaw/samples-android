package me.jimmyshaw.dropbucketlist.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleTouchCallback extends ItemTouchHelper.Callback {
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // dragFlags, swipeFlags
        // By assigning ItemTouchHelper.END, if the app uses a language that supports right-to-left
        // then the end would be right. If the app uses a right-to-left language then end is left.
        return makeMovementFlags(0, ItemTouchHelper.END);
    }

    // We don't want to drag to delete but use swipe to delete instead so we return false and true
    // for those associated methods.
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // This method has to communicate with the recycler view adapter in order to delete the row
        // item that has been swiped.

    }
}
