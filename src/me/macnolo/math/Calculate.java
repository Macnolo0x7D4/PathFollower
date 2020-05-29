package me.macnolo.math;

public class Calculate {
    public static double calc1(double Vd, double Td, double Vt) {
        double V;

        V = Vd * Math.sin(Td + (Math.PI / 4)) + Vt;
        return V;
    }

    public static double calc2(double Vd, double Td, double Vt) {
        double V;
        V = Vd * Math.cos(Td + (Math.PI / 4)) + Vt;
        return V;
    }
}
