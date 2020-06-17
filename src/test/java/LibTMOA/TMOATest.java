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

import LibTMOA.debug.telemetries.ConsolePrinter;
import LibTMOA.io.PathProcessor;
import LibTMOA.io.PathSimulator;
import LibTMOA.models.config.ChassisConfiguration;
import LibTMOA.models.config.ChassisTypes;
import LibTMOA.models.structures.EncoderProperties;
import LibTMOA.utils.CurvePoint;
import org.junit.Test;

import java.util.List;

public class TMOATest {
    private static boolean running = false;
    private static final String ROUTES = "[\n" +
            "  {\n" +
            "    \"x\": 0.0,\n" +
            "    \"y\": 0.0,\n" +
            "    \"move_speed\": 0.5,\n" +
            "    \"turn_speed\": 0.5,\n" +
            "    \"follow_distance\": 50.0,\n" +
            "    \"slow_down_turn_radians\": 50.0,\n" +
            "    \"slow_down_turn_amount\": 1.0\n" +
            "  },\n" +
            "  {\n" +
            "    \"x\": 1.0,\n" +
            "    \"y\": 1.0,\n" +
            "    \"move_speed\": 0.5,\n" +
            "    \"turn_speed\": 0.5,\n" +
            "    \"follow_distance\": 50.0,\n" +
            "    \"slow_down_turn_radians\": 50.0,\n" +
            "    \"slow_down_turn_amount\": 1.0\n" +
            "  },\n" +
            "  {\n" +
            "    \"x\": 2.0,\n" +
            "    \"y\": 2.0,\n" +
            "    \"move_speed\": 0.5,\n" +
            "    \"turn_speed\": 0.5,\n" +
            "    \"follow_distance\": 50.0,\n" +
            "    \"slow_down_turn_radians\": 50.0,\n" +
            "    \"slow_down_turn_amount\": 1.0\n" +
            "  },\n" +
            "  {\n" +
            "    \"x\": 3.0,\n" +
            "    \"y\": 3.0,\n" +
            "    \"move_speed\": 0.5,\n" +
            "    \"turn_speed\": 0.5,\n" +
            "    \"follow_distance\": 50.0,\n" +
            "    \"slow_down_turn_radians\": 50.0,\n" +
            "    \"slow_down_turn_amount\": 1.0\n" +
            "  }\n" +
            "]";

    List<CurvePoint> functionalPath;

    public static ChassisConfiguration getTestingConfiguration() {
        return new ChassisConfiguration(
                List.of(
                        new DcMotorTestDriver((byte) 0),
                        new DcMotorTestDriver((byte) 1),
                        new DcMotorTestDriver((byte) 2),
                        new DcMotorTestDriver((byte) 3)
                ),
                new EncoderProperties(
                        16.16,
                        28,
                        20,
                        2.952755906
                ),
                ChassisTypes.DRIVE_TRAIN
        );
    }

    @Test
    public void testSomeLibraryMethod() {
        TMOA classUnderTest = new TMOA(getTestingConfiguration(), new ConsolePrinter());

        // classUnderTest.getChassisInformation().getMotors().forEach(dcMotor -> System.out.println("[Motor " + dcMotor.getId() + "]: " + dcMotor.getPower()));
        // assertTrue("getDcMotor(2).getPower() == 1.0 should return 'true'", classUnderTest.getDcMotor((byte) 2).getPower() == 1.0);

        // PathReader reader = new PathReader("./file.json");
        PathSimulator reader = new PathSimulator(ROUTES);

        PathProcessor processor = new PathProcessor(reader.getRawPath());

        functionalPath = processor.createFunctionalPath();

        classUnderTest.startPathFollower(functionalPath, Math.toRadians(90));
    }
}

