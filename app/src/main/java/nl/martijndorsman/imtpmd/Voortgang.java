package nl.martijndorsman.imtpmd;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Martijn on 15/06/17.
 */

// Klasse waarin de grafieken komen
public class Voortgang extends AppCompatActivity{
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voortgang);
        ETCS etcs = new ETCS(context);
        int test = etcs.getETCS("Jaar1");

    }






}
