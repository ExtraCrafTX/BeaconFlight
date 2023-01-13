package com.extracraftx.minecraft.beaconflight.events;

import com.extracraftx.minecraft.beaconflight.config.Config;
import com.extracraftx.minecraft.beaconflight.interfaces.FlyEffectable;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

public class EventHandler {
    public static void onBeaconUpdate(PlayerEntity player, int duration, int level){
        if(player instanceof ServerPlayerEntity){
            Config config = Config.INSTANCE;
            if(level < config.minBeaconLevel)
                return;
            if(player.isCreative())
                return;
            ServerPlayerEntity sp = (ServerPlayerEntity)player;
            if(!checkItem(config.mainHand, sp.getMainHandStack()))
                return;
            if(!checkItem(config.offHand, sp.getOffHandStack()))
                return;
            if(!checkItem(config.anyHand, sp.getMainHandStack()) && !checkItem(config.anyHand, sp.getOffHandStack()))
                return;
            if(!checkItem(config.head, sp.getEquippedStack(EquipmentSlot.HEAD)))
                return;
            if(!checkItem(config.chest, sp.getEquippedStack(EquipmentSlot.CHEST)))
                return;
            if(!checkItem(config.legs, sp.getEquippedStack(EquipmentSlot.LEGS)))
                return;
            if(!checkItem(config.feet, sp.getEquippedStack(EquipmentSlot.FEET)))
                return;
            ServerAdvancementLoader advLoader = sp.getServer().getAdvancementLoader();
            PlayerAdvancementTracker playerAdv = sp.getAdvancementTracker();
            for(Identifier advancement : config.advancements){
                if(!playerAdv.getProgress(advLoader.get(advancement)).isDone())
                    return;
            }
            FlyEffectable p = (FlyEffectable)player;
            if(config.flightLingerTime != 0)
                p.allowFlight(config.flightLingerTime, false);
            else
                p.allowFlight(duration, false);
        }
    }
    
    public static void onPlayerTick(FlyEffectable player){
        player.tickFlight();
    }

    public static void onSetGameMode(GameMode gameMode, FlyEffectable player){
        if(gameMode.isCreative())
            player.setFlightTicks(0);
    }

    public static boolean checkItem(Item required, Item current){
        return required == null || required == current;
    }

    public static boolean checkItem(Item required, ItemStack current){
        return checkItem(required, current.getItem());
    }
}