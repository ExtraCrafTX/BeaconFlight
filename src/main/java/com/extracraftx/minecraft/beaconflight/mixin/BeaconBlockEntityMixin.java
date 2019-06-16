package com.extracraftx.minecraft.beaconflight.mixin;

import java.util.Iterator;
import java.util.List;

import com.extracraftx.minecraft.beaconflight.events.EventHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BoundingBox;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {

    @Shadow
    private int level;

    @Inject(method = "applyPlayerEffects",
    at = @At(value = "INVOKE_ASSIGN",
        target = "Lnet/minecraft/entity/player/PlayerEntity;addPotionEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
        ordinal = 0
    ), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onApplyPlayerEffects(CallbackInfo info, double d, int i, int duration, BoundingBox bb, List l, Iterator it, PlayerEntity player) {
        EventHandler.onBeaconUpdate(player, duration, level);
    }

}