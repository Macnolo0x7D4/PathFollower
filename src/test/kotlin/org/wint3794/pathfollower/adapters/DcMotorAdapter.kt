package org.wint3794.pathfollower.adapters

import org.wint3794.pathfollower.debug.Log
import org.wint3794.pathfollower.hardware.DcMotorBase
import org.wint3794.pathfollower.hardware.EncoderBase
import org.wint3794.pathfollower.models.DcMotorVelocities

class DcMotorAdapter(override val id: Byte) : DcMotorBase {

    override val isMaster: Boolean
        get() = TODO("Not yet implemented")

    override var power: Double = 0.0
        set(value) {
            field = if (inverted) -value else value
            println("[Motor $id]: Current power -> $field")
        }

    override var inverted: Boolean = false

    override fun stop() {
        println("[Motor $id]: Stopped!")
    }

    override val encoder: EncoderBase
        get() = TODO("Not yet implemented")

    override fun apply(velocities: DcMotorVelocities) {
        power = velocities.getVelocity(id)
    }
}