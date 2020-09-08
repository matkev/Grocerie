package com.example.android.grocerieDev.MainActivitiesAndFragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.loader.content.CursorLoader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.android.grocerieDev.CategoryCursorAdapter;
import com.example.android.grocerieDev.EmptyRecyclerView;
import com.example.android.grocerieDev.R;
import com.example.android.grocerieDev.RecyclerCursorAdapter;
import com.example.android.grocerieDev.data.CategoryContract.CategoryEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.example.android.grocerieDev.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_POSITION;

public class CategoryListActivity extends AppCompatActivity {

    // The editor request code
    static final int EDITOR_REQUEST = 1;

    //possible results received from editor
    public static final int INSERT_FAIL = 0;
    public static final int INSERT_SUCCESS = 1;
    public static final int UPDATE_FAIL = 2;
    public static final int UPDATE_SUCCESS = 3;
    public static final int DELETE_FAIL = 4;
    public static final int DELETE_SUCCESS = 5;
    public static final int NO_CHANGE = 6;

    //views
    View mainLayout;
    EmptyRecyclerView mRecyclerView;
    RelativeLayout emptyView;

    Context mContext;

    //recyclerview adapter
    private CategoryCursorAdapter mCursorAdapter;

    private static final int CATEGORY_LOADER = 0;

    private LoaderManager.LoaderCallbacks<Cursor> categoryListLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            Log.e("cats", "CatList.onCreateLoader called");

            String[] projection = {
                    CategoryEntry._ID,
                    CategoryEntry.COLUMN_CATEGORY_NAME};

//            for (int j = 0; j < projection.length; j++)
//                Log.e("cats", "CatList.onCreateLoader: projection " + j + ": "+ projection[j]);

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
            mContext.getContentResolver().registerContentObserver(CategoryEntry.CONTENT_URI, false, observer);

            Log.e("cats", "CatList.onCreateLoader: category CONTENT_URI : " + CategoryEntry.CONTENT_URI);

            return new CursorLoader(CategoryListActivity.this,
                    CategoryEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            Log.e("cats", "CatList.onLoadFinished called");
//
//            // Bail early if the cursor is null or there is less than 1 row in the cursor
//            if (data == null || data.getCount() < 1) {
//                if (data == null) {
//                    Log.e("cats", "CatList.onLoadFinished cursor is null");
//                }
//                else if (data.getCount() < 1)
//                {
//                    Log.e("cats", "CatList.onLoadFinished cursor.getCount(): " + data.getCount());
//                }
//                return;
//            }
//
//            if (data.moveToFirst()) {
//                do {
//                    int nameColumnIndex = data.getColumnIndex(CategoryEntry.COLUMN_CATEGORY_NAME);
//                    String name = data.getString(nameColumnIndex);
//
//                    Log.e("cats", "CatListActivity.onLoadFinished name value = " + name);
//                } while (data.moveToNext());
//            }
            mCursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            Log.e("cats", "CatList.onLoadReset called");
            mCursorAdapter.swapCursor(null);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Log.e("cats", "CatListActivity.onCreate called");
        Log.e("cats", "CatListActivity.onCreate context: " + this);
        Log.e("cats", "CatListActivity.onCreate application context: " + getApplicationContext());


        setContentView(R.layout.activity_category);

        initToolbar();

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryListActivity.this, CategoryEditor.class);
                startActivityForResult(intent, EDITOR_REQUEST);
            }
        });

        //binding views
        mainLayout = findViewById(R.id.main_layout_id);
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.categories_list_view_recycler);
        emptyView = findViewById(R.id.empty_view);

        //setting empty view
        mRecyclerView.setEmptyView(emptyView);

        //setting up recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCursorAdapter = new CategoryCursorAdapter();
        mRecyclerView.setAdapter(mCursorAdapter);

        mCursorAdapter.notifyDataSetChanged();

        //starting loader
        LoaderManager.getInstance(this).initLoader(CATEGORY_LOADER, null, categoryListLoader);

    }
//
//    public void onStart(){
//        super.onStart();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mCursorAdapter = new CategoryCursorAdapter();
//        mRecyclerView.setAdapter(mCursorAdapter);
//
//
//
//        //starting loader
//        LoaderManager.getInstance(CategoryListActivity.this).initLoader(CATEGORY_LOADER, null, categoryListLoader);
//    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.scrolling_toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.categories_list_activity_title));
    }

    //decides which snackbar to display depending on the result received from the editor
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EDITOR_REQUEST) {

            // Make sure the request was successful
            switch (resultCode)
            {
                case INSERT_FAIL:
                    Log.e("intent", "return code was 0");
                    insertFailResultHandler();
                    return;
                case INSERT_SUCCESS:
                    Log.e("intent", "return code was 1");
                    insertSuccessResultHandler(data);
                    return;
                case UPDATE_FAIL:
                    Log.e("intent", "return code was 2");
                    updateFailResultHandler();
                    return;
                case UPDATE_SUCCESS:
                    Log.e("intent", "return code was 3");
                    updateSuccessResultHandler(data);
                    return;
                case DELETE_FAIL:
                    Log.e("intent", "return code was 4");
                    deleteFailResultHandler();
                    return;
                case DELETE_SUCCESS:
                    Log.e("intent", "return code was 5");
                    deleteSuccessResultHandler(data);
                    return;
                case NO_CHANGE:
                    Log.e("intent", "return code was 6");
                    return;
                default:
                    return;
            }
        }
    }

    private void insertFailResultHandler()
    {
        showSnackbar(
                mainLayout,
                getString(R.string.editor_insert_category_failed),
                Toast.LENGTH_SHORT);
    }

    private void insertSuccessResultHandler(Intent data)
    {
        Uri newUri = Uri.parse(data.getStringExtra("newUri"));
        insertUndoSnackbar(
                mainLayout,
                getString(R.string.editor_insert_category_succesful),
                Toast.LENGTH_SHORT,
                newUri);
    }

    private void updateFailResultHandler()
    {
        showSnackbar(
                mainLayout,
                getString(R.string.editor_update_category_failed),
                Toast.LENGTH_SHORT);
    }

    private void updateSuccessResultHandler(Intent data)
    {
        Uri currentCategoryUri = Uri.parse(data.getStringExtra("currentCategoryUri"));
        Bundle oldValuesBundle = data.getBundleExtra("oldValues");

        ContentValues oldValues = new ContentValues();
        oldValues.put(CategoryEntry.COLUMN_CATEGORY_NAME, oldValuesBundle.getString("name"));

        updateUndoSnackbar(
                mainLayout,
                getString(R.string.editor_update_category_succesful),
                Toast.LENGTH_SHORT,
                currentCategoryUri,
                oldValues);
    }

    private void deleteFailResultHandler()
    {
        showSnackbar(
                mainLayout,
                getString(R.string.editor_delete_category_failed),
                Toast.LENGTH_SHORT);
    }

    private void deleteSuccessResultHandler(Intent data)
    {
        Bundle oldValuesBundle = data.getBundleExtra("oldValues");

        ContentValues oldValues = new ContentValues();
        oldValues.put(CategoryEntry.COLUMN_CATEGORY_NAME, oldValuesBundle.getString("name"));

//        oldValues.put(IngredientEntry.COLUMN_INGREDIENT_POSITION, oldValuesBundle.getInt("position"));

        deleteUndoSnackBar(
                mainLayout,
                getString(R.string.editor_delete_category_successful),
                Toast.LENGTH_SHORT,
                oldValues,
                oldValuesBundle);
    }


    //snackbar methods
    public void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.show();
    }

    public void insertUndoSnackbar(View view, String message, int duration, Uri uri)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(uri, null, null);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void updateUndoSnackbar(View view, String message, int duration, Uri uri, ContentValues values)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().update(uri, values, null, null);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void deleteUndoSnackBar(View view, String message, int duration, ContentValues values, Bundle bundle)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // Set an action on it, and a handler
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri newUri = getContentResolver().insert(CategoryEntry.CONTENT_URI, values);

//                ContentValues values = new ContentValues();
//                values.put(IngredientEntry.COLUMN_INGREDIENT_POSITION, bundle.getInt("position"));

//                getContentResolver().update(newUri, values, null, null);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}