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

package org.wint3794.pathfollower;

import org.wint3794.pathfollower.robot.Robot;
import org.wint3794.pathfollower.debug.ComputerDebugging;
import org.wint3794.pathfollower.debug.Log;
import org.wint3794.pathfollower.drivebase.mecanum.MecanumKinematic;
import org.wint3794.pathfollower.drivebase.tank.TankKinematic;
import org.wint3794.pathfollower.geometry.CurvePoint;
import org.wint3794.pathfollower.geometry.Pose2d;
import org.wint3794.pathfollower.hardware.DcMotorBase;
import org.wint3794.pathfollower.debug.Telemetry;
import org.wint3794.pathfollower.drivebase.ChassisConfiguration;
import org.wint3794.pathfollower.drivebase.ChassisTypes;
import org.wint3794.pathfollower.robot.ExecutionModes;
import org.wint3794.pathfollower.exceptions.NotCompatibleConfigurationException;
import org.wint3794.pathfollower.models.DcMotorVelocities;
import org.wint3794.pathfollower.models.JoystickCoordinates;
import org.wint3794.pathfollower.models.MecanumDirectives;
import org.wint3794.pathfollower.drivebase.Kinematic;
import org.wint3794.pathfollower.robot.RobotMovement;
import org.wint3794.pathfollower.util.Constants;
import org.wint3794.pathfollower.util.MathUtils;

import java.util.List;
import java.util.Optional;

/**
 * The main class of the library.
 */
public class PathFollower {
    private static final String ORIGIN = "Main Thread";
    private final ChassisConfiguration config;
    private Kinematic chassis;
    private final Robot robot;

    /**
     * Creates an instance of the legendary PathFollower.
     *
     * @param config The Chassis Configuration that will be used in API runtime.
     */
    public PathFollower(ChassisConfiguration config, Telemetry telemetry) {
        this.config = config;

        Log.println("The legendary PathFollower is Running!", ORIGIN);
        Log.println("This API was created by Manuel Diaz and Obed Garcia from WinT 3794.", ORIGIN);
        Log.println("Chassis Configuration Type is: " + config.getType(), ORIGIN);
        Log.println("Chassis Configuration is set to: " + config.getMode(), ORIGIN);

        if (telemetry == null) {
            Log.setDebuggingMode(false);
            Log.println("The Telemetry Interface is invalid.", ORIGIN);
        } else {
            Log.setTelemetry(telemetry);
            Log.init();
            Log.setDebuggingMode(true);
            Log.println("Debug Mode is set to: " + telemetry.toString(), ORIGIN);
        }

        if (config.getMode() == ExecutionModes.COMPLEX || config.getMode() == ExecutionModes.ENCODER) {
            robot = new Robot(this);
            this.config.getMotors().forEach(motor -> motor.setInverted(motor.getId() % 2 == 0));
        } else {
            robot = null;
        }

        Log.update();
    }

    /**
     * Stops the motors, closes connections and finishes the execution of this API.
     */
    public void close() {
        this.config.getMotors().forEach(DcMotorBase::stop);
        Log.println("Gracefully stopped!", ORIGIN);
        Log.update();
        Log.setDebuggingMode(false);
        Log.close();
    }

    /**
     * Returns the current chassis configuration, including objects and values.
     *
     * @return The current Chassis Configuration.
     */
    public ChassisConfiguration getChassisConfiguration() {
        return this.config;
    }

    /**
     * Returns a DcMotor object. Useful if you want to manually send instructions or get its data.
     *
     * @param id The DcMotor Identifier
     * @return Optional {@link DcMotorBase}
     */
    public Optional<DcMotorBase> getDcMotor(byte id) {
        return Optional.of(this.config.getMotors().stream()
                .filter(dcMotor -> dcMotor.getId() == id)
                .findFirst().get());
    }

    /**
     * Indicates to DcMotor driver the specific power to get the expected movement.
     * This method is only available for MECANUM chassis.
     *
     * @param directives MecanumDirectives
     */
    public void move(MecanumDirectives directives) {
        if (this.config.getType() == ChassisTypes.MECANUM) {
            setMultiplePowers(MecanumKinematic.move(directives));
        } else {
            Log.println("Your Chassis Type is not compatible with the desired method", ORIGIN);
        }
    }

    /**
     * Indicates to DcMotor driver the specific power to get the expected movement.
     * Also, calculates the arc-tangent to get its length and its angle.
     * This method is only available for MECANUM or SWERVE chassis.
     * Useful when using joysticks.
     *
     * @param coordinates JoystickCoordinate
     */
    public void move(JoystickCoordinates coordinates) {
        switch(this.config.getType()){
            case MECANUM:
                setMultiplePowers(MecanumKinematic.move(coordinates));
                break;
            case SWERVE:
                break;
            default:
                Log.println("Your Chassis Type is not compatible with the desired method.", ORIGIN);
                break;
        }
    }

    private void setMultiplePowers(DcMotorVelocities velocities) {
        List<DcMotorBase> motors = this.config.getMotors();

        for (DcMotorBase dcMotor : motors) {
            dcMotor.setPower(MathUtils.roundPower(
                    velocities.getVelocity((byte) motors.indexOf(dcMotor))
            ));
        }
    }

    /**
     * Returns Robot Object if you are running COMPLEX mode.
     * @return Optional {@link Robot}
     */
    public Optional<Robot> getRobot() {
        if (robot == null) {
            Log.println("Robot is not available if you are not executing COMPLEX or ENCODER mode.", ORIGIN);
            return Optional.empty();
        }
        return Optional.of(robot);
    }

    /**
     * Initialize PathFollower Engine. Method only available for COMPLEX or ENCODER mode.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    public void init() throws NotCompatibleConfigurationException {
        if(this.config.getMode() != ExecutionModes.SIMPLE){
            Log.println("Process initialized!", "PathFollower");

            switch (this.config.getType()) {
                case MECANUM:
                    this.chassis = new MecanumKinematic();
                    break;
                default:
                    this.chassis = new TankKinematic(this.getChassisConfiguration());
                    break;
            }
        } else {
            Log.println("Your execution mode is not compatible with this method.",ORIGIN);
            throw new NotCompatibleConfigurationException("Your execution mode is not compatible.");
        }
    }

    /**
     * Calculates and move your robot. Use in loop. Only for COMPLEX or ENCODER Mode.
     * @param curvePoints The list with all CurvePoints [List {@link CurvePoint}].
     * @param followAngle The preferred turn angle. [-Math.PI - Math.PI]. The angle is in radians.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    public void calculate(List<CurvePoint> curvePoints, double followAngle) throws NotCompatibleConfigurationException {
        if(this.config.getMode() != ExecutionModes.SIMPLE) {
            if (chassis == null) {
                Log.println("Chassis is not initialized. Please initialize chassis before executing this action.",ORIGIN);
                Log.update();
                return;
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.getRobot().orElseThrow().update();

            RobotMovement.followCurve(curvePoints, followAngle);

            ComputerDebugging.sendRobotLocation();
            ComputerDebugging.sendLogPoint(new Pose2d(Robot.getXPos(), Robot.getYPos()));

            this.chassis.apply();

            Log.update();
        }  else
            throw new NotCompatibleConfigurationException();
    }

    /**
     * Calculates and move your robot. Sets the default
     * followAngle established in {@link org.wint3794.pathfollower.util.Constants}. Use in loop.
     * @param curvePoints The list with all CurvePoints [List {@link CurvePoint}].
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    public void calculate(List<CurvePoint> curvePoints) throws NotCompatibleConfigurationException {
        this.calculate(curvePoints, Constants.DEFAULT_FOLLOW_ANGLE);
    }
}
