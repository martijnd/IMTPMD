package nl.martijndorsman.imtpmd;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.models.CourseModel;

/**
 * Created by Robert on 22-6-2017.
 */

public class ECTS {

    Context context;
    DatabaseAdapter dbAdapter;
    private static int totaalECTSjaar1;
    private static int totaalECTSjaar2;
    private static int totaalECTSjaar3en4;
    private static int totaalECTSKeuze;
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

        int etcs = 0;
        switch (tabel) {
            case "Jaar1":

                totaalECTSjaar1 = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSjaar1 += ECTSint;
                    }
                }
                etcs = totaalECTSjaar1;
                break;
            case "Jaar2":
                totaalECTSjaar2 = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSjaar2 += ECTSint;
                    }
                }
                etcs =  totaalECTSjaar2;
                break;
            case "Jaar3en4":
                totaalECTSjaar3en4 = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSjaar3en4 += ECTSint;
                    }
                }
                ;
                etcs =  totaalECTSjaar3en4;
                break;
            case "Keuze":
                totaalECTSKeuze = 0;
                for (int i = 0; i < courses.size(); i++) {
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5) {
                        totaalECTSKeuze += ECTSint;
                    }
                }
                etcs =  totaalECTSKeuze;
                break;
        }
        return etcs;
    }
}
