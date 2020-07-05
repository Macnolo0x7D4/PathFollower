package org.wint3794.pathfollower

import org.junit.Test
import org.wint3794.pathfollower.adapters.DcMotorAdapter
import org.wint3794.pathfollower.controllers.Follower
import org.wint3794.pathfollower.debug.Log
import org.wint3794.pathfollower.debug.telemetries.ConsolePrinter
import org.wint3794.pathfollower.drivebase.ChassisConfiguration
import org.wint3794.pathfollower.drivebase.ChassisTypes
import org.wint3794.pathfollower.drivebase.mecanum.MecanumChassis
import org.wint3794.pathfollower.drivebase.tank.ClassicTankChassis
import org.wint3794.pathfollower.drivebase.tank.PovTankChassis
import org.wint3794.pathfollower.hardware.DcMotorBase
import org.wint3794.pathfollower.io.PathProcessor
import org.wint3794.pathfollower.io.PathReader
import org.wint3794.pathfollower.models.EncoderProperties
import org.wint3794.pathfollower.models.chassis.MecanumDirectives
import org.wint3794.pathfollower.models.chassis.TankDirectives
import org.wint3794.pathfollower.util.Constants
import java.util.function.Consumer
import kotlin.math.PI

class PathFollowerTest {

    @Test
    fun testPathFollower() {
        val classUnderTest = Follower(getDefaultConfiguration(), ConsolePrinter())
        val reader = PathReader(this::class.java.getResource("/path2.json"))
        val processor = reader.rawPath?.let { PathProcessor(it) }

        classUnderTest.init(processor!!.createFunctionalPath())

        var terminated = false

        while(!terminated) {
            terminated = classUnderTest.calculate()
        }

        classUnderTest.close()
    }

    fun testTankChassis() {
        val tank = PovTankChassis()

        val velocities = tank.move(TankDirectives(1.0, -0.2))

        getDefaultConfiguration().motors.listIterator().forEach { motor: DcMotorBase? -> motor?.apply(velocities) }
    }

    private fun getDefaultConfiguration(): ChassisConfiguration {
        val dcMotors: MutableList<DcMotorBase> = mutableListOf(
            DcMotorAdapter(0.toByte()),
            DcMotorAdapter(1.toByte()),
            DcMotorAdapter(2.toByte()),
            DcMotorAdapter(3.toByte())
        )

        return ChassisConfiguration(dcMotors, getDefaultEncoderProperties(), ChassisTypes.DRIVE_TRAIN)
    }

    private fun getDefaultEncoderProperties(): EncoderProperties {
        return EncoderProperties(16.16, 28.0, 20.0, 2.952755906)
    }
}