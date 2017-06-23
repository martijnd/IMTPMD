package nl.martijndorsman.imtpmd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by Martijn on 16/06/17.
 */

public class DatabaseAdapter {
    Context context;
    SQLiteDatabase db;
    DatabaseHelper helper;

    // Maak een DatabaseHelper object met de context van de huidige klasse
    public DatabaseAdapter(Context context){
        this.context = context;
        helper = new DatabaseHelper(context);
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

    public long update(String tabel, String name, String nieuwCijfer) {
        //Nieuwe waarde voor een kolom
        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.CourseColumn.GRADE, nieuwCijfer);
        String selection = DatabaseInfo.CourseColumn.NAME + " = ?";
        String[] selectionArgs = new String[]{ name };
        return db.update(tabel, values, selection, selectionArgs);
    }

    // Haal een hele tabel op
    public Cursor getAllData(String tabel){
        String[] columns = { DatabaseInfo.CourseColumn.NAME,DatabaseInfo.CourseColumn.ECTS,DatabaseInfo.CourseColumn.PERIOD, DatabaseInfo.CourseColumn.GRADE};
        return db.query(tabel, columns, null, null, null, null, null);
    }

}