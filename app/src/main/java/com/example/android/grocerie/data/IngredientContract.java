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


    public static abstract class IngredientEntry implements BaseColumns {

        public static final String TABLE_NAME = "ingredients";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_INGREDIENT_NAME = "name";
        public static final String COLUMN_INGREDIENT_AMOUNT = "amount";
        public static final String COLUMN_INGREDIENT_UNIT = "unit";
        public static final String COLUMN_INGREDIENT_CHECKED = "checked";


        public static final int CHECKED_NO = 0;
        public static final int CHECKED_YES = 1;



        //URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INGREDIENTS);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;



    }
}
