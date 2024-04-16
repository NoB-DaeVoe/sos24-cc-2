package com.example.codechallenge2;

public class Statistics {
    public static double[] movingAvg(double[] values, int window) {
        double[] sma = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            if (i >= window - 1) {
                double sum = 0;
                for (int j = i - (window - 1); j <= i; j++) {
                    sum += values[j];
                }
                double avg = sum / window;

                sma[i] = avg;
            } else {
                sma[i] = Double.NaN;
            }
        }

        return sma;
    }
}