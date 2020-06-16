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

package LibTMOA;

import LibTMOA.controllers.Robot;
import LibTMOA.debug.ComputerDebugging;
import LibTMOA.debug.Log;
import LibTMOA.models.config.*;
import LibTMOA.models.structures.DcMotorVelocities;
import LibTMOA.models.structures.JoystickCoordinates;
import LibTMOA.models.structures.MecanumDirectives;
import LibTMOA.movement.standard.MecanumMovement;
import LibTMOA.utils.MathUtils;

import java.util.List;
import java.util.Optional;

/**
 * The main class of the library.
 */
public class TMOA {
    private static final String ORIGIN = "Main Thread";
    private final ChassisConfiguration config;
    private final Robot robot;

    /**
     * Creates an instance of the Trigonometric Mecanum Omnidirectional Algorithm.
     *
     * @param config ChassisConfiguration
     */
    public TMOA(ChassisConfiguration config, Telemetry telemetry) {
        this.config = config;

        Log.println("The legendary Trigonometric Mecanum Omnidirectional Algorithm is Running!", ORIGIN);
        Log.println("This API was created by Manuel Diaz and Obed Garcia from WinT 3794.", ORIGIN);
        Log.println("Chassis Configuration Type is: " + config.getType(), ORIGIN);
        Log.println("Chassis Configuration is set to: " + config.getMode(), ORIGIN);

        if(telemetry == null){
            Log.setDebuggingMode(false);
            Log.println("The Telemetry Interface is invalid.");
        } else {
            Log.setTelemetry(telemetry);
            Log.setDebuggingMode(true);
            Log.println("Debug Mode is set to: " + telemetry.toString(), ORIGIN);
        }

        if(config.getMode() == ExecutionModes.COMPLEX || config.getMode() == ExecutionModes.ENCODER) {
            robot = new Robot(this);
            this.config.getMotors().forEach(motor -> motor.setInverted(motor.getId() % 2 == 0));
        } else {
            robot = null;
        }

        Log.update();
    }

    public void close(){
        this.config.getMotors().forEach(DcMotorBase::stop);
        Log.println("Gracefully stopped!", ORIGIN);
        Log.update();
        Log.setDebuggingMode(false);
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
        if(this.config.getType() == ChassisTypes.MECANUM) {
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
        if(this.config.getType() == ChassisTypes.MECANUM) {
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
        if(robot == null){
            Log.println("Robot is not available if you are not executing COMPLEX mode.");
            return Optional.empty();
        }
        return Optional.of(robot);
    }
}
