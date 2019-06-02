package com.example.android.grocerie;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.grocerie.data.IngredientContract;
import com.example.android.grocerie.data.IngredientContract.IngredientEntry;
import com.example.android.grocerie.dragAndDropHelper.ItemTouchHelperAdapter;
import com.example.android.grocerie.MainActivitiesAndFragments.IngredientPositionEditor;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.INGREDIENT_LIST_TYPE;
import static com.example.android.grocerie.data.IngredientContract.IngredientEntry.SHOPPING_LIST_TYPE;


public class CategoryCursorAdapter extends BaseCursorAdapter<CategoryCursorAdapter.IngredientViewHolder>
        implements ItemTouchHelperAdapter
{
    static final int EDITOR_REQUEST = 1;  // The request code

    private Context mContext;

    public CategoryCursorAdapter() {
        super(null);
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        View formNameView = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ingredient_item, parent, false);
        IngredientViewHolder viewHolder = new IngredientViewHolder(formNameView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, Cursor cursor) {

//        Log.e("reorder", "viewHolder position is " + holder.getAdapterPosition());
        Log.e("reorder", "we are in the cursor adapter");

        int idIndex = cursor.getColumnIndex(IngredientEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGREDIENT_NAME);

        int idValue = cursor.getInt(idIndex);
        String ingredientName = cursor.getString(nameColumnIndex);



        holder.nameTextView.setText(ingredientName);

//        holder.ingredientSummary.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Intent intent = new Intent(mContext, IngredientPositionEditor.class);
//                intent.putExtra("currentCategory", category);
//                mContext.startActivity(intent);
//                return true;
//            }
//        });
    }

    public void onItemMove(int fromPosition, int toPosition) {
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder{ // implements ItemTouchHelperViewHolder {

        TextView nameTextView;

        IngredientViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewName);
        }

//        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
//        }
//
//        public void onItemClear() {
//            itemView.setBackgroundColor(0);
//        }
    }
}