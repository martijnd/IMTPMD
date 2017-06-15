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
    private static SQLiteDatabase mSQLDB;
    private static DatabaseHelper mInstance = null;
    private static final int dbVersion = 3;

    // database naam
    private static final String dbName = "vakkenlijst.db";

    // tabelnamen
    private static final String Jaar1 = "Jaar1";
    private static final String Jaar2 = "Jaar2";
    private static final String Jaar3en4 = "Jaar3en4";

    //kolom namen
    private static final String NAME = "NAME";
    private static final String ECTS = "ECTS";
    private static final String PERIOD = "PERIOD";
    private static final String GRADE = "GRADE";

    // create table voor jaar 1
    private static final String CREATE_TABLE_JAAR1 = "CREATE TABLE "
            + Jaar1 + "(" + NAME + " TEXT," + ECTS
            + " TEXT," + PERIOD + " TEXT," + GRADE
            + " TEXT" + ")";

    // create table voor jaar 2
    private static final String CREATE_TABLE_JAAR2 = "CREATE TABLE "
            + Jaar2 + "(" + NAME + " TEXT," + ECTS
            + " TEXT," + PERIOD + " TEXT," + GRADE
            + " TEXT" + ")";

    // create table voor jaar 3 en 4
    private static final String CREATE_TABLE_JAAR3EN4 = "CREATE TABLE "
            + Jaar3en4 + "(" + NAME + " TEXT," + ECTS
            + " TEXT," + PERIOD + " TEXT," + GRADE
            + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);
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
        db.execSQL(CREATE_TABLE_JAAR1);
        db.execSQL(CREATE_TABLE_JAAR2);
        db.execSQL(CREATE_TABLE_JAAR3EN4);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DatabaseInfo.CourseTables.Jaar1);
        db.execSQL("DROP TABLE IF EXISTS "+ DatabaseInfo.CourseTables.Jaar2);
        db.execSQL("DROP TABLE IF EXISTS "+ DatabaseInfo.CourseTables.Jaar3en4);
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