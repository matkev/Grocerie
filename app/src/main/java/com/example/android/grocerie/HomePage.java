package com.example.android.grocerie;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.android.grocerie.MainActivitiesAndFragments.MainIngredientListActivity;
import com.example.android.grocerie.MainActivitiesAndFragments.MainShoppingListActivity;


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
}
