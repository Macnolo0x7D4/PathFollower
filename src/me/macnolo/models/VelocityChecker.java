package me.macnolo.models;

public class VelocityChecker {
    public static boolean checkSpeed(double Vd){
        if(Vd >= -1 && Vd <= 1){
            return true;
        }

        return false;
    }

    public static boolean checkAngle(double Td){
        if (Td >= 0 && Td <= 2 * Math.PI){
            return true;
        }

        return false;
    }
}
