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

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_0;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_1;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_2;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_3;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_4;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_5;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_6;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_7;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CATEGORY_8;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;


public class IngredientFragment extends Fragment {

    private ItemTouchHelper mItemTouchHelper;


    //loader ids
    //TODO: change how the loaders ids are initialised
    private static final int CATEGORY_0_LOADER = CATEGORY_0;
    private static final int CATEGORY_1_LOADER = CATEGORY_1;
    private static final int CATEGORY_2_LOADER = CATEGORY_2;
    private static final int CATEGORY_3_LOADER = CATEGORY_3;
    private static final int CATEGORY_4_LOADER = CATEGORY_4;
    private static final int CATEGORY_5_LOADER = CATEGORY_5;
    private static final int CATEGORY_6_LOADER = CATEGORY_6;
    private static final int CATEGORY_7_LOADER = CATEGORY_7;
    private static final int CATEGORY_8_LOADER = CATEGORY_8;

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
                case CATEGORY_0_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_0);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_0)};
                    break;
                case CATEGORY_1_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_1);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_1)};
                    break;
                case CATEGORY_2_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_2);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_2)};
                    break;
                case CATEGORY_3_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_3);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_3)};
                    break;
                case CATEGORY_4_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_4);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_4)};
                    break;
                case CATEGORY_5_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_5);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_5)};
                    break;
                case CATEGORY_6_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_6);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_6)};
                    break;
                case CATEGORY_7_LOADER:
                    Log.e("myTag", "The selection args is : " + CATEGORY_7);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_7)};
                    break;
                default:
                    Log.e("myTag", "The selection args is : " + CATEGORY_8);
                    selectionArgs = new String[]{Integer.toString(CATEGORY_8)};
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
            case CATEGORY_0:
                Log.e("myTag", "The loader id is : " + CATEGORY_0);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_0_LOADER, null, ingredientListLoader);
                break;
            case CATEGORY_1:
                Log.e("myTag", "The loader id is : " + CATEGORY_1);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_1_LOADER, null, ingredientListLoader);
                break;
            case CATEGORY_2:
                Log.e("myTag", "The loader id is : " + CATEGORY_2);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_2_LOADER, null, ingredientListLoader);
                break;
            case CATEGORY_3:
                Log.e("myTag", "The loader id is : " + CATEGORY_3);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_3_LOADER, null, ingredientListLoader);
                break;
            case CATEGORY_4:
                Log.e("myTag", "The loader id is : " + CATEGORY_4);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_4_LOADER, null, ingredientListLoader);
                break;
            case CATEGORY_5:
                Log.e("myTag", "The loader id is : " + CATEGORY_5);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_5_LOADER, null, ingredientListLoader);
                break;
            case CATEGORY_6:
                Log.e("myTag", "The loader id is : " + CATEGORY_6);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_6_LOADER, null, ingredientListLoader);
                break;
            case CATEGORY_7:
                Log.e("myTag", "The loader id is : " + CATEGORY_7);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_7_LOADER, null, ingredientListLoader);
                break;
            default:
                Log.e("myTag", "The loader id is : " + CATEGORY_8);
                LoaderManager.getInstance(getActivity()).initLoader(CATEGORY_8_LOADER, null, ingredientListLoader);
                break;
        }
        return mRootView;
    }
}
