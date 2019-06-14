package com.extracraftx.minecraft.beaconflight.mixin;

import java.util.Iterator;
import java.util.List;

import com.extracraftx.minecraft.beaconflight.config.Config;
import com.extracraftx.minecraft.beaconflight.interfaces.FlyEffectable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BoundingBox;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {

    @Shadow
    private int level;

    @Inject(method = "applyPlayerEffects",
    at = @At(value = "INVOKE",
        target = "addPotionEffect",
        ordinal = 0
    ), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onApplyPlayerEffects(CallbackInfo info, double d, int i1, int i2, BoundingBox bb, List l, Iterator it, PlayerEntity player) {
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
            for(Identifier advancement : Config.INSTANCE.advancements){
                if(!playerAdv.getProgress(advLoader.get(advancement)).isDone())
                    return;
            }
            FlyEffectable p = (FlyEffectable)player;
            p.allowFlight(100);
        }
    }

}