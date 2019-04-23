package com.example.android.grocerie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.data.IngredientDbHelper;

public class ShoppingList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {



    private static final int SHOP_LOADER = 1;
    private ShoppingCursorAdapter mCursorAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);




        ListView shoppingListView = (ListView) findViewById(R.id.shopping_list_view);

        //TODO: set up empty view

        mCursorAdapter = new ShoppingCursorAdapter(this, null);


        shoppingListView.setAdapter(mCursorAdapter);

        shoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ShoppingList.this, IngredientEditor.class);

                Uri currentPetUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, id);

                intent.setData(currentPetUri);

                startActivity(intent);
            }
        });



        getLoaderManager().initLoader(SHOP_LOADER, null, this);

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
            case R.id.action_clear_list:
                //TODO: implement clear list, uncheck all ingredients
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED};

        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=?";
        String[] selectionArgs = new String[]{"1"};


        return new CursorLoader(this,
                IngredientEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);

    }

}
