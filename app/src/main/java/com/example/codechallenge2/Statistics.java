package com.example.codechallenge2;

public class Statistics {
    public static double[] movingAvg(double[] values, int window) {
        double[] sma = new double[values.length];

        for (int i = window - 1 ; i < values.length ; i++) {
            double sum = 0;

            for (int j = 0; j < window; j++) {
                sum += values[i - j];
            }
            sma[i] = sum / window;
        }
        return sma;
    }
}