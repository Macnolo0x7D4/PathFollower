package me.macnolo.models;

public class DcMotor {

    private byte id;

    private double power;
    private double encoder;

    public DcMotor(byte id){
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
