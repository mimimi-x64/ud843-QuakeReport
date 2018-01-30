package com.example.android.quakereport;

/**
 * Created by phartmann on 30/01/2018.
 */

public class QuakeList {
    private double mag;
    private String local;
    private String date;

    public QuakeList( double mag, String local, String date ) {
        this.mag = mag;
        this.local = local;
        this.date = date;
    }

    public double getMag() {
        return mag;
    }

    public String getLocal() {
        return local;
    }

    public String getDate() {
        return date;
    }
}
