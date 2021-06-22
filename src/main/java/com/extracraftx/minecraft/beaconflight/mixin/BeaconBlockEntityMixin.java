package com.extracraftx.minecraft.beaconflight.mixin;

import com.extracraftx.minecraft.beaconflight.events.EventHandler;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {

    //I dunno why the old one kept failing
    @Inject(method = "applyPlayerEffects",
    at = @At(value = "TAIL"
    ), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void onApplyPlayerEffects(World world, BlockPos blockPos, int i, @Nullable StatusEffect statusEffect, @Nullable StatusEffect statusEffect2, CallbackInfo info, double d, int y, int duration, Box bb, List<PlayerEntity>  list) {
        for (PlayerEntity playerEntity : list) {
            EventHandler.onBeaconUpdate(playerEntity, duration, i);
        }
    }

}