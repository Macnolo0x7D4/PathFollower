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

    private boolean encoder;
    private boolean pid;

    public Configuration( List<DcMotor> motors ) {
        this.motors = motors;
        this.encoder = false;
        this.encoder = false;
    }

    public List<DcMotor> getMotors(){
        return this.motors;
    }

    public boolean isEncoderUsed() {
        return encoder;
    }

    public boolean isPIDUsed() {
        return pid;
    }
}
