package com.example.android.grocerieDev;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grocerieDev.MainActivitiesAndFragments.CategoryEditor;
import com.example.android.grocerieDev.data.CategoryContract;
import com.example.android.grocerieDev.data.CategoryContract.CategoryEntry;
import com.example.android.grocerieDev.dragAndDropHelper.ItemTouchHelperAdapter;


public class CategoryCursorAdapter extends BaseCursorAdapter<CategoryCursorAdapter.CategoryViewHolder>
        implements ItemTouchHelperAdapter
{
    static final int EDITOR_REQUEST = 1;  // The request code

    private Context mContext;

    public CategoryCursorAdapter() {
        super(null);
        Log.e("cats", "CategoryCursorAdapter constructor called");
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        Log.e("cats", "CategoryCursorAdapter.onCreateViewHolder called");

        View categoryItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_ingredient_item, parent, false);
        return new CategoryViewHolder(categoryItemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, Cursor cursor) {
        Log.e("cats", "CategoryCursorAdapter.onBindViewHolder called");

        int idIndex = cursor.getColumnIndex(CategoryEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(CategoryEntry.COLUMN_CATEGORY_NAME);

        int categoryId = cursor.getInt(idIndex);
        String categoryName = cursor.getString(nameColumnIndex);

        holder.nameTextView.setText(categoryName);

//        holder.ingredientSummary.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Intent intent = new Intent(mContext, IngredientPositionEditor.class);
//                intent.putExtra("currentCategory", category);
//                mContext.startActivity(intent);
//                return true;
//            }
//        });

        holder.categorySummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CategoryEditor.class);
                Uri currentCategoryUri = ContentUris.withAppendedId(CategoryContract.CategoryEntry.CONTENT_URI, categoryId);

                Log.e("cats", "CatCursorLoader.onClick: uri of current category is " + currentCategoryUri);

                intent.setData(currentCategoryUri);
                ((Activity)mContext).startActivityForResult(intent, EDITOR_REQUEST);
            }
        });
    }


    public void onItemMove(int fromPosition, int toPosition) {
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{ // implements ItemTouchHelperViewHolder {

        TextView nameTextView;
        LinearLayout categorySummary;

        CategoryViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.textViewName);
            categorySummary = itemView.findViewById(R.id.ingredient_summary);
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