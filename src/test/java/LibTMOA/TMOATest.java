package LibTMOA;

import LibTMOA.models.ChassisConfiguration;
import LibTMOA.models.DcMotorBase;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TMOATest {
    @Test public void testSomeLibraryMethod() {
        TMOA classUnderTest = new TMOA(getTestingConfiguration());

        classUnderTest.move(1,1);
        //assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }

    public static ChassisConfiguration getTestingConfiguration(){
        return new ChassisConfiguration(
            List.of(
                new DcMotorTestDriver( (byte) 0),
                new DcMotorTestDriver( (byte) 1),
                new DcMotorTestDriver( (byte) 2),
                new DcMotorTestDriver( (byte) 3)
            )
        );
    }
}

class DcMotorTestDriver implements DcMotorBase{

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
