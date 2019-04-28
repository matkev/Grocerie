package com.example.android.grocerie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.grocerie.data.IngredientContract.IngredientEntry;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CHECKED_YES;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.CHECKED_NO;


import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.PICKED_UP_YES;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.PICKED_UP_NO;

/**
 * Created by matth on 4/5/2019.
 */

/*
 * {@link ContentProvider} for Pets app.
 */
public class IngredientProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = IngredientProvider.class.getSimpleName();


    IngredientDbHelper mDbHelper;



    /** URI matcher code for the content URI for the pets table */
    private static final int INGREDIENTS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int INGREDIENT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(IngredientContract.CONTENT_AUTHORITY, IngredientContract.PATH_INGREDIENTS, INGREDIENTS);
        sUriMatcher.addURI(IngredientContract.CONTENT_AUTHORITY, IngredientContract.PATH_INGREDIENTS + "/#", INGREDIENT_ID);

    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new IngredientDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;


        int match = sUriMatcher.match(uri);
        switch (match)
        {
            case INGREDIENTS:
                cursor = database.query(IngredientContract.IngredientEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INGREDIENT_ID:
                selection = IngredientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(IngredientEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;


    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case INGREDIENTS:
            {
                return insertIngredient(uri, contentValues);
            }
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }


    }

    private Uri insertIngredient (Uri uri, ContentValues values)
    {
        String name = values.getAsString(IngredientEntry.COLUMN_INGREDIENT_NAME);
        if (name == null || TextUtils.isEmpty(name))
        {
            Log.e(LOG_TAG, "string name is null");

            throw new IllegalArgumentException("Ingredient requires a name");
        }

        Integer amount = values.getAsInteger(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
        //TODO: data validation of the other database columns
//
//        if (amount == null || !isValidChecked(gender)) {
//
//            Log.e(LOG_TAG, "gender is null or invalid");
//
//            throw new IllegalArgumentException("Pet requires a valid gender");
//        }
//
//        Integer weight = values.getAsInteger(IngredientEntry.COLUMN_PET_WEIGHT);
//        if (weight != null && weight < 0)
//        {
//            Log.e(LOG_TAG, "weight is negative");
//
//            throw new IllegalArgumentException("Pet requires a valid weight");
//        }

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        long id = database.insert(IngredientEntry.TABLE_NAME, null, values);

        if (id == -1)
        {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id);
    }

    public static boolean isValidChecked(int checked)
    {
        if (checked == CHECKED_YES || checked == CHECKED_NO ) {
            return true;
        }

        return false;
    }



    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch(match)
        {
            case INGREDIENTS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case INGREDIENT_ID:
                selection = IngredientEntry._ID + "=?";

                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }



    }


    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        if (values.containsKey(IngredientEntry.COLUMN_INGREDIENT_NAME))
        {
            String name = values.getAsString(IngredientEntry.COLUMN_INGREDIENT_NAME);
            if (name == null || TextUtils.isEmpty(name))
            {
                Log.e(LOG_TAG, "string name is null");

                throw new IllegalArgumentException("Ingredient requires a name");
            }
        }

        //TODO: data validation for updating table

//        if (values.containsKey(IngredientEntry.COLUMN_PET_GENDER))
//        {
//            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
//            if (gender == null || !isValidGender(gender)) {
//
//                Log.e(LOG_TAG, "gender is null or invalid");
//
//                throw new IllegalArgumentException("Pet requires a valid gender");
//            }
//        }
//
//        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT))
//        {
//            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
//            if (weight != null && weight < 0)
//            {
//                Log.e(LOG_TAG, "weight is negative");
//
//                throw new IllegalArgumentException("Pet requires a valid weight");
//            }
//        }


        if (values.size() == 0)
        {
            return 0;
        }




        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        int rowsUpdated = database.update(IngredientEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;


    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INGREDIENTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INGREDIENT_ID:
                // Delete a single row given by the ID in the URI
                selection = IngredientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        if (rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;



    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INGREDIENTS:
                return IngredientEntry.CONTENT_LIST_TYPE;
            case INGREDIENT_ID:
                return IngredientEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}