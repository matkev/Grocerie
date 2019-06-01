package com.example.android.grocerie;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.grocerie.deprecated.MainIngredientArrayListActivity;
import com.example.android.grocerie.deprecated.MainShoppingArrayListActivity;
import com.example.android.grocerie.fragmentVersion.MainIngredientListActivity;
import com.example.android.grocerie.fragmentVersion.MainShoppingListActivity;
import com.example.android.grocerie.deprecated.IngredientsList;
import com.example.android.grocerie.deprecated.ShoppingList;
import com.example.android.grocerie.deprecated.IngredientsListRecycler;
import com.example.android.grocerie.deprecated.ShoppingListRecycler;

public class HomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

    }

    public void sendToListViewShoppingList(View view)
    {
        Intent intent = new Intent (this, ShoppingList.class);

        startActivity(intent);
    }

    public void sendToListViewIngredientList(View view)
    {
        Intent intent = new Intent(this, IngredientsList.class);

        startActivity(intent);
    }

    public void sendToRecyclerViewShoppingList(View view)
    {
        Intent intent = new Intent (this, ShoppingListRecycler.class);

        startActivity(intent);
    }

    public void sendToRecyclerViewIngredientList(View view)
    {
        Intent intent = new Intent(this, IngredientsListRecycler.class);

        startActivity(intent);
    }

    public void sendToFragmentsShoppingList(View view)
    {
        Intent intent = new Intent(this, MainShoppingListActivity.class);

        startActivity(intent);
    }

    public void sendToFragmentsIngredientList (View view)
    {
        Intent intent = new Intent(this, MainIngredientListActivity.class);

        startActivity(intent);
    }

    public void sendToArrayListShoppingList(View view)
    {
        Intent intent = new Intent(this, MainShoppingArrayListActivity.class);

        startActivity(intent);
    }

    public void sendToArrayListIngredientList (View view)
    {
        Intent intent = new Intent(this, MainIngredientArrayListActivity.class);

        Log.e("reorder", "sending to main ingredient array list activity");

        startActivity(intent);
    }
}
