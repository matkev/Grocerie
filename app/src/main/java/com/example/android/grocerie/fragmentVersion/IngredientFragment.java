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
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.grocerie.R;
import com.example.android.grocerie.recyclerViewVersion.EmptyRecyclerView;
import com.example.android.grocerie.recyclerViewVersion.RecyclerCursorAdapter;
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

    private static final int Fruit_AND_VEGGIE_LOADER = 0;
    private static final int MEAT_AND_PROT_LOADER = 1;
    private static final int BREAD_AND_GRAIN_LOADER = 2;
    private static final int DAIRY_LOADER = 3;
    private static final int FROZEN_LOADER = 4;
    private static final int CANNED_LOADER = 5;
    private static final int DRINKS_LOADER = 6;
    private static final int SNACKS_LOADER = 7;
    private static final int MISC_LOADER = 8;

    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;
    ConstraintLayout mRootView;

    private RecyclerCursorAdapter mCursorAdapter;

    private static final String ingredientCategoryKey = "IngredientCategory";
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
            return ingredientListLoader();

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

    public static IngredientFragment newInstance(int ingredientCategory) {
        IngredientFragment fragment = new IngredientFragment();
//        mIngredientCategory = ingredientCategory;
        Log.e("myTag", "Called from newInstance: this ingredient category is : " + ingredientCategory);

        Bundle args = new Bundle();
        args.putInt(ingredientCategoryKey, ingredientCategory);
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
            mRootView = (ConstraintLayout) inflater.inflate(
                    R.layout.ingredient_fragment, container, false);
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
        mCursorAdapter = new RecyclerCursorAdapter(INGREDIENT_LIST_TYPE);
        mRecyclerView.setAdapter(mCursorAdapter);

        Log.e("myTag", "Called from onCreateView: this ingredient category is : " + mIngredientCategory);
        Log.e("myTag", "new loader to be initiated");

        switch (mIngredientCategory) {
            case IngredientEntry.FRUIT_AND_VEG:
                Log.e("myTag", "The loader id is : " + FRUIT_AND_VEG);
                LoaderManager.getInstance(getActivity()).initLoader(Fruit_AND_VEGGIE_LOADER, null, ingredientListLoader);
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


    private Loader<Cursor> ingredientListLoader()
    {
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
        switch (mIngredientCategory) {
            case IngredientEntry.FRUIT_AND_VEG:
                Log.e("myTag", "The selection args is : " + FRUIT_AND_VEG);
                selectionArgs = new String[]{Integer.toString(FRUIT_AND_VEG)};
                break;
            case IngredientEntry.MEAT_AND_PROT:
                Log.e("myTag", "The selection args is : " + MEAT_AND_PROT);
                selectionArgs = new String[]{Integer.toString(MEAT_AND_PROT)};
                break;
            case IngredientEntry.BREAD_AND_GRAIN:
                Log.e("myTag", "The selection args is : " + BREAD_AND_GRAIN);
                selectionArgs = new String[]{Integer.toString(BREAD_AND_GRAIN)};
                break;
            case IngredientEntry.DAIRY:
                Log.e("myTag", "The selection args is : " + DAIRY);
                selectionArgs = new String[]{Integer.toString(DAIRY)};
                break;
            case IngredientEntry.FROZEN:
                Log.e("myTag", "The selection args is : " + FROZEN);
                selectionArgs = new String[]{Integer.toString(FROZEN)};
                break;
            case IngredientEntry.CANNED:
                Log.e("myTag", "The selection args is : " + CANNED);
                selectionArgs = new String[]{Integer.toString(CANNED)};
                break;
            case IngredientEntry.DRINKS:
                Log.e("myTag", "The selection args is : " + DRINKS);
                selectionArgs = new String[]{Integer.toString(DRINKS)};
                break;
            case IngredientEntry.SNACKS:
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


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
}
