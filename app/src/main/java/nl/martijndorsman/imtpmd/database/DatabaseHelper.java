package nl.martijndorsman.imtpmd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Martijn on 21/05/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase mSQLDB;
    private static DatabaseHelper mInstance;
    public static final String dbName = "vakkenlijst.db";
    public static final int dbVersion = 1;

    public DatabaseHelper(Context ctx) {
        super(ctx, dbName, null, dbVersion);
    }

    public static synchronized DatabaseHelper getHelper (Context ctx){
        if (mInstance == null){
            mInstance = new DatabaseHelper(ctx);
            mSQLDB = mInstance.getWritableDatabase();
        }
        return mInstance;
    }

    @Override										// Maak je tabel met deze kolommen
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseInfo.CourseTables.COURSE + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseInfo.CourseColumn.NAME + " TEXT," +
                DatabaseInfo.CourseColumn.ECTS + " TEXT," +
                DatabaseInfo.CourseColumn.GRADE + " TEXT);"
        );
    }
    // CREATE TABLE course (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ects TEXT, grade TEXT);

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DatabaseInfo.CourseTables.COURSE);
        onCreate(db);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ){
        super(context,name,factory, version);
    }

    public void insert(String table, String nullColumnHack, ContentValues values){
        mSQLDB.insert(table, nullColumnHack, values);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectArgs, String groupBy, String having, String orderBy){
        return mSQLDB.query(table, columns, selection, selectArgs, groupBy, having, orderBy);
    }

}