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

package org.wint3794.pathfollower;

import org.junit.Test;
import org.wint3794.pathfollower.debug.Log;
import org.wint3794.pathfollower.debug.telemetries.ConsolePrinter;
import org.wint3794.pathfollower.debug.telemetries.Server;
import org.wint3794.pathfollower.drivers.DcMotorTestDriver;
import org.wint3794.pathfollower.io.PathProcessor;
import org.wint3794.pathfollower.io.PathSimulator;
import org.wint3794.pathfollower.models.config.ChassisConfiguration;
import org.wint3794.pathfollower.models.config.ChassisTypes;
import org.wint3794.pathfollower.models.exceptions.NotCompatibleConfigurationException;
import org.wint3794.pathfollower.models.structures.EncoderProperties;
import org.wint3794.pathfollower.geometry.CurvePoint;

import java.util.List;

public class PathFollowerTest {
    private static final String ROUTES = "[\n" +
            "  {\n" +
            "    \"x\": 10.0,\n" +
            "    \"y\": 14.0,\n" +
            "    \"move_speed\": 0.5,\n" +
            "    \"turn_speed\": 0.5,\n" +
            "    \"follow_distance\": 50.0,\n" +
            "    \"slow_down_turn_radians\": 50.0,\n" +
            "    \"slow_down_turn_amount\": 1.0\n" +
            "  },\n" +
            "  {\n" +
            "    \"x\": 200.0,\n" +
            "    \"y\": 240.0,\n" +
            "    \"move_speed\": 0.5,\n" +
            "    \"turn_speed\": 0.5,\n" +
            "    \"follow_distance\": 50.0,\n" +
            "    \"slow_down_turn_radians\": 50.0,\n" +
            "    \"slow_down_turn_amount\": 1.0\n" +
            "  },\n" +
            "  {\n" +
            "    \"x\": 240.0,\n" +
            "    \"y\": 200.0,\n" +
            "    \"move_speed\": 0.5,\n" +
            "    \"turn_speed\": 0.5,\n" +
            "    \"follow_distance\": 50.0,\n" +
            "    \"slow_down_turn_radians\": 50.0,\n" +
            "    \"slow_down_turn_amount\": 1.0\n" +
            "  },\n" +
            "  {\n" +
            "    \"x\": 10.0,\n" +
            "    \"y\": 14.0,\n" +
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
        PathFollower classUnderTest = new PathFollower(getTestingConfiguration(), new Server());

        // classUnderTest.getChassisInformation().getMotors().forEach(dcMotor -> System.out.println("[Motor " + dcMotor.getId() + "]: " + dcMotor.getPower()));
        // assertTrue("getDcMotor(2).getPower() == 1.0 should return 'true'", classUnderTest.getDcMotor((byte) 2).getPower() == 1.0);

        // PathReader reader = new PathReader("./file.json");
        PathSimulator reader = new PathSimulator(ROUTES);

        PathProcessor processor = new PathProcessor(reader.getRawPath());

        functionalPath = processor.createFunctionalPath();

        try {
            classUnderTest.init();

            // for (int i = 0; i < 10; i++) {
            while (true){
                classUnderTest.calculate(functionalPath, Math.toRadians(90));
                Log.update();
            }
        } catch (NotCompatibleConfigurationException e) {
            e.printStackTrace();
        }

        classUnderTest.close();
    }
}

