package com.example.android.grocerie;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

    }

    public void sendToList(View view)
    {
        Intent intent = new Intent (this, ShoppingList.class);

        startActivity(intent);
    }

    public void sendToIngredients(View view)
    {
        Intent intent = new Intent(this, IngredientsList.class);

        startActivity(intent);
    }
}
