package com.example.android.grocerie.recyclerViewVersion;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.BaseCursorAdapter;
import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class ShoppingRecyclerCursorAdapter extends BaseCursorAdapter<ShoppingRecyclerCursorAdapter.IngredientViewHolder> {

    private Context mContext;

    public ShoppingRecyclerCursorAdapter() {super(null);}

    @Override
    public ShoppingRecyclerCursorAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        View formNameView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingRecyclerCursorAdapter.IngredientViewHolder(formNameView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {

        holder.pickedUpCheckBox.setOnCheckedChangeListener(null);

        int idIndex = cursor.getColumnIndex(IngredientEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
        int categoryindex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);
        int pickedUpColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP);

        int idValue = cursor.getInt(idIndex);
        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);
        int category = cursor.getInt(categoryindex);
        int ingredientPickedUp = cursor.getInt(pickedUpColumnIndex);

        if (TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        if (ingredientPickedUp == 1) {
            holder.pickedUpCheckBox.setChecked(true);
        } else {
            holder.pickedUpCheckBox.setChecked(false);
        }

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

        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit);

        holder.pickedUpCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                String pickedUpString;

                if (isChecked) {
                    pickedUpString = "1";
                } else {
                    pickedUpString = "0";
                }

                Log.e("myTag", "The picked up checkbox of the current row is " + pickedUpString);

                ContentValues values = new ContentValues();

                values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, pickedUpString);

                mContext.getContentResolver().update(currentIngredientUri, values, null, null);
            }
        });

        holder.ingredientSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Intent intent = new Intent(mContext, IngredientEditor.class);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                intent.setData(currentIngredientUri);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView summaryTextView;
        TextView categoryTextView;
        CheckBox pickedUpCheckBox;
        LinearLayout ingredientSummary;

        IngredientViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            pickedUpCheckBox = itemView.findViewById(R.id.checkBoxView);
            ingredientSummary = itemView.findViewById(R.id.ingredient_summary);
        }
    }
}