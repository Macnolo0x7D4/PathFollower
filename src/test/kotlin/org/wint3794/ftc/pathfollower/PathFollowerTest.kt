package org.wint3794.ftc.pathfollower

import org.junit.Test
import org.wint3794.ftc.pathfollower.adapters.DcMotorAdapter
import org.wint3794.ftc.pathfollower.controllers.Follower
import org.wint3794.ftc.pathfollower.debug.DebugConfiguration
import org.wint3794.ftc.pathfollower.debug.telemetries.ConsolePrinter
import org.wint3794.ftc.pathfollower.debug.telemetries.UdpServer
import org.wint3794.ftc.pathfollower.drivebase.ChassisConfiguration
import org.wint3794.ftc.pathfollower.drivebase.ChassisTypes
import org.wint3794.ftc.pathfollower.drivebase.tank.PovTankChassis
import org.wint3794.ftc.pathfollower.hardware.DcMotorBase
import org.wint3794.ftc.pathfollower.io.PathProcessor
import org.wint3794.ftc.pathfollower.io.PathReader
import org.wint3794.ftc.pathfollower.models.EncoderProperties
import org.wint3794.ftc.pathfollower.models.chassis.TankDirectives

/**
 * A testing class for PathFollower Simulator.
 */
class PathFollowerTest {

    @Test
    fun testPathFollower() {
        val path = PathReader.readJSON(this::class.java.getResource("/path2.json"))

        val classUnderTest = Follower(
            getChassisConfiguration(),
            DebugConfiguration(
                debug = true,
                telemetry = UdpServer(15645)
            ),
            path!!
        )

        var terminated = false

        while(!terminated) {
            terminated = classUnderTest.calculate()
        }

        classUnderTest.close()
    }

    private fun getChassisConfiguration(): ChassisConfiguration {
        val dcMotors: MutableList<DcMotorBase> = mutableListOf(
            DcMotorAdapter(0.toByte()),
            DcMotorAdapter(1.toByte()),
            DcMotorAdapter(2.toByte()),
            DcMotorAdapter(3.toByte())
        )

        return ChassisConfiguration(
            ChassisTypes.TANK,
            dcMotors,
            getEncoderProperties()
        )
    }

    private fun getEncoderProperties(): EncoderProperties {
        return EncoderProperties(16.16, 28.0, 20.0, 2.952755906)
    }
}