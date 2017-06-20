package nl.martijndorsman.imtpmd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.models.CourseModel;

import static nl.martijndorsman.imtpmd.MainActivity.courses;
import static nl.martijndorsman.imtpmd.PopSpinner.item;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar1;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar2;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar3en4;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Keuze;

/**
 * Created by Martijn on 21/05/17.
 */

public class VakkenlijstActivity extends AppCompatActivity {
    // init de variabelen
    public static String currentTable = "";
    static RecyclerView rv;
    static MyAdapter adapter;
    // De url waar het json-bestand op staat


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakkenlijst);
        // maak een JsonTask Object aan en voer hem uit met de url
        rv = (RecyclerView) findViewById(R.id.mRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        // adapter
        adapter = new MyAdapter(this, courses);
        // laad de tabel afhankelijk van de waarde van de PopSpinner
        tableSwitch();
        adapter.notifyDataSetChanged();
    }



    // Retrieve en bind het aan de recyclerview
    public void retrieve(String tabel) {
        courses.clear();
        DatabaseAdapter db = new DatabaseAdapter(this);
        db.openDB();
        //RETRIEVE
        Cursor c = db.getAllData(tabel);
        //LOOP EN VOEG AAN ARRAYLIST TOE
        while (c.moveToNext()) {
            String name = c.getString(0);
            String ects = c.getString(1);
            String period = c.getString(2);
            String grade = c.getString(3);
            CourseModel p = new CourseModel(name, ects, period, grade);
            //VOEG TOE AAN ARRAYLIST
            courses.add(p);
        }
        adapter.notifyDataSetChanged();
        //CHECK OF DE ARRAYLIST LEEG IS
        if (!(courses.size() < 1)) {
            rv.setAdapter(adapter);

        }
        c.close();
        db.closeDB();
    }

    private void tableSwitch (){
        switch (item){
            case "Jaar 1":
                currentTable = "Jaar1";
                retrieve(Jaar1);
                break;
            case "Jaar 2": retrieve(Jaar2);
                currentTable = "Jaar2";
                break;
            case "Jaar 3 en 4": retrieve(Jaar3en4);
                currentTable = "Jaar3en4";
                break;
            case "Keuzevakken": retrieve(Keuze);
                currentTable = "Keuze";
                break;
        }
    }
}
