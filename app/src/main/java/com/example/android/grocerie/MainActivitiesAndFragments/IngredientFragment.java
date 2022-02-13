package com.example.android.grocerie.MainActivitiesAndFragments;

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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.grocerie.R;
import com.example.android.grocerie.dragAndDropHelper.SimpleItemTouchHelperCallback;
import com.example.android.grocerie.EmptyRecyclerView;
import com.example.android.grocerie.RecyclerCursorAdapter;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.BREAD_AND_GRAIN;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CANNED;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.DAIRY;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.DRINKS;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.FROZEN;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.FRUIT_AND_VEG;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.MEAT_AND_PROT;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.MISC;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SNACKS;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SPICES;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.NON_FOOD;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CONDIMENTS;


public class IngredientFragment extends Fragment {

    private ItemTouchHelper mItemTouchHelper;


    //loader ids
    //TODO: change how the loaders ids are initialised
    private static final int FRUIT_AND_VEGGIE_LOADER = FRUIT_AND_VEG;
    private static final int MEAT_AND_PROT_LOADER = MEAT_AND_PROT;
    private static final int BREAD_AND_GRAIN_LOADER = BREAD_AND_GRAIN;
    private static final int DAIRY_LOADER = DAIRY;
    private static final int FROZEN_LOADER = FROZEN;
    private static final int CANNED_LOADER = CANNED;
    private static final int DRINKS_LOADER = DRINKS;
    private static final int SNACKS_LOADER = SNACKS;
    private static final int MISC_LOADER = MISC;
    private static final int SPICES_LOADER = SPICES;
    private static final int NON_FOOD_LOADER = NON_FOOD;
    private static final int CONDIMENTS_LOADER = CONDIMENTS;

    //views
    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    ConstraintLayout mRootView;

    //reyclerview adapter
    private RecyclerCursorAdapter mCursorAdapter;

    private static final String ingredientCategoryKey = "IngredientCategory";

    //stores which category the current fragment is showing
    private int mIngredientCategory;

    public IngredientFragment() {
        // Required empty public constructor
    }

    private LoaderManager.LoaderCallbacks<Cursor> ingredientListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            String [] projection = {
                    IngredientEntry._ID,
                    IngredientEntry.COLUMN_INGREDIENT_NAME,
                    IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                    IngredientEntry.COLUMN_INGREDIENT_UNIT,
                    IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                    IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
                    IngredientEntry.COLUMN_INGREDIENT_PICKED_UP,
                    IngredientEntry.COLUMN_INGREDIENT_POSITION};

            String selection = IngredientEntry.COLUMN_INGREDIENT_CATEGORY + "=?";

            String[] selectionArgs;
            switch (id) {
                case FRUIT_AND_VEGGIE_LOADER:
                    Log.e("myTag", "The selection args is : " + FRUIT_AND_VEG);
                    selectionArgs = new String[]{Integer.toString(FRUIT_AND_VEG)};
                    break;
                case MEAT_AND_PROT_LOADER:
                    Log.e("myTag", "The selection args is : " + MEAT_AND_PROT);
                    selectionArgs = new String[]{Integer.toString(MEAT_AND_PROT)};
                    break;
                case BREAD_AND_GRAIN_LOADER:
                    Log.e("myTag", "The selection args is : " + BREAD_AND_GRAIN);
                    selectionArgs = new String[]{Integer.toString(BREAD_AND_GRAIN)};
                    break;
                case DAIRY_LOADER:
                    Log.e("myTag", "The selection args is : " + DAIRY);
                    selectionArgs = new String[]{Integer.toString(DAIRY)};
                    break;
                case FROZEN_LOADER:
                    Log.e("myTag", "The selection args is : " + FROZEN);
                    selectionArgs = new String[]{Integer.toString(FROZEN)};
                    break;
                case CANNED_LOADER:
                    Log.e("myTag", "The selection args is : " + CANNED);
                    selectionArgs = new String[]{Integer.toString(CANNED)};
                    break;
                case DRINKS_LOADER:
                    Log.e("myTag", "The selection args is : " + DRINKS);
                    selectionArgs = new String[]{Integer.toString(DRINKS)};
                    break;
                case SNACKS_LOADER:
                    Log.e("myTag", "The selection args is : " + SNACKS);
                    selectionArgs = new String[]{Integer.toString(SNACKS)};
                    break;
                case SPICES_LOADER:
                    Log.e("myTag", "The selection args is : " + SPICES);
                    selectionArgs = new String[]{Integer.toString(SPICES)};
                    break;
                case CONDIMENTS_LOADER:
                    Log.e("myTag", "The selection args is : " + CONDIMENTS);
                    selectionArgs = new String[]{Integer.toString(CONDIMENTS)};
                    break;
                case NON_FOOD_LOADER:
                    Log.e("myTag", "The selection args is : " + NON_FOOD);
                    selectionArgs = new String[]{Integer.toString(NON_FOOD)};
                    break;
                default:
                    Log.e("myTag", "The selection args is : " + MISC);
                    selectionArgs = new String[]{Integer.toString(MISC)};
                    break;
            }


            ContentObserver observer = new ContentObserver(new Handler()) {
                @Override
                public boolean deliverSelfNotifications() {
                    return super.deliverSelfNotifications();
                }

                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                }

                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                }
            };
            getActivity().getContentResolver().registerContentObserver(IngredientEntry.CONTENT_URI, false, observer);

            return new CursorLoader(getActivity(),
                    IngredientEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    IngredientEntry.COLUMN_INGREDIENT_POSITION);
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

    //factory method with bundled arguments instead of a constructor with arguments
    //default constructor is called when fragment is drestroyed by default
    public static IngredientFragment newInstance(int ingredientCategory) {
        IngredientFragment fragment = new IngredientFragment();
        Log.e("myTag", "Called from newInstance: this ingredient category is : " + ingredientCategory);

        Bundle args = new Bundle();
        args.putInt(ingredientCategoryKey, ingredientCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("reorder", "we are in the normal ingredient fragment");

        Bundle bundle = getArguments();
        mIngredientCategory = bundle.getInt(ingredientCategoryKey);

        //only inflate the rootview if it's null
        if (mRootView == null)
        {
            Log.e("myTag", "The root view is null");
            mRootView = (ConstraintLayout) inflater.inflate(
                    R.layout.fragment_ingredient, container, false);
        }

        //binding views
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        emptyView = mRootView.findViewById(R.id.empty_view);

        //setting empty view
        mRecyclerView.setEmptyView(emptyView);

        //setting up recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCursorAdapter = new RecyclerCursorAdapter(INGREDIENT_LIST_TYPE);
        mRecyclerView.setAdapter(mCursorAdapter);

        //setting up drag and drop implementation
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter, this.getContext());
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        Log.e("myTag", "Called from onCreateView: this ingredient category is : " + mIngredientCategory);

        //starting loader based on the category
        //TODO find different way to initiliase loaders
        switch (mIngredientCategory) {
            case IngredientEntry.FRUIT_AND_VEG:
                Log.e("myTag", "The loader id is : " + FRUIT_AND_VEG);
                LoaderManager.getInstance(getActivity()).initLoader(FRUIT_AND_VEGGIE_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.MEAT_AND_PROT:
                Log.e("myTag", "The loader id is : " + MEAT_AND_PROT);
                LoaderManager.getInstance(getActivity()).initLoader(MEAT_AND_PROT_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.BREAD_AND_GRAIN:
                Log.e("myTag", "The loader id is : " + BREAD_AND_GRAIN);
                LoaderManager.getInstance(getActivity()).initLoader(BREAD_AND_GRAIN_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.DAIRY:
                Log.e("myTag", "The loader id is : " + DAIRY);
                LoaderManager.getInstance(getActivity()).initLoader(DAIRY_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.FROZEN:
                Log.e("myTag", "The loader id is : " + FROZEN);
                LoaderManager.getInstance(getActivity()).initLoader(FROZEN_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.CANNED:
                Log.e("myTag", "The loader id is : " + CANNED);
                LoaderManager.getInstance(getActivity()).initLoader(CANNED_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.DRINKS:
                Log.e("myTag", "The loader id is : " + DRINKS);
                LoaderManager.getInstance(getActivity()).initLoader(DRINKS_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.SNACKS:
                Log.e("myTag", "The loader id is : " + SNACKS);
                LoaderManager.getInstance(getActivity()).initLoader(SNACKS_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.SPICES:
                Log.e("myTag", "The loader id is : " + SPICES);
                LoaderManager.getInstance(getActivity()).initLoader(SPICES_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.CONDIMENTS:
                Log.e("myTag", "The loader id is : " + CONDIMENTS);
                LoaderManager.getInstance(getActivity()).initLoader(CONDIMENTS_LOADER, null, ingredientListLoader);
                break;
            case IngredientEntry.NON_FOOD:
                Log.e("myTag", "The loader id is : " + NON_FOOD);
                LoaderManager.getInstance(getActivity()).initLoader(NON_FOOD_LOADER, null, ingredientListLoader);
                break;
            default:
                Log.e("myTag", "The loader id is : " + MISC);
                LoaderManager.getInstance(getActivity()).initLoader(MISC_LOADER, null, ingredientListLoader);
                break;
        }
        return mRootView;
    }
}
