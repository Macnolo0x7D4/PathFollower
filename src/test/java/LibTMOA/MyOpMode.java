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
import LibTMOA.io.PathReader;
import LibTMOA.models.config.OpMode;
import LibTMOA.utils.CurvePoint;

import java.util.List;

import static LibTMOA.movement.road.RobotMovement.followCurve;

public class MyOpMode extends OpMode {

    List<CurvePoint> functionalPath;

    @Override
    public void init() {
        PathReader reader = new PathReader("/Users/manueldiaz/LibTMOA/src/test/java/LibTMOA/file.json");

        PathProcessor processor = new PathProcessor(reader.getRawPath());

        functionalPath = processor.createFunctionalPath();
    }

    @Override
    public void loop() {
        followCurve(functionalPath, Math.toRadians(90));
    }
}
