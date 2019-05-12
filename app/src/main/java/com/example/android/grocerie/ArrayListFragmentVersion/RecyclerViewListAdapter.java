package com.example.android.grocerie.ArrayListFragmentVersion;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.DragStartHelper;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.BaseCursorAdapter;
import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.data.IngredientDbHelper;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperAdapter;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.core.view.DragStartHelper.*;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_POSITION;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry._ID;


public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.IngredientViewHolder>
        implements ItemTouchHelperAdapter
{

    static final int EDITOR_REQUEST = 1;  // The request code

    private final OnStartDragListener mDragStartListener;
    private Context mContext;
    private int mType;
    private List<Ingredient> mItems = new ArrayList<>();

    //from SO
    LayoutInflater inflater;
    IngredientDbHelper dbHelper;

    public RecyclerViewListAdapter(int type, List<Ingredient> datalist, OnStartDragListener dragStartListener) {
        super();
        mType = type;
        mItems = datalist;
        mDragStartListener = dragStartListener;
        if (mDragStartListener == null)
        {
            Log.e("reorder", "mdragstartlistener is null");
        }
        else
        {
            Log.e("reorder", "mdragstartlistener is not null");

        }
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

//        Log.e("reorder", "viewHolder position is " + holder.getAdapterPosition());
        Log.e("reorder", "we are in the list adapter");

        holder.CheckBox.setOnCheckedChangeListener(null);
        Ingredient currentIngredient = mItems.get(position);

        int idValue = currentIngredient.getId();
        String ingredientName = currentIngredient.getName();
        String ingredientAmount = currentIngredient.getAmount();
        String ingredientUnit = currentIngredient.getUnit();
        int ingredientCategory = currentIngredient.getCategory();
        int ingredientPosition = currentIngredient.getPosition();

        int ingredientChecked;
        if (mType == INGREDIENT_LIST_TYPE)
        {
            ingredientChecked = currentIngredient.getTo_buy();
        }
        else
        {
            ingredientChecked = currentIngredient.getPicked_up();
        }

        if (ingredientAmount == null || TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }


        if (mType == INGREDIENT_LIST_TYPE)
        {
            Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

            Log.e("reorder", "the current ingredient is " + currentIngredientUri);
            int databasePosition = currentIngredient.getPosition();;
            int listPosition = holder.getAdapterPosition();
            if (databasePosition != listPosition)
            {
                Log.e("reorder", "order of " + currentIngredientUri.toString() + " was updated from " + databasePosition + " to " + listPosition);

                currentIngredient.setPosition(listPosition);
            }
        }


        if (ingredientChecked == 1) {
            holder.CheckBox.setChecked(true);
        } else {
            holder.CheckBox.setChecked(false);
        }

        if (mType == SHOPPING_LIST_TYPE) // || mType == INGREDIENT_LIST_TYPE)
        {
            holder.handleView.setVisibility(View.GONE);

            switch (ingredientCategory) {
                case IngredientEntry.FRUIT_AND_VEG:
                    holder.categoryTextView.setText(R.string.fruit_and_veggie);
                    break;
                case IngredientEntry.MEAT_AND_PROT:
                    holder.categoryTextView.setText(R.string.meat_and_prot);
                    break;
                case IngredientEntry.BREAD_AND_GRAIN:
                    holder.categoryTextView.setText(R.string.bread_and_grain);
                    break;
                case IngredientEntry.DAIRY:
                    holder.categoryTextView.setText(R.string.dairy);
                    break;
                case IngredientEntry.FROZEN:
                    holder.categoryTextView.setText(R.string.frozen);
                    break;
                case IngredientEntry.CANNED:
                    holder.categoryTextView.setText(R.string.canned);
                    break;
                case IngredientEntry.DRINKS:
                    holder.categoryTextView.setText(R.string.drinks);
                    break;
                case IngredientEntry.SNACKS:
                    holder.categoryTextView.setText(R.string.snacks);
                    break;
                default:
                    holder.categoryTextView.setText(R.string.misc);
                    break;
            }
        }

        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit + " position: " + ingredientPosition);

        holder.CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                String checkboxString;
                int checkboxInt;

                if (isChecked) {
                    checkboxString = "1";
                    checkboxInt = 1;
                } else {
                    checkboxString = "0";
                    checkboxInt = 0;
                }

                Log.e("myTag", "The checked checkbox of the current row is " + checkboxString);

                ContentValues values = new ContentValues();

                if (mType == INGREDIENT_LIST_TYPE)
                {
                    values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkboxString);
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, 0);

                    currentIngredient.setTo_buy(checkboxInt);
                    currentIngredient.setPicked_up(0);

                }
                else
                {
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, checkboxString);
                    currentIngredient.setPicked_up(checkboxInt);

                }

                mContext.getContentResolver().update(currentIngredientUri, values, null, null);
            }
        });

        holder.ingredientSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Intent intent = new Intent(mContext, IngredientEditor.class);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                intent.setData(currentIngredientUri);

                ((Activity)mContext).startActivityForResult(intent, EDITOR_REQUEST);
            }
        });

        if (mType == INGREDIENT_LIST_TYPE)
        {
            // Start a drag whenever the handle view it touched
            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    if (mDragStartListener == null)
                    {
                        Log.e("reorder", "in listener: mdragstartlistener is null");
                    }
                    else
                    {
                        Log.e("reorder", "in listener: mdragstartlistener is not null");

                    }

                    mDragStartListener.onStartDrag(holder);
//                }
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {

    }

//    @Override
//    public void onItemDismiss(int position) {
//        mItems.remove(position);
//        notifyItemRemoved(position);
//    }

    public void onItemMove(int fromPosition, int toPosition) {

//        Ingredient prev = mItems.remove(fromPosition);
//        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);

//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mItems, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mItems, i, i - 1);
//            }
//        }

        Collections.swap(mItems, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }


    class IngredientViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView nameTextView;
        TextView summaryTextView;
        TextView categoryTextView;
        CheckBox CheckBox;
        LinearLayout ingredientSummary;
        ImageView handleView;

        IngredientViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
            CheckBox = itemView.findViewById(R.id.checkBoxView);
            ingredientSummary = itemView.findViewById(R.id.ingredient_summary);


            if (mType == SHOPPING_LIST_TYPE) //|| mType == INGREDIENT_LIST_TYPE)
            {
//                handleView.setVisibility(View.GONE);
                categoryTextView = itemView.findViewById(R.id.textViewCategory);
            }
            else
            {
                handleView = itemView.findViewById(R.id.handle);
            }
        }

        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
        }

        public void onItemClear() {
//            itemView.setBackgroundColor(0);
        }
    }
}