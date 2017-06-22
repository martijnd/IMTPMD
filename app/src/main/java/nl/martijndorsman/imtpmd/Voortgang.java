package nl.martijndorsman.imtpmd;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;

/**
 * Created by Martijn on 15/06/17.
 */


// Klasse waarin de grafieken komen
public class Voortgang extends Activity{
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.voortgang);
    }
}
