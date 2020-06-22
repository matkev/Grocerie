package com.example.android.grocerieDev;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.grocerieDev.MainActivitiesAndFragments.CategoryActivity;
import com.example.android.grocerieDev.MainActivitiesAndFragments.MainIngredientListActivity;
import com.example.android.grocerieDev.MainActivitiesAndFragments.MainShoppingListActivity;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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

    public void sendToCategoryList (View view)
    {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }
}
