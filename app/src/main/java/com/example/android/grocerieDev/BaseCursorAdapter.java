package com.example.android.grocerieDev;

import android.database.Cursor;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseCursorAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIDColumn;

    public abstract void onBindViewHolder(V holder, Cursor cursor);

    public BaseCursorAdapter(Cursor c) {
        Log.e("cats", "BaseCursorAdapter constructed, calling swapCursor");
        setHasStableIds(true);
        swapCursor(c);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {

        Log.e("cats", "BaseCursorAdapter.onBindViewHolder called");

        if (!mDataValid) {
            throw new IllegalStateException("Cannot bind view holder when cursor is in invalid state.");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Could not move cursor to position " + position + " when trying to bind view holder");
        }

        onBindViewHolder(holder, mCursor);
    }

    @Override
    public int getItemCount() {
        Log.e("cats", "BaseCursorAdapter.getItemCount called");

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        Log.e("cats", "BaseCursorAdapter.getItemCount stackTrace 1: " + stackTraceElements[1]);
//        Log.e("cats", "BaseCursorAdapter.getItemCount stackTrace 1: " + stackTraceElements[1].getMethodName());
//        Log.e("cats", "BaseCursorAdapter.getItemCount stackTrace 2: " + stackTraceElements[2]);
//        Log.e("cats", "BaseCursorAdapter.getItemCount stackTrace 2: " + stackTraceElements[2].getMethodName());
        Log.e("cats", "BaseCursorAdapter.getItemCount caller method: " + stackTraceElements[3]);
        Log.e("cats", "BaseCursorAdapter.getItemCount grandpa caller method: " + stackTraceElements[4]);
        Log.e("cats", "BaseCursorAdapter.getItemCount great grandpa caller method: " + stackTraceElements[5]);
        Log.e("cats", "BaseCursorAdapter.getItemCount ggreat grandpa caller method: " + stackTraceElements[6]);
        Log.e("cats", "BaseCursorAdapter.getItemCount gggreat grandpa caller method: " + stackTraceElements[7]);
        Log.e("cats", "BaseCursorAdapter.getItemCount ggggreat grandpa caller method: " + stackTraceElements[8]);
        Log.e("cats", "BaseCursorAdapter.getItemCount gggggreat grandpa caller method: " + stackTraceElements[9]);
        Log.e("cats", "BaseCursorAdapter.getItemCount ggggggreat grandpa caller method: " + stackTraceElements[10]);



//        Log.e("cats", "BaseCursorAdapter.getItemCount caller method: " + stackTraceElements[3].getMethodName());
        if (mDataValid) {
            Log.e("cats", "BaseCursorAdapter.getItemCount data valid and count is: " + mCursor.getCount());

            Log.e("cats", "BaseCursorAdapter.getItemCount finished");

            return mCursor.getCount();
        } else {
            Log.e("cats", "BaseCursorAdapter.getItemCount no data");

            Log.e("cats", "BaseCursorAdapter.getItemCount finished");

            return 0;
        }

    }

    @Override
    public long getItemId(int position) {
        Log.e("cats", "BaseCursorAdapter.getItemId called");

        if (!mDataValid) {
            throw new IllegalStateException("Cannot lookup item id when cursor is in invalid state.");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Could not move cursor to position " + position + " when trying to get an item id");
        }

        Log.e("cats", "BaseCursorAdapter.getItemId finished");

        return mCursor.getLong(mRowIDColumn);
    }

    public Cursor getItem(int position) {
        if (!mDataValid) {
            throw new IllegalStateException("Cannot lookup item id when cursor is in invalid state.");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Could not move cursor to position " + position + " when trying to get an item id");
        }
        return mCursor;
    }

    public void swapCursor(Cursor newCursor) {
        Log.e("cats", "BaseCursorAdapter.swapCursor called");

        if (newCursor == mCursor) {
            Log.e("cats", "BaseCursorAdapter.swapCursor newCursor == oldCursor");
            if (newCursor == null)
                Log.e("cats", "They are both null");
            else {
                Log.e("cats", "They have the same data");

            }
            Log.e("cats", "BaseCursorAdapter.swapCursor finished");

            return;
        }

        if (newCursor != null) {
            Log.e("cats", "BaseCursorAdapter.swapCursor newCursor has data");
            mCursor = newCursor;
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            Log.e("cats", "BaseCursorAdapter.swapCursor newCursor is null");
            notifyItemRangeRemoved(0, getItemCount());
            mCursor = null;
            mRowIDColumn = -1;
            mDataValid = false;
        }

        Log.e("cats", "BaseCursorAdapter.swapCursor finished");
    }
}