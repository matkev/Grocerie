package com.example.android.grocerie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;


//import androidx.cursoradapter.widget.CursorAdapter;

import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;


/**
 * Created by matth on 4/14/2019.
 */

public class IngredientCursorAdapter extends CursorAdapter implements CompoundButton.OnCheckedChangeListener {

    private Context context;

    /**
     * Constructs a new {@link IngredientCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public IngredientCursorAdapter(Context context, Cursor c) {
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

        return LayoutInflater.from(context).inflate(R.layout.ingredient_list_item, parent, false);
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
    public void bindView(View view, Context context, Cursor cursor) {
        this.context = context;


        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        CheckBox checked = (CheckBox) view.findViewById(R.id.ingredient_list_checkBox);

        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
        int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);

        String ingredientName = cursor.getString(nameColumnIndex);
        String ingredientAmount = cursor.getString(amountColumnIndex);
        String ingredientUnit = cursor.getString(unitColumnIndex);
        Integer ingredientChecked = cursor.getInt(checkedColumnIndex);

        if (ingredientChecked == 1) {
            checked.setChecked(true);
        } else {
            checked.setChecked(false);
        }

        if (TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }

        nameTextView.setText(ingredientName);
        summaryTextView.setText(ingredientAmount + " " + ingredientUnit);


        checked.setOnCheckedChangeListener(this);
        int idIndex = cursor.getColumnIndex(IngredientEntry._ID);
        int idValue = cursor.getInt(idIndex);
        checked.setTag(idValue);


    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = (Integer) compoundButton.getTag();

        Log.e("myTag", "The id of the current row is " + id);
        Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, id);


        Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

        String checkedString;

        if (compoundButton.isChecked()) {
            checkedString = "1";
        } else {
            checkedString = "0";
        }

        Log.e("myTag", "The checkbox of the current row is " + checkedString);

        ContentValues values = new ContentValues();

        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkedString);

        context.getContentResolver().update(currentIngredientUri, values, null, null);


    }


}
