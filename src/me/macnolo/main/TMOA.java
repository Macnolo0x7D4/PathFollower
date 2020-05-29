package me.macnolo.main;

import me.macnolo.models.Configuration;

public class TMOA {

    Configuration config;

    public TMOA(Configuration config){
        this.config = config;
        System.out.println("Running!");
    }

    public Configuration getChassisInformation(){
        return this.config;
    }
}
