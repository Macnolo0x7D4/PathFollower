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

package org.wint3794.pathfollower.io;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.wint3794.pathfollower.models.structures.Pose2D;
import org.wint3794.pathfollower.utils.CurvePoint;

import java.util.ArrayList;
import java.util.List;

public class PathProcessor {
    private final JSONArray json;

    public PathProcessor(JSONArray json) {
        this.json = json;
    }

    public Pose2D getPosition(JSONObject object) {
        Long x = (Long) object.get("x");
        Long y = (Long) object.get("y");

        return new Pose2D((double) x, (double) y);
    }

    public JSONObject getInstructionByIndex(int index) {
        return (JSONObject) json.get(index);
    }

    public CurvePoint createCurvePointFromInstruction(JSONObject object) {
        double x = (double) object.get("x");
        double y = (double) object.get("y");

        double moveSpeed = (double) object.get("move_speed");

        double turnSpeed = (double) object.get("turn_speed");
        double followDistance = (double) object.get("follow_distance");
        double slowDownTurnRadians = Math.toRadians((double) object.get("slow_down_turn_radians"));
        double slowDownTurnAmount = (double) object.get("slow_down_turn_amount");

        return new CurvePoint(x, y, moveSpeed, turnSpeed, followDistance, slowDownTurnRadians, slowDownTurnAmount);
    }

    public List<CurvePoint> createFunctionalPath() {
        List<CurvePoint> list = new ArrayList<>();

        // json.forEach( object -> list.add(createCurvePointFromInstruction((JSONObject) object)));
        for (Object object : json) {
            list.add(createCurvePointFromInstruction((JSONObject) object));
        }

        return list;
    }
}
