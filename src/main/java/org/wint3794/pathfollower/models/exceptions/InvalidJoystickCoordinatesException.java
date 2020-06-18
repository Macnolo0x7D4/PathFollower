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

package org.wint3794.pathfollower.models.exceptions;

/**
 * Exception for Invalid Joystick Coordinates
 */
public class InvalidJoystickCoordinatesException extends Exception {

    /**
     * Creates an InvalidJoystickCoordinateException.
     */
    public InvalidJoystickCoordinatesException() {
        super();
    }

    /**
     * Creates an InvalidJoystickCoordinateException.
     *
     * @param message Message to print in Exception Stack Trace
     */
    public InvalidJoystickCoordinatesException(String message) {
        super(message);
    }
}
