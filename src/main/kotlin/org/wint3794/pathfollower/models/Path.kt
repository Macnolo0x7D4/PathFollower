package org.wint3794.pathfollower.models

import org.wint3794.pathfollower.geometry.CurvePoint

data class Path (val points: List<CurvePoint>){
    fun get(id: Int): CurvePoint {
        return points[id]
    }

    fun findFirst(): CurvePoint {
        return get(0)
    }

    fun toList(): List<CurvePoint> {
        return points
    }
}