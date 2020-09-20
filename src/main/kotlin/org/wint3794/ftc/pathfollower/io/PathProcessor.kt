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
package org.wint3794.ftc.pathfollower.io

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.wint3794.ftc.pathfollower.geometry.CurvePoint
import org.wint3794.ftc.pathfollower.models.Path
import java.util.*

object PathProcessor {
    private fun createCurvePoint(`object`: JSONObject): CurvePoint {
        return CurvePoint(
            `object`["x"] as Double,
            `object`["y"] as Double,
            `object`["move_speed"] as Double,
            `object`["turn_speed"] as Double,
            `object`["follow_distance"] as Double,
            Math.toRadians(`object`["slow_down_turn_radians"] as Double),
            `object`["slow_down_turn_amount"] as Double,
            `object`["point_length"] as Double
        )
    }

    /**
     * Returns a functional Path as List of CurvePoints.
     * @return Functional Path
     */
    fun create(json: JSONArray?): Path {
        val list: MutableList<CurvePoint> = ArrayList()

        if (json != null) {
            for (`object` in json) {
                list.add(createCurvePoint(`object` as JSONObject))
            }
        }

        return Path(list)
    }
}