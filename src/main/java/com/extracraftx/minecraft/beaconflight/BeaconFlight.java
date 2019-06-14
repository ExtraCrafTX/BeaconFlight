package com.extracraftx.minecraft.beaconflight;


import com.extracraftx.minecraft.beaconflight.config.Config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class BeaconFlight implements ModInitializer{

    public static final String MOD_ID = "beaconflight";
    public static final String MOD_NAME = "BeaconFlight";
    public static final String MOD_VER = "0.0.1";

    public static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Config.loadConfig();
        log(Level.INFO, "Initialized successfully.");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}