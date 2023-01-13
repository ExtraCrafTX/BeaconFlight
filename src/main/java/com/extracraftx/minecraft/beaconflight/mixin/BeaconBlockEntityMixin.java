package com.extracraftx.minecraft.beaconflight.mixin;

import java.util.Iterator;
import java.util.List;

import com.extracraftx.minecraft.beaconflight.events.EventHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    @Inject(method = "applyPlayerEffects", at = @At("TAIL"))
    private static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel, StatusEffect primaryEffect, StatusEffect secondaryEffect, CallbackInfo ci) {
        // Calc range of beacon
        double d = beaconLevel * 10 + 10;
        // Create box around beacon that represents the status effect range
        Box box = (new Box(pos)).expand(d).stretch(0.0D, world.getHeight(), 0.0D);
        // Get all players in the box
        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);

        // Iterate over all players
        for (Iterator<PlayerEntity> iterator = players.iterator(); iterator.hasNext();) {
            PlayerEntity player = iterator.next();
            // Call event handler
            EventHandler.onBeaconUpdate(player, 320, beaconLevel);
        }
    }
}