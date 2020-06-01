package LibTMOA;

import LibTMOA.direction.StandardMovement;
import LibTMOA.math.Calculate;
import LibTMOA.models.ChassisConfiguration;
import LibTMOA.models.DcMotorBase;

import java.util.List;

public class TMOA {

    ChassisConfiguration config;

    public TMOA(ChassisConfiguration config){
        this.config = config;
        System.out.println("[Main Thread]: The legendary Trigonometric Mecanum Omnidirectional Algorithm is Running!");
    }

    public ChassisConfiguration getChassisInformation(){
        return this.config;
    }

    public DcMotorBase getDcMotor(byte id){
        return this.config.getMotors().stream()
                .filter( dcMotor -> dcMotor.getId() == id)
                .findFirst().get();
    }

    public void move(double Vd, double Td, double Vt){
        setMultiplePowers(StandardMovement.move(Vd, Td, Vt));
    }

    public void move(double y, double x){
        setMultiplePowers(StandardMovement.move(y, x));
    }

    public void setMultiplePowers(double[] velocities){
        List<DcMotorBase> motors = this.config.getMotors();

        for( DcMotorBase dcMotor : motors ){
            dcMotor.setPower(Calculate.roundPower( velocities[motors.indexOf(dcMotor)] ));
        }
    }
}
