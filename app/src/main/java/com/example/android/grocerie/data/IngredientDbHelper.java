package com.example.android.grocerie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.grocerie.data.IngredientContract.IngredientEntry;


/**
 * Created by matth on 9/13/2018.
 */

public class IngredientDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredient_list.db";

    private static final int DATABASE_VERSION = 1;

    public IngredientDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_INGREDIENTS_TABLE =  "CREATE TABLE " + IngredientEntry.TABLE_NAME + " ("
                + IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, "
                + IngredientEntry.COLUMN_INGREDIENT_AMOUNT + " INTEGER, "
                + IngredientEntry.COLUMN_INGREDIENT_UNIT + " TEXT, "
                + IngredientEntry.COLUMN_INGREDIENT_CHECKED + " INTEGER NOT NULL DEFAULT 0, "
                + IngredientEntry.COLUMN_INGREDIENT_CATEGORY + " INTEGER NOT NULL, "
                + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + " INTEGER NOT NULL DEFAULT 0, "
                + IngredientEntry.COLUMN_INGREDIENT_POSITION + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
