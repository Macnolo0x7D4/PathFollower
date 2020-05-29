package me.macnolo.direction;

import me.macnolo.main.TMOA;
import me.macnolo.math.Calculate;
import me.macnolo.models.VelocityChecker;

import java.lang.reflect.Array;

public class StandardMovement {
    public static double[] move(double Vd, double Td, double Vt ){

        if (!VelocityChecker.checkSpeed(Vd)){ return null; }
        if (!VelocityChecker.checkAngle(Td)){ return null; }
        if (!VelocityChecker.checkSpeed(Vt)){ return null; }

        double[] velocities = new double[4];

        velocities[0] = Calculate.calc2(Vd, Td, Vt);
        velocities[1] = Calculate.calc1(Vd, Td, Vt);
        velocities[2] = Calculate.calc1(Vd, Td, Vt);
        velocities[3] = Calculate.calc2(Vd, Td, Vt);

        return velocities;
    }
}