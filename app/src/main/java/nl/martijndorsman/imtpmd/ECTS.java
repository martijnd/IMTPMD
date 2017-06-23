package nl.martijndorsman.imtpmd;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.models.CourseModel;

/**
 * Created by Robert on 22-6-2017.
 */
// Deze klassen is een ECTS object waar in de functies staan om de ECTS op te kunnen halen
public class ECTS {

    private Context context;
    private ArrayList<CourseModel> courses = new ArrayList<>();


    public ECTS(Context context){
        this.context = context;
    }

    public int getECTS(String tabel){
        retrieve(tabel, context);
        return totaalBehaaldeECTS(tabel);
    }

    private void retrieve(String tabel, Context context) {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
        dbAdapter.openDB();
        courses.clear();

        Cursor c = dbAdapter.getAllData(tabel);
        //LOOP EN VOEG AAN ARRAYLIST TOE
        while (c.moveToNext()) {

            String name = c.getString(0);
            String ects = c.getString(1);
            String period = c.getString(2);
            String grade = c.getString(3);
            String status = "Niet behaald";
            Double gradeDouble = Double.parseDouble(grade);
            if (gradeDouble >= 5.5) {
                status = "Behaald";
            }
            CourseModel p = new CourseModel(name, ects, period, grade, status);
            //VOEG TOE AAN ARRAYLIST
            courses.add(p);
        }
        c.close();
        dbAdapter.closeDB();
    }

    private int totaalBehaaldeECTS(String tabel) {

        int ects = 0;
        switch (tabel) {
            case "Jaar1":

                int totaalECTSjaar1 = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSjaar1 += ECTSint;
                    }
                }
                ects = totaalECTSjaar1;
            break;
            case "Jaar2":
                int totaalECTSjaar2 = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSjaar2 += ECTSint;
                    }
                }
                ects = totaalECTSjaar2;
            break;
            case "Jaar3en4":
                int totaalECTSjaar3en4 = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSjaar3en4 += ECTSint;
                    }
                }
                ects = totaalECTSjaar3en4;
            break;
            case "Keuze":
                int totaalECTSKeuze = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSKeuze += ECTSint;
                    }
                }
                ects = totaalECTSKeuze;
            break;
        }
        return ects;
    }
}
