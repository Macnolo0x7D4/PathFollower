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

package LibTMOA.models.structures;

/**
 * A Model for coordinates
 */
public class JoystickCoordinates {
    private final double y;
    private final double x;

    /**
     * Creates JoystickCoordinates from [y, x]
     *
     * @param y Ordinate [double]
     * @param x Abscissa [double]
     */
    public JoystickCoordinates(double y, double x) {
        this.y = y;
        this.x = x;
    }

    /**
     * Returns Ordinate value
     *
     * @return Ordinate [double]
     */
    public double getY() {
        return y;
    }

    /**
     * Returns Abscissa value
     *
     * @return Abscissa [double]
     */
    public double getX() {
        return x;
    }

    /**
     * Returns JoystickCoordinates as an array of doubles
     *
     * @return Pose2D [double[]]
     */
    @Deprecated
    public double[] getPosition() {
        double[] pos = new double[2];

        pos[0] = y;
        pos[1] = x;

        return pos;
    }

    /**
     * Return JoystickCoordinates as a string
     *
     * @return JoystickCoordinates [string]
     */
    @Override
    public String toString() {
        return "JoystickCoordinate{" +
                "y=" + y +
                ", x=" + x +
                '}';
    }
}
