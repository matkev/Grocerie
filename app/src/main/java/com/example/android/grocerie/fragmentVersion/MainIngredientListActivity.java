package com.example.android.grocerie.fragmentVersion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.FRUIT_AND_VEG;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.MEAT_AND_PROT;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.BREAD_AND_GRAIN;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.DAIRY;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.FROZEN;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CANNED;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.DRINKS;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SNACKS;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.MISC;

public class MainIngredientListActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ingredient_list_fragments);

        initToolbar();

        initViewPagerAndTabs();

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainIngredientListActivity.this, IngredientEditor.class);
                intent.putExtra("IngredientCategory", viewPager.getCurrentItem());
                startActivity(intent);
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

        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
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
            case R.id.action_clear_all_entries:
                clearAllItems();
                return true;
            //TODO: sort by alphabet or most recent
        }
        return super.onOptionsItemSelected(item);
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

    private void clearAllItems()
    {
        ContentValues values = new ContentValues();

        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, "0");

        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=?";

        String[] selectionArgs = new String[]{"1"};
        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, selection, selectionArgs);

        Log.e("myTag", "rows of checked items updated: " + rowsUpdated);

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

    static class PagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public PagerAdapter(Context context, FragmentManager fragmentManager) {
            super(fragmentManager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            return new IngredientFragment(position);
        }

        @Override
        public int getCount() {
            return 9;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case FRUIT_AND_VEG:
                    return mContext.getString(R.string.fruit_and_veggie);
                case MEAT_AND_PROT:
                    return mContext.getString(R.string.meat_and_prot);
                case BREAD_AND_GRAIN:
                    return mContext.getString(R.string.bread_and_grain);
                case DAIRY:
                    return mContext.getString(R.string.dairy);
                case FROZEN:
                    return mContext.getString(R.string.frozen);
                case CANNED:
                    return mContext.getString(R.string.canned);
                case DRINKS:
                    return mContext.getString(R.string.drinks);
                case SNACKS:
                    return mContext.getString(R.string.snacks);
                default:
                    return mContext.getString(R.string.misc);
            }
        }
    }
}

