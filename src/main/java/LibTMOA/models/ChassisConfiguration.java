
package LibTMOA.models;

import java.util.List;

public class ChassisConfiguration {
    private List<DcMotorBase> motors;

    private double width;
    private int cpr;
    private int gearRatio;
    private double diameter;
    private double cpi;
    private double conversion;

    private ExecutionModes mode;

    /**
     * Creates an instance of a Simple Chassis Configuration.
     *
     * @param motors A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     */
    public ChassisConfiguration(List<DcMotorBase> motors) {
        this.mode = ExecutionModes.SIMPLE;
        this.motors = motors;
    }

    /**
     * Creates an instance of an Using-Encoders Chassis Configuration.
     * @param motors A List of 4 DcMotorBase (not interface, your driver). Order: LF, RF, LB, RB.
     * @param width
     * @param cpr
     * @param gearRatio
     * @param diameter
     */
    public ChassisConfiguration(List<DcMotorBase> motors, double width, int cpr, int gearRatio, double diameter) {
        this.mode = ExecutionModes.ENCODER;
        this.motors = motors;
        this.width = width;
        this.cpr = cpr;
        this.gearRatio = gearRatio;
        this.diameter = diameter;

        this.cpi = (cpr * gearRatio) / (Math.PI * diameter);
    }

    public List<DcMotorBase> getMotors(){
        return this.motors;
    }

    public ExecutionModes getMode(){
        return mode;
    }
}
