package com.example.android.grocerie.recyclerViewVersion;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class IngredientRecyclerCursorAdapter extends BaseCursorAdapter<IngredientRecyclerCursorAdapter.IngredientViewHolder>
//        implements CompoundButton.OnCheckedChangeListener
{

    private Context context;


    public IngredientRecyclerCursorAdapter() {
        super(null);
    }

    @Override
    public IngredientRecyclerCursorAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View formNameView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientRecyclerCursorAdapter.IngredientViewHolder(formNameView);
    }

    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {


        holder.checkedCheckBox.setOnCheckedChangeListener(null);


        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
        int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);

        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);
        Integer ingredientChecked = cursor.getInt(checkedColumnIndex);


        int idIndex = cursor.getColumnIndex(IngredientEntry._ID);
        int idValue = cursor.getInt(idIndex);

        if (TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        if (ingredientChecked == 1) {
            holder.checkedCheckBox.setChecked(true);
        } else {
            holder.checkedCheckBox.setChecked(false);
        }

        Log.e("myTag", "ingredientChecked at row " + idValue + " is equal to " + ingredientChecked);



        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit);


        //TODO implement check box listener
        holder.checkedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                String checkedString;

                if (isChecked) {
                    checkedString = "1";
                } else {
                    checkedString = "0";
                }

                Log.e("myTag", "The checkbox of the current row is " + checkedString);

                ContentValues values = new ContentValues();

                values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkedString);

                context.getContentResolver().update(currentIngredientUri, values, null, null);
            }
        });

//        holder.checkedCheckBox.setTag(idValue);


    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

//    @Override
//    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        int id = (Integer) compoundButton.getTag();
//
//        Log.e("myTag", "The id of the current row is " + id);
//        Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, id);
//
//
//        Log.e("myTag", "The uri of the current row is " + currentIngredientUri);
//
//        String checkedString;
//
//        if (compoundButton.isChecked()) {
//            checkedString = "1";
//        } else {
//            checkedString = "0";
//        }
//
//        Log.e("myTag", "The checkbox of the current row is " + checkedString);
//
//        ContentValues values = new ContentValues();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkedString);
//
//        context.getContentResolver().update(currentIngredientUri, values, null, null);
//
//
//    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView summaryTextView;
        CheckBox checkedCheckBox;


        IngredientViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
            checkedCheckBox = itemView.findViewById(R.id.ingredient_list_checkBox);

        }
    }
}