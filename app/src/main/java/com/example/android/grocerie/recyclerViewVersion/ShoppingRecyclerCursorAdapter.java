package com.example.android.grocerie.recyclerViewVersion;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class ShoppingRecyclerCursorAdapter extends BaseCursorAdapter<ShoppingRecyclerCursorAdapter.IngredientViewHolder> {

    public ShoppingRecyclerCursorAdapter() {super(null);}

    @Override
    public ShoppingRecyclerCursorAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View formNameView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingRecyclerCursorAdapter.IngredientViewHolder(formNameView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {

        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);

        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);

        if (TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit);


    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView summaryTextView;

        IngredientViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
        }
    }
}