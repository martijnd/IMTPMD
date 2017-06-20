package nl.martijndorsman.imtpmd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

import nl.martijndorsman.imtpmd.models.CourseModel;

/**
 * Created by Martijn on 16/06/17.
 */

public class DatabaseAdapter {
    public static Context c;
    static SQLiteDatabase db;
    DatabaseHelper helper;

    // Maak een DatabaseHelper object met de context van de huidige klasse
    public DatabaseAdapter(Context c){
        this.c = c;
        helper = new DatabaseHelper(c);
    }

    // Open database
    public DatabaseAdapter openDB(){
        try{
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    // close database
    public void closeDB()
    {
        try {
            helper.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    // Voeg data toe aan de database aan de hand van JSONArrays
    public void addFromJson(JSONArray jsonpart, String tabel){
        DatabaseHelper dbHelper = new DatabaseHelper(c);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            for(int i = 0; i < jsonpart.length(); i++) {
                ContentValues values = new ContentValues();
                String name = jsonpart.getJSONObject(i).getString("name");
                String ects = jsonpart.getJSONObject(i).getString("ects");
                String period = jsonpart.getJSONObject(i).getString("period");
                String grade = jsonpart.getJSONObject(i).getString("grade");
                values.put(DatabaseInfo.CourseColumn.NAME, name);
                values.put(DatabaseInfo.CourseColumn.ECTS, ects);
                values.put(DatabaseInfo.CourseColumn.PERIOD, period);
                values.put(DatabaseInfo.CourseColumn.GRADE, grade);
                db.insertWithOnConflict(tabel, null, values,SQLiteDatabase.CONFLICT_REPLACE);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public long update(String tabel, String name, String ects, String period, String nieuwCijfer) {
        DatabaseHelper dbHelper = new DatabaseHelper(c);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Nieuwe waarde voor een kolom
        ContentValues values = new ContentValues();
        Log.w("DB Values", values.toString());
        values.put(DatabaseInfo.CourseColumn.GRADE, nieuwCijfer);
        String selection = DatabaseInfo.CourseColumn.NAME + " = ?";

        String[] selectionArgs = new String[]{ name };
        return db.update(tabel, values, selection, selectionArgs);
    }

//    public static String tableToString(String tableName) {
//        DatabaseAdapter dbAdapter = new DatabaseAdapter(c);
//        dbAdapter.openDB();
//        Log.d("","tableToString called");
//        String tableString = String.format("Table %s:\n", tableName);
//        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
//        tableString += cursorToString(allRows);
//        return tableString;
//    }
//
//    public static String cursorToString(Cursor cursor){
//        String cursorString = "";
//        if (cursor.moveToFirst() ){
//            String[] columnNames = cursor.getColumnNames();
//            for (String name: columnNames)
//                cursorString += String.format("%s ][ ", name);
//            cursorString += "\n";
//            do {
//                for (String name: columnNames) {
//                    cursorString += String.format("%s ][ ",
//                            cursor.getString(cursor.getColumnIndex(name)));
//                }
//                cursorString += "\n";
//            } while (cursor.moveToNext());
//        }
//        return cursorString;
//    }


    // insert into database
//    public long add(String tabel, String name, String ects, String period, String grade){
//        try {
//                ContentValues cv = new ContentValues();
//                cv.put(DatabaseInfo.CourseColumn.NAME, name);
//                cv.put(DatabaseInfo.CourseColumn.ECTS, ects);
//                cv.put(DatabaseInfo.CourseColumn.PERIOD, period);
//                cv.put(DatabaseInfo.CourseColumn.GRADE, grade);
//                return db.insert(tabel, null, cv);
//        } catch(SQLException e){
//            e.printStackTrace();
//        }
//        return 0;
//    }
    // Haal een hele tabel op
    public Cursor getAllData(String tabel){
        String[] columns = { DatabaseInfo.CourseColumn.NAME,DatabaseInfo.CourseColumn.ECTS,DatabaseInfo.CourseColumn.PERIOD, DatabaseInfo.CourseColumn.GRADE};
        return db.query(tabel, columns, null, null, null, null, null);
    }

}