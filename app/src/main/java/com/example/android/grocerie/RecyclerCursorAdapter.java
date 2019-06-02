package com.example.android.grocerie;

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
import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperAdapter;
import com.example.android.grocerie.MainActivitiesAndFragments.IngredientPositionEditor;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;


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
        Log.e("reorder", "we are in the cursor adapter");

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
//        int position = cursor.getInt(positionIndex);


//        if (mType == INGREDIENT_LIST_TYPE)
//        {
//            Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);
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
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit); // + " position: " + position);

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


        holder.ingredientSummary.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(mContext, IngredientPositionEditor.class);
                intent.putExtra("currentCategory", category);
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