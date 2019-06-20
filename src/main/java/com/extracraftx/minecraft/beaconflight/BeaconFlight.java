package com.extracraftx.minecraft.beaconflight;


import com.extracraftx.minecraft.beaconflight.config.Config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class BeaconFlight implements ModInitializer{

    public static final String MOD_ID = "beaconflight";
    public static final String MOD_NAME = "BeaconFlight";
    public static final String MOD_VER = "1.1.0";

    public static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        log(Level.INFO, "version " + MOD_VER);
        Config.loadConfig();
        log(Level.INFO, "Initialized successfully.");
    }

    public static void log(Level level, String message){
        if(Config.INSTANCE.log == null || level.isMoreSpecificThan(Config.INSTANCE.log))
            LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}