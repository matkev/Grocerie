package com.example.android.grocerieDev.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.grocerieDev.BuildConfig;

/**
 * Created by matth on 4/21/2019.
 */

public final class CategoryContract {

    private CategoryContract(){}

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID ;

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CATEGORIES = "categories";

    public static abstract class CategoryEntry implements BaseColumns {

                //name of category table
        public static final String TABLE_NAME = "categories";

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

        //columns within categories table
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CATEGORY_NAME = "name";

        //category uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);

        //does this need the
    }
}
