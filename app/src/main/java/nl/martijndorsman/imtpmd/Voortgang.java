package nl.martijndorsman.imtpmd;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import static nl.martijndorsman.imtpmd.PopSpinner.item;


/**
 * Created by Martijn on 15/06/17.
 */

// Klasse waarin de grafieken komen
public class Voortgang extends Activity{
    Context context;
    int aantalects;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voortgang);
        PopVoortgang popvoortgang = new PopVoortgang();
        String keuze = popvoortgang.getItem();
        ECTS ects = new ECTS(getApplicationContext());
        switch (keuze){
            case "Jaar 1":
                aantalects = ects.getECTS("Jaar1");
                break;
            case "Jaar 2":
                aantalects = ects.getECTS("Jaar2");
                break;
            case "Jaar 3 en 4":
                aantalects = ects.getECTS("Jaar3en4");
                break;
            case "Keuzevakken":
                aantalects = ects.getECTS("Keuze");
                break;
        }

        ProgressBar progresbarECTS = (ProgressBar) findViewById(R.id.voortgangProgressBar);
        progresbarECTS.setMax(60);
        progresbarECTS.setProgress(aantalects);
    }







}
