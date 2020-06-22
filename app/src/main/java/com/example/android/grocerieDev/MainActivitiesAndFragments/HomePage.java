package com.example.android.grocerieDev.MainActivitiesAndFragments;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.grocerieDev.R;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void sendToFragmentsShoppingList(View view)
    {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
    }

    public void sendToFragmentsIngredientList (View view)
    {
        Intent intent = new Intent(this, IngredientListActivity.class);
        startActivity(intent);
    }

    public void sendToCategoryList (View view)
    {
        Intent intent = new Intent(this, CategoryListActivity.class);
        startActivity(intent);
    }
}
