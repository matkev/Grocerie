package com.example.android.grocerie.fragmentVersion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.grocerie.R;
import com.google.android.material.tabs.TabLayout;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

public class MainShoppingListActivity extends AppCompatActivity {

    ViewPager viewPager;
    private static final int TO_BUY_LIST = 0;
    private static final int PICKED_UP_LIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shopping_list_fragments);

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

        String[] selectionArgs = new String[]{"1"};
        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, selection, selectionArgs);

        Log.e("myTag", "rows of checked items updated: " + rowsUpdated);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsUpdated == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.shopping_list_clear_all_items_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.shopping_list_clear_all_items_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void clearPickedUpItems()
    {
        ContentValues values = new ContentValues();
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, "0");
        values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, "0");

        String selection = IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + "=?";

        String[] selectionArgs = new String[]{"1"};

        int rowsUpdated = getContentResolver().update(IngredientEntry.CONTENT_URI, values, selection, selectionArgs);

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsUpdated == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.shopping_list_clear_picked_up_items_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.shopping_list_clear_picked_up_items_successful),
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
            return new ShoppingFragment(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PICKED_UP_LIST:
                    return mContext.getString(R.string.shopping_list_picked_up);
                default:
                    return mContext.getString(R.string.shopping_list_my_list);
            }
        }
    }
}
