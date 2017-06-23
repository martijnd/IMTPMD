package nl.martijndorsman.imtpmd.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables;

/**
 * Created by Martijn on 21/05/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int dbVersion = 2;

    // database naam
    private static final String dbName = "vakkenlijst.db";

    // tabelnamen
    private static final String Jaar1 = "Jaar1";
    private static final String Jaar2 = "Jaar2";
    private static final String Jaar3en4 = "Jaar3en4";
    private static final String Keuze = "Keuze";

    // kolomnamen
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

    // create table voor keuzemodulen
    private static final String CREATE_TABLE_KEUZE = "CREATE TABLE "
        + Keuze + "(" + NAME + " TEXT," + ECTS
        + " TEXT," + PERIOD + " TEXT," + GRADE
        + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, dbVersion);

    }

    // Maak tabel met deze kolommen
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JAAR1);
        db.execSQL(CREATE_TABLE_JAAR2);
        db.execSQL(CREATE_TABLE_JAAR3EN4);
        db.execSQL(CREATE_TABLE_KEUZE);
    }


    // Drop alle tabellen als de versie geüpdate wordt
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CourseTables.Jaar1);
        db.execSQL("DROP TABLE IF EXISTS "+ CourseTables.Jaar2);
        db.execSQL("DROP TABLE IF EXISTS "+ CourseTables.Jaar3en4);
        db.execSQL("DROP TABLE IF EXISTS "+ CourseTables.Keuze);
        onCreate(db);
    }
}