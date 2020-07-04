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
package org.wint3794.pathfollower.io

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.wint3794.pathfollower.geometry.CurvePoint
import java.util.*

class PathProcessor(json: JSONArray) {
    private val json: JSONArray

    /**
     * Returns CurvePoint from Path (JSON Array) if exists.
     * @param index Point Index
     * @return The desired CurvePoint
     */
    fun getPointByIndex(index: Int): Optional<CurvePoint> {
        return Optional.of(createCurvePoint(json.get(index) as JSONObject))
    }

    private fun createCurvePoint(`object`: JSONObject): CurvePoint {
        val x = `object`.get("x") as Double
        val y = `object`.get("y") as Double
        val moveSpeed = `object`.get("move_speed") as Double
        val turnSpeed = `object`.get("turn_speed") as Double
        val followDistance = `object`.get("follow_distance") as Double
        val pointLength = `object`.get("point_length") as Double
        val slowDownTurnRadians =
            Math.toRadians(`object`.get("slow_down_turn_radians") as Double)
        val slowDownTurnAmount = `object`.get("slow_down_turn_amount") as Double
        return CurvePoint(
            x,
            y,
            moveSpeed,
            turnSpeed,
            followDistance,
            pointLength,
            slowDownTurnRadians,
            slowDownTurnAmount
        )
    }

    /**
     * Returns a functional Path as List of CurvePoints.
     * @return Functional Path
     */
    fun createFunctionalPath(): List<CurvePoint> {
        val list: MutableList<CurvePoint> = ArrayList()
        for (`object` in json) {
            list.add(createCurvePoint(`object` as JSONObject))
        }
        return list
    }

    /**
     * Creates path from JSON Array. Preferably use
     * PathReader or PathSimulator before.
     * @param json A JSON Array that includes [org.wint3794.pathfollower.geometry.CurvePoint] List.
     */
    init {
        this.json = json
    }
}