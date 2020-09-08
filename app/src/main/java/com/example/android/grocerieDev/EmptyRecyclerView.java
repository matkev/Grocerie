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
        Log.e("cats", "EmptyRecyclerView.initEmptyView called");
        if (mEmptyView != null) {
            if (getAdapter() == null || getAdapter().getItemCount() == 0)
            {
                Log.e("cats", "EmptyRecyclerView.initEmptyView set emptyView visibility VISIBLE");
                if (getAdapter() == null)
                    Log.e("cats", "because getAdapter() == null");
                if (getAdapter() != null && getAdapter().getItemCount() == 0)
                    Log.e("cats", "because getAdapter().getItemCount() == 0");
                mEmptyView.setVisibility(VISIBLE);
                Log.e("myTag", "empty view is visible");
            }
            else
            {
                Log.e("cats", "EmptyRecyclerView.initEmptyView set emptyView visibility GONE");
                mEmptyView.setVisibility(GONE);
                Log.e("myTag", "empty view is gone");
            }

            if (getAdapter() == null || getAdapter().getItemCount() == 0)
            {
                Log.e("cats", "EmptyRecyclerView.initEmptyView set recyclerview visibility GONE");
                if (getAdapter() == null)
                    Log.e("cats", "because getAdapter() == null");
                if (getAdapter() != null && getAdapter().getItemCount() == 0)
                    Log.e("cats", "because getAdapter().getItemCount() == 0");
                EmptyRecyclerView.this.setVisibility(GONE);
                Log.e("myTag", "recycler view is gone");
            }
            else
            {
                Log.e("cats", "EmptyRecyclerView.initEmptyView set recyclerView visibility VISIBLE");
                EmptyRecyclerView.this.setVisibility(VISIBLE);
                Log.e("myTag", "recycler view is visible");
            }
        }
    }
    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Log.e("cats", "/////////////Calling from onChanged");
            initEmptyView();
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            Log.e("cats", "/////////////Calling from onItemRangeInserted");
            initEmptyView();
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            Log.e("cats", "/////////////Calling from onItemRangeRemoved");
            initEmptyView();
        }
    };
    @Override
    public void setAdapter(Adapter adapter) {
        Log.e("cats", "EmptyRecyclerView.setAdapter called");
        Adapter oldAdapter = getAdapter();
        super.setAdapter(adapter);
        if (oldAdapter != null) {
            Log.e("cats", "EmptyRecyclerView.setAdapter oldAdapter is not null");
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        else {
            Log.e("cats", "EmptyRecyclerView.setAdapter oldAdapter is null");
        }
        if (adapter != null) {
            Log.e("cats", "EmptyRecyclerView.setAdapter newAdapter is not null");
            adapter.registerAdapterDataObserver(observer);
        }
        else {
            Log.e("cats", "EmptyRecyclerView.setAdapter newAdapter is null");
        }
    }

    public void setEmptyView(View view) {
        Log.e("cats", "EmptyRecyclerView.setEmptyView called");
        this.mEmptyView = view;
        initEmptyView();
    }
}