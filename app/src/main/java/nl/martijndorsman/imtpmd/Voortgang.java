package nl.martijndorsman.imtpmd;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * Created by Martijn on 15/06/17.
 */

// Klasse waarin de grafieken komen
public class Voortgang extends AppCompatActivity{
    public static final int maxEctsJaar1 = 60;
    public static final int maxEctsJaar2 = 54;
    public static final int maxEctsJaar3en4 = 120;
    public static final int maxEctsJaarKeuze = 12;
    public static int aantalECTSjaar1 = 0;
    public static int aantalECTSjaar2 = 0;
    public static int aantalECTSjaar3en4 = 0;
    public static int aantalECTSKeuze = 0;
    PieChart mChart, mChart2, mChart3en4, mChartKeuze;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voortgang);
        ECTS etcs = new ECTS(getApplicationContext());
        aantalECTSjaar1 = etcs.getECTS("Jaar1");
        aantalECTSjaar2 = etcs.getECTS("Jaar2");
        aantalECTSjaar3en4 = etcs.getECTS("Jaar3en4");
        aantalECTSKeuze = etcs.getECTS("Keuze");
        mChart = (PieChart) findViewById(R.id.chart1);
        setData(aantalECTSjaar1, mChart, maxEctsJaar1);

        mChart2 = (PieChart) findViewById(R.id.chart2);
        setData(aantalECTSjaar2, mChart2, maxEctsJaar2);

        mChart3en4 = (PieChart) findViewById(R.id.chart3en4);
        setData(aantalECTSjaar3en4, mChart3en4, maxEctsJaar3en4);

        mChartKeuze = (PieChart) findViewById(R.id.chartKeuze);
        setData(aantalECTSKeuze, mChartKeuze, maxEctsJaarKeuze);
    }

    private void setData(int aantal, PieChart chart, int maxECTS) {
        chart.setTouchEnabled(false);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        chart.getLegend().setEnabled(true);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Double percentage = (double) ((aantal / maxECTS) * 100);
        String percentageS = String.valueOf(percentage);
        Log.d(String.valueOf(percentage), String.valueOf(percentageS));
        chart.setCenterText(percentageS + "%");
        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(aantal, 0));
        yValues.add(new PieEntry(maxECTS - aantal, 1));
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(67,160,71));
        colors.add(Color.rgb(255,0,0));
        PieDataSet dataSet = new PieDataSet(yValues, "Studiepunten");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.invalidate();
        Log.d("aantal =", String.valueOf(aantal));
    }
}
