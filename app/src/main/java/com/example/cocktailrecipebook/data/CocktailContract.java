package com.example.cocktailrecipebook.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CocktailContract {
    public static final String CONTENT_AUTHORITY = "com.example.cocktailrecipebook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String pathCocktail = "cocktails";
    private CocktailContract() {}
    /* Inner class that defines the table contents */
    public static class Cocktail_Table implements BaseColumns {
        public static final String TABLE_NAME = "cocktails_table";
        public static final String COL_0 = "_id";
        public static final String COL_1 = "name";
        public static final String COL_2 = "abv";
        public static final String COL_3 = "ingredients";
        public static final String COL_4 = "recipe";
        public static final String COL_5 = "favourite";
        //Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(pathCocktail).build();
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/"+CONTENT_AUTHORITY+"/"+pathCocktail;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/"+CONTENT_AUTHORITY+"/"+pathCocktail;

        public static Uri buildCocktailUriWithID(long ID){
            return ContentUris.withAppendedId(CONTENT_URI,ID);
        }

    }
    String query = "CREATE TABLE IF NOT EXISTS " + CocktailContract.Cocktail_Table.TABLE_NAME + " (" +
            Cocktail_Table.COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Cocktail_Table.COL_1 + " TEXT NOT NULL "+
            Cocktail_Table.COL_2 + " INTEGER NOT NULL "+
            Cocktail_Table.COL_3 + " TEXT NOT NULL " +
                    Cocktail_Table.COL_4 + " TEXT NOT NULL " +
                    Cocktail_Table.COL_5 + " BOOLEAN NOT NULL );";

                    }
