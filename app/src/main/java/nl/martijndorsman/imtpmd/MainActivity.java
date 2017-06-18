package nl.martijndorsman.imtpmd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.models.CourseModel;

import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar1;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar2;
import static nl.martijndorsman.imtpmd.database.DatabaseInfo.CourseTables.Jaar3en4;

/**
 * Created by Martijn on 13/06/17.
 */

public class MainActivity extends AppCompatActivity {
    ProgressDialog pd;
    boolean check = false;
    DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
    public JSONArray jaar1;
    public JSONArray jaar2;
    public JSONArray jaar3en4;
    public static ArrayList<CourseModel> courses = new ArrayList<>();
    MyAdapter adapter;
    RecyclerView rv;
    private String TAG = MainActivity.class.getSimpleName();
    private boolean success = true;
    CharSequence text;
    private static String url = "http://martijndorsman.nl/vakken_lijst.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bind de activity aan de layout
        setContentView(R.layout.activity_main);
        // Bind de button aan de onClickListener met de startActivity methode
        Button vakkenlijstbutton = (Button) findViewById(R.id.vakkenlijstbutton);
        Button vakkenbutton = (Button) findViewById(R.id.vakkenbutton);

        vakkenlijstbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!check) {
                    new JsonTask().execute(url);
                }
                else {
                    Toast.makeText(MainActivity.this, "Vakkenlijst is al opgehaald", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vakkenbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VakkenlijstActivity.class));
            }
        });
    }

    // Mbh van deze klasse kan er op een 2e thread de JSON-string worden binnengehaald via internet
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // Maak een dialoog voor tijdens het wachten
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler sh = new HttpHandler();
            // Stuur een request naar de url, en wacht op antwoord
            String jsonStr = sh.makeServiceCall(url);

//            Log.e(TAG, "Response from url: " + jsonStr);
            // Als de json string nog niet binnengehaald is
            if (jsonStr != null) {
                //  Maak een JSONObject aan waarmee de string ontleed kan worden
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
                // Voeg de JSON string toe aan de database mbh de addFromJson functie
                //
                try {
                    dbAdapter.openDB();
                    dbAdapter.addFromJson(jaar1, Jaar1);
                    dbAdapter.addFromJson(jaar2, Jaar2);
                    dbAdapter.addFromJson(jaar3en4, Jaar3en4);
                    dbAdapter.closeDB();

                } catch(NullPointerException e){
                    e.printStackTrace();
                    Log.d("String ", "succes32434234243");
                }
            } else {
                Log.d(TAG, "Couldn't get json from server.");
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Verwijder de dialoog na het binnehalen
            if (pd.isShowing()) {
                pd.dismiss();
                // Verschillende tekst afhankelijk van het resultaat
                if (success) {
                    text = "Ophalen vakkenlijst succesvol";
                    check = true;
                } else {
                    text = "Ophalen vakkenlijst mislukt";
                }
                Context context = getApplicationContext();
                // Maak een Toast voor de UX
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }

    }
}
