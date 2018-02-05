package com.example.android.quakereport;

/**
 * Created by phartmann on 30/01/2018.
 */

public class QuakeList {

    private double mag;
    private String local;
    private long timeInMilliseconds;
    private String url;

    public QuakeList( double mag, String local, long timeInMilliseconds, String url ) {
        this.mag = mag;
        this.local = local;
        this.timeInMilliseconds = timeInMilliseconds;
        this.url = url;
    }

    public double getMag() {
        return mag;
    }
    public String getLocal() {
        return local;
    }
    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }
    public String getUrl(){
        return url;
    }

}
