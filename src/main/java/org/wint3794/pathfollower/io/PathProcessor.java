/*
 * Copyright 2020 WinT 3794 (Manuel Diaz Rojo and Alexis Obed Garcia Hernandez)
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

package org.wint3794.pathfollower.io;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.wint3794.pathfollower.models.structures.Pose2D;
import org.wint3794.pathfollower.geometry.CurvePoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathProcessor {
    private final JSONArray json;

    /**
     * Creates path from JSON Array. Preferably use
     * PathReader or PathSimulator before.
     * @param json A JSON Array that includes {@link org.wint3794.pathfollower.geometry.CurvePoint} List.
     */
    public PathProcessor(JSONArray json) {
        this.json = json;
    }

    /**
     * Returns CurvePoint from Path (JSON Array) if exists.
     * @param index Point Index
     * @return The desired CurvePoint
     */
    public Optional<CurvePoint> getPointByIndex(int index) {
        return Optional.of(createCurvePoint((JSONObject) json.get(index)));
    }

    private CurvePoint createCurvePoint(JSONObject object) {
        double x = (double) object.get("x");
        double y = (double) object.get("y");

        double moveSpeed = (double) object.get("move_speed");

        double turnSpeed = (double) object.get("turn_speed");
        double followDistance = (double) object.get("follow_distance");
        double pointLength = (double) object.get("point_length");
        double slowDownTurnRadians = Math.toRadians((double) object.get("slow_down_turn_radians"));
        double slowDownTurnAmount = (double) object.get("slow_down_turn_amount");

        return new CurvePoint(x, y, moveSpeed, turnSpeed, followDistance, pointLength, slowDownTurnRadians, slowDownTurnAmount);
    }

    /**
     * Returns a functional Path as List of CurvePoints.
     * @return Functional Path
     */
    public List<CurvePoint> createFunctionalPath() {
        List<CurvePoint> list = new ArrayList<>();

        for (Object object : json) {
            list.add(createCurvePoint((JSONObject) object));
        }

        return list;
    }
}
