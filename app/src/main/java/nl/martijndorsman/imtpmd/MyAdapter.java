package nl.martijndorsman.imtpmd;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.models.CourseModel;

/**
 * Created by Martijn on 16/06/17.
 */
// Adapter om de ViewHolder in te stellen om de RecyclerView te gebruiken met de SQLiteDatabase
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
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
                Snackbar.make(v, courses.get(pos).getName(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
