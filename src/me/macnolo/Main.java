package me.macnolo;

import me.macnolo.main.TMOA;
import me.macnolo.testing.TestConfig;

public class Main {

    public static void main(String[] args) {
        TMOA mecanum = new TMOA(TestConfig.getDefaultConfiguration());

        mecanum.move(1, (3 * Math.PI) / 5, 0);
    }
}
