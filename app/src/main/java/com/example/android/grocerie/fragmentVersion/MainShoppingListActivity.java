package com.example.android.grocerie.fragmentVersion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.grocerie.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

import java.util.ArrayList;
import java.util.List;

public class MainShoppingListActivity extends AppCompatActivity {

    static final int EDITOR_REQUEST = 1;  // The request code

    public static final int UPDATE_FAIL = 2;
    public static final int UPDATE_SUCCESS = 3;
    public static final int DELETE_FAIL = 4;
    public static final int DELETE_SUCCESS = 5;
    public static final int NO_CHANGE = 6;

    View mainLayout;

    ViewPager viewPager;
    private static final int TO_BUY_LIST = 0;
    private static final int PICKED_UP_LIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        mainLayout = findViewById(R.id.main_layout_id);

        initToolbar();

        initViewPagerAndTabs();
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.shopping_list_activity_title));
    }

    private void initViewPagerAndTabs() {
        viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(this, getSupportFragmentManager());


        pagerAdapter.addFragment(ShoppingFragment.newInstance(0), getString(R.string.shopping_list_my_list));
        pagerAdapter.addFragment(ShoppingFragment.newInstance(1), getString(R.string.shopping_list_picked_up));


        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
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
                clearAllItems();
                return true;
            case R.id.action_uncheck_picked_up_entries:
                clearPickedUpItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
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


        String [] projection = {
                IngredientEntry._ID};


        Cursor toBuyCursor = getContentResolver().query(IngredientEntry.CONTENT_URI, projection, toBuySelection, toBuySelectionArgs, null);
        Cursor pickedUpCursor = getContentResolver().query(IngredientEntry.CONTENT_URI, projection, pickedUpSelection, pickedUpSelectionArgs, null);


        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, selection, selectionArgs);

        Log.e("myTag", "rows of checked items updated: " + rowsUpdated);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsUpdated == 0) {
            // If no rows were deleted, then there was an error with the delete.
            showSnackbar(
                    mainLayout,
                    getString(R.string.shopping_list_clear_all_items_failed),
                    Toast.LENGTH_SHORT);

        } else {
            // Otherwise, the delete was successful and we can display a toast.
            clearAllItemsUndoSnackBar(
                    mainLayout,
                    getString(R.string.shopping_list_clear_all_items_successful),
                    Toast.LENGTH_SHORT,
                    toBuyCursor,
                    pickedUpCursor);
        }
    }

    private void clearPickedUpItems()
    {
        ContentValues values = new ContentValues();
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, "0");

        String selection = IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + "=?";

        String[] selectionArgs = new String[]{"1"};

        String [] projection = {
                IngredientEntry._ID};


        Cursor cursor = getContentResolver().query(IngredientEntry.CONTENT_URI, projection, selection, selectionArgs, null);


        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, selection, selectionArgs);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsUpdated == 0) {
            // If no rows were deleted, then there was an error with the delete.
            showSnackbar(
                    mainLayout,
                    getString(R.string.shopping_list_clear_picked_up_items_failed),
                    Toast.LENGTH_SHORT);
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            clearAllPickedUpUndoSnackBar(
                    mainLayout,
                    getString(R.string.shopping_list_clear_picked_up_items_successful),
                    Toast.LENGTH_SHORT,
                    cursor);
        }
    }

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

    //decides which snackbar to display depending on the result received from the editor
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDITOR_REQUEST) {
            Bundle oldValues;
            ContentValues values;

            // Make sure the request was successful
            switch (resultCode)
            {
                case UPDATE_FAIL:
                    Log.e("intent", "return code was 2");
                    showSnackbar(
                            mainLayout,
                            getString(R.string.editor_update_ingredient_failed),
                            Toast.LENGTH_SHORT);
                    return;
                case UPDATE_SUCCESS:
                    Log.e("intent", "return code was 3");

                    Uri currentIngredientUri = Uri.parse(data.getStringExtra("currentIngredientUri"));
                    oldValues = data.getBundleExtra("oldValues");

                    values = new ContentValues();
                    values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, oldValues.getString("name"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, oldValues.getInt("amount"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, oldValues.getString("unit"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, oldValues.getInt("toBuy"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, oldValues.getInt("category"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, oldValues.getInt("pickedUp"));

                    updateUndoSnackbar(
                            mainLayout,
                            getString(R.string.editor_update_ingredient_succesful),
                            Toast.LENGTH_SHORT,
                            currentIngredientUri,
                            values);
                    return;
                case DELETE_FAIL:
                    Log.e("intent", "return code was 4");
                    showSnackbar(
                            mainLayout,
                            getString(R.string.editor_delete_ingredient_failed),
                            Toast.LENGTH_SHORT);
                    return;
                case DELETE_SUCCESS:
                    Log.e("intent", "return code was 5");

                    oldValues = data.getBundleExtra("oldValues");

                    values = new ContentValues();
                    values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, oldValues.getString("name"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, oldValues.getInt("amount"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, oldValues.getString("unit"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, oldValues.getInt("toBuy"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, oldValues.getInt("category"));
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, oldValues.getInt("pickedUp"));

                    deleteUndoSnackBar(
                            mainLayout,
                            getString(R.string.editor_delete_ingredient_successful),
                            Toast.LENGTH_SHORT,
                            values);
                    return;
                case NO_CHANGE:
                    Log.e("intent", "return code was 6");

                    return;
                default:
                    return;
            }
        }
    }

    //snackbar methods
    public void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
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

    public void deleteUndoSnackBar(View view, String message, int duration, ContentValues values)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void clearAllItemsUndoSnackBar(View view, String message, int duration, Cursor toBuyCursor, Cursor pickedUpCursor)
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

    public void clearAllPickedUpUndoSnackBar(View view, String message, int duration, Cursor cursor)
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
                        int IDColumnIndex = cursor.getColumnIndex(IngredientEntry._ID);

                        // Extract out the value from the Cursor for the given column index
                        int id = cursor.getInt(IDColumnIndex);

                        ContentValues values = new ContentValues();
                        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, 1);
                        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, 1);


                        getContentResolver().update(ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id), values, null, null);
                    }while(cursor.moveToNext());
                }

                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
