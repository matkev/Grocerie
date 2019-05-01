package com.example.android.grocerie.fragmentVersion;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.grocerie.R;
import com.example.android.grocerie.recyclerViewVersion.EmptyRecyclerView;
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


public class IngredientFragment extends Fragment {

    //loader ids
    private static final int FRUIT_AND_VEGGIE_LOADER = FRUIT_AND_VEG;
    private static final int MEAT_AND_PROT_LOADER = MEAT_AND_PROT;
    private static final int BREAD_AND_GRAIN_LOADER = BREAD_AND_GRAIN;
    private static final int DAIRY_LOADER = DAIRY;
    private static final int FROZEN_LOADER = FROZEN;
    private static final int CANNED_LOADER = CANNED;
    private static final int DRINKS_LOADER = DRINKS;
    private static final int SNACKS_LOADER = SNACKS;
    private static final int MISC_LOADER = MISC;

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

    public IngredientFragment(int ingredientCategory)
    {
        Log.e("myTag", "Called from constructor: this ingredient category is : " + ingredientCategory);
        mIngredientCategory = ingredientCategory;
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
                    IngredientEntry.COLUMN_INGREDIENT_PICKED_UP};

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
                default:
                    Log.e("myTag", "The selection args is : " + MISC);
                    selectionArgs = new String[]{Integer.toString(MISC)};
                    break;
            }

            return new CursorLoader(getActivity(),
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
    };

    public static IngredientFragment newInstance(int ingredientCategory) {
        IngredientFragment fragment = new IngredientFragment();
        Log.e("myTag", "Called from newInstance: this ingredient category is : " + ingredientCategory);

        Bundle args = new Bundle();
        args.putInt(ingredientCategoryKey, ingredientCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mRootView == null)
        {
            Log.e("myTag", "The root view is null");
            mRootView = (ConstraintLayout) inflater.inflate(
                    R.layout.fragment_ingredient, container, false);
        }

        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        emptyView = mRootView.findViewById(R.id.empty_view);

        mRecyclerView.setEmptyView(emptyView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCursorAdapter = new RecyclerCursorAdapter(INGREDIENT_LIST_TYPE);
        mRecyclerView.setAdapter(mCursorAdapter);

        Log.e("myTag", "Called from onCreateView: this ingredient category is : " + mIngredientCategory);

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
            default:
                Log.e("myTag", "The loader id is : " + MISC);
                LoaderManager.getInstance(getActivity()).initLoader(MISC_LOADER, null, ingredientListLoader);
                break;
        }

        return mRootView;
    }


//    private Loader<Cursor> ingredientListLoader()
//    {
//        String [] projection = {
//                IngredientEntry._ID,
//                IngredientEntry.COLUMN_INGREDIENT_NAME,
//                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
//                IngredientEntry.COLUMN_INGREDIENT_UNIT,
//                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
//                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
//                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP};
//
//        String selection = IngredientEntry.COLUMN_INGREDIENT_CATEGORY + "=?";
//
//        String[] selectionArgs;
//        switch (mIngredientCategory) {
//            case IngredientEntry.FRUIT_AND_VEG:
//                Log.e("myTag", "The selection args is : " + FRUIT_AND_VEG);
//                selectionArgs = new String[]{Integer.toString(FRUIT_AND_VEG)};
//                break;
//            case IngredientEntry.MEAT_AND_PROT:
//                Log.e("myTag", "The selection args is : " + MEAT_AND_PROT);
//                selectionArgs = new String[]{Integer.toString(MEAT_AND_PROT)};
//                break;
//            case IngredientEntry.BREAD_AND_GRAIN:
//                Log.e("myTag", "The selection args is : " + BREAD_AND_GRAIN);
//                selectionArgs = new String[]{Integer.toString(BREAD_AND_GRAIN)};
//                break;
//            case IngredientEntry.DAIRY:
//                Log.e("myTag", "The selection args is : " + DAIRY);
//                selectionArgs = new String[]{Integer.toString(DAIRY)};
//                break;
//            case IngredientEntry.FROZEN:
//                Log.e("myTag", "The selection args is : " + FROZEN);
//                selectionArgs = new String[]{Integer.toString(FROZEN)};
//                break;
//            case IngredientEntry.CANNED:
//                Log.e("myTag", "The selection args is : " + CANNED);
//                selectionArgs = new String[]{Integer.toString(CANNED)};
//                break;
//            case IngredientEntry.DRINKS:
//                Log.e("myTag", "The selection args is : " + DRINKS);
//                selectionArgs = new String[]{Integer.toString(DRINKS)};
//                break;
//            case IngredientEntry.SNACKS:
//                Log.e("myTag", "The selection args is : " + SNACKS);
//                selectionArgs = new String[]{Integer.toString(SNACKS)};
//                break;
//            default:
//                Log.e("myTag", "The selection args is : " + MISC);
//                selectionArgs = new String[]{Integer.toString(MISC)};
//                break;
//        }
//
//        return new CursorLoader(getActivity(),
//                IngredientEntry.CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                null);
//    }
}
