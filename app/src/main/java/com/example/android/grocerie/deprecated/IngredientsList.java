package com.example.android.grocerie.deprecated;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.CursorLoader;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class IngredientsList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

//    private IngredientDbHelper mDbHelper;


    private static final int INGREDIENT_LOADER = 0;
    private IngredientCursorAdapter mCursorAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deprecated_activity_ingredients_list);
        setTitle(R.string.ingredients_list_activity_title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngredientsList.this, IngredientEditor.class);
                startActivity(intent);
            }
        });


        final ListView ingredientListView = (ListView) findViewById(R.id.ingredient_list_view);

        //TODO: set up empty view

        mCursorAdapter = new IngredientCursorAdapter(this, null);


        ingredientListView.setAdapter(mCursorAdapter);

        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(IngredientsList.this, IngredientEditor.class);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id);

                intent.setData(currentIngredientUri);

                startActivity(intent);
            }
        });

//        CheckBox checked = (CheckBox) findViewById(R.id.ingredient_list_checkBox);
//
//        checked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CheckBox checked_view = (CheckBox) view;
//                int pos = ingredientListView.getPositionForView(checked_view);
//                long id = ingredientListView.getItemIdAtPosition(pos);
//
//                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, id);
//
//                String checkedString;
//
//                if (checked_view.isChecked())
//                {
//                    checkedString = "1";
//                }
//                else
//                {
//                    checkedString = "0";
//                }
//                ContentValues values = new ContentValues();
//
//                values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkedString);
//
//                getContentResolver().update(currentIngredientUri, values, null, null);
//            }
//        });



        getLoaderManager().initLoader(INGREDIENT_LOADER, null, this);

    }


    private void insertDummyData()
    {


        ContentValues values = new ContentValues();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Apples");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "12");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);


        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Feta cheese");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "150");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "grams");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "1");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();
        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Eggs");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "dozen");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.clear();
        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Naan");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "3");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "packs");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "1");

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
            // Respond to a click on the "Delete all entries" menu option
            //TODO: sort by alphabet or most recent
        }
        return super.onOptionsItemSelected(item);
    }




    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED};


        return new CursorLoader(this,
                IngredientEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }


}
