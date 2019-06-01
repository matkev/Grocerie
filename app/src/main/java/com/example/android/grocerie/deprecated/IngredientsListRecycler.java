package com.example.android.grocerie.recyclerViewVersion;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.grocerie.EmptyRecyclerView;
import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.example.android.grocerie.RecyclerCursorAdapter;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IngredientsListRecycler extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    private static final int INGREDIENT_LOADER = 0;
    private RecyclerCursorAdapter mCursorAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list_recycler);

        setTitle(R.string.ingredients_list_activity_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(toolbar);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngredientsListRecycler.this, IngredientEditor.class);
                startActivity(intent);
            }
        });

        //bind view
        mRecyclerView = findViewById(R.id.ingredients_list_view_recycler);
        emptyView = findViewById(R.id.empty_view);

        EmptyRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //set layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set default animator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCursorAdapter = new RecyclerCursorAdapter(IngredientEntry.INGREDIENT_LIST_TYPE);
        mRecyclerView.setAdapter(mCursorAdapter);

        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));

        LoaderManager.getInstance(IngredientsListRecycler.this).initLoader(INGREDIENT_LOADER, null, IngredientsListRecycler.this);
    }

    private void insertDummyData() {
        ContentValues values = new ContentValues();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Apples");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "12");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Feta cheese");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "150");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "grams");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "3");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();
        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Eggs");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "dozen");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "1");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.clear();
        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Naan");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "3");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "packs");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "2");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.clear();
        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Peanut Butter");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "jar");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "1");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.clear();
        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Cholula Hot Sauce");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "2");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "bottle");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "8");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.clear();
        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Orange Juice");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "bottle");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "6");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_ingredients_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_add_dummy_data:
                insertDummyData();

                return true;
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();

                return true;
            case R.id.action_uncheck_all_entries:
                uncheckAllIngredients();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            //TODO: sort by alphabet or most recent
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == INGREDIENT_LOADER) {
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
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP,
                IngredientEntry.COLUMN_INGREDIENT_POSITION};

        return new CursorLoader(this,
                IngredientEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteIngredients();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteIngredients() {

        int rowsDeleted = getContentResolver().delete(IngredientEntry.CONTENT_URI, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.ingredient_list_delete_all_ingredient_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.ingredient_list_delete_all_ingredient_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void uncheckAllIngredients() {

        ContentValues values = new ContentValues();

        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");

        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsUpdated == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.ingredient_list_uncheck_all_ingredient_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.ingredient_list_uncheck_all_ingredient_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }
}


