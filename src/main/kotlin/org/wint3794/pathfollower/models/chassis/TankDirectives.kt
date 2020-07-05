package org.wint3794.pathfollower.models.chassis

class TankDirectives (var inputA: Double, var inputB: Double) {
    fun directives(): DoubleArray {
        return doubleArrayOf(inputA, inputB)
    }
}