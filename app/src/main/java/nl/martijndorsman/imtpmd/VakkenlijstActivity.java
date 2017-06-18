package nl.martijndorsman.imtpmd;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.database.DatabaseHelper;
import nl.martijndorsman.imtpmd.database.DatabaseInfo;
import nl.martijndorsman.imtpmd.models.CourseModel;

import static nl.martijndorsman.imtpmd.MainActivity.courses;
import static nl.martijndorsman.imtpmd.R.id.nameTxt;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar1;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar2;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar3en4;

/**
 * Created by Martijn on 21/05/17.
 */

public class VakkenlijstActivity extends AppCompatActivity {
    // init de variabelen
    TextView nametxt, ectstxt, periodtxt, gradetxt;
    RecyclerView rv;
    MyAdapter adapter;
    SQLiteDatabase db;
    public ArrayList<HashMap<String, String>> vakkenlijst;

    // De url waar het json-bestand op staat


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakkenlijst);
        vakkenlijst = new ArrayList<>();
        // maak een JsonTask Object aan en voer hem uit met de url
        rv = (RecyclerView) findViewById(R.id.mRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        // adapter
        adapter = new MyAdapter(this, courses);
        retrieve(Jaar1);

    }

    // Retrieve en bind het aan de recyclerview
    private void retrieve(String tabel) {
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
        //CHECK OF DE ARRAYLIST LEEG IS
        if (!(courses.size() < 1)) {
            rv.setAdapter(adapter);
        }
        db.closeDB();
    }

}
