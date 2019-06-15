package com.extracraftx.minecraft.beaconflight.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.extracraftx.minecraft.beaconflight.BeaconFlight;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Config{
    public static Config INSTANCE = new Config();

    private static final File configDir = new File("config");
    private static final File configFile = new File("config/" + BeaconFlight.MOD_ID + "_config.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public int minBeaconLevel = 4;
    public String mainHandItem;
    public transient Item mainHand;
    public String offHandItem = "minecraft:feather";
    public transient Item offHand;
    public String anyHandItem;
    public transient Item anyHand;
    public String[] advancementsRequired = new String[]{"minecraft:end/elytra"};
    public transient Identifier[] advancements;
    public int slowFallingTime = 10;

    public static void loadConfig(){
        try{
            configDir.mkdirs();
            if(configFile.createNewFile()){
                FileWriter fw = new FileWriter(configFile);
                fw.append(gson.toJson(INSTANCE));
                fw.close();
            }else{
                FileReader fr = new FileReader(configFile);
                INSTANCE = gson.fromJson(fr, Config.class);
                fr.close();
                BeaconFlight.log(Level.INFO, "Config loaded.");
            }
        }catch(Exception e){
            BeaconFlight.log(Level.WARN, "Error loading config, using default values.");
        }
        INSTANCE.generateTransients();
    }

    public static void saveConfigs(){
        try{
            configDir.mkdirs();
            FileWriter fw = new FileWriter(configFile);
            fw.append(gson.toJson(INSTANCE));
            fw.close();
            BeaconFlight.log(Level.INFO, "Config saved.");
        }catch(Exception e){
            BeaconFlight.log(Level.WARN, "Error saving config");
        }
    }

    private void generateTransients(){
        advancements = new Identifier[advancementsRequired.length];
        for(int i = 0; i < advancements.length; i++){
            advancements[i] = new Identifier(advancementsRequired[i]);
        }
        if(mainHandItem != null)
            mainHand = Registry.ITEM.get(new Identifier(mainHandItem));
        if(offHandItem != null)
            offHand = Registry.ITEM.get(new Identifier(offHandItem));
        if(anyHandItem != null)
            anyHand = Registry.ITEM.get(new Identifier(anyHandItem));
    }
}