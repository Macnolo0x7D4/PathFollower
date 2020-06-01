package LibTMOA.utils;

public class VelocityChecker {
    public static boolean checkSpeed(double Vd){
        if(Math.abs(Vd) <= 1){
            return true;
        }

        return false;
    }

    public static boolean checkAngle(double Td){
        if (Math.abs(Td) <= 2 * Math.PI){
            return true;
        }

        return false;
    }

    public static boolean checkCoordinates(double y, double x){
        if (Math.abs((x + y) / 2) <= 1){
            return true;
        }

        return false;
    }

}
