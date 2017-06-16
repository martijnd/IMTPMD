package nl.martijndorsman.imtpmd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import org.json.JSONArray;

import nl.martijndorsman.imtpmd.models.CourseModel;

/**
 * Created by Martijn on 16/06/17.
 */

public class DatabaseAdapter {
    Context c;
    SQLiteDatabase db;
    DatabaseHelper helper;

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

    public void addFromJson(JSONArray jsonpart, String tabel){
        try {
            Gson gson = new Gson();
            CourseModel[] courses = gson.fromJson(String.valueOf(jsonpart), CourseModel[].class);
            for(CourseModel course : courses) {
                ContentValues cv = new ContentValues();
                cv.put(DatabaseInfo.CourseColumn.NAME, course.name);
                cv.put(DatabaseInfo.CourseColumn.ECTS, course.ects);
                cv.put(DatabaseInfo.CourseColumn.PERIOD, course.period);
                cv.put(DatabaseInfo.CourseColumn.GRADE, course.grade);
                db.insert(tabel, null, cv);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }


    // insert into database
    public long add(String tabel, String name, String ects, String period, String grade){
        try {
                ContentValues cv = new ContentValues();
                cv.put(DatabaseInfo.CourseColumn.NAME, name);
                cv.put(DatabaseInfo.CourseColumn.ECTS, ects);
                cv.put(DatabaseInfo.CourseColumn.PERIOD, period);
                cv.put(DatabaseInfo.CourseColumn.GRADE, grade);
                return db.insert(tabel, null, cv);
        } catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public Cursor getAllData(String tabel){
        String[] columns = { DatabaseInfo.CourseColumn.NAME,DatabaseInfo.CourseColumn.ECTS,DatabaseInfo.CourseColumn.PERIOD, DatabaseInfo.CourseColumn.GRADE};
        return db.query(tabel, columns, null, null, null, null, null);
    }

}