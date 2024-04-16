package com.example.codechallenge2;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLine {
    double[] values;
    String label;
    int color;
    int startPosition;

    public DataLine(double[] values, String label, int color, int startPosition) {
        this.values = values;
        this.label = label;
        this.color = color;
        this.startPosition = startPosition;
        System.out.println(Arrays.toString(values));
    }

    public List<Entry> getEntries() {
        List<Entry> entries = new ArrayList<>();

        for (int i = startPosition ; i < values.length ; i++) {
            entries.add(new Entry(i, (float) values[i]));
        }
        return entries;
    }

    public double[] getValues() {
        return values;
    }

    public String getLabel() {
        return label;
    }

    public int getColor() {
        return color;
    }

    public int getStartPosition() {
        return startPosition;
    }
}
