package nl.martijndorsman.imtpmd;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by Martijn on 15/06/17.
 */

// Klasse waarin de grafieken komen
public class VoortgangActivity extends AppCompatActivity{
    // De max aantal ECTS die je per cat. kan halen
    public static final int maxEctsJaar1 = 60;
    public static final int maxEctsJaar2 = 54;
    public static final int maxEctsJaar3en4 = 120;
    public static final int maxEctsJaarKeuze = 12;
    // Init de huidige ECTS op nul. Deze worden in de onCreate uit het ECTS object gehaald.
    public static int aantalECTSjaar1 = 0;
    public static int aantalECTSjaar2 = 0;
    public static int aantalECTSjaar3en4 = 0;
    public static int aantalECTSKeuze = 0;
    // De 4 PieCharts waarin we de data gaan weergeven
    PieChart mChart, mChart2, mChart3en4, mChartKeuze;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Bind de layout.activity_voortgang aan deze activity
        setContentView(R.layout.activity_voortgang);
        // Maak een nieuw ECTS object aan waarmee de ECTS van elk jaar opgevraagd kunnen worden
        ECTS ects = new ECTS(getApplicationContext());
        // Vraag per cat. de ECTS op
        aantalECTSjaar1 = ects.getECTS("Jaar1");
        aantalECTSjaar2 = ects.getECTS("Jaar2");
        aantalECTSjaar3en4 = ects.getECTS("Jaar3en4");
        aantalECTSKeuze = ects.getECTS("Keuze");

        // Bind per PieChart de PieChart aan een specifieke id in de layout
        mChart = (PieChart) findViewById(R.id.chart1);
        // Stel de data in van de PieCharts
        setData(aantalECTSjaar1, mChart, maxEctsJaar1);

        mChart2 = (PieChart) findViewById(R.id.chart2);
        setData(aantalECTSjaar2, mChart2, maxEctsJaar2);

        mChart3en4 = (PieChart) findViewById(R.id.chart3en4);
        setData(aantalECTSjaar3en4, mChart3en4, maxEctsJaar3en4);

        mChartKeuze = (PieChart) findViewById(R.id.chartKeuze);
        setData(aantalECTSKeuze, mChartKeuze, maxEctsJaarKeuze);
    }
    // Functie om per PieChart de layout en de data toe te voegen
    private void setData(int BehaaldeECTS, PieChart chart, int maxECTS) {
        chart.setTouchEnabled(false);
        // Stel de legenda in en zet hem in het midden
        Legend legend = chart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        // Geen beschrijving
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        // De inlaad-animatie instellen
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // Het percentage in het midden van de PieChart berekenen en instellen
        double aantalDouble = (double) BehaaldeECTS / (double) maxECTS;
        Double percentage = round(aantalDouble * 100.0);
        String percentageS = String.valueOf(percentage);
        chart.setCenterText(percentageS + "%");
        // De waardes voor de 'y-as' toevoegen
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(BehaaldeECTS, 0));
        yValues.add(new PieEntry(maxECTS - BehaaldeECTS, 1));
        // De kleuren toevoegen
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(getApplicationContext(), R.color.behaald));
        colors.add(ContextCompat.getColor(getApplicationContext(), R.color.nietbehaald));
        // De yValues een label geven
        PieDataSet dataSet = new PieDataSet(yValues, "Studiepunten");
        // Verschillende uiterlijk instellingen
        dataSet.setSliceSpace(2);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(16);
        // Zorg dat de studiepunten getoond worden als geheel getal zonder decimalen
        data.setValueFormatter(new MyValueFormatter());
        // Stel de data in van de PieChart
        chart.setData(data);
        chart.invalidate();
    }

    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
