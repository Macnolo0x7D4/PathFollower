/*
 * Copyright 2020 WinT 3794 (Manuel Díaz Rojo and Alexis Obed García Hernández)
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

package LibTMOA.controllers;

import LibTMOA.models.config.DcMotorBase;
import LibTMOA.models.structures.PIDFCoefficients;
import LibTMOA.models.structures.PIDValues;

/**
 * A PID controller Class. This class was inspired by Arcrobotics PID Controller.
 */
public class PIDController {

    private final double kP, kI, kD, kF;
    private final double period;
    private double setPoint;
    private double measuredValue;
    private double errorVal_p;
    private double errorVal_v;
    private double totalError;
    private double prevErrorVal;
    private double errorTolerance_p = 0.05;
    private double errorTolerance_v = Double.POSITIVE_INFINITY;

    /**
     * Creates an instance of PID Controller
     *
     * @param coefficients PIDFCoefficients
     */
    public PIDController(PIDFCoefficients coefficients) {
        kP = coefficients.getkP();
        kI = coefficients.getkI();
        kD = coefficients.getkD();
        kF = coefficients.getkF();

        final PIDValues values = coefficients.getPIDValues();

        setPoint = values.getSp();
        measuredValue = values.getPv();
        period = values.getPeriod();

        errorVal_p = setPoint - measuredValue;
        reset();
    }

    public void reset() {
        totalError = 0;
        prevErrorVal = 0;
    }


    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor  DcMotorBase
     * @param values PIDValues
     * @param speed  The maximum speed the motor should rotate.
     */
    public void control(DcMotorBase motor, PIDValues values, double speed) {
        final double sp = values.getSp();
        final double pv = values.getPv();

        if (Math.abs(sp) > Math.abs(pv)) motor.setPower(speed * calculate(pv, sp));
        else motor.stop();
    }

    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor  DcMotorBase
     * @param values PIDValues
     */
    public void control(DcMotorBase motor, PIDValues values) {
        control(motor, values, 1);
    }

    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor The affected DcMotorBase.
     * @param pv    The setpoint of the calculation
     */
    public void control(DcMotorBase motor, double pv) {
        final PIDValues values = new PIDValues(this.setPoint, pv);
        control(motor, values);
    }

    /**
     * Implements calculation onto the DcMotorBase.
     *
     * @param motor DcMotorBase
     */
    public void control(DcMotorBase motor) {
        control(motor, this.prevErrorVal);
    }

    /**
     * Sets the error which is considered tolerable for use with {@link #atSetPoint()}.
     *
     * @param positionTolerance Pose2D error which is tolerable.
     * @param velocityTolerance Velocity error which is tolerable.
     */
    public void setTolerance(double positionTolerance, double velocityTolerance) {
        errorTolerance_p = positionTolerance;
        errorTolerance_v = velocityTolerance;
    }

    /**
     * Returns the current setpoint of the PIDFController.
     *
     * @return The current setpoint.
     */
    public double getSetPoint() {
        return setPoint;
    }

    /**
     * Sets the setpoint for the PIDFController
     *
     * @param sp The desired setpoint.
     */
    public void setSetPoint(double sp) {
        setPoint = sp;
    }

    /**
     * Returns true if the error is within the percentage of the total input range, determined by
     * {@link #setTolerance}.
     *
     * @return Whether the error is within the acceptable bounds.
     */
    public boolean atSetPoint() {
        return Math.abs(errorVal_p) < errorTolerance_p
                && Math.abs(errorVal_v) < errorTolerance_v;
    }

    /**
     * @return the period of time between the interval
     */
    public double getPeriod() {
        return period;
    }

    /**
     * @return the PIDF coefficients
     */
    public double[] getCoefficients() {
        return new double[]{kP, kI, kD, kF};
    }

    /**
     * @return the positional error e(t)
     */
    public double getPositionError() {
        return errorVal_p;
    }

    /**
     * @return the tolerances of the controller
     */
    public double[] getTolerance() {
        return new double[]{errorTolerance_p, errorTolerance_v};
    }

    /**
     * Sets the error which is considered tolerable for use with {@link #atSetPoint()}.
     *
     * @param positionTolerance Pose2D error which is tolerable.
     */
    public void setTolerance(double positionTolerance) {
        setTolerance(positionTolerance, Double.POSITIVE_INFINITY);
    }

    /**
     * @return the velocity error e'(t)
     */
    public double getVelocityError() {
        return errorVal_v;
    }

    /**
     * Calculates the next output of the PIDF controller.
     *
     * @return the next output using the current measured value via
     * {@link #calculate(double)}.
     */
    public double calculate() {
        return calculate(measuredValue);
    }

    /**
     * Calculates the next output of the PIDF controller.
     *
     * @param pv The given measured value.
     * @param sp The given setpoint.
     * @return the next output using the given measurd value via
     * {@link #calculate(double)}.
     */
    public double calculate(double pv, double sp) {
        // set the setpoint to the provided value
        setSetPoint(sp);
        return calculate(pv);
    }

    /**
     * Calculates the control value, u(t).
     *
     * @param pv The current measurement of the process variable.
     * @return the value produced by u(t).
     */
    public double calculate(double pv) {
        prevErrorVal = errorVal_p;

        if (measuredValue == pv) {
            errorVal_p = setPoint - measuredValue;
        } else {
            errorVal_p = setPoint - pv;
            measuredValue = pv;
        }

        if (period != 0) {
            errorVal_v = (errorVal_p - prevErrorVal) / period;
        } else {
            errorVal_v = 0;
        }

        totalError = period * (setPoint - measuredValue);

        return kP * errorVal_p + kI * totalError + kD * errorVal_v + kF * setPoint;
    }

}
