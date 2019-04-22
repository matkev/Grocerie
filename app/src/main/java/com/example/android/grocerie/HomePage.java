package com.example.android.grocerie;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.grocerie.data.IngredientDbHelper;

public class HomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



    }

    public void sendToList(View view)
    {
        Intent intent = new Intent (this, ShoppingLists.class);

        startActivity(intent);
    }

    public void sendToIngredients(View view)
    {
        Intent intent = new Intent(this, IngredientsList.class);

        startActivity(intent);
    }
}
