package com.example.android.grocerieDev;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class EmptyRecyclerView extends RecyclerView {
    private View mEmptyView;
    public EmptyRecyclerView(Context context) {
        super(context);
    }
    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private void initEmptyView() {
        if (mEmptyView != null) {
            if (getAdapter() == null || getAdapter().getItemCount() == 0)
            {
                mEmptyView.setVisibility(VISIBLE);
                Log.e("myTag", "empty view is visible");
            }
            else
            {
                mEmptyView.setVisibility(GONE);
                Log.e("myTag", "empty view is gone");
            }

            if (getAdapter() == null || getAdapter().getItemCount() == 0)
            {
                EmptyRecyclerView.this.setVisibility(GONE);
                Log.e("myTag", "recycler view is gone");
            }
            else
            {
                EmptyRecyclerView.this.setVisibility(VISIBLE);
                Log.e("myTag", "recycler view is visible");
            }
        }
    }
    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
//            Log.e("myTag", "/////////////Calling from onChanged");
            initEmptyView();
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            Log.e("myTag", "/////////////Calling from onItemRangeInserted");
            initEmptyView();
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            Log.e("myTag", "/////////////Calling from onItemRangeRemoved");
            initEmptyView();
        }
    };
    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        super.setAdapter(adapter);
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
        initEmptyView();
    }
}