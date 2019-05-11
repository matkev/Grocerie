package com.example.android.grocerie.dragAndDropHelper;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.data.IngredientContract.IngredientEntry;


/**
 * An implementation of {@link ItemTouchHelper.Callback} that enables basic drag & drop and
 * swipe-to-dismiss. Drag events are automatically started by an item long-press.<br/>
 * </br/>
 * Expects the <code>RecyclerView.Adapter</code> to react to {@link
 * ItemTouchHelperAdapter} callbacks and the <code>RecyclerView.ViewHolder</code> to implement
 * {@link ItemTouchHelperViewHolder}.
 *
 * @author Paul Burke (ipaulpro)
 */


public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{

    static Context mContext;

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, Context context) {
        mAdapter = adapter;
        mContext = context;

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        Log.e("reorder", "moving from " + source.getAdapterPosition() + " to " + target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
//        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemClear();
    }



    //my own implementation
//    public static void updateDatabase(Uri ingredientToBeUpdated, int newPosition) {
//
//        //update the table
//        // Update task_table set task_position += task_position + 1 where task_position >= newPosition;
//        // Update task_table set task_position = newPosition where taskId = taskTobeUpdated.taskId
//
//        String [] projection = {
//                IngredientEntry._ID,
//                IngredientEntry.COLUMN_INGREDIENT_NAME,
//                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
//                IngredientEntry.COLUMN_INGREDIENT_UNIT,
//                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
//                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
//                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP};
//
//
//        Cursor allIngredientsCursor = mContext.getContentResolver().query(IngredientEntry.CONTENT_URI, projection, null, null, null);
//
//        if (allIngredientsCursor.moveToFirst())
//        {
//            do {
//                int IDColumnIndex = allIngredientsCursor.getColumnIndex(IngredientEntry._ID);
//
//                // Extract out the value from the Cursor for the given column index
//                int id = allIngredientsCursor.getInt(IDColumnIndex);
//
//                if (id >= newPosition)
//                {
//                    ContentValues values = new ContentValues();
//                    values.put(IngredientEntry._ID, id + 1);
//
//                    mContext.getContentResolver().update(ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id), values, null, null);
//
//                }
//
//            } while (allIngredientsCursor.moveToNext());
//
//
//            Cursor updatedIngredientCursor = mContext.getContentResolver().query(ingredientToBeUpdated, null, null, null, null);
//
//            int IDColumnIndex = updatedIngredientCursor.getColumnIndex(IngredientEntry._ID);
//
//            // Extract out the value from the Cursor for the given column index
//            int idToUpdate = updatedIngredientCursor.getInt(IDColumnIndex);
//
//            ContentValues values = new ContentValues();
//            values.put(IngredientEntry._ID, newPosition);
//
//            mContext.getContentResolver().update(ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, idToUpdate), values, null, null);
//
//        }
//
//        mContext.getContentResolver().notifyChange(IngredientEntry.CONTENT_URI, null);
//    }

}
