package me.macnolo.testing;

import me.macnolo.models.Configuration;
import me.macnolo.models.DcMotor;

import java.util.List;

public class TestConfig {
    public static Configuration getDefaultConfiguration(){
        return new Configuration(
            List.of(
                    new TestDcMotor((byte) 0),
                    new TestDcMotor((byte) 1),
                    new TestDcMotor((byte) 2),
                    new TestDcMotor((byte) 3)
            ),
            0,
            0,
            0,
            0,
            0,
            0
        );
    }
}
