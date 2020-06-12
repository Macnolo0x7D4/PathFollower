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

package LibTMOA.utils;

public class CurvePoint {
    public double x;
    public double y;
    public double moveSpeed;
    public double turnSpeed;
    public double followDistance;
    public double slowDownTurnRadians;
    public double slowDownTurnAmount;
    public double pointLength;


    public CurvePoint(double x, double y, double moveSpeed, double turnSpeed,
                      double followDistance, double slowDownTurnRadians, double slowDownTurnAmount){
        this.x = x;
        this.y = y;
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
        this.followDistance = followDistance;
        this.pointLength = followDistance;
        this.slowDownTurnRadians = slowDownTurnRadians;
        this.slowDownTurnAmount = slowDownTurnAmount;

        System.out.println(this.toString());
    }


    public CurvePoint(double x, double y, double moveSpeed, double turnSpeed,
                      double followDistance,double pointLength, double slowDownTurnRadians, double slowDownTurnAmount){
        this.x = x;
        this.y = y;
        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;
        this.followDistance = followDistance;
        this.pointLength = pointLength;
        this.slowDownTurnRadians = slowDownTurnRadians;
        this.slowDownTurnAmount = slowDownTurnAmount;
    }

    public CurvePoint(CurvePoint nextPoint) {
        x = nextPoint.x;
        y = nextPoint.y;
        moveSpeed = nextPoint.moveSpeed;
        turnSpeed = nextPoint.turnSpeed;
        followDistance = nextPoint.followDistance;
        slowDownTurnRadians = nextPoint.slowDownTurnRadians;
        slowDownTurnAmount = nextPoint.slowDownTurnAmount;
        pointLength = nextPoint.pointLength;

    }

    public Point toPoint(){
        return new Point(x,y);
    }
    public void setPoint(Point p){
        x = p.x;
        y = p.y;
    }

    @Override
    public String toString() {
        return "CurvePoint{" +
                "x=" + x +
                ", y=" + y +
                ", moveSpeed=" + moveSpeed +
                ", turnSpeed=" + turnSpeed +
                ", followDistance=" + followDistance +
                ", slowDownTurnRadians=" + slowDownTurnRadians +
                ", slowDownTurnAmount=" + slowDownTurnAmount +
                ", pointLength=" + pointLength +
                '}';
    }
}