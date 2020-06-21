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
import org.wint3794.pathfollower.io.PathReader;
import org.wint3794.pathfollower.models.config.ChassisConfiguration;
import org.wint3794.pathfollower.models.config.ChassisTypes;
import org.wint3794.pathfollower.models.exceptions.NotCompatibleConfigurationException;
import org.wint3794.pathfollower.models.structures.EncoderProperties;
import org.wint3794.pathfollower.geometry.CurvePoint;

import java.util.List;

public class PathFollowerTest {

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

        PathReader reader = new PathReader(this.getClass().getResource("/file.json"));

        PathProcessor processor = new PathProcessor(reader.getRawPath());

        functionalPath = processor.createFunctionalPath();

        try {
            classUnderTest.init();

            for (int i = 0; i < 100; i++) {
                classUnderTest.calculate(functionalPath);
                Log.update();
            }
        } catch (NotCompatibleConfigurationException e) {
            e.printStackTrace();
        }

        classUnderTest.close();
    }
}

