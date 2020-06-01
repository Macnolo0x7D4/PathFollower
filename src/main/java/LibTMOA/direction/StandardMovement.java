package LibTMOA.direction;

import LibTMOA.math.Calculate;
import LibTMOA.utils.VelocityChecker;

public class StandardMovement {
    public static double[] move(double Vd, double Td, double Vt ){

        if (!VelocityChecker.checkSpeed(Vd)){ return null; }
        if (!VelocityChecker.checkAngle(Td)){ return null; }
        if (!VelocityChecker.checkSpeed(Vt)){ return null; }

        return velocitiesCreator(Vd, Td, Vt);
    }

    public static double[] move(double y, double x){
        if (!VelocityChecker.checkCoordinates(y, x)){ return null; }

        double Vd = Calculate.getSpeed(y, x);
        double Td = Calculate.getAngle(y, x);

        if (!VelocityChecker.checkAngle(Td)){ return null; }

        return velocitiesCreator(Vd, Td);
    }

    private static double[] velocitiesCreator(double Vd, double Td, double Vt){
        double[] velocities = new double[4];

        velocities[0] = Calculate.calc2(Vd, Td, Vt);
        velocities[1] = Calculate.calc1(Vd, Td, Vt);
        velocities[2] = Calculate.calc1(Vd, Td, Vt);
        velocities[3] = Calculate.calc2(Vd, Td, Vt);

        return velocities;
    }

    private static double[] velocitiesCreator(double Vd, double Td){
        double[] velocities = new double[4];

        velocities[0] = Calculate.calc2(Vd, Td);
        velocities[1] = Calculate.calc1(Vd, Td);
        velocities[2] = Calculate.calc1(Vd, Td);
        velocities[3] = Calculate.calc2(Vd, Td);

        return velocities;
    }
}