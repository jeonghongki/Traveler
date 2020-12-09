package com.example.traeveler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skt.Tmap.TMapMarkerItem;

import java.util.ArrayList;

public class TravelerDB extends SQLiteOpenHelper {

    public TravelerDB(Context context) { super(context, "TravelDB", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE travelTBL (" +
                "ID INTEGER PRIMARY KEY," +
                "Date CHAR(12));");
        sqLiteDatabase.execSQL("CREATE TABLE travelListTBL (" +
                "ListID INTEGER," +
                "Title VARCHAR(100)," +
                "Longitude DOUBLE," +
                "Latitude DOUBLE," +
                "DateID INTEGER REFERENCES travelTBL(ID));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS travelTBL");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS travelListTBL");
        onCreate(sqLiteDatabase);
    }

    public void InsertDate(TravelerDB travelerDB, String ID, String Date) {
        SQLiteDatabase sqlDB = travelerDB.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO travelTBL VALUES ('" + ID + "'," + Date + ");");
        sqlDB.close();
    }

    public void InsertSchedule(TravelerDB travelerDB, String ListID, String Title, String Longitude, String Latitude, String DateID) {
        SQLiteDatabase sqlDB = travelerDB.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO travelListTBL VALUES ('" + ListID + "', '" + Title + "', '" + Longitude + "', '" + Latitude + "', " + DateID + ");");
        sqlDB.close();
    }
    
    public void DeleteSchedule(TravelerDB travelerDB, String Date) {
        Cursor c_travelTBL;
        SQLiteDatabase sqlDB = travelerDB.getWritableDatabase();
        c_travelTBL = sqlDB.rawQuery("SELECT * FROM travelTBL WHERE Date = " + Date + ";", null);
        c_travelTBL.moveToFirst();
        String DateID = c_travelTBL.getString(0);

        sqlDB.execSQL("DELETE FROM travelListTBL WHERE DateID = " + DateID + ";");
        sqlDB.execSQL("DELETE FROM travelTBL WHERE Date = " + Date + ";");
        sqlDB.close();
    }

    public void GetQueryData(TravelerDB travelerDB, String Date, ArrayList<String> Title, ArrayList<Double> Longitude, ArrayList<Double> Latitude) {
        Cursor c_travelTBL, c_travelListTBL;
        SQLiteDatabase sqlDB = travelerDB.getWritableDatabase();
        c_travelTBL = sqlDB.rawQuery("SELECT * FROM travelTBL WHERE Date = " + Date + ";", null);
        c_travelTBL.moveToFirst();
        String DateID = c_travelTBL.getString(0);

        c_travelListTBL = sqlDB.rawQuery("SELECT * FROM travelListTBL WHERE DateID = " + DateID + ";", null);
        c_travelListTBL.moveToFirst();
        do {
            Title.add(c_travelListTBL.getString(1));
            Longitude.add(Double.parseDouble(c_travelListTBL.getString(2)));
            Latitude.add(Double.parseDouble(c_travelListTBL.getString(3)));
        } while (c_travelListTBL.moveToNext());
        sqlDB.close();
    }

    public boolean isExistData(TravelerDB travelerDB, String Id_or_Date, String key) {
        Cursor c_travelTBL = null;
        boolean result;
        SQLiteDatabase sqlDB = travelerDB.getWritableDatabase();
        switch (key) {
            case "Id":
                c_travelTBL =  sqlDB.rawQuery("SELECT * FROM travelTBL WHERE ID = " + Id_or_Date + ";", null );
                break;
            case "Date":
                c_travelTBL =  sqlDB.rawQuery("SELECT * FROM travelTBL WHERE Date = " + Id_or_Date + ";", null );
                break;
        }
        result = c_travelTBL.getCount() > 0;
        sqlDB.close();
        return result;
    }

    public void resetDB(TravelerDB travelerDB) {
        SQLiteDatabase sqlDB = travelerDB.getWritableDatabase();
        onUpgrade(sqlDB, 1, 2);
        sqlDB.close();
    }
}
