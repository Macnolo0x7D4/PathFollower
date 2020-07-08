package org.wint3794.pathfollower.geometry

open class Point @JvmOverloads constructor(x: Double = 0.0, y: Double = 0.0) {
    var x = 0.0
    var y = 0.0

    constructor(vals: DoubleArray?) : this() {
        set(vals)
    }

    fun set(vals: DoubleArray?) {
        if (vals != null) {
            x = if (vals.isNotEmpty()) vals[0] else 0.0
            y = if (vals.size > 1) vals[1] else 0.0
        } else {
            x = 0.0
            y = 0.0
        }
    }

    fun clone(): Point {
        return Point(x, y)
    }

    fun dot(p: Point): Double {
        return x * p.x + y * p.y
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

    init {
        this.x = x
        this.y = y
    }
}