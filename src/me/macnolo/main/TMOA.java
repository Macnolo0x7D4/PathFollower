package me.macnolo.main;

import me.macnolo.models.Configuration;
import me.macnolo.models.DcMotor;

public class TMOA {

    Configuration config;

    public TMOA(Configuration config){
        this.config = config;
        System.out.println("[Main Thread]: The legendary Trigonometric Mecanum Omnidirectional Algorithm is Running!");

        getDcMotor((byte) 1).setPower(1);
    }

    public Configuration getChassisInformation(){
        return this.config;
    }

    public DcMotor getDcMotor(byte id){
        return this.config.getMotors().stream()
                .filter( dcMotor -> dcMotor.getId() == id)
                .findFirst().get();
    }
}
