package com.example.android.grocerie.ArrayListFragmentVersion;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.grocerie.R;
import com.example.android.grocerie.dragAndDropHelper.SimpleItemTouchHelperCallback;
import com.example.android.grocerie.EmptyRecyclerView;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.fragmentVersion.IngredientFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.BREAD_AND_GRAIN;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CANNED;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_CATEGORY;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_POSITION;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.DAIRY;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.DRINKS;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.FROZEN;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.FRUIT_AND_VEG;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.MEAT_AND_PROT;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.MISC;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SNACKS;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry._ID;


public class IngredientArrayListFragment extends Fragment {

    private ItemTouchHelper mItemTouchHelper;

    //views
    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    ConstraintLayout mRootView;

    //reyclerview adapter
    private RecyclerViewListAdapter mArrayListAdapter;

    private static final String ingredientCategoryKey = "IngredientCategory";

    //stores which category the current fragment is showing
    private int mIngredientCategory;

    public IngredientArrayListFragment() {
        // Required empty public constructor
    }

    //factory method with bundled arguments instead of a constructor with arguments
    //default constructor is called when fragment is drestroyed by default
    public static IngredientArrayListFragment newInstance(int ingredientCategory) {
        IngredientArrayListFragment fragment = new IngredientArrayListFragment();
        Log.e("myTag", "Called from newInstance: this ingredient category is : " + ingredientCategory);

        Bundle args = new Bundle();
        args.putInt(ingredientCategoryKey, ingredientCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("reorder", "we are in the array list ingredient fragment");

        Bundle bundle = getArguments();
        mIngredientCategory = bundle.getInt(ingredientCategoryKey);

        //only inflate the rootview if it's null
        if (mRootView == null)
        {
            Log.e("myTag", "The root view is null");
            mRootView = (ConstraintLayout) inflater.inflate(
                    R.layout.fragment_ingredient_array_list, container, false);
        }

        //binding views
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        emptyView = mRootView.findViewById(R.id.empty_view);

        //setting empty view
        mRecyclerView.setEmptyView(emptyView);

       List<Ingredient> ingredientData = getAllIngredients(mIngredientCategory);

        //setting up recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArrayListAdapter = new RecyclerViewListAdapter(INGREDIENT_LIST_TYPE, ingredientData);
        mRecyclerView.setAdapter(mArrayListAdapter);

        //setting up drag and drop implementation
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mArrayListAdapter, this.getContext());
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(ingredientData, position_dragged, position_target);

                mArrayListAdapter.notifyItemMoved(position_dragged, position_target);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        helper.attachToRecyclerView(mRecyclerView);

        Log.e("myTag", "Called from onCreateView: this ingredient category is : " + mIngredientCategory);

        return mRootView;
    }

    public void onResume()
    {
        super.onResume();
        //binding views
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        emptyView = mRootView.findViewById(R.id.empty_view);

        //setting empty view
        mRecyclerView.setEmptyView(emptyView);

        List<Ingredient> ingredientData = getAllIngredients(mIngredientCategory);

        //setting up recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArrayListAdapter = new RecyclerViewListAdapter(INGREDIENT_LIST_TYPE, ingredientData);
        mRecyclerView.setAdapter(mArrayListAdapter);

//        //setting up drag and drop implementation
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mArrayListAdapter, this.getContext());
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(ingredientData, position_dragged, position_target);

                mArrayListAdapter.notifyItemMoved(position_dragged, position_target);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        helper.attachToRecyclerView(mRecyclerView);
    }


    public ArrayList<Ingredient> getAllIngredients(int ingredientCategory)
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

        Cursor cursor = getContext().getContentResolver().query(IngredientEntry.CONTENT_URI, projection, selection, selectionArgs, COLUMN_INGREDIENT_POSITION);

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
}
