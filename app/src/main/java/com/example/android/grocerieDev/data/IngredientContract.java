package com.example.android.grocerieDev.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.grocerieDev.BuildConfig;

/**
 * Created by matth on 4/21/2019.
 */

public final class IngredientContract {

    private IngredientContract(){}

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID ;

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INGREDIENTS = "ingredients";

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

        //TODO: why are these never used?
        public static final int PICKED_UP_NO = 0;
        public static final int PICKED_UP_YES = 1;

        public static final int CHECKED_NO = 0;
        public static final int CHECKED_YES = 1;

        //URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INGREDIENTS);

        //This is the Android platform's base MIME type for a content: URI containing a Cursor of zero or more items.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        //This is the Android platform's base MIME type for a content: URI containing a Cursor of a single item.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public static final int INGREDIENT_LIST_TYPE = 0;
        public static final int SHOPPING_LIST_TYPE = 1;
    }
}
