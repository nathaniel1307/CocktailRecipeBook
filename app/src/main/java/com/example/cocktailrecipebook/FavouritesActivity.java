package com.example.cocktailrecipebook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocktailrecipebook.data.CocktailContentProvider;
import com.example.cocktailrecipebook.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {
    static CocktailContentProvider cocktailDB;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_cocktails:
                    cocktails();
                    return true;
                case R.id.navigation_favourites:
                    favourites();
                    return true;
                case R.id.navigation_cocktailtime:
                    cocktailTime();
                    return true;
                case R.id.navigation_userguide:
                    userGuide();
                    return true;
            }
            return false;
        }
    };

    protected void cocktails(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    protected void cocktailTime(){
        Intent intent = new Intent(this, CocktailTimeActivity.class);
        startActivity(intent);
    }
    protected void favourites(){
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
    }
    protected void userGuide(){
        Intent intent = new Intent(this, UserGuideActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        cocktailDB = new CocktailContentProvider();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);


        //Create Cocktails List
        final List<String> cocktails = new ArrayList<>();
        //Add Cocktails from DB to list
        Cursor res = cocktailDB.getFavCocktails(getContentResolver());
        while(res.moveToNext()){
            cocktails.add(res.getString(0));
        }


        //Using an ArrayAdapter for displaying cocktails in ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, cocktails);
        //Get hold of the ListView
        ListView cocktailList = findViewById(R.id.listview_cocktails);
        //Bind adapter to the ListView
        cocktailList.setAdapter(adapter);

        //Handle long clicks on list items
        cocktailList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Gets name of item clicked
                String name = ((TextView) view).getText().toString();

                if(!cocktailDB.changeFav(name, getContentResolver())){
                    Toast toast = Toast.makeText(getApplicationContext(), "No longer Favourite", Toast.LENGTH_SHORT);
                    toast.show();
                    cocktails.clear();
                    Cursor res = cocktailDB.getFavCocktails(getContentResolver());
                    while(res.moveToNext()){
                        cocktails.add(res.getString(0));
                    }
                    adapter.notifyDataSetChanged();

                }
                return true;
            }
        });

        //Handle clicks on list items
        cocktailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Gets name of item clicked
                String name = ((TextView) view).getText().toString();

                //create an explicit intent
                Intent intent = new Intent(FavouritesActivity.this, CocktailDetails.class);
                //put extra data in the intent
                intent.putExtra("name", name);
                //start the activity with intent
                startActivity(intent);
            }
        });
    }

}
