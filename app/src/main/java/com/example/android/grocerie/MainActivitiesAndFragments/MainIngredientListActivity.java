package com.example.android.grocerie.MainActivitiesAndFragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

import java.util.ArrayList;
import java.util.List;


public class MainIngredientListActivity extends AppCompatActivity {

    // The editor request code
    static final int EDITOR_REQUEST = 1;

    //possible results received from editor
    public static final int INSERT_FAIL = 0;
    public static final int INSERT_SUCCESS = 1;
    public static final int UPDATE_FAIL = 2;
    public static final int UPDATE_SUCCESS = 3;
    public static final int DELETE_FAIL = 4;
    public static final int DELETE_SUCCESS = 5;
    public static final int NO_CHANGE = 6;

    //needed to display snackbars
    View mainLayout;

    //viewpager of category tabs
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);
        mainLayout = findViewById(R.id.main_layout_id);

        //setup custom toolbar
        initToolbar();

        //setup category tabs
        initViewPagerAndTabs();

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainIngredientListActivity.this, IngredientEditor.class);
                intent.putExtra("currentCategory", viewPager.getCurrentItem());
                startActivityForResult(intent, EDITOR_REQUEST);
            }
        });
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.ingredients_list_activity_title));
    }

    private void initViewPagerAndTabs() {
        viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(this, getSupportFragmentManager());

        //TODO: change how the fragments are titled
        pagerAdapter.addFragment(IngredientFragment.newInstance(0), getString(R.string.fruit_and_veggie));
        pagerAdapter.addFragment(IngredientFragment.newInstance(1), getString(R.string.meat_and_prot));
        pagerAdapter.addFragment(IngredientFragment.newInstance(2), getString(R.string.bread_and_grain));
        pagerAdapter.addFragment(IngredientFragment.newInstance(3), getString(R.string.dairy));
        pagerAdapter.addFragment(IngredientFragment.newInstance(4), getString(R.string.frozen));
        pagerAdapter.addFragment(IngredientFragment.newInstance(5), getString(R.string.canned));
        pagerAdapter.addFragment(IngredientFragment.newInstance(6), getString(R.string.drinks));
        pagerAdapter.addFragment(IngredientFragment.newInstance(7), getString(R.string.snacks));
        pagerAdapter.addFragment(IngredientFragment.newInstance(8), getString(R.string.spices));
        pagerAdapter.addFragment(IngredientFragment.newInstance(9), getString(R.string.condiments));
        pagerAdapter.addFragment(IngredientFragment.newInstance(10), getString(R.string.misc));
        pagerAdapter.addFragment(IngredientFragment.newInstance(11), getString(R.string.non_food));

        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
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
            // Respond to a click on the "Delete All Ingredients" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Uncheck All" menu option
            case R.id.action_clear_all_entries:
                clearAllItems();
                return true;
            // Respond to a click on the "Edit List Order" menu option
            case R.id.action_edit_mode:
                startEditMode();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
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

        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP};

        Cursor cursor = getContentResolver().query(IngredientEntry.CONTENT_URI, projection, null, null, null);

        int rowsDeleted = getContentResolver().delete(IngredientEntry.CONTENT_URI, null, null);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            showSnackbar(
                    mainLayout,
                    getString(R.string.ingredient_list_delete_all_ingredient_failed),
                    Toast.LENGTH_SHORT);
        }
        else
        {
            // Otherwise, the delete was successful and we can display a toast.
            deleteAllUndoSnackBar(
                    mainLayout,
                    getString(R.string.ingredient_list_delete_all_ingredient_successful),
                    Toast.LENGTH_SHORT,
                    cursor);
        }
    }

    private void clearAllItems()
    {
        ContentValues values = new ContentValues();
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, "0");

        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=?";
        String toBuySelection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=? AND " + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + "=?";
        String pickedUpSelection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=? AND " + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + "=?";

        String[] selectionArgs= new String[]{"1"};
        String[] toBuySelectionArgs = new String[]{"1","0"};
        String[] pickedUpSelectionArgs = new String[]{"1","1"};

        String [] projection = {IngredientEntry._ID};

        Cursor toBuyCursor = getContentResolver().query(IngredientEntry.CONTENT_URI, projection, toBuySelection, toBuySelectionArgs, null);
        Cursor pickedUpCursor = getContentResolver().query(IngredientEntry.CONTENT_URI, projection, pickedUpSelection, pickedUpSelectionArgs, null);

        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, selection, selectionArgs);

        Log.e("myTag", "rows of checked items updated: " + rowsUpdated);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsUpdated == 0) {
            // If no rows were deleted, then there was an error with the delete.
            showSnackbar(
                    mainLayout,
                    getString(R.string.ingredient_list_uncheck_all_ingredient_failed),
                    Toast.LENGTH_SHORT);
        }
        else
        {
            // Otherwise, the delete was successful and we can display a toast.
            uncheckAllUndoSnackBar(
                    mainLayout,
                    getString(R.string.ingredient_list_uncheck_all_ingredient_successful),
                    Toast.LENGTH_SHORT,
                    toBuyCursor,
                    pickedUpCursor);
        }
    }

    private void startEditMode()
    {
        Intent intent = new Intent(MainIngredientListActivity.this, IngredientPositionEditor.class);
        intent.putExtra("currentCategory", viewPager.getCurrentItem());
        startActivity(intent);
    }

    //decides which snackbar to display depending on the result received from the editor
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDITOR_REQUEST) {

            // Make sure the request was successful
            switch (resultCode)
            {
                case INSERT_FAIL:
                    Log.e("intent", "return code was 0");
                    insertFailResultHandler();
                    return;
                case INSERT_SUCCESS:
                    Log.e("intent", "return code was 1");
                    insertSuccessResultHandler(data);
                    return;
                case UPDATE_FAIL:
                    Log.e("intent", "return code was 2");
                    updateFailResultHandler();
                    return;
                case UPDATE_SUCCESS:
                    Log.e("intent", "return code was 3");
                    updateSuccessResultHandler(data);
                    return;
                case DELETE_FAIL:
                    Log.e("intent", "return code was 4");
                    deleteFailResultHandler();
                    return;
                case DELETE_SUCCESS:
                    Log.e("intent", "return code was 5");
                    deleteSuccessResultHandler(data);
                    return;
                case NO_CHANGE:
                    Log.e("intent", "return code was 6");
                    return;
                default:
                    return;
            }
        }
    }

    private void insertFailResultHandler()
    {
        showSnackbar(
                mainLayout,
                getString(R.string.editor_insert_ingredient_failed),
                Toast.LENGTH_SHORT);
    }

    private void insertSuccessResultHandler(Intent data)
    {
        Uri newUri = Uri.parse(data.getStringExtra("newUri"));
        insertUndoSnackbar(
                mainLayout,
                getString(R.string.editor_insert_ingredient_succesful),
                Toast.LENGTH_SHORT,
                newUri);
    }

    private void updateFailResultHandler()
    {
        showSnackbar(
                mainLayout,
                getString(R.string.editor_update_ingredient_failed),
                Toast.LENGTH_SHORT);
    }

    private void updateSuccessResultHandler(Intent data)
    {
        Uri currentIngredientUri = Uri.parse(data.getStringExtra("currentIngredientUri"));
        Bundle oldValuesBundle = data.getBundleExtra("oldValues");

        ContentValues oldValues = new ContentValues();
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_NAME, oldValuesBundle.getString("name"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, oldValuesBundle.getInt("amount"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, oldValuesBundle.getString("unit"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, oldValuesBundle.getInt("toBuy"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, oldValuesBundle.getInt("category"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, oldValuesBundle.getInt("pickedUp"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_POSITION, oldValuesBundle.getInt("position"));

        updateUndoSnackbar(
                mainLayout,
                getString(R.string.editor_update_ingredient_succesful),
                Toast.LENGTH_SHORT,
                currentIngredientUri,
                oldValues);
    }

    private void deleteFailResultHandler()
    {
        showSnackbar(
                mainLayout,
                getString(R.string.editor_delete_ingredient_failed),
                Toast.LENGTH_SHORT);
    }

    private void deleteSuccessResultHandler(Intent data)
    {
        Bundle oldValuesBundle = data.getBundleExtra("oldValues");

        ContentValues oldValues = new ContentValues();
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_NAME, oldValuesBundle.getString("name"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, oldValuesBundle.getInt("amount"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, oldValuesBundle.getString("unit"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, oldValuesBundle.getInt("toBuy"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, oldValuesBundle.getInt("category"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, oldValuesBundle.getInt("pickedUp"));
        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_POSITION, oldValuesBundle.getInt("position"));

        deleteUndoSnackBar(
                mainLayout,
                getString(R.string.editor_delete_ingredient_successful),
                Toast.LENGTH_SHORT,
                oldValues,
                oldValuesBundle);
    }


    //snackbar methods
    public void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.show();
    }

    public void insertUndoSnackbar(View view, String message, int duration, Uri uri)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(uri, null, null);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void updateUndoSnackbar(View view, String message, int duration, Uri uri, ContentValues values)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().update(uri, values, null, null);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void deleteUndoSnackBar(View view, String message, int duration, ContentValues values, Bundle bundle)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri newUri = getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

                ContentValues values = new ContentValues();
                values.put(IngredientEntry.COLUMN_INGREDIENT_POSITION, bundle.getInt("position"));

                getContentResolver().update(newUri, values, null, null);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void deleteAllUndoSnackBar(View view, String message, int duration, Cursor cursor)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor.moveToFirst())
                {

                    do {
                        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
                        int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
                        int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
                        int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
                        int pickedUpColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP);
                        int categoryColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);

                        // Extract out the value from the Cursor for the given column index
                        String name = cursor.getString(nameColumnIndex);
                        int amount = cursor.getInt(amountColumnIndex);
                        String unit = cursor.getString(unitColumnIndex);
                        int checked = cursor.getInt(checkedColumnIndex);
                        int category = cursor.getInt(categoryColumnIndex);
                        int pickedUp = cursor.getInt(pickedUpColumnIndex);

                        ContentValues values = new ContentValues();
                        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, name);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, amount);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, unit);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checked);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, category);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, pickedUp);

                        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
                    }while(cursor.moveToNext());
                }
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void uncheckAllUndoSnackBar(View view, String message, int duration, Cursor toBuyCursor, Cursor pickedUpCursor)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toBuyCursor.moveToFirst())
                {

                    do {
                        int IDColumnIndex = toBuyCursor.getColumnIndex(IngredientEntry._ID);

                        // Extract out the value from the Cursor for the given column index
                        int id = toBuyCursor.getInt(IDColumnIndex);

                        ContentValues values = new ContentValues();
                        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, 1);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, 0);


                        getContentResolver().update(ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id), values, null, null);
                    }while(toBuyCursor.moveToNext());
                }
                if (pickedUpCursor.moveToFirst())
                {

                    do {
                        int IDColumnIndex = pickedUpCursor.getColumnIndex(IngredientEntry._ID);

                        // Extract out the value from the Cursor for the given column index
                        int id = pickedUpCursor.getInt(IDColumnIndex);

                        ContentValues values = new ContentValues();
                        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, 1);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, 1);


                        getContentResolver().update(ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id), values, null, null);
                    }while(pickedUpCursor.moveToNext());
                }



                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void insertDummyData() {


//        ContentValues values = new ContentValues();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "1");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "2");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "3");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "4");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "5");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "6");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "7");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "8");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "9");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();
//
//
//        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "10");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
//        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");
//
//        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
//
//        values.clear();


        ContentValues values = new ContentValues();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Green Apples");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "12");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "0");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Bananas");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "6");
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

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Naan");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "3");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "packs");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "2");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Baguette");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "2");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "2");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Peanut Butter");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "jar");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "1");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Cholula Hot Sauce");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "2");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "bottle");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "8");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Orange Juice");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "bottle");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "6");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, "Popcorn");
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, "1");
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, "bottle");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, "7");

        getContentResolver().insert(IngredientEntry.CONTENT_URI, values);

        values.clear();
    }

    //populates the viewpager with fragments and titles
    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();
        private Context mContext;

        public PagerAdapter(Context context, FragmentManager fragmentManager) {
            super(fragmentManager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}

