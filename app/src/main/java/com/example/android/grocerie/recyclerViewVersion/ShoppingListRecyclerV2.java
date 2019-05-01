package com.example.android.grocerie.recyclerViewVersion;


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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.grocerie.R;
import com.example.android.grocerie.RecyclerCursorAdapter;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class ShoppingListRecyclerV2 extends AppCompatActivity {
    EmptyRecyclerView mRecyclerView;
    EmptyRecyclerView mPickedUpRecyclerView;
    RelativeLayout emptyView;
    private static final int SHOP_LOADER = 1;
    private static final int PICKED_UP_LOADER = 2;

    private RecyclerCursorAdapter mCursorAdapter;
    private RecyclerCursorAdapter mPickedUpAdapter;

    private LoaderManager.LoaderCallbacks<Cursor> shoppingListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
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
    };

    private LoaderManager.LoaderCallbacks<Cursor> pickedUpListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            if (id == PICKED_UP_LOADER) {
                return pickedUpListLoader();
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            mPickedUpAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            mPickedUpAdapter.swapCursor(null);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_recycler_v2);

        setTitle(R.string.shopping_list_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(toolbar);

        //bind view
        mRecyclerView = findViewById(R.id.shopping_list_view_recycler);

        mPickedUpRecyclerView = findViewById(R.id.picked_up_list_view_recycler);
        emptyView = findViewById(R.id.empty_view);

        EmptyRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        EmptyRecyclerView.LayoutManager mPickedUpLayoutManager = new LinearLayoutManager(getApplicationContext());

        //set layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPickedUpRecyclerView.setLayoutManager(mPickedUpLayoutManager);

        //set default animator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCursorAdapter = new RecyclerCursorAdapter(IngredientEntry.SHOPPING_LIST_TYPE);
        mRecyclerView.setAdapter(mCursorAdapter);

        mPickedUpRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPickedUpAdapter = new RecyclerCursorAdapter(IngredientEntry.SHOPPING_LIST_TYPE);
        mPickedUpRecyclerView.setAdapter(mPickedUpAdapter);

        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));

        LoaderManager.getInstance(ShoppingListRecyclerV2.this).initLoader(SHOP_LOADER, null, shoppingListLoader);
        LoaderManager.getInstance(ShoppingListRecyclerV2.this).initLoader(PICKED_UP_LOADER, null, pickedUpListLoader);
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

    private Loader<Cursor> shoppingListLoader()
    {
        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP};

        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=? AND " + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + " =?";
        String[] selectionArgs = new String[]{"1","0"};

        return new CursorLoader(this,
                IngredientEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    private Loader<Cursor> pickedUpListLoader()
    {
        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP};

        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=? AND " + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + " =?";
        String[] selectionArgs = new String[]{"1","1"};

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




