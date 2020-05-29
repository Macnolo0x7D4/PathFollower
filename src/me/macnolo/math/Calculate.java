package me.macnolo.math;

import me.macnolo.utils.Utilities;

public class Calculate {
    public static double calc1(double Vd, double Td, double Vt) {
        return Vd * Math.sin(Td + (Math.PI / 4)) + Vt;
    }

    public static double calc2(double Vd, double Td, double Vt) {
        return Vd * Math.cos(Td + (Math.PI / 4)) + Vt;
    }

    public static double calc1(double Vd, double Td) {
        return Vd * Math.sin(Td + (Math.PI / 4));
    }

    public static double calc2(double Vd, double Td) {
        return Vd * Math.cos(Td + (Math.PI / 4));
    }

    public static double getAngle(double y, double x){
        return Math.atan2(y, x);
    }

    public static double getSpeed(double y, double x){
        if(Math.abs(y) >= Math.abs(x)){
            return y;
        }

        return x;
    }

    public static double roundPower(double value){
        long factor = (long) Math.pow(10, Utilities.roundPower);
        value = value * factor;
        return (double) Math.round(value) / factor;
    }
}
