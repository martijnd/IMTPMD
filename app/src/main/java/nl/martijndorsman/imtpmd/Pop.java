package nl.martijndorsman.imtpmd;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.util.Log;


/**
 * Created by Robert on 18-6-2017.
 */

public class Pop extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Zet het popup effect. Dus een window over de vorige heen
        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Het formaat van de window instellen
        int width = dm.widthPixels;
        int heigth = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(heigth*.4));

        String[] arraySpinner = new String[]{
                "Jaar 1", "Jaar 2", "Jaar 3", "Jaar 4"
        };
        final Spinner s = (Spinner) findViewById(R.id.popSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);

        final Button popbutton = (Button) findViewById(R.id.popButton);
        popbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String item = s.getSelectedItem().toString();
                Log.w("PopMessage", "Value of Item: " +  item);
            }
        });

    }
}
