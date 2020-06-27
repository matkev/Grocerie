package com.example.android.grocerieDev.MainActivitiesAndFragments;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.android.grocerieDev.R;
import com.example.android.grocerieDev.data.CategoryContract.CategoryEntry;

public class CategoryEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //possible results to set
    public static final int INSERT_FAIL = 0;
    public static final int INSERT_SUCCESS = 1;
    public static final int UPDATE_FAIL = 2;
    public static final int UPDATE_SUCCESS = 3;
    public static final int DELETE_FAIL = 4;
    public static final int DELETE_SUCCESS = 5;
    public static final int NO_CHANGE = 6;


    //has the current ingredient in the editor (blank or pre-existing) been changed
    private boolean mCategoryHasChanged = false;

    //ID of the loader for the current ingredient
    private static final int CURRENT_INGREDIENT_LOADER = 0;

    //uri of the current ingredient
    private Uri mCurrentCategoryUri;

    //views within the editor
    private EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_editor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //getting the current ingredient uri from the intent data
        Intent intent = getIntent();
        mCurrentCategoryUri = intent.getData();

        Log.e("myTag", "The uri of the current row is " + mCurrentCategoryUri);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_category_name);



        //if the uri is null, editor is adding a new ingredient
        //sets the spinner to the current category
        if (mCurrentCategoryUri == null) {
            setTitle(R.string.editor_activity_title_new_ingredient);

            invalidateOptionsMenu();
        }
        //otherwise, the editor is updating an existing ingredient
        else
        {
            setTitle(R.string.editor_activity_title_edit_ingredient);
            //loads the current ingredient's values into the editor
            getLoaderManager().initLoader(CURRENT_INGREDIENT_LOADER, null, this);

        }


        //to check if any of the views are changed
        mNameEditText.setOnTouchListener(mTouchListener);
    }

        private void saveCategory() {

//        String[] projection = {
//                IngredientEntry.COLUMN_INGREDIENT_POSITION};
//
//        Cursor positionCursor = getContentResolver().query(mCurrentCategoryUri, projection, null, null, null);
//
//        int currentPosition = positionCursor.getInt(positionCursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_POSITION));

        String nameString = mNameEditText.getText().toString().trim();

        if (mCurrentCategoryUri == null && TextUtils.isEmpty(nameString)) {
            return;
        }

        ContentValues values = new ContentValues();



        values.put(CategoryEntry.COLUMN_CATEGORY_NAME, nameString);
//        values.put(IngredientEntry.COLUMN_INGREDIENT_POSITION, currentPosition);

        if (mCurrentCategoryUri == null)
        {
            Uri newUri = getContentResolver().insert(CategoryEntry.CONTENT_URI, values);
            if (newUri == null)
            {
                Intent returnIntent = new Intent();
                setResult(INSERT_FAIL, returnIntent);
            }
            else
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("newUri", newUri.toString());
                setResult(INSERT_SUCCESS, returnIntent);
            }
        }
        else
        {
            Bundle oldValues = getBundleFromUri(mCurrentCategoryUri);
            int rowsAffected = getContentResolver().update(mCurrentCategoryUri, values, null, null);

            if (rowsAffected == 0)
            {
                Intent returnIntent = new Intent();
                setResult(UPDATE_FAIL, returnIntent);
            }
            else
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("currentCategoryUri", mCurrentCategoryUri.toString());
                returnIntent.putExtra("oldValues", oldValues);
                setResult(UPDATE_SUCCESS, returnIntent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_ingredient_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //save pet to the database
                saveCategory();
                finish();

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:

                Log.e("intent", "up button pressed");
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mCategoryHasChanged) {
                    setResult(NO_CHANGE, null);
                    NavUtils.navigateUpFromSameTask(CategoryEditor.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                setResult(NO_CHANGE, null);
                                NavUtils.navigateUpFromSameTask(CategoryEditor.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                CategoryEntry._ID,
                CategoryEntry.COLUMN_CATEGORY_NAME};



        return new CursorLoader(this,
                mCurrentCategoryUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(CategoryEntry.COLUMN_CATEGORY_NAME);

            Log.e("myTag", "name column = " + nameColumnIndex);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
    }


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mCategoryHasChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mCategoryHasChanged) {

            Intent returnIntent = new Intent();
            setResult(NO_CHANGE, returnIntent);
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        Intent returnIntent = new Intent();
                        setResult(NO_CHANGE, returnIntent);
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentCategoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete category?");
        //TODO: change ingredient and category dialog delete strings
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteCategory();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteCategory() {

        Bundle oldValues = getBundleFromUri(mCurrentCategoryUri);
        if (mCurrentCategoryUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentCategoryUri, null, null);

            if (rowsDeleted == 0)
            {
                Intent returnIntent = new Intent();
                setResult(DELETE_FAIL, returnIntent);
            }
            else
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("oldValues", oldValues);
                setResult(DELETE_SUCCESS, returnIntent);
            }
        }
        finish();
    }

    private Bundle getBundleFromUri(Uri uri)
    {
        Cursor cursor = getContentResolver().query(mCurrentCategoryUri, null, null, null, null);

        Bundle bundle = new Bundle();

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(CategoryEntry.COLUMN_CATEGORY_NAME);
//            int positionColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_POSITION);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
//            int position = cursor.getInt(positionColumnIndex);

            bundle.putString("name", name);
//            bundle.putInt("position", position);
        }
        return bundle;
    }

//    private ContentValues getValuesFromUri(Uri uri)
//    {
//        Cursor cursor = getContentResolver().query(mCurrentCategoryUri, null, null, null, null);
//
//        ContentValues values = new ContentValues();
//
//        if (cursor.moveToFirst()) {
//            int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
//            int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
//            int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
//            int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
//            int pickedUpColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP);
//            int categoryColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);
//
//            // Extract out the value from the Cursor for the given column index
//            String name = cursor.getString(nameColumnIndex);
//            int amount = cursor.getInt(amountColumnIndex);
//            String unit = cursor.getString(unitColumnIndex);
//            int checked = cursor.getInt(checkedColumnIndex);
//            int category = cursor.getInt(categoryColumnIndex);
//            int pickedUp = cursor.getInt(pickedUpColumnIndex);
//
//            values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, name);
//            values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, amount);
//            values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, unit);
//            values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checked);
//            values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, category);
//            values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, pickedUp);
//
//
//        }
//        return values;
//    }
}