package nl.martijndorsman.imtpmd;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Martijn on 19/06/17.
 */

public class GradeEditWindow extends Activity {
    public String nieuwCijfer;

    public GradeEditWindow (){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gradeeditwindow);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.4));

        final EditText gradeEdit = (EditText) findViewById(R.id.etGradeEdit);
        final Button gradeButton = (Button) findViewById(R.id.gradeButton);
        gradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nieuwCijfer = gradeEdit.getText().toString();
            }
        });

    }
}
