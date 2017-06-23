package nl.martijndorsman.imtpmd;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Martijn on 23/06/17.
 */
//  Klasse om de Format in te stellen van de Studiepunten value in de PieChart
//  Hierdoor worden de studiepunten hele getallen zonder decimalen
//  Deze klasse hoort bij de MPAndroidChart library
public class MyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // geen decimalen
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }
}