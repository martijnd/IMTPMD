package nl.martijndorsman.imtpmd;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import nl.martijndorsman.imtpmd.database.DatabaseHelper;
import nl.martijndorsman.imtpmd.database.DatabaseInfo;
import nl.martijndorsman.imtpmd.models.CourseModel;

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
    private static String url = "http://martijndorsman.nl/vakken_lijst.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakkenlijst);
        lv = (ListView) findViewById(R.id.list);
        vakkenlijst = new ArrayList<>();
        new JsonTask().execute(url);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(view+" lol", position+" haha");
                //based on item add info to intent
            }
        });
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
                Gson gson = new Gson();
                CourseModel[] courses = gson.fromJson(jsonStr, CourseModel[].class);
                DatabaseHelper dbHelper = DatabaseHelper.getHelper(getApplicationContext());
                for(CourseModel course : courses) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseInfo.CourseColumn.NAME, course.name);
                    values.put(DatabaseInfo.CourseColumn.ECTS, course.ects);
                    values.put(DatabaseInfo.CourseColumn.GRADE, course.grade);
                    dbHelper.insert(DatabaseInfo.CourseTables.COURSE, null, values);
                }

            } else {
                Log.d(TAG, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
                // hier moet actie
            }
        }
    }

}

