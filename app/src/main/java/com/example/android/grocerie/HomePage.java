package com.example.android.grocerie;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.grocerie.fragmentVersion.MainIngredientListActivity;
import com.example.android.grocerie.fragmentVersion.MainShoppingListActivity;
import com.example.android.grocerie.listViewVersion.IngredientsList;
import com.example.android.grocerie.listViewVersion.ShoppingList;
import com.example.android.grocerie.recyclerViewVersion.IngredientsListRecycler;
import com.example.android.grocerie.recyclerViewVersion.ShoppingListRecycler;
import com.example.android.grocerie.recyclerViewVersion.ShoppingListRecyclerV2;

public class HomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

    }

    public void sendToList(View view)
    {
        Intent intent = new Intent (this, ShoppingListRecyclerV2.class);

        startActivity(intent);
    }

    public void sendToIngredients(View view)
    {
        Intent intent = new Intent(this, MainIngredientListActivity.class);

        startActivity(intent);
    }

    public void sendToRecyclerList(View view)
    {
        Intent intent = new Intent(this, MainShoppingListActivity.class);

        startActivity(intent);
    }

    public void sendToRecyclerIngredients (View view)
    {
        Intent intent = new Intent(this, IngredientsListRecycler.class);

        startActivity(intent);
    }
}
