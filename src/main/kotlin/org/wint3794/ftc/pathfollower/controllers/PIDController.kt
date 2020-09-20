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
package org.wint3794.ftc.pathfollower.controllers

import org.wint3794.ftc.pathfollower.hardware.DcMotorBase
import org.wint3794.ftc.pathfollower.models.PIDFCoefficients
import org.wint3794.ftc.pathfollower.models.PIDValues

/**
 * A PID controller Class. This class was inspired by Arcrobotics PID Controller.
 */
class PIDController(coefficients: PIDFCoefficients) {
    private val kP: Double
    private val kI: Double
    private val kD: Double
    private val kF: Double

    /**
     * @return the period of time between the interval
     */
    val period: Double
    /**
     * Returns the current setpoint of the PIDFController.
     *
     * @return The current setpoint.
     */
    /**
     * Sets the setpoint for the PIDFController
     *
     * @param sp The desired setpoint.
     */
    var setPoint: Double
    private var measuredValue: Double

    /**
     * @return the positional error e(t)
     */
    var positionError: Double
        private set

    /**
     * @return the velocity error e'(t)
     */
    var velocityError = 0.0
        private set
    private var totalError = 0.0
    private var prevErrorVal = 0.0
    private var errorTolerance_p = 0.05
    private var errorTolerance_v = Double.POSITIVE_INFINITY
    fun reset() {
        totalError = 0.0
        prevErrorVal = 0.0
    }
    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor  DcMotorBase
     * @param values PIDValues
     * @param speed  The maximum speed the motor should rotate.
     */
    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor  DcMotorBase
     * @param values PIDValues
     */
    @JvmOverloads
    fun control(motor: DcMotorBase, values: PIDValues, speed: Double = 1.0) {
        val sp = values.sp
        val pv = values.pv
        if (Math.abs(sp) > Math.abs(pv)) motor.power = speed * calculate(pv, sp) else motor.stop()
    }
    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor The affected DcMotorBase.
     * @param pv    The setpoint of the calculation
     */
    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor DcMotorBase
     */
    @JvmOverloads
    fun control(motor: DcMotorBase, pv: Double = prevErrorVal) {
        val values = PIDValues(setPoint, pv)
        control(motor, values)
    }

    /**
     * Sets the error which is considered tolerable for use with [.atSetPoint].
     *
     * @param positionTolerance Pose2d error which is tolerable.
     * @param velocityTolerance Velocity error which is tolerable.
     */
    fun setTolerance(positionTolerance: Double, velocityTolerance: Double) {
        errorTolerance_p = positionTolerance
        errorTolerance_v = velocityTolerance
    }

    /**
     * Returns true if the error is within the percentage of the total input range, determined by
     * [.setTolerance].
     *
     * @return Whether the error is within the acceptable bounds.
     */
    fun atSetPoint(): Boolean {
        return (Math.abs(positionError) < errorTolerance_p
                && Math.abs(velocityError) < errorTolerance_v)
    }

    /**
     * @return the PIDF coefficients
     */
    val coefficients: DoubleArray
        get() = doubleArrayOf(kP, kI, kD, kF)

    /**
     * @return the tolerances of the controller
     */
    val tolerance: DoubleArray
        get() = doubleArrayOf(errorTolerance_p, errorTolerance_v)

    /**
     * Sets the error which is considered tolerable for use with [.atSetPoint].
     *
     * @param positionTolerance Pose2d error which is tolerable.
     */
    fun setTolerance(positionTolerance: Double) {
        setTolerance(positionTolerance, Double.POSITIVE_INFINITY)
    }

    /**
     * Calculates the next output of the PIDF controller.
     *
     * @param pv The given measured value.
     * @param sp The given setpoint.
     * @return the next output using the given measurd value via
     * [.calculate].
     */
    fun calculate(pv: Double, sp: Double): Double {
        // set the setpoint to the provided value
        setPoint = sp
        return calculate(pv)
    }
    /**
     * Calculates the control value, u(t).
     *
     * @param pv The current measurement of the process variable.
     * @return the value produced by u(t).
     */
    /**
     * Calculates the next output of the PIDF controller.
     *
     * @return the next output using the current measured value via
     * [.calculate].
     */
    @JvmOverloads
    fun calculate(pv: Double = measuredValue): Double {
        prevErrorVal = positionError
        if (measuredValue == pv) {
            positionError = setPoint - measuredValue
        } else {
            positionError = setPoint - pv
            measuredValue = pv
        }
        if (period != 0.0) {
            velocityError = (positionError - prevErrorVal) / period
        } else {
            velocityError = 0.0
        }
        totalError = period * (setPoint - measuredValue)
        return kP * positionError + kI * totalError + kD * velocityError + kF * setPoint
    }

    /**
     * Creates an instance of PID Controller
     *
     * @param coefficients PIDFCoefficients
     */
    init {
        kP = coefficients.kP
        kI = coefficients.kI
        kD = coefficients.kD
        kF = coefficients.kF
        val values = coefficients.values
        setPoint = values.sp
        measuredValue = values.pv
        period = values.period
        positionError = setPoint - measuredValue
        reset()
    }
}