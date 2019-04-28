package com.example.android.grocerie.recyclerViewVersion;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.loader.content.CursorLoader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class ShoppingListRecycler extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    private static final int SHOP_LOADER = 1;
    private ShoppingRecyclerCursorAdapter mCursorAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_recycler);

        setTitle(R.string.shopping_list_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(toolbar);

        //bind view
        mRecyclerView = findViewById(R.id.shopping_list_view_recycler);
        emptyView = findViewById(R.id.empty_view);

        EmptyRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //set layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set default animator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCursorAdapter = new ShoppingRecyclerCursorAdapter();
        mRecyclerView.setAdapter(mCursorAdapter);

        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));

        LoaderManager.getInstance(ShoppingListRecycler.this).initLoader(SHOP_LOADER, null, ShoppingListRecycler.this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_uncheck_all_entries:
                uncheckAllIngredients();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == SHOP_LOADER) {
            return shoppingListLoader();
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

    private Loader<Cursor> shoppingListLoader()
    {
        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY};

        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=?";
        String[] selectionArgs = new String[]{"1"};

        return new CursorLoader(this,
                IngredientEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    private void uncheckAllIngredients()
    {
        ContentValues values = new ContentValues();

        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");

        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsUpdated == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.editor_uncheck_all_ingredient_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_uncheck_all_ingredient_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }
}




