package com.example.android.grocerie.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by matth on 4/21/2019.
 */

public final class IngredientContract {

    private IngredientContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.grocerie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INGREDIENTS = "ingredients";

    public static final String PATH_CATEGORIES = "categories";

    public static abstract class IngredientEntry implements BaseColumns {

        //name of the table
        public static final String TABLE_NAME = "ingredients";

        //columns within ingredients table
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_INGREDIENT_NAME = "name";
        public static final String COLUMN_INGREDIENT_AMOUNT = "amount";
        public static final String COLUMN_INGREDIENT_UNIT = "unit";
        public static final String COLUMN_INGREDIENT_CHECKED = "checked";
        public static final String COLUMN_INGREDIENT_CATEGORY = "category";
        public static final String COLUMN_INGREDIENT_PICKED_UP = "picked_up";
        public static final String COLUMN_INGREDIENT_POSITION = "position";

        //name of category table
        public static final String CATEGORIES_TABLE_NAME = "categories";

        //names of the categories
        //TODO: change how the categories are named
        public static final int CATEGORY_0 = 0;
        public static final int CATEGORY_1 = 1;
        public static final int CATEGORY_2 = 2;
        public static final int CATEGORY_3 = 3;
        public static final int CATEGORY_4 = 4;
        public static final int CATEGORY_5 = 5;
        public static final int CATEGORY_6 = 6;
        public static final int CATEGORY_7 = 7;
        public static final int CATEGORY_8 = 8;

        public static final int PICKED_UP_NO = 0;
        public static final int PICKED_UP_YES = 1;

        public static final int CHECKED_NO = 0;
        public static final int CHECKED_YES = 1;

        //URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INGREDIENTS);

        //columns within categories table
        public static final String CATEGORY_ID = BaseColumns._ID;
        public static final String COLUMN_CATEGORY_NAME = "name";

        //category uri
        public static final Uri CATEGORY_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);



        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public static final int INGREDIENT_LIST_TYPE = 0;
        public static final int SHOPPING_LIST_TYPE = 1;
    }
}
