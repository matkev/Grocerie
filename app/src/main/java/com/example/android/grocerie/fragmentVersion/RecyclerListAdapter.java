package com.example.android.grocerie.fragmentVersion;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.Ingredient;
import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperAdapter;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperViewHolder;
import com.example.android.grocerie.dragAndDropHelper.OnStartDragListener;

import java.util.Collections;
import java.util.List;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_POSITION;


public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.IngredientViewHolder>
        implements ItemTouchHelperAdapter {

//    private boolean isActionMode = false;

    private final OnStartDragListener mDragStartListener;
    private Context mContext;
    private int mType;
    private List<Ingredient> mItems;


    public RecyclerListAdapter(int type, List<Ingredient> datalist, OnStartDragListener dragStartListener) {
        super();
        mType = type;
        mItems = datalist;
        mDragStartListener = dragStartListener;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ingredient_item, parent, false);
        IngredientViewHolder viewHolder = new IngredientViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {


        Ingredient currentIngredient = mItems.get(position);

        String ingredientName = currentIngredient.getName();
        String ingredientAmount = currentIngredient.getAmount();
        String ingredientUnit = currentIngredient.getUnit();
        int ingredientPosition = currentIngredient.getPosition();

        if (ingredientAmount == null || TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit); // + " position: " + ingredientPosition);

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder);
//                }
                return false;
            }
        });


        holder.handleView.setVisibility(View.VISIBLE);
        holder.CheckBox.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {

        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }


    class IngredientViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView nameTextView;
        TextView summaryTextView;
        CheckBox CheckBox;
        LinearLayout ingredientSummary;
        ImageView handleView;
        int beforeMovePosition;
        int afterMovePosition;

        IngredientViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
            CheckBox = itemView.findViewById(R.id.checkBoxView);
            ingredientSummary = itemView.findViewById(R.id.ingredient_summary);
            handleView = itemView.findViewById(R.id.handle);

            handleView.setVisibility(View.VISIBLE);
            CheckBox.setVisibility(View.GONE);

        }

        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
            beforeMovePosition = this.getAdapterPosition();

            displayItems();

        }

        public void onItemClear() {
            itemView.setBackgroundColor(0);
            afterMovePosition = this.getAdapterPosition();
            displayItems();

            if (afterMovePosition != beforeMovePosition) {
                if (afterMovePosition > beforeMovePosition) {
                    for (int i = beforeMovePosition; i < afterMovePosition; i++) {
                        int id = mItems.get(i).getId();
                        updatePosition(id, i);
                        mItems.get(i).setPosition(i);
                    }
                }
                if (afterMovePosition < beforeMovePosition) {
                    for (int i = beforeMovePosition; i > afterMovePosition; i--) {
                        int id = mItems.get(i).getId();
                        updatePosition(id, i);
                        mItems.get(i).setPosition(i);
                    }
                }
            }

            int id = mItems.get(afterMovePosition).getId();
            updatePosition(id, afterMovePosition);
            mItems.get(afterMovePosition).setPosition(afterMovePosition);

            notifyDataSetChanged();
        }

    }

    public void updatePosition(int uriId, int newPosition) {

        Uri uriToUpdate = ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, uriId);
        ContentValues values = new ContentValues();
        values.put(COLUMN_INGREDIENT_POSITION, Integer.toString(newPosition));

        mContext.getContentResolver().update(uriToUpdate, values, null, null);
    }

    public void displayItems() {
        Log.e("reorder", "displaying all items names///////");

        for (int i = 0; i < mItems.size(); i++) {
            String name = mItems.get(i).getName();
            Log.e("reorder", name);
        }
    }
}