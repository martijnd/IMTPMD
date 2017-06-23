package nl.martijndorsman.imtpmd;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nl.martijndorsman.imtpmd.database.DatabaseAdapter;
import nl.martijndorsman.imtpmd.models.CourseModel;

import static nl.martijndorsman.imtpmd.VakkenlijstActivity.currentTable;
import static nl.martijndorsman.imtpmd.VakkenlijstActivity.totaalECTSKeuze;
import static nl.martijndorsman.imtpmd.VakkenlijstActivity.totaalECTSjaar1;
import static nl.martijndorsman.imtpmd.VakkenlijstActivity.totaalECTSjaar2;
import static nl.martijndorsman.imtpmd.VakkenlijstActivity.totaalECTSjaar3en4;

/**
 * Created by Martijn on 16/06/17.
 */
// Adapter om de ViewHolder in te stellen om de RecyclerView te gebruiken met de SQLiteDatabase
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    private EditText gradetxt;
    private Button gradeButton, cancelButton;
    private String newGrade;
    private TextView statustxt;
    Context context;
    ECTS ects;
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
        return new MyHolder(v);

    }

    // bind view aan de holder
    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.nametxt.setText(courses.get(position).getName());
        holder.ectstxt.setText(courses.get(position).getEcts());
        holder.periodtxt.setText(courses.get(position).getPeriod());
        holder.gradetxt.setText(courses.get(position).getGrade());
        holder.statustxt.setText(courses.get(position).getStatus());
        if(holder.statustxt.getText() == "Behaald"){
            holder.statustxt.setTextColor(ContextCompat.getColor(context, R.color.behaald));
        }   else {
            holder.statustxt.setTextColor(ContextCompat.getColor(context, R.color.nietbehaald));
        }



        // clicked action
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                String name = courses.get(pos).getName();
                // Voeg een invoerscherm toe
                showDialog(name, pos);
                // Toon een Snackbar met het huidige ingedrukte cijfer (UX design)
                Snackbar.make(v, courses.get(pos).getName(), Snackbar.LENGTH_SHORT).show();
                // Vertel de adapter dat de huidige dataset is aangepast, en de View dus geupdate moet worden
                notifyDataSetChanged();
            }
        });
    }
    // Functie om het cijfer invoerscherm te tonen
    private void showDialog(final String name, final int pos){
        final String tabel = currentTable;
        final DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
        final Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.gradeeditwindow);
        gradetxt = (EditText) d.findViewById(R.id.etGradeEdit);
        gradeButton = (Button) d.findViewById(R.id.gradeButton);
        cancelButton = (Button) d.findViewById(R.id.cancelButton);
        statustxt = (TextView) d.findViewById(R.id.behaaldresulttxt);
//      Keyboard pop up wanneer het dialoog tevoorschijn komt (UX design)
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
        // OnClicklistener voor de accepteerbutton in het dialog
        gradeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //vraag de waarde van de EditText op
                newGrade = gradetxt.getText().toString();
                // Controleer of de EditText niet leeg is
                if(!newGrade.equals("")) {
                    Double newGradeDouble = Double.parseDouble(newGrade);
                    // Controleer of het ingevoerde cijfer tussen de 1 en 10 ligt
                    if (newGradeDouble <= 10 && newGradeDouble > 0) {
                        // Open de database en update de database met het nieuwe cijfer
                        dbAdapter.openDB();
                        dbAdapter.update(tabel, name, newGrade);
                        d.dismiss();
                        // Vraag de nieuwe info op uit de database
                        ects = new ECTS(context);
                        ects.getECTS(tabel);

                        // Sluit de database weer netjes
                        dbAdapter.closeDB();
                        courses.get(pos).setGrade(newGrade);
                        // Controleer of het cijfer een voldoende is of niet
                        if (newGradeDouble >= 5.5){
                            courses.get(pos).setStatus("Behaald");

                        } else {
                            courses.get(pos).setStatus("Niet behaald");
                        }
                        notifyDataSetChanged();
                        // Show een Toast wanneer de input leeg is of geen legitiem cijfer is
                    } else {
                        Toast.makeText(context, "Geef een cijfer tussen de 1 en 10", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Geef een cijfer tussen de 1 en 10", Toast.LENGTH_SHORT).show();
                }
                totaalBehaaldeECTS(tabel);
            }
        });
        // Maak een onClicklistener voor de annuleerknop die het dialoog weghaalt
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

    public void totaalBehaaldeECTS(String tabel){
        switch(tabel){
            case "Jaar1":
                totaalECTSjaar1 = 0;
                for (int i = 0; i < courses.size(); i++){
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5){
                        totaalECTSjaar1 += ECTSint;
                    }
                }
                break;
            case "Jaar2":
                totaalECTSjaar2 = 0;
                for (int i = 0; i < courses.size(); i++){
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5){
                        totaalECTSjaar2 += ECTSint;
                    }
                }
                break;
            case "Jaar3en4":
                totaalECTSjaar3en4 = 0;
                for (int i = 0; i < courses.size(); i++){
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5){
                        totaalECTSjaar3en4 += ECTSint;
                    }
                }
                break;
            case "Keuze":
                totaalECTSKeuze = 0;
                for (int i = 0; i < courses.size(); i++){
                    Double gradeDouble = Double.parseDouble(courses.get(i).getGrade());
                    int ECTSint = Integer.parseInt(courses.get(i).getEcts());
                    if (gradeDouble >= 5.5){
                        totaalECTSKeuze += ECTSint;
                    }
                }
        }
    }
}