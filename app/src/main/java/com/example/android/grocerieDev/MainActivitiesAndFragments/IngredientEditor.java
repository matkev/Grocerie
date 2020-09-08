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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.android.grocerieDev.R;
import com.example.android.grocerieDev.data.IngredientContract.IngredientEntry;
import com.example.android.grocerieDev.data.CategoryContract.CategoryEntry;

public class IngredientEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //possible results to set
    public static final int INSERT_FAIL = 0;
    public static final int INSERT_SUCCESS = 1;
    public static final int UPDATE_FAIL = 2;
    public static final int UPDATE_SUCCESS = 3;
    public static final int DELETE_FAIL = 4;
    public static final int DELETE_SUCCESS = 5;
    public static final int NO_CHANGE = 6;

    //has the current ingredient in the editor (blank or pre-existing) been changed
    private boolean mIngredientHasChanged = false;

    //ID of the loader for the current ingredient
    private static final int CURRENT_INGREDIENT_LOADER = 0;

    //uri of the current ingredient
    private Uri mCurrentIngredientUri;

    //views within the editor
    private EditText mNameEditText;
    private EditText mAmountEditText;
    private EditText mUnitEditText;
    private Switch mCheckedSwitch;
    private Spinner mCategorySpinner;

    //value of the category to be set
    private int mCategory = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_editor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting the current ingredient uri from the intent data
        Intent intent = getIntent();
        mCurrentIngredientUri = intent.getData();

        Log.e("myTag", "The uri of the current row is " + mCurrentIngredientUri);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_ingredient_name);
        mAmountEditText = (EditText) findViewById(R.id.edit_ingredient_amount);
        mUnitEditText = (EditText) findViewById(R.id.edit_ingredient_unit);
        mCheckedSwitch = (Switch) findViewById(R.id.edit_ingredient_checked);
        mCategorySpinner = (Spinner) findViewById(R.id.edit_ingredient_category);

        setupSpinner();

        //if the uri is null, editor is adding a new ingredient
        //sets the spinner to the current category
        if (mCurrentIngredientUri == null) {
            setTitle(R.string.editor_activity_title_new_ingredient);
            //what category fragment was open when the editor was opened
            int mCurrentCategory = intent.getIntExtra("currentCategory", 0);
            mCategorySpinner.setSelection(mCurrentCategory);

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
        mAmountEditText.setOnTouchListener(mTouchListener);
        mUnitEditText.setOnTouchListener(mTouchListener);
        mCheckedSwitch.setOnTouchListener(mTouchListener);
        mCategorySpinner.setOnTouchListener(mTouchListener);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the category of the ingredient.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        //todo: use cursoradapter for spinner setup, order by position, query categories
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        // Set the integer mCategory to the constant values
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.fruit_and_veggie))) {
                        mCategory = CategoryEntry.CATEGORY_0;
                    } else if (selection.equals(getString(R.string.meat_and_prot))) {
                        mCategory = CategoryEntry.CATEGORY_1;
                    } else if (selection.equals(getString(R.string.bread_and_grain))) {
                        mCategory = CategoryEntry.CATEGORY_2;
                    } else if (selection.equals(getString(R.string.dairy))) {
                        mCategory = CategoryEntry.CATEGORY_3;
                    } else if (selection.equals(getString(R.string.frozen))) {
                        mCategory = CategoryEntry.CATEGORY_4;
                    } else if (selection.equals(getString(R.string.canned))) {
                        mCategory = CategoryEntry.CATEGORY_5;
                    } else if (selection.equals(getString(R.string.drinks))) {
                        mCategory = CategoryEntry.CATEGORY_6;
                    } else if (selection.equals(getString(R.string.snacks))) {
                        mCategory = CategoryEntry.CATEGORY_7;
                    } else {
                        mCategory = CategoryEntry.CATEGORY_8;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //todo: this will never happen because of selection in onCreate
                mCategory = CategoryEntry.CATEGORY_8; // miscellaneous

            }
        });
    }


    private void saveIngredient() {

        String nameString = mNameEditText.getText().toString().trim();
        String amountString = mAmountEditText.getText().toString().trim();
        String unitString = mUnitEditText.getText().toString().trim();

        if (mCurrentIngredientUri == null && TextUtils.isEmpty(nameString)) {
            return;
        }

        int amount = 0;
        if (!TextUtils.isEmpty(amountString)) {
            amount = Integer.parseInt(amountString);
        }

        ContentValues values = new ContentValues();

        int checked;
        if (mCheckedSwitch.isChecked()) {
            checked = 1;
        } else {
            checked = 0;
            values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, 0);
        }

        values.put(IngredientEntry.COLUMN_INGREDIENT_NAME, nameString);
        values.put(IngredientEntry.COLUMN_INGREDIENT_AMOUNT, amount);
        values.put(IngredientEntry.COLUMN_INGREDIENT_UNIT, unitString);
        values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checked);
        values.put(IngredientEntry.COLUMN_INGREDIENT_CATEGORY, mCategory);

        if (mCurrentIngredientUri == null)
        {
            Uri newUri = getContentResolver().insert(IngredientEntry.CONTENT_URI, values);
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
            Bundle oldValues = getBundleFromUri(mCurrentIngredientUri);
            int rowsAffected = getContentResolver().update(mCurrentIngredientUri, values, null, null);

            if (rowsAffected == 0)
            {
                Intent returnIntent = new Intent();
                setResult(UPDATE_FAIL, returnIntent);
            }
            else
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("currentIngredientUri", mCurrentIngredientUri.toString());
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
                saveIngredient();
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
                if (!mIngredientHasChanged) {
                    setResult(NO_CHANGE, null);
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
                                setResult(NO_CHANGE, null);
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGREDIENT_NAME,
                IngredientEntry.COLUMN_INGREDIENT_AMOUNT,
                IngredientEntry.COLUMN_INGREDIENT_UNIT,
                IngredientEntry.COLUMN_INGREDIENT_CHECKED,
                IngredientEntry.COLUMN_INGREDIENT_CATEGORY};

        Log.e("cats", "mCurrentIngredientUri: " + mCurrentIngredientUri);

        return new CursorLoader(this,
                mCurrentIngredientUri,
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
                case CategoryEntry.CATEGORY_0:
                    mCategorySpinner.setSelection(0);
                    break;
                case CategoryEntry.CATEGORY_1:
                    mCategorySpinner.setSelection(1);
                    break;
                case CategoryEntry.CATEGORY_2:
                    mCategorySpinner.setSelection(2);
                    break;
                case CategoryEntry.CATEGORY_3:
                    mCategorySpinner.setSelection(3);
                    break;
                case CategoryEntry.CATEGORY_4:
                    mCategorySpinner.setSelection(4);
                    break;
                case CategoryEntry.CATEGORY_5:
                    mCategorySpinner.setSelection(5);
                    break;
                case CategoryEntry.CATEGORY_6:
                    mCategorySpinner.setSelection(6);
                    break;
                case CategoryEntry.CATEGORY_7:
                    mCategorySpinner.setSelection(7);
                    break;
                default:
                    mCategorySpinner.setSelection(8);
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
        if (!mIngredientHasChanged) {
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
        if (mCurrentIngredientUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_ingredient_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteIngredient();
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

    //Perform the deletion of the ingredient in the database.
    private void deleteIngredient() {

        Bundle oldValues = getBundleFromUri(mCurrentIngredientUri);
        if (mCurrentIngredientUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentIngredientUri, null, null);

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
        Cursor cursor = getContentResolver().query(mCurrentIngredientUri, null, null, null, null);

        Bundle bundle = new Bundle();

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);
            int amountColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_AMOUNT);
            int unitColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_UNIT);
            int checkedColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CHECKED);
            int pickedUpColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP);
            int categoryColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_CATEGORY);
            int positionColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_POSITION);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int amount = cursor.getInt(amountColumnIndex);
            String unit = cursor.getString(unitColumnIndex);
            int checked = cursor.getInt(checkedColumnIndex);
            int category = cursor.getInt(categoryColumnIndex);
            int pickedUp = cursor.getInt(pickedUpColumnIndex);
            int position = cursor.getInt(positionColumnIndex);

            bundle.putString("name", name);
            bundle.putInt("amount", amount);
            bundle.putString("unit", unit);
            bundle.putInt("toBuy", checked);
            bundle.putInt("category", category);
            bundle.putInt("pickedUp", pickedUp);
            bundle.putInt("position", position);
        }
        return bundle;
    }
}
