package com.example.codechallenge2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String currency, datefrom, dateto;
    LineChart chart;
    ToggleButton sma10, sma30;
    TextView info;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chart = (LineChart) findViewById(R.id.chart);
        sma10 = findViewById(R.id.sma10);
        sma30 = findViewById(R.id.sma30);
        info = findViewById(R.id.textView);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayList<DataLine> dataLines = new ArrayList<>();

        currency = prefs.getString("setCurrency", "SEK Kronor");
        datefrom = prefs.getString("startDate", "2024-01-01");
        dateto = prefs.getString("endDate", "2024-03-31");

        info.setText(currency + "  |  " + datefrom + " - " + dateto);

        ArrayList<Double> currencyValues = getCurrencyValues(currency, datefrom, dateto);
        double[] currencyValue = parseArrayListToDoubleArray(currencyValues);

        dataLines.add(new DataLine(currencyValue, currency, Color.BLACK, 0));
        improvedChart(dataLines);

        sma10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataLines.add(new DataLine(Statistics.movingAvg(currencyValue, 10), "SMA 10", Color.RED, 9));
                    improvedChart(dataLines);
                } else {
                    Iterator<DataLine> iterator = dataLines.iterator();
                    while (iterator.hasNext()) {
                        DataLine dataLine = iterator.next();
                        if (dataLine.getLabel().equals("SMA 10")) {
                            iterator.remove();
                            break;
                        }
                    }
                    improvedChart(dataLines);
                }
            }
        });

        sma30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataLines.add(new DataLine(Statistics.movingAvg(currencyValue, 30), "SMA 30", Color.BLUE, 29));
                    improvedChart(dataLines);
                } else {
                    Iterator<DataLine> iterator = dataLines.iterator();
                    while (iterator.hasNext()) {
                        DataLine dataLine = iterator.next();
                        if (dataLine.getLabel().equals("SMA 30")) {
                            iterator.remove();
                            break;
                        }
                    }
                    improvedChart(dataLines);
                }
            }
        });
    }

    public void improvedChart(ArrayList<DataLine> dataLines) {
        List<ILineDataSet> dataSeries = new ArrayList<>();

        for (DataLine dataLine : dataLines) {
            LineDataSet lineDataSet = new LineDataSet(
                    dataLine.getEntries(),
                    dataLine.getLabel()
            );
            lineDataSet.setDrawValues(false);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setColor(dataLine.getColor());

            dataSeries.add(lineDataSet);
        }
        LineData lineData = new LineData(dataSeries);

        chart.setData(lineData);
        chart.invalidate();
    }

    public ArrayList<Double> getCurrencyValues(String currency, String from, String to) {

        CurrencyAPI api = new CurrencyAPI();
        ArrayList<Double> currencyData = null;

        String urlString = String.format("https://api.frankfurter.app/%s..%s",
                from.trim(),
                to.trim());

        try {
            String jsonData = api.execute(urlString).get();

            if (jsonData != null) {
                currencyData = api.getCurrencyData(jsonData, currency.trim());
                Toast.makeText(getApplicationContext(), String.format("Hämtade %s valutakursvärden från servern", currencyData.size()), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Kunde inte hämta växelkursdata från servern: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return currencyData;
    }

    public static double[] parseArrayListToDoubleArray(ArrayList<Double> arrayList) {
        double[] doubleArray = new double[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            doubleArray[i] = arrayList.get(i);
        }
        return doubleArray;
    }

    public void openSettings(View view) {
        Intent intent = new Intent (this, SettingsActivity.class);
        startActivity(intent);
    }
}