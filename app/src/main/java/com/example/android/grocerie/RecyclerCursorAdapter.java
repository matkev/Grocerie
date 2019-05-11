package com.example.android.grocerie;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.data.IngredientProvider;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperAdapter;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperViewHolder;

import java.util.Collections;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_POSITION;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry._ID;


public class RecyclerCursorAdapter extends BaseCursorAdapter<RecyclerCursorAdapter.IngredientViewHolder>
                implements ItemTouchHelperAdapter
{

    static final int EDITOR_REQUEST = 1;  // The request code

    private Context mContext;
    private int mType;

    public RecyclerCursorAdapter(int type) {
        super(null);
        mType = type;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        View formNameView = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ingredient_item, parent, false);
        IngredientViewHolder viewHolder = new IngredientViewHolder(formNameView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {

//        Log.e("reorder", "viewHolder position is " + holder.getAdapterPosition());

        holder.CheckBox.setOnCheckedChangeListener(null);

        int idIndex = cursor.getColumnIndex(IngredientEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
        int categoryindex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);
        int checkboxIndex;
        if (mType == INGREDIENT_LIST_TYPE)
        {
            checkboxIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
        }
        else
        {
            checkboxIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP);
        }

        int positionIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_POSITION);


        int idValue = cursor.getInt(idIndex);
        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);
        int ingredientChecked = cursor.getInt(checkboxIndex);
        int category = cursor.getInt(categoryindex);
        int position = cursor.getInt(positionIndex);


        Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

        Log.e("reorder", "the current ingredient is " + currentIngredientUri);
        int databasePosition = getPosition(currentIngredientUri);
        int listPosition = holder.getAdapterPosition();
        if (databasePosition != listPosition)
        {
            Log.e("reorder", "order of " + currentIngredientUri.toString() + " was updated from " + databasePosition + " to " + listPosition);

            ContentValues values = new ContentValues();
            values.put(COLUMN_INGREDIENT_POSITION, listPosition);
            mContext.getContentResolver().update(currentIngredientUri, values, null, null);
        }

        if (TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        if (ingredientChecked == 1) {
            holder.CheckBox.setChecked(true);
        } else {
            holder.CheckBox.setChecked(false);
        }

        if (mType == SHOPPING_LIST_TYPE) // || mType == INGREDIENT_LIST_TYPE)
        {
            switch (category) {
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
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit + " position: " + position);

        holder.CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                String checkboxString;

                if (isChecked) {
                    checkboxString = "1";
                } else {
                    checkboxString = "0";
                }

                Log.e("myTag", "The checked checkbox of the current row is " + checkboxString);

                ContentValues values = new ContentValues();

                if (mType == INGREDIENT_LIST_TYPE)
                {
                    values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkboxString);
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, 0);
                }
                else
                {
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, checkboxString);
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
    }

//    @Override
//    public void onItemDismiss(int position) {
//        mItems.remove(position);
//        notifyItemRemoved(position);
//    }

    public void onItemMove(int fromPosition, int toPosition) {

//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mItems, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mItems, i, i - 1);
//            }
//        }
        swapPosition(fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);


//        String prev = mItems.remove(fromPosition);
//        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
//        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    public int getPosition(Uri uri)
    {
        String [] projection = {IngredientEntry.COLUMN_INGREDIENT_POSITION};

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null,null);

        int position = 0;
        if (cursor.moveToFirst())
        {
            int positionIndex = cursor.getColumnIndex(COLUMN_INGREDIENT_POSITION);
            position = cursor.getInt(positionIndex);
        }


        return position;
    }

    public void swapPosition(int fromPosition, int toPosition)
    {
        String[] projection = {_ID};
        String selection = COLUMN_INGREDIENT_POSITION + "=?";
        String[] fromSelectionArgs = new String[]{Integer.toString(fromPosition)};
        String[] toSelectionArgs = new String[]{Integer.toString(toPosition)};

        Cursor fromCursor = mContext.getContentResolver().query(IngredientEntry.CONTENT_URI, projection, selection, fromSelectionArgs, null);
        int fromIdValue;
        if (fromCursor.moveToFirst()) {
            int fromIdIndex = fromCursor.getColumnIndex(_ID);
            fromIdValue = fromCursor.getInt(fromIdIndex);
        }
        else
        {
            return;
        }

        Uri fromUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, fromIdValue);
        ContentValues fromValues = new ContentValues();
        fromValues.put(COLUMN_INGREDIENT_POSITION, toPosition);



        Cursor toCursor = mContext.getContentResolver().query(IngredientEntry.CONTENT_URI, projection, selection, toSelectionArgs, null);
        int toIdValue;
        if (toCursor.moveToFirst()) {
            int toIdIndex = toCursor.getColumnIndex(_ID);
            toIdValue = toCursor.getInt(toIdIndex);
        }
        else
        {
            return;
        }

        Uri toUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, toIdValue);
        ContentValues toValues = new ContentValues();
        toValues.put(COLUMN_INGREDIENT_POSITION, fromPosition);


        mContext.getContentResolver().update(fromUri, fromValues, null, null);
        mContext.getContentResolver().update(toUri, toValues, null, null);

    }

    class IngredientViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView nameTextView;
        TextView summaryTextView;
        TextView categoryTextView;
        CheckBox CheckBox;
        LinearLayout ingredientSummary;

        IngredientViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
            CheckBox = itemView.findViewById(R.id.checkBoxView);
            ingredientSummary = itemView.findViewById(R.id.ingredient_summary);

            if (mType == SHOPPING_LIST_TYPE) //|| mType == INGREDIENT_LIST_TYPE)
            {
                categoryTextView = itemView.findViewById(R.id.textViewCategory);
            }
        }

        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}