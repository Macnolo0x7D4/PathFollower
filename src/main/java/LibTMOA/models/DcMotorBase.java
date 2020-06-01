package LibTMOA.models;

public class DcMotorBase {

    private byte id;

    private double power;
    private double encoder;

    public DcMotorBase(byte id){
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }
}
