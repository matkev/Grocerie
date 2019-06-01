package com.example.android.grocerie.deprecated;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerie.Ingredient;
import com.example.android.grocerie.IngredientEditor;
import com.example.android.grocerie.R;
import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperAdapter;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperViewHolder;
import com.example.android.grocerie.dragAndDropHelper.OnStartDragListener;

import java.util.Collections;
import java.util.List;

import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.COLUMN_INGREDIENT_POSITION;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;


public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.IngredientViewHolder>
        implements ItemTouchHelperAdapter {

//    private boolean isActionMode = false;

    static final int EDITOR_REQUEST = 1;  // The request code

    private final OnStartDragListener mDragStartListener;
    private Context mContext;
    private int mType;
    private List<Ingredient> mItems;

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();

            menuInflater.inflate(R.menu.menu_edit_mode, menu);
            actionMode.setTitle("Edit Ingredient Positions");
            Log.e("reorder", "action mode created");
            MainIngredientArrayListActivity.isActionMode = true;
            notifyDataSetChanged();


            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            Log.e("reorder", "on prepare action mode called");
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            actionMode.finish();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            MainIngredientArrayListActivity.isActionMode = false;
            notifyDataSetChanged();
        }
    };

    public RecyclerViewListAdapter(int type, List<Ingredient> datalist, OnStartDragListener dragStartListener) {
        super();
        mType = type;
        mItems = datalist;
        mDragStartListener = dragStartListener;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ingredient_item, parent, false);
        IngredientViewHolder viewHolder = new IngredientViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

//        Log.e("reorder", "viewHolder position is " + holder.getAdapterPosition());
        Log.e("reorder", "we are in the list adapter");

        holder.CheckBox.setOnCheckedChangeListener(null);
        Ingredient currentIngredient = mItems.get(position);

        int idValue = currentIngredient.getId();
        String ingredientName = currentIngredient.getName();
        String ingredientAmount = currentIngredient.getAmount();
        String ingredientUnit = currentIngredient.getUnit();
        int ingredientCategory = currentIngredient.getCategory();
        int ingredientPosition = currentIngredient.getPosition();

        int ingredientChecked;
        if (mType == INGREDIENT_LIST_TYPE) {
            ingredientChecked = currentIngredient.getTo_buy();
        } else {
            ingredientChecked = currentIngredient.getPicked_up();
        }

        if (ingredientAmount == null || TextUtils.isEmpty(ingredientAmount)) {
            ingredientAmount = "";
        }


        if (mType == INGREDIENT_LIST_TYPE) {
            Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

            Log.e("reorder", "the current ingredient is " + currentIngredientUri);
            int databasePosition = currentIngredient.getPosition();
            ;
            int listPosition = holder.getAdapterPosition();
            if (databasePosition != listPosition) {
                Log.e("reorder", "order of " + currentIngredientUri.toString() + " was updated from " + databasePosition + " to " + listPosition);

                currentIngredient.setPosition(listPosition);
            }
        }


        if (ingredientChecked == 1) {
            holder.CheckBox.setChecked(true);
        } else {
            holder.CheckBox.setChecked(false);
        }

        if (mType == SHOPPING_LIST_TYPE) // || mType == INGREDIENT_LIST_TYPE)
        {
            holder.handleView.setVisibility(View.INVISIBLE);

            switch (ingredientCategory) {
                case IngredientEntry.FRUIT_AND_VEG:
                    holder.categoryTextView.setText(R.string.fruit_and_veggie);
                    break;
                case IngredientEntry.MEAT_AND_PROT:
                    holder.categoryTextView.setText(R.string.meat_and_prot);
                    break;
                case IngredientEntry.BREAD_AND_GRAIN:
                    holder.categoryTextView.setText(R.string.bread_and_grain);
                    break;
                case IngredientEntry.DAIRY:
                    holder.categoryTextView.setText(R.string.dairy);
                    break;
                case IngredientEntry.FROZEN:
                    holder.categoryTextView.setText(R.string.frozen);
                    break;
                case IngredientEntry.CANNED:
                    holder.categoryTextView.setText(R.string.canned);
                    break;
                case IngredientEntry.DRINKS:
                    holder.categoryTextView.setText(R.string.drinks);
                    break;
                case IngredientEntry.SNACKS:
                    holder.categoryTextView.setText(R.string.snacks);
                    break;
                default:
                    holder.categoryTextView.setText(R.string.misc);
                    break;
            }
        }

        holder.nameTextView.setText(ingredientName);
        holder.summaryTextView.setText(ingredientAmount + " " + ingredientUnit + " position: " + ingredientPosition);

        holder.CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                String checkboxString;
                int checkboxInt;

                if (isChecked) {
                    checkboxString = "1";
                    checkboxInt = 1;
                } else {
                    checkboxString = "0";
                    checkboxInt = 0;
                }

                Log.e("myTag", "The checked checkbox of the current row is " + checkboxString);

                ContentValues values = new ContentValues();

                if (mType == INGREDIENT_LIST_TYPE) {
                    values.put(IngredientEntry.COLUMN_INGREDIENT_CHECKED, checkboxString);
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, 0);

                    currentIngredient.setTo_buy(checkboxInt);
                    currentIngredient.setPicked_up(0);

                } else {
                    values.put(IngredientEntry.COLUMN_INGREDIENT_PICKED_UP, checkboxString);
                    currentIngredient.setPicked_up(checkboxInt);

                }

                mContext.getContentResolver().update(currentIngredientUri, values, null, null);
            }
        });

        holder.ingredientSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("myTag", "The id of the current row is " + idValue);

                Intent intent = new Intent(mContext, IngredientEditor.class);

                Uri currentIngredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, idValue);

                Log.e("myTag", "The uri of the current row is " + currentIngredientUri);

                intent.setData(currentIngredientUri);

                ((Activity) mContext).startActivityForResult(intent, EDITOR_REQUEST);
            }
        });

        holder.ingredientSummary.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                return true;
            }
        });

        if (mType == INGREDIENT_LIST_TYPE) {
            // Start a drag whenever the handle view it touched
            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
//                }
                    return false;
                }
            });
        }

        if (MainIngredientArrayListActivity.isActionMode)
        {
            holder.handleView.setVisibility(View.VISIBLE);
            Log.e("reorder", "set handles to visible");
        }
        else
        {
            holder.handleView.setVisibility(View.GONE);
            Log.e("reorder", "set handles to gone");

        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {

        Collections.swap(mItems, fromPosition, toPosition);


        notifyItemMoved(fromPosition, toPosition);
    }


    class IngredientViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView nameTextView;
        TextView summaryTextView;
        TextView categoryTextView;
        CheckBox CheckBox;
        LinearLayout ingredientSummary;
        ImageView handleView;
        int beforeMovePosition;
        int afterMovePosition;

        IngredientViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewName);
            summaryTextView = itemView.findViewById(R.id.textViewSummary);
            CheckBox = itemView.findViewById(R.id.checkBoxView);
            ingredientSummary = itemView.findViewById(R.id.ingredient_summary);
            handleView = itemView.findViewById(R.id.handle);


        }

        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
            beforeMovePosition = this.getAdapterPosition();

            displayItems();

        }

        public void onItemClear() {
            itemView.setBackgroundColor(0);
            afterMovePosition = this.getAdapterPosition();
            displayItems();

            if (afterMovePosition != beforeMovePosition) {
                if (afterMovePosition > beforeMovePosition) {
                    for (int i = beforeMovePosition; i < afterMovePosition; i++) {
                        int id = mItems.get(i).getId();
                        updatePosition(id, i);
                        mItems.get(i).setPosition(i);
                    }
                }
                if (afterMovePosition < beforeMovePosition)
                {
                    for (int i = beforeMovePosition; i > afterMovePosition; i--)
                    {
                        int id = mItems.get(i).getId();
                        updatePosition(id, i);
                        mItems.get(i).setPosition(i);
                    }
                }
            }

            int id = mItems.get(afterMovePosition).getId();
            updatePosition(id, afterMovePosition);
            mItems.get(afterMovePosition).setPosition(afterMovePosition);

            notifyDataSetChanged();
        }

        public void update()
        {

        }
    }

    public void updatePosition(int uriId, int newPosition) {

        Uri uriToUpdate = ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, uriId);
        ContentValues values = new ContentValues();
        values.put(COLUMN_INGREDIENT_POSITION, Integer.toString(newPosition));

        mContext.getContentResolver().update(uriToUpdate, values, null, null);
    }

    public void displayItems()
    {
        Log.e("reorder", "displaying all items names///////");

        for (int i = 0;  i < mItems.size(); i ++)
        {
            String name = mItems.get(i).getName();
            Log.e("reorder", name);
        }
    }
}