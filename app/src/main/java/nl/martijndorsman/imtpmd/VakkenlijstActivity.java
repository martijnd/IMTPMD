package nl.martijndorsman.imtpmd;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.database.DatabaseHelper;
import nl.martijndorsman.imtpmd.models.CourseModel;

import static nl.martijndorsman.imtpmd.KeuzevakPopup.item1;
import static nl.martijndorsman.imtpmd.KeuzevakPopup.item2;
import static nl.martijndorsman.imtpmd.KeuzevakPopup.item3;
import static nl.martijndorsman.imtpmd.KeuzevakPopup.item4;
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
    public DatabaseHelper helper;
    DatabaseAdapter dbAdapter;
    public ArrayList<CourseModel> courses = new ArrayList<>();
    LinearLayoutManager mLayoutManager;
    public static int totaalECTSjaar1;
    public static int totaalECTSjaar2;
    public static int totaalECTSjaar3en4;
    public static int totaalECTSKeuze;
    ECTS ects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakkenlijst);
        // maak een JsonTask Object aan en voer hem uit met de url
        rv = (RecyclerView) findViewById(R.id.mRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mLayoutManager = new LinearLayoutManager(this);
        rv.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                mLayoutManager.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);
        helper = new DatabaseHelper(this);
        // adapter
        adapter = new MyAdapter(this, courses);
        // laad de tabel afhankelijk van de waarde van de PopSpinner
        tableSwitch();
        ects = new ECTS(getApplicationContext());
        ects.getECTS(currentTable);

        adapter.notifyDataSetChanged();
    }

    // Retrieve en bind het aan de recyclerview
    public void retrieve(String tabel, Context context) {
        dbAdapter = new DatabaseAdapter(context);
        dbAdapter.openDB();
        courses.clear();

        Cursor c = dbAdapter.getAllData(tabel);
        //Loop en voeg aan ArrayList toe
        while (c.moveToNext()) {
            String name = c.getString(0);
            String ects = c.getString(1);
            String period = c.getString(2);
            String grade = c.getString(3);
            String status = "Niet behaald";
            Double gradeDouble = Double.parseDouble(grade);
            if (gradeDouble>=5.5){
                status = "Behaald";
            }
            CourseModel p = new CourseModel(name, ects, period, grade, status);
            //Voeg toe aan de ArrayList
            courses.add(p);
        }
        //Controleer of de ArrayList leeg is
        if (!(courses.size() < 1)) {
            rv.setAdapter(adapter);
        }
        c.close();
        dbAdapter.closeDB();
    }

    // Retrieve functie voor de keuzevakken
    public void retrieveSubject(String tabel, Context context) {
        dbAdapter = new DatabaseAdapter(context);
        dbAdapter.openDB();
        courses.clear();
        Cursor c = dbAdapter.getAllData(tabel);
        //Loop en voeg aan ArrayList toe
        while (c.moveToNext()) {
            String name = c.getString(0);
            String ects = c.getString(1);
            String period = c.getString(2);
            String grade = c.getString(3);
            String status = "Niet behaald";
            Double gradeDouble = Double.parseDouble(grade);
            if (gradeDouble>=5.5){
                status = "Behaald";
            }
            if(name.equals(item1) || name.equals(item2) || name.equals(item3) || name.equals(item4)) {
                CourseModel p = new CourseModel(name, ects, period, grade, status);
                //Voeg toe aan ArrayList
                courses.add(p);
            }
        }
        //Check of ArrayList leeg is
        if (!(courses.size() < 1)) {
            rv.setAdapter(adapter);
        }
        c.close();
        dbAdapter.closeDB();
    }

    // Functie om aan de hand van de waarde van de popSpinner de retrieve functie te laden met de juiste waardes
    private void tableSwitch (){
        switch (item){
            case "Jaar 1": retrieve(Jaar1, this);
                currentTable = "Jaar1";
                break;
            case "Jaar 2": retrieve(Jaar2, this);
                currentTable = "Jaar2";
                break;
            case "Jaar 3 en 4": retrieve(Jaar3en4, this);
                currentTable = "Jaar3en4";
                break;
            // Wanneer keuzevakken geselecteerd wordt, show een dialog om te keuzevakken te selecteren
            case "Keuzevakken": retrieveSubject(Keuze, this);
                currentTable = "Keuze";
                break;
        }
    }
}