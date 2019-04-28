package com.example.android.grocerie;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.data.IngredientDbHelper;

public class IngredientEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private boolean mIngredientHasChanged = false;

    private static final int EXISTING_INGREDIENT_LOADER = 0;

    private Uri mCurrentIngredientUri;

    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the pet's Amount
     */
    private EditText mAmountEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mUnitEditText;

    private Switch mCheckedSwitch;

    private Spinner mCategorySpinner;

    private int mCategory = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_editor);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mCurrentIngredientUri = intent.getData();

        Log.e("myTag", "The uri of the current row is " + mCurrentIngredientUri);



        if (mCurrentIngredientUri == null) {
            setTitle(R.string.editor_activity_title_new_ingredient);
            invalidateOptionsMenu();
        } else {
            setTitle(R.string.editor_activity_title_edit_ingredient);
            getLoaderManager().initLoader(EXISTING_INGREDIENT_LOADER, null, this);

        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_ingredient_name);
        mAmountEditText = (EditText) findViewById(R.id.edit_ingredient_amount);
        mUnitEditText = (EditText) findViewById(R.id.edit_ingredient_unit);
        mCheckedSwitch = (Switch) findViewById(R.id.edit_ingredient_checked);
        mCategorySpinner = (Spinner) findViewById(R.id.edit_ingredient_category);



        setupSpinner();


        mNameEditText.setOnTouchListener(mTouchListener);
        mAmountEditText.setOnTouchListener(mTouchListener);
        mUnitEditText.setOnTouchListener(mTouchListener);
        mCheckedSwitch.setOnTouchListener(mTouchListener);
        mCategorySpinner.setOnTouchListener(mTouchListener);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.fruit_and_veggie))) {
                        mCategory = IngredientEntry.FRUIT_AND_VEG;
                    } else if (selection.equals(getString(R.string.meat_and_prot))) {
                        mCategory = IngredientEntry.MEAT_AND_PROT;
                    } else if (selection.equals(getString(R.string.bread_and_grain))) {
                        mCategory = IngredientEntry.BREAD_AND_GRAIN;
                    } else if (selection.equals(getString(R.string.dairy))) {
                        mCategory = IngredientEntry.DAIRY;
                    } else if (selection.equals(getString(R.string.frozen))) {
                        mCategory = IngredientEntry.FROZEN;
                    } else if (selection.equals(getString(R.string.canned))) {
                        mCategory = IngredientEntry.CANNED;
                    } else if (selection.equals(getString(R.string.drinks))) {
                        mCategory = IngredientEntry.DRINKS;
                    } else if (selection.equals(getString(R.string.snacks))) {
                        mCategory = IngredientEntry.SNACKS;
                    } else {
                        mCategory = IngredientEntry.MISC;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCategory = IngredientEntry.MISC; // Unknown

            }
        });
    }


        private void saveIngredient () {

            String nameString = mNameEditText.getText().toString().trim();
            String amountString = mAmountEditText.getText().toString().trim();
            String unitString = mUnitEditText.getText().toString().trim();

            if (mCurrentIngredientUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(amountString) &&
                    TextUtils.isEmpty(unitString) && mCategory == IngredientEntry.MISC) {
                return;
            }


            int amount = 0;
            if (!TextUtils.isEmpty(amountString)) {
                amount = Integer.parseInt(amountString);
            }

            int checked;
            if (mCheckedSwitch.isChecked()) {
                checked = 1;
            } else {
                checked = 0;
            }


            IngredientDbHelper mDbHelper = new IngredientDbHelper(this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, nameString);
            values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, amount);
            values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, unitString);
            values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checked);
            values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, mCategory);


            if (mCurrentIngredientUri == null) {
                Uri newUri = getContentResolver().insert(IngredientEntry.CONTENT_URI, values);


                if (newUri == null) {
                    Toast.makeText(this, R.string.editor_insert_ingredient_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.editor_insert_ingredient_succesful, Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowsAffected = getContentResolver().update(mCurrentIngredientUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, R.string.editor_update_ingredient_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.editor_update_ingredient_succesful, Toast.LENGTH_SHORT).show();
                }
            }
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu options from the res/menu/menu_editor.xml file.
            // This adds menu items to the app bar.
            getMenuInflater().inflate(R.menu.menu_ingredient_editor, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // User clicked on a menu option in the app bar overflow menu
            switch (item.getItemId()) {
                // Respond to a click on the "Save" menu option
                case R.id.action_save:
                    //save pet to the database
                    saveIngredient();
                    //exit activity
                    finish();

                    return true;
                // Respond to a click on the "Delete" menu option
                case R.id.action_delete:

                    showDeleteConfirmationDialog();
                    return true;
                // Respond to a click on the "Up" arrow button in the app bar
                case android.R.id.home:
                    // If the pet hasn't changed, continue with navigating up to parent activity
                    // which is the {@link CatalogActivity}.
                    if (!mIngredientHasChanged) {
                        NavUtils.navigateUpFromSameTask(IngredientEditor.this);
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
                                    NavUtils.navigateUpFromSameTask(IngredientEditor.this);
                                }
                            };

                    // Show a dialog that notifies the user they have unsaved changes
                    showUnsavedChangesDialog(discardButtonClickListener);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public Loader<Cursor> onCreateLoader ( int i, Bundle bundle){

            String[] projection = {
                    IngredientEntry._ID,
                    IngredientEntry.COLUMN_INGREDIENT_NAME,
                    IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                    IngredientEntry.COLUMN_INGREDIENT_UNIT,
                    IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                    IngredientEntry.COLUMN_INGREDIENT_CATEGORY};


            return new CursorLoader(this,
                    mCurrentIngredientUri,
                    projection,
                    null,
                    null,
                    null);

        }

        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor cursor){

            // Bail early if the cursor is null or there is less than 1 row in the cursor
            if (cursor == null || cursor.getCount() < 1) {
                return;
            }

            if (cursor.moveToFirst()) {
                int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
                int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
                int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
                int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
                int categoryColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);



                Log.e("myTag", "name column = " + nameColumnIndex);
                Log.e("myTag", "amount column = " + amountColumnIndex);
                Log.e("myTag", "unit column = " + unitColumnIndex);
                Log.e("myTag", "checked column = " + checkedColumnIndex);
                Log.e("myTag", "category column = " + categoryColumnIndex);

                // Extract out the value from the Cursor for the given column index
                String name = cursor.getString(nameColumnIndex);
                int amount = cursor.getInt(amountColumnIndex);
                String unit = cursor.getString(unitColumnIndex);
                int checked = cursor.getInt(checkedColumnIndex);
                int category = cursor.getInt(categoryColumnIndex);


                // Update the views on the screen with the values from the database
                mNameEditText.setText(name);
                mAmountEditText.setText(Integer.toString(amount));
                mUnitEditText.setText(unit);

                if (checked == 1) {
                    mCheckedSwitch.setChecked(true);
                } else {
                    mCheckedSwitch.setChecked(false);
                }

                switch (category) {
                    case IngredientEntry.FRUIT_AND_VEG:
                        mCategorySpinner.setSelection(0);
                        break;
                    case IngredientEntry.MEAT_AND_PROT:
                        mCategorySpinner.setSelection(1);
                        break;
                    case IngredientEntry.BREAD_AND_GRAIN:
                        mCategorySpinner.setSelection(2);
                        break;
                    case IngredientEntry.DAIRY:
                        mCategorySpinner.setSelection(3);
                        break;
                    case IngredientEntry.FROZEN:
                        mCategorySpinner.setSelection(4);
                        break;
                    case IngredientEntry.CANNED:
                        mCategorySpinner.setSelection(5);
                        break;
                    case IngredientEntry.DRINKS:
                        mCategorySpinner.setSelection(6);
                        break;
                    case IngredientEntry.SNACKS:
                        mCategorySpinner.setSelection(7);
                        break;
                    default:
                        mCategorySpinner.setSelection(8);
                        break;
                }
            }

        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {

            // If the loader is invalidated, clear out all the data from the input fields.
            mNameEditText.setText("");
            mAmountEditText.setText("");
            mUnitEditText.setText("");
            mCheckedSwitch.setChecked(false);
            mCategorySpinner.setSelection(8);

        }


        private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mIngredientHasChanged = true;
                return false;
            }
        };

        private void showUnsavedChangesDialog (
                DialogInterface.OnClickListener discardButtonClickListener){
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
        public void onBackPressed () {
            // If the pet hasn't changed, continue with handling back button press
            if (!mIngredientHasChanged) {
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
                            finish();
                        }
                    };

            // Show dialog that there are unsaved changes
            showUnsavedChangesDialog(discardButtonClickListener);
        }

        public boolean onPrepareOptionsMenu (Menu menu){
            super.onPrepareOptionsMenu(menu);
            // If this is a new pet, hide the "Delete" menu item.
            if (mCurrentIngredientUri == null) {
                MenuItem menuItem = menu.findItem(R.id.action_delete);
                menuItem.setVisible(false);
            }
            return true;
        }

        private void showDeleteConfirmationDialog () {
            // Create an AlertDialog.Builder and set the message, and click listeners
            // for the postivie and negative buttons on the dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_dialog_msg);
            builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked the "Delete" button, so delete the pet.
                    deletePet();
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
        private void deletePet () {
            if (mCurrentIngredientUri != null) {
                // Call the ContentResolver to delete the pet at the given content URI.
                // Pass in null for the selection and selection args because the mCurrentPetUri
                // content URI already identifies the pet that we want.
                int rowsDeleted = getContentResolver().delete(mCurrentIngredientUri, null, null);

                // Show a toast message depending on whether or not the delete was successful.
                if (rowsDeleted == 0) {
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(this, getString(R.string.editor_delete_ingredient_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_delete_ingredient_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        }
    }
