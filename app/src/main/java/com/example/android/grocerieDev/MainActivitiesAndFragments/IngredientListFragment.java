package com.example.android.grocerieDev.MainActivitiesAndFragments;

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

import com.example.android.grocerieDev.R;
import com.example.android.grocerieDev.dragAndDropHelper.SimpleItemTouchHelperCallback;
import com.example.android.grocerieDev.EmptyRecyclerView;
import com.example.android.grocerieDev.RecyclerCursorAdapter;
import com.example.android.grocerieDev.data.IngredientContract.IngredientEntry;

import static com.example.android.grocerieDev.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;


public class IngredientListFragment extends Fragment {

    private ItemTouchHelper mItemTouchHelper;

    //views
    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    ConstraintLayout mRootView;

    //reyclerview adapter
    private RecyclerCursorAdapter mCursorAdapter;

    private static final String ingredientCategoryKey = "IngredientCategory";

    //stores which category the current fragment is showing
    private int mIngredientCategory;

    public IngredientListFragment() {
        // Required empty public constructor
    }

    private LoaderManager.LoaderCallbacks<Cursor> ingredientListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            Log.e("cats", "IngListFrag.onCreateLoader called");


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
            Log.e("cats", "The selection args is : " + id);
            selectionArgs = new String[]{Integer.toString(id)};

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
            Log.e("cats", "IngListFrag.onLoadFinished called");
            mCursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            Log.e("cats", "IngListFrag.onLoadReset called");
            mCursorAdapter.swapCursor(null);
        }
    };

    //factory method with bundled arguments instead of a constructor with arguments
    //default constructor is called when fragment is drestroyed by default
    public static IngredientListFragment newInstance(int ingredientCategory) {
        IngredientListFragment fragment = new IngredientListFragment();
        Log.e("myTag", "Called from newInstance: this ingredient category is : " + ingredientCategory);

        Bundle args = new Bundle();
        args.putInt(ingredientCategoryKey, ingredientCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("cats", "IngListFrag.onCreateView called");
        Log.e("cats", "IngListFrag getActivity: " + getActivity());

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
        Log.e("myTag", "The loader id is : " + mIngredientCategory);
        LoaderManager.getInstance(getActivity()).initLoader(mIngredientCategory, null, ingredientListLoader);

        return mRootView;
    }
}
