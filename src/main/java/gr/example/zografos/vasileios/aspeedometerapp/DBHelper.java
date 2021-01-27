package gr.example.zografos.vasileios.aspeedometerapp;

import java.util.HashMap;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper  {
    public static final String DATABASE_NAME = "MyDB.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists locations " +
                        "(username text, speed text, lat text, lng text, timestamp text)"
        );
        db.execSQL(
                "create table if not exists users " +
                        "(name text, password text)"
        );
        db.execSQL(
                "insert into users (name, password) values ('bob', '123456')"
        );
        db.execSQL(
                "insert into users (name, password) values ('bill', '123456')"
        );
        db.execSQL(
                "insert into users (name, password) values ('john', '123456')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS locations");
        onCreate(db);
    }

    public boolean insertUser (String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", username);
        contentValues.put("password", password);
        db.insert("users", null, contentValues);
        db.close();
        return true;
    }

    public boolean insertLocation (String user, String speed, String lat, String lng, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user);
        contentValues.put("speed", speed);
        contentValues.put("lat", lat);
        contentValues.put("lng", lng);
        contentValues.put("timestamp", Long.toString(timestamp));
        db.insert("locations", null, contentValues);
        db.close();
        return true;
    }

    public boolean checkUser(String username, String password) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select name from users where name = '" + username + "' and password = '" + password + "';", null);
            if (res.getCount() == 1) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select name from users where name = '"+username+"';", null );
        if (res.getCount() > 1){
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public ArrayList<HashMap<String, String>> findLocations(String username) {
        ArrayList<HashMap<String, String>> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locations where username = '"+username+"';", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String lng = res.getString(res.getColumnIndex("lng"));
            String lat = res.getString(res.getColumnIndex("lat"));
            String speed = res.getString(res.getColumnIndex("speed"));
            String timestamp = res.getString(res.getColumnIndex("timestamp"));

            HashMap<String, String> pair = new HashMap<>();
            pair.put("lat", lat);
            pair.put("lng", lng);
            pair.put("speed", speed);
            pair.put("timestamp", timestamp);
            array_list.add(pair);

            res.moveToNext();
        }

        db.close();

        return array_list;
    }

    public ArrayList<HashMap<String, String>> allLocations() {
        ArrayList<HashMap<String, String>> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locations;", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String uname = res.getString(res.getColumnIndex("username"));
            String lng = res.getString(res.getColumnIndex("lng"));
            String lat = res.getString(res.getColumnIndex("lat"));
            String speed = res.getString(res.getColumnIndex("speed"));
            String timestamp = res.getString(res.getColumnIndex("timestamp"));

            HashMap<String, String> pair = new HashMap<>();
            pair.put("username", uname);
            pair.put("lat", lat);
            pair.put("lng", lng);
            pair.put("speed", speed);
            pair.put("timestamp", timestamp);

            array_list.add(pair);

            res.moveToNext();
        }

        db.close();

        return array_list;
    }
}