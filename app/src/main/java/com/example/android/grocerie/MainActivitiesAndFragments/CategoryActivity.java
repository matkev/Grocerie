package com.example.android.grocerie.MainActivitiesAndFragments;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.loader.content.CursorLoader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;


import com.example.android.grocerie.CategoryCursorAdapter;
import com.example.android.grocerie.EmptyRecyclerView;
import com.example.android.grocerie.R;
import com.example.android.grocerie.RecyclerCursorAdapter;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class CategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EmptyRecyclerView mRecyclerView;
//    RelativeLayout emptyView;
    private static final int CATEGORY_LOADER = 0;
    private CategoryCursorAdapter mCursorAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setTitle(R.string.ingredients_list_activity_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(toolbar);

        //bind view
        mRecyclerView = findViewById(R.id.ingredients_list_view_recycler);
//        emptyView = findViewById(R.id.empty_view);

        EmptyRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //set layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set default animator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCursorAdapter = new CategoryCursorAdapter();
        mRecyclerView.setAdapter(mCursorAdapter);

//        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));

//        ContentValues values = new ContentValues();
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_0);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_1);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_2);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_3);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_4);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_5);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_6);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_7);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);
//
//        values.put(IngredientEntry.COLUMN_CATEGORY_NAME, IngredientEntry.CATEGORY_8);
//        getContentResolver().insert(IngredientEntry.CATEGORY_CONTENT_URI, values);

        LoaderManager.getInstance(CategoryActivity.this).initLoader(CATEGORY_LOADER, null, CategoryActivity.this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CATEGORY_LOADER) {
            return ingredientListLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private Loader<Cursor> ingredientListLoader() {
        String[] projection = {
                IngredientEntry.CATEGORY_ID,
                IngredientEntry.COLUMN_CATEGORY_NAME};

        return new CursorLoader(this,
                IngredientEntry.CATEGORY_CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

}