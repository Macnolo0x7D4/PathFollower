package me.macnolo.main;

import me.macnolo.direction.StandardMovement;
import me.macnolo.models.Configuration;
import me.macnolo.models.DcMotor;

import java.util.List;

public class TMOA {

    Configuration config;

    public TMOA(Configuration config){
        this.config = config;
        System.out.println("[Main Thread]: The legendary Trigonometric Mecanum Omnidirectional Algorithm is Running!");
    }

    public Configuration getChassisInformation(){
        return this.config;
    }

    public DcMotor getDcMotor(byte id){
        return this.config.getMotors().stream()
                .filter( dcMotor -> dcMotor.getId() == id)
                .findFirst().get();
    }

    public void move(double Vd, double Td, double Vt){
        if (this.config.isEncoderUsed()){

        } else if (this.config.isPIDUsed()){

        } else {
            setMultiplePowers(StandardMovement.move(Vd, Td, Vt));
        }
    }

    public void setMultiplePowers(double[] velocities){
        List<DcMotor> motors = this.config.getMotors();

        for( DcMotor dcMotor : motors ){
            dcMotor.setPower(velocities[motors.indexOf(dcMotor)]);
        }
    }
}
