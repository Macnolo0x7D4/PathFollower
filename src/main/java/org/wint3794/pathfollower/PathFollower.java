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

package org.wint3794.pathfollower;

import org.wint3794.pathfollower.controllers.Robot;
import org.wint3794.pathfollower.debug.ComputerDebugging;
import org.wint3794.pathfollower.debug.Log;
import org.wint3794.pathfollower.io.Telemetry;
import org.wint3794.pathfollower.models.config.*;
import org.wint3794.pathfollower.models.structures.DcMotorVelocities;
import org.wint3794.pathfollower.models.structures.JoystickCoordinates;
import org.wint3794.pathfollower.models.structures.MecanumDirectives;
import org.wint3794.pathfollower.models.structures.Pose2D;
import org.wint3794.pathfollower.movement.Movement;
import org.wint3794.pathfollower.movement.road.RobotMovement;
import org.wint3794.pathfollower.movement.standard.DriveTrainMovement;
import org.wint3794.pathfollower.movement.standard.MecanumMovement;
import org.wint3794.pathfollower.utils.CurvePoint;
import org.wint3794.pathfollower.utils.MathUtils;

import java.util.List;
import java.util.Optional;

/**
 * The main class of the library.
 */
public class PathFollower {
    private static final String ORIGIN = "Main Thread";
    private static boolean runningPathFollower = false;
    private final ChassisConfiguration config;
    private final Robot robot;

    /**
     * Creates an instance of the legendary PathFollower.
     *
     * @param config ChassisConfiguration
     */
    public PathFollower(ChassisConfiguration config, Telemetry telemetry) {
        this.config = config;

        Log.println("The legendary PathFollower is Running!", ORIGIN);
        Log.println("This API was created by Manuel Diaz and Obed Garcia from WinT 3794.", ORIGIN);
        Log.println("Chassis Configuration Type is: " + config.getType(), ORIGIN);
        Log.println("Chassis Configuration is set to: " + config.getMode(), ORIGIN);

        if (telemetry == null) {
            Log.setDebuggingMode(false);
            Log.println("The Telemetry Interface is invalid.");
        } else {
            Log.setTelemetry(telemetry);
            Log.initializer();
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

    public void close() {
        runningPathFollower = false;
        this.config.getMotors().forEach(DcMotorBase::stop);
        Log.println("Gracefully stopped!", ORIGIN);
        Log.update();
        Log.setDebuggingMode(false);
        Log.close();
    }

    /**
     * Returns the current chassis configuration, including objects and values.
     *
     * @return ChassisConfiguration
     */
    public ChassisConfiguration getChassisConfiguration() {
        return this.config;
    }

    /**
     * Returns a DcMotor object. Useful if you want to manually send instructions or get its data.
     *
     * @param id The DcMotor Identifier
     * @return DcMotor Object
     */
    public DcMotorBase getDcMotor(byte id) {
        return this.config.getMotors().stream()
                .filter(dcMotor -> dcMotor.getId() == id)
                .findFirst().get();
    }

    /**
     * Indicates to DcMotor driver the specific power to get the expected movement.
     *
     * @param directives MecanumDirectives
     */
    public void move(MecanumDirectives directives) {
        if (this.config.getType() == ChassisTypes.MECANUM) {
            setMultiplePowers(MecanumMovement.move(directives));
        } else {
            Log.println("Your Chassis Type is not compatible with the desired method");
        }
    }

    /**
     * Indicates to DcMotor driver the specific power to get the expected movement.
     * Also, calculates the arc-tangent to get its length and its angle. Useful when using joysticks.
     *
     * @param coordinates JoystickCoordinate
     */
    public void move(JoystickCoordinates coordinates) {
        if (this.config.getType() == ChassisTypes.MECANUM) {
            setMultiplePowers(MecanumMovement.move(coordinates));
        } else {
            Log.println("Your Chassis Type is not compatible with the desired method");
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

    public Optional<Robot> getRobot() {
        if (robot == null) {
            Log.println("Robot is not available if you are not executing COMPLEX mode.");
            return Optional.empty();
        }
        return Optional.of(robot);
    }

    public void startPathFollower(List<CurvePoint> curvePoints, double followAngle){
        runningPathFollower = true;
        Log.println("Process initialized!", "PathFollower");

        Movement chassis;

        switch(this.config.getType()){
            default:
                chassis = new DriveTrainMovement(this.getChassisConfiguration());
        }

        while(PathFollower.isPathFollowerRunning()){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.getRobot().orElseThrow().update();

            RobotMovement.followCurve(curvePoints, followAngle);

            ComputerDebugging.sendRobotLocation();
            ComputerDebugging.sendLogPoint(new Pose2D(Robot.getXPos(), Robot.getYPos()));

            chassis.apply();

            Log.update();
        }
    }

    private static synchronized boolean isPathFollowerRunning(){
        return runningPathFollower;
    }
}
