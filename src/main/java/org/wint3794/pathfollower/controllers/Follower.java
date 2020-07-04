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

package org.wint3794.pathfollower.controllers;

import org.wint3794.pathfollower.debug.ComputerDebugging;
import org.wint3794.pathfollower.debug.RobotLogger;
import org.wint3794.pathfollower.debug.Log;
import org.wint3794.pathfollower.drivebase.mecanum.MecanumKinematic;
import org.wint3794.pathfollower.drivebase.RobotMovement;
import org.wint3794.pathfollower.drivebase.tank.TankKinematic;
import org.wint3794.pathfollower.geometry.CurvePoint;
import org.wint3794.pathfollower.geometry.Point;
import org.wint3794.pathfollower.geometry.Pose2d;
import org.wint3794.pathfollower.hardware.DcMotorBase;
import org.wint3794.pathfollower.debug.Telemetry;
import org.wint3794.pathfollower.drivebase.ChassisConfiguration;
import org.wint3794.pathfollower.util.ExecutionModes;
import org.wint3794.pathfollower.exceptions.NotCompatibleConfigurationException;
import org.wint3794.pathfollower.drivebase.Kinematic;
import org.wint3794.pathfollower.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The main class of the library.
 */
public class Follower {
    private static final String ORIGIN = "Main Thread";
    private final ChassisConfiguration config;
    private static List<CurvePoint> curvePoints = new ArrayList<>();
    private Kinematic chassis;
    private final Robot robot;

    /**
     * Creates an instance of the legendary PathFollower.
     *
     * @param config The Chassis Configuration that will be used in API runtime.
     */
    public Follower(ChassisConfiguration config, Telemetry telemetry, String ip, int port) {
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

            if(port != 0) {
                if (ip.equals("")){
                    new ComputerDebugging(port);
                } else {
                    new ComputerDebugging(ip, port);
                }

                Log.println("Graphical Debugger is listening: " + (ip.equals("") ? port : (ip + port)), ORIGIN);
            }
        }

        if (config.getMode() == ExecutionModes.COMPLEX || config.getMode() == ExecutionModes.ENCODER) {
            robot = new Robot(this);
            this.config.getMotors().forEach(motor -> motor.setInverted(motor.getId() % 2 == 0));
        } else {
            robot = null;
        }

        Log.update();
    }

    public Follower(ChassisConfiguration config, Telemetry telemetry) {
        this(config, telemetry, "", Constants.DEFAULT_CLIENT_PORT );
    }

    public Follower(ChassisConfiguration config, Telemetry telemetry, int port) {
        this(config, telemetry, "", port );
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
     * Initialize Follower Engine. Method only available for COMPLEX or ENCODER mode.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    public void init(List<CurvePoint> curvePoints) throws NotCompatibleConfigurationException {
        Follower.curvePoints = curvePoints;

        if(this.config.getMode() != ExecutionModes.SIMPLE){
            Log.println("Process initialized!", "Follower");

            switch (this.config.getType()) {
                case MECANUM:
                    this.chassis = new MecanumKinematic();
                    break;
                default:
                    this.chassis = new TankKinematic(this.getChassisConfiguration());
                    break;
            }

            Robot.worldXPosition = curvePoints.get(0).x;
            Robot.worldYPosition = curvePoints.get(0).y;
            Robot.worldAngle = curvePoints.get(0).slowDownTurnRadians;

            ComputerDebugging.clearLogPoints();

        } else {
            Log.println("Your execution mode is not compatible with this method.",ORIGIN);
            throw new NotCompatibleConfigurationException("Your execution mode is not compatible.");
        }
    }

    /**
     * Calculates and move your robot. Use in loop. Only for COMPLEX or ENCODER Mode.
     * @param followAngle The preferred turn angle. [-Math.PI - Math.PI]. The angle is in radians.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    public void calculate(double followAngle) throws NotCompatibleConfigurationException {
        if(this.config.getMode() != ExecutionModes.SIMPLE) {
            if (chassis == null || curvePoints.isEmpty()) {
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

            RobotLogger.sendRobotLocation();
            ComputerDebugging.sendLogPoint(new Point(Robot.worldXPosition,Robot.worldYPosition));
            ComputerDebugging.markEndOfUpdate();

            this.chassis.apply();

            robot.update();
            Log.update();
        }  else
            throw new NotCompatibleConfigurationException();
    }

    /**
     * Calculates and move your robot. Sets the default
     * followAngle established in {@link org.wint3794.pathfollower.util.Constants}. Use in loop.
     * @throws NotCompatibleConfigurationException Only if your configuration is invalid for this method.
     */
    public void calculate() throws NotCompatibleConfigurationException {
        this.calculate(Constants.DEFAULT_FOLLOW_ANGLE);
    }
}
