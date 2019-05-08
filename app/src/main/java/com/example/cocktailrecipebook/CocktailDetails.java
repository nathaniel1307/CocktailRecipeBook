package com.example.cocktailrecipebook;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.cocktailrecipebook.data.CocktailContentProvider;
import com.example.cocktailrecipebook.data.DatabaseHelper;

public class CocktailDetails extends AppCompatActivity {
    static CocktailContentProvider cocktailDB;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_details);
        cocktailDB = new CocktailContentProvider();
        //get the intent
        Intent intent = getIntent();
        //get the data
        String name = intent.getStringExtra("name");
        //Change activity title to name of cocktail
        setTitle(name);
        //display the data in the TextView
        viewRecipe(name);
    }
//    public void shareText(View view) {
//        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        String shareBodyText = "Your shearing message goes here";
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
//        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
//        startActivity(Intent.createChooser(intent, "Choose sharing method"));
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("share", "create");
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("share","click");
        switch (item.getItemId()) {
            case R.id.share:
                TextView tv_name = findViewById(R.id.tv_name);
                TextView tv_abv = findViewById(R.id.tv_abv);
                TextView tv_ingredients = findViewById(R.id.tv_ingredients);
                TextView tv_recipe = findViewById(R.id.tv_recipe);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText =  tv_abv.getText().toString()+"\n"+tv_ingredients.getText().toString()+"\n"+tv_recipe.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,tv_name.getText());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Share Recipe"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void viewRecipe(String name){
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_abv = findViewById(R.id.tv_abv);
        TextView tv_ingredients = findViewById(R.id.tv_ingredients);
        TextView tv_recipe = findViewById(R.id.tv_recipe);
        Cursor res  = cocktailDB.getCocktail(name, getContentResolver());
        while(res.moveToNext()){
            tv_name.setText(res.getString(1));

            String abvText = "ABV: " + res.getString(2) + "%";
            tv_abv.setText(abvText);

            String ingredients = res.getString(3);
            String ingredientsText = "Ingredients:\n";
            while(ingredients.contains("|")){
                ingredientsText = ingredientsText + ingredients.substring(0,ingredients.indexOf("|"))+ "\n";
                ingredients = ingredients.substring(ingredients.indexOf("|")+1);
            }
            ingredientsText = ingredientsText + ingredients;
            tv_ingredients.setText(ingredientsText);

            String recipe = res.getString(4);
            String recipeText = "Recipe:\n";
            while(recipe.contains("|")){
                recipeText = recipeText + recipe.substring(0,recipe.indexOf("|"))+"\n";
                recipe = recipe.substring(recipe.indexOf("|")+1);
            }
            recipeText = recipeText + recipe;
            tv_recipe.setText(recipeText);
        }
    }
}
