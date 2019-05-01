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

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;


public class ShoppingFragment extends Fragment {
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


    public ShoppingFragment() {
        // Required empty public constructor
    }

    public ShoppingFragment(int listType)
    {
        Log.e("myTag", "Called from constructor: this list type is : " + listType);
        mListType = listType;
    }

    private LoaderManager.LoaderCallbacks<Cursor> shoppingListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return shoppingListLoader();

//            if (id == INGREDIENT_LOADER) {
//                Log.e("myTag", "new loader in onCreate");
//                return ingredientListLoader();
//            }
//            return null;
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

    public static ShoppingFragment newInstance(int listType) {
        ShoppingFragment fragment = new ShoppingFragment();

        Bundle args = new Bundle();
        args.putInt(listTypeKey, listType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mRecyclerView = (EmptyRecyclerView) inflater.inflate(
//                R.layout.ingredient_fragment, container, false);

        if (mRootView == null)
        {
            Log.e("myTag", "The root view is null");
            if (mListType == TO_BUY_LIST)
            {
                mRootView = (ConstraintLayout) inflater.inflate(
                        R.layout.to_buy_shopping_fragment, container, false);
            }
            else
            {
                mRootView = (ConstraintLayout) inflater.inflate(
                        R.layout.picked_up_shopping_fragment, container, false);
            }

        }

//        // Setup FAB to open EditorActivity
//        FloatingActionButton fab = mRootView.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), IngredientEditor.class);
//                intent.putExtra(ingredientCategoryKey, mIngredientCategory);
//                startActivity(intent);
//            }
//        });

        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        emptyView = mRootView.findViewById(R.id.empty_view);

        mRecyclerView.setEmptyView(emptyView);




        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCursorAdapter = new RecyclerCursorAdapter(SHOPPING_LIST_TYPE);
        mRecyclerView.setAdapter(mCursorAdapter);

        Log.e("myTag", "Called from onCreateView: this list type is : " + mListType);
        Log.e("myTag", "new loader to be initiated");

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

    private Loader<Cursor> shoppingListLoader()
    {
        String [] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY,
                IngredientEntry.COLUMN_INGREDIENT_PICKED_UP};

        String selection = IngredientEntry.COLUMN_INGREDIENT_CHECKED + "=? AND " + IngredientEntry.COLUMN_INGREDIENT_PICKED_UP + " =?";

        String[] selectionArgs;

        switch (mListType) {
            case PICKED_UP_LIST:
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
                null);
    }


}
