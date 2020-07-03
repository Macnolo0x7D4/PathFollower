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
import org.wint3794.pathfollower.controllers.Follower;
import org.wint3794.pathfollower.debug.ComputerDebugging;
import org.wint3794.pathfollower.debug.Log;
import org.wint3794.pathfollower.adapters.DcMotorAdapter;
import org.wint3794.pathfollower.debug.telemetries.ConsolePrinter;
import org.wint3794.pathfollower.drivebase.ChassisConfiguration;
import org.wint3794.pathfollower.drivebase.ChassisTypes;
import org.wint3794.pathfollower.exceptions.NotCompatibleConfigurationException;
import org.wint3794.pathfollower.models.EncoderProperties;
import org.wint3794.pathfollower.geometry.CurvePoint;
import java.util.ArrayList;
import java.util.List;

public class FollowerTest {

    List<CurvePoint> functionalPath;

    public static ChassisConfiguration getTestingConfiguration() {
        return new ChassisConfiguration(
                List.of(
                        new DcMotorAdapter((byte) 0),
                        new DcMotorAdapter((byte) 1),
                        new DcMotorAdapter((byte) 2),
                        new DcMotorAdapter((byte) 3)
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
        //UDPServer server = null;

        /*try {
            server = new UDPServer(11115, InetAddress.getByName("192.168.0.9"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

         */

        ComputerDebugging computerDebugging = new ComputerDebugging();
        ComputerDebugging.clearLogPoints();

        Follower classUnderTest = new Follower(getTestingConfiguration(), new ConsolePrinter());

        functionalPath = new ArrayList<>();

        functionalPath.add(new CurvePoint(0, 0, 1, 1, 30, Math.PI / 4, 1.0));
        functionalPath.add(new CurvePoint(180, 180, 1, 1, 30, Math.PI / 4, 1.0));
        functionalPath.add(new CurvePoint(250, 180, 1.0, 1.0, 30, Math.PI / 4, 1.0));
        functionalPath.add(new CurvePoint(280, 50, 1.0, 1.0, 30, Math.PI / 4, 1.0));
        functionalPath.add(new CurvePoint(180, 0, 1.0, 1.0, 30, Math.PI / 4, 1.0));

        try {
            classUnderTest.init(functionalPath);

            while (true) {
            //for (int i = 0; i < 20; i++) {
                classUnderTest.calculate();
                Log.update();
            }
        } catch (NotCompatibleConfigurationException e) {
            e.printStackTrace();
        }

        classUnderTest.close();
    }
}

