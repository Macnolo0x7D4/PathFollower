package org.wint3794.pathfollower.geometry;

public class CurvePoint extends Point{
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
    }

    public CurvePoint(Point point) {
        this.x = point.x;
        this.y = point.y;
    }


    public CurvePoint(double x, double y, double moveSpeed, double turnSpeed, double followDistance,double pointLength, double slowDownTurnRadians, double slowDownTurnAmount){

        this(x,y, moveSpeed, turnSpeed, followDistance, slowDownTurnRadians, slowDownTurnAmount);
        this.pointLength = pointLength;
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
}