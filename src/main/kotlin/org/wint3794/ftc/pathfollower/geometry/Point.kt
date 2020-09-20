package org.wint3794.ftc.pathfollower.geometry

/**
 * A simple Point class
 */
data class Point (
    /**
     * The X value
     */
    var x: Double = 0.0,

    /**
     * The Y value
     */
    var y: Double = 0.0
) {

    fun set(values: DoubleArray?) {
        if (values != null) {
            x = if (values.isNotEmpty()) values[0] else 0.0
            y = if (values.size > 1) values[1] else 0.0
        } else {
            x = 0.0
            y = 0.0
        }
    }

    fun clone(): Point {
        return Point(x, y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false
        val it = other
        return x == it.x && y == it.y
    }

    override fun toString(): String {
        return "{$x, $y}"
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}