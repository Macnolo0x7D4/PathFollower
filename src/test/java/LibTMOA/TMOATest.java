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

import LibTMOA.models.config.ChassisConfiguration;
import LibTMOA.models.structures.JoystickCoordinates;
import LibTMOA.models.structures.MecanumDirectives;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TMOATest {
    @Test
    public void testSomeLibraryMethod() {
        TMOA classUnderTest = new TMOA(getTestingConfiguration());

        classUnderTest.move(new JoystickCoordinates(2,0));
        //classUnderTest.move(new MecanumDirectives(1,0,0));

        classUnderTest.getChassisInformation().getMotors().forEach(dcMotor -> System.out.println("[Motor " + dcMotor.getId() + "]: " + dcMotor.getPower()));
        // assertTrue("getDcMotor(2).getPower() == 1.0 should return 'true'", classUnderTest.getDcMotor((byte) 2).getPower() == 1.0);
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

