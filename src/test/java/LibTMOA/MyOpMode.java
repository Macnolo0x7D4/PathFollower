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

import LibTMOA.models.structures.CurvePoint;
import LibTMOA.models.config.OpMode;

import java.util.ArrayList;

import static LibTMOA.movement.road.RobotMovement.followCurve;

public class MyOpMode extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        ArrayList<CurvePoint> allPoints = new ArrayList<>();
        allPoints.add(new CurvePoint(0,0,0.5,0.5,50,Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(180,90,0.5,0.5,50,Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(360,180,0.5,0.5,50,Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(280,50,0.5,0.5,50,Math.toRadians(50),1.0));
        allPoints.add(new CurvePoint(180,0,0.5,0.5,50,Math.toRadians(50),1.0));

        followCurve(allPoints, Math.toRadians(90));

    }
}
