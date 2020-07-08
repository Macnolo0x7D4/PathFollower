package org.wint3794.pathfollower.drivebase.tank

import org.wint3794.pathfollower.models.DcMotorVelocities
import org.wint3794.pathfollower.models.chassis.TankDirectives
import kotlin.math.abs

class PovTankChassis: ClassicTankChassis() {
    override fun move(directives: TankDirectives): DcMotorVelocities {
        val drive = directives.inputA
        val turn  = directives.inputB

        var left  = drive + turn;
        var right = drive - turn;

        val max = abs(left).coerceAtLeast(abs(right));

        if (max > 1.0){
            left /= max;
            right /= max;
        }

        return DcMotorVelocities(doubleArrayOf(- left, right, - left, right))
    }
}