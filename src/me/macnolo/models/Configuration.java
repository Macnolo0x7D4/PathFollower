package me.macnolo.models;

import java.util.List;

public class Configuration {
    private List<DcMotor> motors;

    private double width;
    private int cpr;
    private int gearratio;
    private double diameter;
    private double cpi;
    private double conversion;

    public Configuration(
            List<DcMotor> motors,
            double width,
            int cpr, int gearratio,
            double diameter,
            double cpi,
            double conversion
    ) {
        this.motors = motors;
        this.width = width;
        this.cpr = cpr;
        this.gearratio = gearratio;
        this.diameter = diameter;
        this.cpi = cpi;
        this.conversion = conversion;
    }

    public List<DcMotor> getMotors(){
        return this.motors;
    }
}
