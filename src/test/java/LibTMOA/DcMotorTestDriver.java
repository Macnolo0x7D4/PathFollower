package LibTMOA;

import LibTMOA.models.DcMotorBase;

class DcMotorTestDriver implements DcMotorBase {

    private byte id;

    private double power;

    public DcMotorTestDriver(byte id) {
        this.id = id;
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public void setPower(double power) {
        this.power = power;
        System.out.println("[Motor " + getId() + "] - Now, power is: " + getPower());
    }

    @Override
    public byte getId() {
        return id;
    }
}
