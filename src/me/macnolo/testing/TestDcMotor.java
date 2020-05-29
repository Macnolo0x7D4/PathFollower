package me.macnolo.testing;

import me.macnolo.models.DcMotor;

public class TestDcMotor extends DcMotor {

    public TestDcMotor(byte id) {
        super(id);
    }

    @Override
    public void setPower(double power) {
        super.setPower(power);
        System.out.println("[Motor " + getId() + "]: Now, power is: " + power);
    }

    @Override
    public double getPower() {
        System.out.println("[Motor " + getId() + "]: Power is: " + super.getPower());
        return super.getPower();
    }
}
