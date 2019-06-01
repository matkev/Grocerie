package com.example.android.grocerie.deprecated;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class ShoppingCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link IngredientCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ShoppingCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.generic_ingredient_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView nameTextView = (TextView) view.findViewById(R.id.textViewName);
        TextView amountTextView = (TextView) view.findViewById(R.id.textViewSummary);

        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);

        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);

        if (TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        nameTextView.setText(ingredientName);
        amountTextView.setText(ingredientAmount + " " + ingredientUnit);






    }
}
