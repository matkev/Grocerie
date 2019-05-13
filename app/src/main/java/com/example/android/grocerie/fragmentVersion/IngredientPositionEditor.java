package com.example.android.grocerie.fragmentVersion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.android.grocerie.fragmentVersion.RecyclerListAdapter;
import com.example.android.grocerie.EmptyRecyclerView;
import com.example.android.grocerie.Ingredient;
import com.example.android.grocerie.R;
import com.example.android.grocerie.dragAndDropHelper.OnStartDragListener;
import com.example.android.grocerie.dragAndDropHelper.SimpleItemTouchHelperCallback;


import java.util.ArrayList;
import java.util.List;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_CATEGORY;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_POSITION;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry._ID;

public class IngredientPositionEditor extends AppCompatActivity implements OnStartDragListener {


    private ItemTouchHelper mItemTouchHelper;

    //views
    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    ConstraintLayout mRootView;

    private RecyclerListAdapter mArrayListAdapter;

    private int mIngredientCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_position_editor);

        //setup custom toolbar
        initToolbar();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting the current ingredient uri from the intent data
        Intent intent = getIntent();
        mIngredientCategory = intent.getIntExtra("currentCategory", 0);


        //binding views
        mRecyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.empty_view);

        //setting empty view
        mRecyclerView.setEmptyView(emptyView);

        List<Ingredient> ingredientData = getCategoryIngredients(mIngredientCategory);

        //setting up recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayListAdapter = new RecyclerListAdapter(INGREDIENT_LIST_TYPE, ingredientData, this);
        mRecyclerView.setAdapter(mArrayListAdapter);

//        setting up drag and drop implementation
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mArrayListAdapter, this);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public void onResume()
    {
        super.onResume();
        //binding views
        mRecyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.empty_view);

        //setting empty view
        mRecyclerView.setEmptyView(emptyView);

        List<Ingredient> ingredientData = getCategoryIngredients(mIngredientCategory);

        //setting up recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayListAdapter = new RecyclerListAdapter(INGREDIENT_LIST_TYPE, ingredientData, this);
        mRecyclerView.setAdapter(mArrayListAdapter);

        //setting up drag and drop implementation
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mArrayListAdapter, this);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Edit Ingredient Positions");


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_edit_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Ingredient> getCategoryIngredients(int ingredientCategory)
    {

        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP,
                IngredientEntry.COLUMN_INGREDIENT_POSITION};

        String selection = COLUMN_INGREDIENT_CATEGORY + "=?";

        String[] selectionArgs = new String[]{Integer.toString(ingredientCategory)};

        Cursor cursor = this.getContentResolver().query(IngredientEntry.CONTENT_URI, projection, selection, selectionArgs, COLUMN_INGREDIENT_POSITION);

        ArrayList<Ingredient> allIngredients = new ArrayList<>();
        if (cursor.moveToFirst())
        {

            do {
                int idColumnIndex = cursor.getColumnIndex(_ID);
                int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
                int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
                int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
                int toBuyColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
                int pickedUpColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP);
                int categoryColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);
                int positionColumnIndex = cursor.getColumnIndex(COLUMN_INGREDIENT_POSITION);

                // Extract out the value from the Cursor for the given column index
                int id = cursor.getInt(idColumnIndex);
                String name = cursor.getString(nameColumnIndex);
                String amount = cursor.getString(amountColumnIndex);
                String unit = cursor.getString(unitColumnIndex);
                int toBuy = cursor.getInt(toBuyColumnIndex);
                int category = cursor.getInt(categoryColumnIndex);
                int pickedUp = cursor.getInt(pickedUpColumnIndex);
                int position = cursor.getInt(positionColumnIndex);


                allIngredients.add(new Ingredient (id, name, amount, unit, toBuy, category, pickedUp, position));


            }while(cursor.moveToNext());
        }
        return allIngredients;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}


