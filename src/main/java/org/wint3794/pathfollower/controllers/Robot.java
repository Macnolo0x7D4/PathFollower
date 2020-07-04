package org.wint3794.pathfollower.controllers;

import org.wint3794.pathfollower.util.Range;
import org.wint3794.pathfollower.util.MovementVars;
import org.wint3794.pathfollower.util.SpeedOmeter;

public class Robot {
    public static boolean usingComputer = true;

    public Robot(double x, double y, double angle) {
        worldXPosition = x;
        worldYPosition = y;
        worldAngle = angle;
    }

    public Robot(Follower ignored) {
        worldXPosition = 0;
        worldYPosition = 0;
        worldAngle = 0;
    }

    /**
     * Creates a robot simulation
     */


    //robotprueba
    public static double xSpeed = 0;
    public static double ySpeed = 0;
    public static double turnSpeed = 0;

    public static double worldXPosition;
    public static double worldYPosition;
    public static double worldAngle;

    public static double getXPos(){
        return worldXPosition;
    }

    public static double getYPos(){
        return worldYPosition;
    }


    public static double getWorldAngle() {
        return worldAngle;
    }


    //last update time
    private long lastUpdateTime = 0;

    /**
     * Calculates the change in position of the robot
     */
    public void update(){
        //tiempo
        long currentTimeMillis = System.currentTimeMillis();

        double elapsedTime = (currentTimeMillis - lastUpdateTime)/1000.0;

        lastUpdateTime = currentTimeMillis;
        if(elapsedTime > 1){return;}



        //incrementa la posicion
        double totalSpeed = Math.hypot(xSpeed,ySpeed);
        double angle = Math.atan2(ySpeed,xSpeed) - Math.toRadians(90);
        double outputAngle = worldAngle + angle;
        worldXPosition += totalSpeed * Math.cos(outputAngle) * elapsedTime * 1000 * 0.2;
        worldYPosition += totalSpeed * Math.sin(outputAngle) * elapsedTime * 1000 * 0.2;

        worldAngle += MovementVars.movementTurn * elapsedTime * 20 / (2 * Math.PI);


        xSpeed += Range.clip((MovementVars.movementX -xSpeed)/0.2,-1,1) * elapsedTime;
        ySpeed += Range.clip((MovementVars.movementY -ySpeed)/0.2,-1,1) * elapsedTime;
        turnSpeed += Range.clip((MovementVars.movementTurn -turnSpeed)/0.2,-1,1) * elapsedTime;


        SpeedOmeter.yDistTraveled += ySpeed * elapsedTime * 1000;
        SpeedOmeter.xDistTraveled += xSpeed * elapsedTime * 1000;

        SpeedOmeter.update();


        xSpeed *= 1.0 - (elapsedTime);
        ySpeed *= 1.0 - (elapsedTime);
        turnSpeed *= 1.0 - (elapsedTime);



    }
}
