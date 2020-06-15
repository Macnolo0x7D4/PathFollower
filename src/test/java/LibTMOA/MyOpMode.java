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

import LibTMOA.io.PathProcessor;
import LibTMOA.io.PathSimulator;
import LibTMOA.models.config.OpMode;
import LibTMOA.utils.CurvePoint;

import java.util.List;

import static LibTMOA.movement.road.RobotMovement.followCurve;

public class MyOpMode extends OpMode {

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


    //"[{'x': 0.0,'y': 0.0,'move_speed': 0.5,'turn_speed': 0.5,'follow_distance': 50.0,'slow_down_turn_radians': 50.0,'slow_down_turn_amount': 1.0}]";

    List<CurvePoint> functionalPath;

    @Override
    public void init() {
        // PathReader reader = new PathReader("./file.json");
        PathSimulator reader = new PathSimulator(ROUTES);

        PathProcessor processor = new PathProcessor(reader.getRawPath());

        functionalPath = processor.createFunctionalPath();
    }

    @Override
    public void loop() {
        followCurve(functionalPath, Math.toRadians(90));
    }
}
