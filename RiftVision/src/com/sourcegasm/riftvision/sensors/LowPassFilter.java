package com.sourcegasm.riftvision.sensors;

/**
 * Created by klemen on 18.8.2015.
 */
public class LowPassFilter {
    private boolean first = true;
    public double smoothedValue;
    //private long lastUpdate;
    public int smoothing;

    public LowPassFilter(int smoothing){
        this.smoothing = smoothing;
    }

    public double calculate(double newValue) {
        if(first) {
            smoothedValue = newValue;
            //lastUpdate = System.currentTimeMillis();
            first = false;
            return newValue;
        }else {
            smoothedValue += (10.0/20.0) * (newValue - smoothedValue) / (float) smoothing;
            //lastUpdate = System.currentTimeMillis();
            return smoothedValue;
        }
    }

    public void clear(){
        first = true;
    }
}
