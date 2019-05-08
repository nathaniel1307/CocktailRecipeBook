package com.example.cocktailrecipebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class UserGuideActivity extends AppCompatActivity {
    WebView wv;

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
        setContentView(R.layout.activity_user_guide);

        wv=findViewById(R.id.webview);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl("file:///android_asset/userguide.html");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(3).setChecked(true);
    }

}
