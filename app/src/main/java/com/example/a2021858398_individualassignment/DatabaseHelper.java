package com.example.a2021858398_individualassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String table = "entry";
    private static final String PK = "ID";
    private static final String column1 = "weight";
    private static final String column2 = "height";
    private static final String column3 = "bmi";

    public DatabaseHelper(Context context){
        super(context, table, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + table + " (" + PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " + column1 + " DOUBLE, "
                + column2 + " DOUBLE, " + column3 + " DOUBLE)");
    }

    public Boolean insertRow(String weightData, String heightData, String BMIData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //contentValues0.put(PK, weightData);
        contentValues.put(column1, weightData);
        contentValues.put(column2, heightData);
        contentValues.put(column3, BMIData);

        Log.d(TAG, "Data Inserted: W: " + weightData + ", H: " + heightData + ", BMI: " + BMIData + " into " + table + ".");

        //long result0 = db.insert(table, null, contentValues1);
        long result = db.insert(table, null, contentValues);

        if(result == -1)
            return false;
        else
            return  true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + table;
        Cursor data= db.rawQuery(query, null);
        return data;
    }

    public Cursor loadID(String input){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + table + " WHERE " + PK + " = " + input;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void wipeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE * FROM ";
        db.execSQL(query + table);
    }
}
