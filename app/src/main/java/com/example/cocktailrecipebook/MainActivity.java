package com.example.cocktailrecipebook;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cocktailrecipebook.data.CocktailContentProvider;
import com.example.cocktailrecipebook.data.CocktailContract;
import com.example.cocktailrecipebook.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static CocktailContentProvider cocktailDB;
    DatabaseHelper dbhelp;


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
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);
        //dbhelp = new DatabaseHelper();
        dbhelp = new DatabaseHelper(DatabaseHelper.myContext ,DatabaseHelper.DATABASE_NAME,null, DatabaseHelper.DATABASE_VERSION);

        dbhelp.createDataBase();
        cocktailDB = new CocktailContentProvider();
//        ContentValues values = new ContentValues();
//        values.put(CocktailContract.Cocktail_Table.COL_1, "Bloody Mary");
//        values.put(CocktailContract.Cocktail_Table.COL_2, "25");
//        values.put(CocktailContract.Cocktail_Table.COL_3, "Blood|Mary");
//        values.put(CocktailContract.Cocktail_Table.COL_4, "1) Put Blood In Mary|2) Stir that Bitch");
//        values.put(CocktailContract.Cocktail_Table.COL_5, "0");
//
//        cocktailDB.insert(CocktailContract.Cocktail_Table.CONTENT_URI,values);
        //Create Cocktails List
        List<String> cocktails = new ArrayList<>();
        //Add Cocktails from DB to list
        Cursor res = cocktailDB.getCocktails(getContentResolver());
        while(res.moveToNext()){
            cocktails.add(res.getString(0));
        }


        //Using an ArrayAdapter for displaying cocktails in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, cocktails);
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

                if(cocktailDB.changeFav(name, getContentResolver())){
                    Toast toast = Toast.makeText(getApplicationContext(), "Set as Favourite", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "No longer Favourite", Toast.LENGTH_SHORT);
                    toast.show();
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
                Intent intent = new Intent(MainActivity.this, CocktailDetails.class);
                //put extra data in the intent
                intent.putExtra("name", name);
                //start the activity with intent
                startActivity(intent);
            }
        });

    }


}
