package nl.martijndorsman.imtpmd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


/**
 * Created by Robert on 18-6-2017.
 */

public class PopSpinner extends Activity{
    public static String item;
    public boolean keuzeVakkenIngesteld = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Zet het popup effect. Dus een window over de vorige heen
        setContentView(R.layout.choosetablewindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Het formaat van de window instellen
        int width = dm.widthPixels;

        getWindow().setLayout((int)(width*.8),WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        String[] arraySpinner = new String[]{
                "Jaar 1", "Jaar 2", "Jaar 3 en 4", "Keuzevakken"
        };
        final Spinner s = (Spinner) findViewById(R.id.tableSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, arraySpinner);
        s.setAdapter(adapter);

        final Button popbutton = (Button) findViewById(R.id.tableButton);
        popbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                item = s.getSelectedItem().toString();
                if(!item.equals("Keuzevakken")) {
                    startActivity(new Intent(PopSpinner.this, VakkenlijstActivity.class));
                }
                else{
                    if(!keuzeVakkenIngesteld) {
                        keuzeVakkenIngesteld = true;
                        startActivity(new Intent(PopSpinner.this, KeuzevakPopup.class));
                    } else {
                        startActivity((new Intent(PopSpinner.this, VakkenlijstActivity.class)));
                    }
                }
            }
        });
        final Button cancelbutton = (Button) findViewById(R.id.cancelButton1);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
