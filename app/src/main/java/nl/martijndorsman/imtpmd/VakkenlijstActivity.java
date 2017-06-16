package nl.martijndorsman.imtpmd;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import nl.martijndorsman.imtpmd.database.DatabaseHelper;
import nl.martijndorsman.imtpmd.database.DatabaseInfo;
import nl.martijndorsman.imtpmd.models.CourseModel;

import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar1;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar2;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar3en4;

/**
 * Created by Martijn on 21/05/17.
 */

public class VakkenlijstActivity extends AppCompatActivity {
    public ArrayList<HashMap<String, String>> vakkenlijst;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<HashMap<String, String>> myDataset;
    ProgressDialog pd;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private boolean success = true;
    CharSequence text;
    private static String url = "http://martijndorsman.nl/vakken_lijst.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakkenlijst);
        vakkenlijst = new ArrayList<>();
        new JsonTask().execute(url);
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
            JSONArray jaar1 = null;
            JSONArray jaar2 = null;
            JSONArray jaar3en4 = null;
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

                dbInput(jaar1, Jaar1);
                dbInput(jaar2, Jaar2);
                dbInput(jaar3en4, Jaar3en4);

            } else {
                Log.d(TAG, "Couldn't get json from server.");
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
                if (success){
                    text = "Ophalen vakkenlijst succesvol";
                }
                else{
                    text = "Ophalen vakkenlijst mislukt";
                }
                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

        // functie om elke tabel te vullen met content
        public void dbInput(JSONArray jsonpart, String tabel) {
            Gson gson = new Gson();
            CourseModel[] courses1 = gson.fromJson(String.valueOf(jsonpart), CourseModel[].class);
            DatabaseHelper dbHelper = DatabaseHelper.getHelper(getApplicationContext());
            for(CourseModel course : courses1) {
                ContentValues values = new ContentValues();
                values.put(DatabaseInfo.CourseColumn.NAME, course.name);
                values.put(DatabaseInfo.CourseColumn.ECTS, course.ects);
                values.put(DatabaseInfo.CourseColumn.PERIOD, course.period);
                values.put(DatabaseInfo.CourseColumn.GRADE, course.grade);
                dbHelper.insert(tabel, null, values);
            }
        }
    }

}
