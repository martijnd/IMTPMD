package nl.martijndorsman.imtpmd;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.models.CourseModel;

/**
 * Created by Martijn on 16/06/17.
 */
// Adapter om de ViewHolder in te stellen om de RecyclerView te gebruiken met de SQLiteDatabase
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    EditText gradetxt;
    Button gradeButton;
    Context c;
    // Een arraylist volgens de layout van de Coursemodel klasse
    ArrayList<CourseModel> courses;
    public MyAdapter(Context c, ArrayList<CourseModel> courses){
        this.c = c;
        this.courses = courses;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // view object
        // Stel de view in met de LayoutInflater en de contxt. Vervolgens inflate de items van de recyclerview
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitem, null);

        // holder
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    // bind view aan de holder
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.nametxt.setText(courses.get(position).getName());
        holder.ectstxt.setText((courses.get(position).getEcts()));
        holder.periodtxt.setText((courses.get(position).getPeriod()));
        holder.gradetxt.setText((courses.get(position).getGrade()));

        // clicked action
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                String name = courses.get(pos).getName();
                String ects = courses.get(pos).getEcts();
                String period = courses.get(pos).getPeriod();
                String grade = courses.get(pos).getGrade();
                // Voeg een invoerscherm toe
                showDialog(name, ects, period, grade);
                Snackbar.make(v, courses.get(pos).getName(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    //TODO: Zorgen dat er een refresh op de activity VakkenLijst komt.
    private void showDialog(final String name, final String ects, final String period, final String newGrade){
        final String tabel = VakkenlijstActivity.currentTable;
        final DatabaseAdapter dbAdapter = new DatabaseAdapter(c);
        final Dialog d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.gradeeditwindow);
        gradetxt = (EditText) d.findViewById(R.id.etGradeEdit);
        gradeButton = (Button) d.findViewById(R.id.gradeButton);
        gradeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String newGrade = gradetxt.getText().toString();
                dbAdapter.update(tabel, name, ects, period, newGrade);
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
