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

import static nl.martijndorsman.imtpmd.R.id.nameTxt;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar1;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar2;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar3en4;

/**
 * Created by Martijn on 21/05/17.
 */

public class VakkenlijstActivity extends AppCompatActivity {
    TextView nametxt, ectstxt, periodtxt, gradetxt;
    RecyclerView rv;
    MyAdapter adapter;
    ArrayList<CourseModel> courses = new ArrayList<>();
    public ArrayList<HashMap<String, String>> vakkenlijst;
    ProgressDialog pd;
    DatabaseAdapter db = new DatabaseAdapter(this);
    public JSONArray jaar1 = null;
    public JSONArray jaar2 = null;
    public JSONArray jaar3en4 = null;
    private String TAG = MainActivity.class.getSimpleName();
    private boolean success = true;
    CharSequence text;
    private static String url = "http://martijndorsman.nl/vakken_lijst.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakkenlijst);
        vakkenlijst = new ArrayList<>();
        // maak een JsonTask Object aan en voer hem uit met de url
        new JsonTask().execute(url);
        rv = (RecyclerView) findViewById(R.id.mRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        // adapter
        adapter = new MyAdapter(this, courses);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VakkenlijstActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {

                JSONObject reader = null;
                try {
                    reader = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // ontleed de ontvangen json string in stukken van elk jaar
                try {
                    if (reader != null) {
                        jaar1 = reader.getJSONArray("jaar1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (reader != null) {
                        jaar2 = reader.getJSONArray("jaar2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (reader != null) {
                        jaar3en4 = reader.getJSONArray("jaar3en4");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                db.addFromJson(jaar1, Jaar1);
                db.addFromJson(jaar2, Jaar2);
                db.addFromJson(jaar3en4, Jaar3en4);

            } else {
                Log.d(TAG, "Couldn't get json from server.");
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
                if (success) {
                    text = "Ophalen vakkenlijst succesvol";
                } else {
                    text = "Ophalen vakkenlijst mislukt";
                }
                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

    }


    private void save(String tabel, String name, String ects, String period, String grade) {
        DatabaseAdapter db = new DatabaseAdapter(this);
        //OPEN DB
        db.openDB();
        //COMMIT
        long result = db.add(tabel, name, ects, period, grade);
        if (result > 0) {
            nametxt.setText("");
            ectstxt.setText("");
            periodtxt.setText("");
            gradetxt.setText("");
        } else {
            Snackbar.make(nametxt, "Unable To Save", Snackbar.LENGTH_SHORT).show();
        }
        db.closeDB();
        //REFRESH
        retrieve(tabel);
    }

    //RETRIEVE
    private void retrieve(String tabel) {
        courses.clear();
        DatabaseAdapter db = new DatabaseAdapter(this);
        db.openDB();
        //RETRIEVE
        Cursor c = db.getAllData(tabel);
        //LOOP AND ADD TO ARRAYLIST
        while (c.moveToNext()) {
            String name = c.getString(0);
            String ects = c.getString(1);
            String period = c.getString(2);
            String grade = c.getString(3);
            CourseModel p = new CourseModel(name, ects, period, grade);
            //ADD TO ARRAYLIST
            courses.add(p);
        }
        //CHECK IF ARRAYLIST ISNT EMPTY
        if (!(courses.size() < 1)) {
            rv.setAdapter(adapter);
        }
        db.closeDB();
        ;
    }
}
