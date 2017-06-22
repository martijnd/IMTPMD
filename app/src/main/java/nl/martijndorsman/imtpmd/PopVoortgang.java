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
 * Created by Robert on 22-6-2017.
 */

public class PopVoortgang extends Activity {

    public String item;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.selectvoortgang);
        //Set the size of the popup window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        getWindow().setLayout((int)(width*.8), WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //Add items to the spinner
        String[] arraySpinner = new String[]{
                "Jaar 1", "Jaar 2", "Jaar 3 en 4", "Keuzevakken"
        };
        final Spinner s = (Spinner) findViewById(R.id.svtableSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, arraySpinner);
        s.setAdapter(adapter);

        //Set buttons to go to the correct view
        Button bevestig = (Button) findViewById(R.id.svtableButton);
        Button cancel = (Button) findViewById(R.id.svcancelButton1);


        bevestig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = s.getSelectedItem().toString();
                startActivity(new Intent(PopVoortgang.this,Voortgang.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopVoortgang.this,MainActivity.class));
            }
        });


    }

}
