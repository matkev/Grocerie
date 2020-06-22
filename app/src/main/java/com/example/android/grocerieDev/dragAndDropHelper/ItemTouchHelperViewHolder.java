package com.example.android.grocerieDev.dragAndDropHelper;

import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Interface to notify an item ViewHolder of relevant callbacks from {
 * android.support.v7.widget.helper.ItemTouchHelper.Callback}.
 */


public interface ItemTouchHelperViewHolder {
    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemClear();
}
