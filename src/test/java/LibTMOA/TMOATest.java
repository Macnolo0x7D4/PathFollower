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
        assertTrue("getDcMotor(2).getPower() == 1.0 should return 'true'", classUnderTest.getDcMotor((byte)2).getPower() == 1.0);
    }

    public static ChassisConfiguration getTestingConfiguration(){
        return new ChassisConfiguration(
            List.of(
                new DcMotorTestDriver( (byte) 0),
                new DcMotorTestDriver( (byte) 1),
                new DcMotorTestDriver( (byte) 2),
                new DcMotorTestDriver( (byte) 3)
            ),
            16.16,
            28,
            20,
            2.952755906
        );
    }
}

