package LibTMOA.models;

import java.util.List;

public class ChassisConfiguration {
    private List<DcMotorBase> motors;

    private double width;
    private int cpr;
    private int gearratio;
    private double diameter;
    private double cpi;
    private double conversion;

    private boolean encoder;
    private boolean pid;

    public ChassisConfiguration(List<DcMotorBase> motors ) {
        this.motors = motors;
        this.encoder = false;
        this.encoder = false;
    }

    public List<DcMotorBase> getMotors(){
        return this.motors;
    }

    public boolean isEncoderUsed() {
        return encoder;
    }

    public boolean isPIDUsed() {
        return pid;
    }
}
