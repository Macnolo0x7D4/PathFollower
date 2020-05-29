package me.macnolo;

import me.macnolo.main.TMOA;
import me.macnolo.models.DcMotor;
import me.macnolo.testing.TestConfig;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TMOA mecanum = new TMOA(TestConfig.getDefaultConfiguration());

        List<DcMotor> motors = mecanum.getChassisInformation().getMotors();

        motors.stream().forEach( dcMotor -> dcMotor.getPower());
    }
}
