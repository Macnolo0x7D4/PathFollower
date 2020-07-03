package org.wint3794.pathfollower.debug;

import org.wint3794.pathfollower.controllers.Robot;
import org.wint3794.pathfollower.debug.telemetries.UdpServer;
import org.wint3794.pathfollower.geometry.Point;
import org.wint3794.pathfollower.geometry.Pose2d;

import java.net.UnknownHostException;
import java.text.DecimalFormat;

public class ComputerDebugging {
    //manda losmensajes
    private static UdpServer udpServer;
    //arma los mensajes
    private static StringBuilder messageBuilder = new StringBuilder();

    //sistema decimal
    private static DecimalFormat df = new DecimalFormat("#.00");

    /**
     * Initializes udp server and starts it's thread
     */
    public ComputerDebugging(){
        UdpServer.kill = false;

        try {
            udpServer = new UdpServer("192.168.0.9", 11115);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Thread runner = new Thread(udpServer);
        runner.start();//go go go
    }



    /**
     * Sends the robot location to the debug computer
     */
    public static void sendRobotLocation(Pose2d robot){
        //si no se usa lacompu
        if(!Robot.usingComputer){return;}

        //manda la direcciondel robot
        messageBuilder.append("ROBOT,");
        messageBuilder.append(df.format(robot.getX()));
        messageBuilder.append(",");
        messageBuilder.append(df.format(robot.getY()));
        messageBuilder.append(",");
        messageBuilder.append(df.format(robot.getAngle()));
        messageBuilder.append("%");

    }

    /**
     * Sends the location of any point you would like to send
     * @param floatPoint the point you want to send
     */
    public static void sendKeyPoint(Point floatPoint) {
        if(!Robot.usingComputer){return;}


        messageBuilder.append("P,")
                .append(df.format(floatPoint.x))
                .append(",")
                .append(df.format(floatPoint.y))
                .append("%");
    }


    /**
     * This is a point you don't want to clear every update
     * @param floatPoint the point you want to send
     */
    public static void sendLogPoint(Point floatPoint) {
        if(!Robot.usingComputer){return;}


        messageBuilder.append("LP,")
                .append(df.format(floatPoint.x))
                .append(",")
                .append(df.format(floatPoint.y))
                .append("%");
    }


    /**
     * Used for debugging lines
     * @param point1
     * @param point2
     */
    public static void sendLine(Point point1, Point point2){
        //no compu
        if(!Robot.usingComputer){return;}
        messageBuilder.append("LINE,")
                .append(df.format(point1.x))
                .append(",")
                .append(df.format(point1.y))
                .append(",")
                .append(df.format(point2.x))
                .append(",")
                .append(df.format(point2.y))
                .append("%");
    }


    /**
     * This kills the udpServer background thread
     */
    public static void stopAll() {
        if(!Robot.usingComputer){return;}

        UdpServer.kill = true;
    }

    /**
     * Sends the data accumulated over the update by adding it to the udpServer
     */
    public static void markEndOfUpdate() {
        if(!Robot.usingComputer){return;}
        messageBuilder.append("CLEAR,%");

//        udpServer.addMessage(messageBuilder.toString());
        udpServer.send(messageBuilder.toString());
        messageBuilder = new StringBuilder();
    }

    /**
     * Forces a clear log
     */
    public static void clearLogPoints() {
        if(!Robot.usingComputer){return;}
        udpServer.send("CLEARLOG,%");

    }
}
