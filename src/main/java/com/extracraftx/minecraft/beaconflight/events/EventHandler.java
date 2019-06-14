package com.extracraftx.minecraft.beaconflight.events;

import com.extracraftx.minecraft.beaconflight.config.Config;
import com.extracraftx.minecraft.beaconflight.interfaces.FlyEffectable;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.player.PlayerEntity;
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
            if(config.mainHand != null){
                if(sp.getMainHandStack().getItem() != config.mainHand)
                    return;
            }
            if(config.offHand != null){
                if(sp.getOffHandStack().getItem() != config.offHand)
                    return;
            }
            if(config.anyHand != null){
                if(sp.getOffHandStack().getItem() != config.anyHand && sp.getMainHandStack().getItem() != config.anyHand)
                    return;
            }
            ServerAdvancementLoader advLoader = sp.getServer().getAdvancementManager();
            PlayerAdvancementTracker playerAdv = sp.getAdvancementManager();
            for(Identifier advancement : config.advancements){
                if(!playerAdv.getProgress(advLoader.get(advancement)).isDone())
                    return;
            }
            FlyEffectable p = (FlyEffectable)player;
            p.allowFlight(duration);
        }
    }
    
    public static void onPlayerTick(FlyEffectable player){
        player.tickFlight();
    }

    public static void onSetGameMode(GameMode gameMode, FlyEffectable player){
        if(gameMode.isCreative())
            player.setFlightTicks(0);
    }
}