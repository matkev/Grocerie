package com.example.android.grocerieDev.MainActivitiesAndFragments;

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

import com.example.android.grocerieDev.R;
import com.example.android.grocerieDev.EmptyRecyclerView;
import com.example.android.grocerieDev.RecyclerCursorAdapter;

import com.example.android.grocerieDev.data.IngredientContract.IngredientEntry;

import static com.example.android.grocerieDev.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_CATEGORY;
import static com.example.android.grocerieDev.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;


public class ShoppingListFragment extends Fragment {
    private static final int TO_BUY_LOADER = 0;
    private static final int PICKED_UP_LOADER = 1;

    private static final int TO_BUY_LIST = 0;
    private static final int PICKED_UP_LIST = 1;

    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    ConstraintLayout mRootView;

    private RecyclerCursorAdapter mCursorAdapter;

    private static final String listTypeKey = "listType";

    private int mListType;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

//    public ShoppingFragment(int listType)
//    {
//        Log.e("myTag", "Called from constructor: this list type is : " + listType);
//        mListType = listType;
//    }

    private LoaderManager.LoaderCallbacks<Cursor> shoppingListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
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

            String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=? AND " + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + " =?";

            String[] selectionArgs;

            switch (id) {
                case PICKED_UP_LOADER:
                    Log.e("myTag", "The loader id is : " + PICKED_UP_LOADER);
                    selectionArgs = new String[]{"1","1"};
                    break;
                default:
                    Log.e("myTag", "The loader id is : " + TO_BUY_LOADER);
                    selectionArgs = new String[]{"1","0"};
                    break;
            }

            return new CursorLoader(getActivity(),
                    IngredientEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    COLUMN_INGREDIENT_CATEGORY);
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

    public static ShoppingListFragment newInstance(int listType) {
        ShoppingListFragment fragment = new ShoppingListFragment();

        Bundle args = new Bundle();
        args.putInt(listTypeKey, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mRecyclerView = (EmptyRecyclerView) inflater.inflate(
//                R.layout.fragment_ingredient, container, false);

        Bundle bundle = getArguments();

        mListType = bundle.getInt(listTypeKey);

        if (mRootView == null)
        {
            Log.e("myTag", "The root view is null");
            if (mListType == TO_BUY_LIST)
            {
                mRootView = (ConstraintLayout) inflater.inflate(
                        R.layout.fragment_to_buy_shopping, container, false);
            }
            else
            {
                mRootView = (ConstraintLayout) inflater.inflate(
                        R.layout.fragment_picked_up_shopping, container, false);
            }

        }

        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        emptyView = mRootView.findViewById(R.id.empty_view);

        mRecyclerView.setEmptyView(emptyView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCursorAdapter = new RecyclerCursorAdapter(SHOPPING_LIST_TYPE);
        mRecyclerView.setAdapter(mCursorAdapter);

        Log.e("myTag", "Called from onCreateView: this list type is : " + mListType);

        switch (mListType) {
            case PICKED_UP_LIST:
                Log.e("myTag", "The loader id is : " + PICKED_UP_LOADER);
                LoaderManager.getInstance(getActivity()).initLoader(PICKED_UP_LOADER, null, shoppingListLoader);
                break;
            default:
                Log.e("myTag", "The loader id is : " + TO_BUY_LOADER);
                LoaderManager.getInstance(getActivity()).initLoader(TO_BUY_LOADER, null, shoppingListLoader);
                break;
        }

        return mRootView;
    }

//    private Loader<Cursor> shoppingListLoader()
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
//        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=? AND " + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + " =?";
//
//        String[] selectionArgs;
//
//        switch (mListType) {
//            case PICKED_UP_LIST:
//                Log.e("myTag", "The loader id is : " + PICKED_UP_LOADER);
//                selectionArgs = new String[]{"1","1"};
//                break;
//            default:
//                Log.e("myTag", "The loader id is : " + TO_BUY_LOADER);
//                selectionArgs = new String[]{"1","0"};
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
