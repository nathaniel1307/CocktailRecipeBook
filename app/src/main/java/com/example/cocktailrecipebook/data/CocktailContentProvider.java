package com.example.cocktailrecipebook.data;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class CocktailContentProvider extends ContentProvider {

    public static final int COCKTAIL = 100;
    public static final int COCKTAIL_WITH_ID = 101;
    private static final UriMatcher myUriMatcher = buildUriMatcher();
    public DatabaseHelper myDBHelper;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CocktailContract.CONTENT_AUTHORITY,CocktailContract.pathCocktail, COCKTAIL);

        matcher.addURI(CocktailContract.CONTENT_AUTHORITY,CocktailContract.pathCocktail +"/#", COCKTAIL_WITH_ID);

        return matcher;
    }

    public CocktailContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int match_code = myUriMatcher.match(uri);

        switch(match_code){
            case COCKTAIL:{
                myDBHelper.clearTable();
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        int match_code = myUriMatcher.match(uri);

        switch(match_code){
            case COCKTAIL:
                return CocktailContract.Cocktail_Table.CONTENT_TYPE_DIR;
            case COCKTAIL_WITH_ID:
                return CocktailContract.Cocktail_Table.CONTENT_TYPE_ITEM;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        int match_code = myUriMatcher.match(uri);
        Uri retUri = null;
        switch(match_code){
            case COCKTAIL:{
                SQLiteDatabase db = myDBHelper.getWritableDatabase();
                long _id = db.insert(CocktailContract.Cocktail_Table.TABLE_NAME,null,values);
                if (_id > 0) {
                    retUri = CocktailContract.Cocktail_Table.buildCocktailUriWithID(_id);
                }
                else
                    throw new SQLException("failed to insert");
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return retUri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        myDBHelper = new DatabaseHelper(getContext(),DatabaseHelper.DATABASE_NAME,null, DatabaseHelper.DATABASE_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        int match_code = myUriMatcher.match(uri);
        Cursor myCursor;

        switch(match_code) {
            case COCKTAIL: {
                SQLiteDatabase db = myDBHelper.getWritableDatabase();
                myCursor = db.query(
                        CocktailContract.Cocktail_Table.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case COCKTAIL_WITH_ID: {
                myCursor = myDBHelper.getReadableDatabase().query(
                        CocktailContract.Cocktail_Table.TABLE_NAME,
                        projection,
                        CocktailContract.Cocktail_Table._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        myCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return myCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        //Set arguments for update matching the _id
        String where = "_id=?";
        String[] whereArgs = new String[] {values.getAsString("_id")};
        //Run Database update on record
        db.update(CocktailContract.Cocktail_Table.TABLE_NAME, values, where, whereArgs);
        return 0;
    }



    public Cursor getCocktails(ContentResolver resolver){
        ContentResolver contentResolver = resolver;
        Cursor res = contentResolver.query(CocktailContract.Cocktail_Table.CONTENT_URI, new String[]{"Name"}, null, null, null);
        //Cursor res = sqLiteDatabase.rawQuery("SELECT Name FROM "+CocktailContract.Cocktail_Table.TABLE_NAME,null);
        return res;
    }



    public Cursor getFavCocktails(ContentResolver resolver){
        ContentResolver contentResolver = resolver;
        Cursor res = contentResolver.query(CocktailContract.Cocktail_Table.CONTENT_URI, new String[]{"Name"},"Favourite = ?",new String[]{"1"},null);
        //Cursor res = sqLiteDatabase.rawQuery("SELECT Name FROM " + CocktailContract.Cocktail_Table.TABLE_NAME+" WHERE Favourite='1'",null);
        return res;
    }



    public Cursor getCocktail(String name,ContentResolver resolver){
        ContentResolver contentResolver = resolver;
        Cursor res = contentResolver.query(CocktailContract.Cocktail_Table.CONTENT_URI,null,"Name = ?",new String[]{name},null);
        //Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+CocktailContract.Cocktail_Table.TABLE_NAME+" WHERE Name='"+name+"'",null);
        return res;
    }



    public Boolean changeFav(String name,ContentResolver resolver){
        ContentResolver contentResolver = resolver;
        //Get Current Values of Record
        Cursor res = contentResolver.query(CocktailContract.Cocktail_Table.CONTENT_URI, null,"Name = ?",new String[]{name},null);
        res.moveToFirst();

        //Assign current record values to ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(CocktailContract.Cocktail_Table.COL_0,res.getString(0));
        contentValues.put(CocktailContract.Cocktail_Table.COL_1,res.getString(1));
        contentValues.put(CocktailContract.Cocktail_Table.COL_2,res.getString(2));
        contentValues.put(CocktailContract.Cocktail_Table.COL_3,res.getString(3));
        contentValues.put(CocktailContract.Cocktail_Table.COL_4,res.getString(4));

        //Check wether currently a favourite or not
        if(res.getString(5).equals("1")){
            //add the opposite option to ContentValues
            contentValues.put("favourite","0");
            //Perform Update
            contentResolver.update(CocktailContract.Cocktail_Table.CONTENT_URI, contentValues, "Name = ?",new String[]{name});
            return false;
        }else{
            //Add the opposite to ContentValues
            contentValues.put(CocktailContract.Cocktail_Table.COL_5,"1");
            //Perform Update
            contentResolver.update(CocktailContract.Cocktail_Table.CONTENT_URI, contentValues, "ID = ?",new String[] { res.getString(0) });
            return true;
        }
    }
}
