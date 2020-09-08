package com.example.android.grocerieDev;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerieDev.MainActivitiesAndFragments.IngredientEditor;
import com.example.android.grocerieDev.data.IngredientContract;
import com.example.android.grocerieDev.data.IngredientContract.IngredientEntry;
import com.example.android.grocerieDev.dragAndDropHelper.ItemTouchHelperAdapter;
import com.example.android.grocerieDev.MainActivitiesAndFragments.IngredientPositionEditor;
import static com.example.android.grocerieDev.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerieDev.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;


public class RecyclerCursorAdapter extends BaseCursorAdapter<RecyclerCursorAdapter.IngredientViewHolder>
                implements ItemTouchHelperAdapter
{
    static final int EDITOR_REQUEST = 1;  // The request code

    private Context mContext;
    private int mType;

    public RecyclerCursorAdapter(int type) {
        super(null);
        mType = type;
        Log.e("cats", "RecyclerCursorAdapter constructor called");
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        Log.e("cats", "RecyclerCursorAdapter.onCreateViewHolder called");

        View ingredientItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ingredient_item, parent, false);
        return new IngredientViewHolder(ingredientItemView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {
        Log.e("cats", "RecyclerCursorAdapter.onBindViewHolder called");

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

        int ingredientId = cursor.getInt(idIndex);
        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);
        int ingredientChecked = cursor.getInt(checkboxIndex);
        int ingredientCategory = cursor.getInt(categoryindex);

//        int positionIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_POSITION);
//        int position = cursor.getInt(positionIndex);


//        if (mType == INGREDIENT_LIST_TYPE)
//        {
//            Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, ingredientId);
//
//            Log.e("reorder", "the current ingredient is " + currentIngredientUri);
//            int databasePosition = getPosition(currentIngredientUri);
//            int listPosition = holder.getAdapterPosition();
//            if (databasePosition != listPosition)
//            {
//                Log.e("reorder", "order of " + currentIngredientUri.toString() + " was updated from " + databasePosition + " to " + listPosition);
//
//                ContentValues values = new ContentValues();
//                values.put(COLUMN_INGREDIENT_POSITION, listPosition);
//                mContext.getContentResolver().update(currentIngredientUri, values, null, null);
//            }
//        }

        if (ingredientAmount == null || TextUtils.isEmpty(ingredientAmount) || ingredientAmount.equalsIgnoreCase("0")) {
            ingredientAmount = "";
        }

        if (ingredientChecked == 1) {
            holder.CheckBox.setChecked(true);
        } else {
            holder.CheckBox.setChecked(false);
        }

        //todo: once categories tables linked with ingredients, use category uris to fill in category textview
        if (mType == SHOPPING_LIST_TYPE) // || mType == INGREDIENT_LIST_TYPE)
        {
//            Uri currentCategoryUri = ContentUris.withAppendedId(CategoryEntry.CONTENT_URI, ingredientCategory);
//
//            Cursor categoryCursor = mContext.getContentResolver().query(currentCategoryUri, null, null, null, null);
//            holder.categoryTextView.setText(categoryCursor.getString(nameColumnIndex));
        }

        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit); // + " position: " + position);

        holder.CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e("myTag", "The id of the current row is " + ingredientId);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, ingredientId);

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
                Log.e("myTag", "The id of the current row is " + ingredientId);
                Intent intent = new Intent(mContext, IngredientEditor.class);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, ingredientId);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                intent.setData(currentIngredientUri);
                ((Activity)mContext).startActivityForResult(intent, EDITOR_REQUEST);
            }
        });


        holder.ingredientSummary.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(mContext, IngredientPositionEditor.class);
                intent.putExtra("currentCategory", ingredientCategory);
                mContext.startActivity(intent);
                return true;
            }
        });
    }

//    @Override
//    public void onItemDismiss(int position) {
//        mItems.remove(position);
//        notifyItemRemoved(position);
//    }

    public void onItemMove(int fromPosition, int toPosition) {
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder{ // implements ItemTouchHelperViewHolder {

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
            handleView = itemView.findViewById(R.id.handle);

            handleView.setVisibility(View.INVISIBLE);

            if (mType == SHOPPING_LIST_TYPE) //|| mType == INGREDIENT_LIST_TYPE)
            {
                categoryTextView = itemView.findViewById(R.id.textViewCategory);
            }
        }

//        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
//        }
//
//        public void onItemClear() {
//            itemView.setBackgroundColor(0);
//        }
    }
}