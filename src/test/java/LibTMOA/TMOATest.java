/*
 * Copyright 2020 WinT 3794 (Manuel Díaz Rojo and Alexis Obed García Hernández)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package LibTMOA;

import LibTMOA.controllers.Robot;
import LibTMOA.math.road.FloatPoint;
import LibTMOA.models.config.ChassisConfiguration;
import LibTMOA.models.config.OpMode;
import LibTMOA.server.ComputerDebugging;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TMOATest {
    @Test
    public void testSomeLibraryMethod() {

        TMOA classUnderTest = new TMOA(getTestingConfiguration());

        // classUnderTest.getChassisInformation().getMotors().forEach(dcMotor -> System.out.println("[Motor " + dcMotor.getId() + "]: " + dcMotor.getPower()));
        // assertTrue("getDcMotor(2).getPower() == 1.0 should return 'true'", classUnderTest.getDcMotor((byte) 2).getPower() == 1.0);

        ComputerDebugging computerDebugging = new ComputerDebugging();
        Robot robot = new Robot();
        OpMode opMode = new MyOpMode();
        opMode.init();

        ComputerDebugging.clearLogPoints();


        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(true){

            opMode.loop();

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.update();
            ComputerDebugging.sendRobotLocation(robot);
            ComputerDebugging.sendLogPoint(new FloatPoint(Robot.worldXPosition, Robot.worldYPosition));
            ComputerDebugging.markEndOfUpdate();
        }
    }

    public static ChassisConfiguration getTestingConfiguration() {
        return new ChassisConfiguration(
            List.of(
                    new DcMotorTestDriver((byte) 0),
                    new DcMotorTestDriver((byte) 1),
                    new DcMotorTestDriver((byte) 2),
                    new DcMotorTestDriver((byte) 3)
            ),
            16.16,
            28,
            20,
            2.952755906
        );
    }
}

