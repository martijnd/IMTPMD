package nl.martijndorsman.imtpmd;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.models.CourseModel;

/**
 * Created by Martijn on 16/06/17.
 */
// Adapter om de ViewHolder in te stellen om de RecyclerView te gebruiken met de SQLiteDatabase
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    private EditText gradetxt;
    private Button gradeButton, cancelButton;
    Context context;
    VakkenlijstActivity vlActivity = new VakkenlijstActivity();
    // Een arraylist volgens de layout van de Coursemodel klasse
    ArrayList<CourseModel> courses;

    public MyAdapter(Context context, ArrayList<CourseModel> courses){
        this.context = context;
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

    private void showDialog(final String name, final String ects, final String period, final String newGrade){
        final String tabel = VakkenlijstActivity.currentTable;
        final DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("Test", "Geklikt op item");
        d.setContentView(R.layout.gradeeditwindow);
        gradetxt = (EditText) d.findViewById(R.id.etGradeEdit);
        gradeButton = (Button) d.findViewById(R.id.gradeButton);
        cancelButton = (Button) d.findViewById(R.id.cancelButton);

        gradetxt.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean focused)
            {
                if (focused)
                {
                    d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        gradeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String newGrade = gradetxt.getText().toString();
                if(!newGrade.equals("")) {
                    Double newGradeDouble = Double.parseDouble(newGrade);
                    if (newGradeDouble <= 10 && newGradeDouble > 0) {
                        dbAdapter.openDB();
                        dbAdapter.update(tabel, name, newGrade);
                        d.dismiss();
                        vlActivity.retrieve(tabel, context);
                        dbAdapter.closeDB();
                    } else {
                        Toast.makeText(context, "Geef een cijfer tussen de 1 en 10", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Geef een cijfer tussen de 1 en 10", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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