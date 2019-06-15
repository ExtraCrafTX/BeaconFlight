package com.extracraftx.minecraft.beaconflight.mixin;

import com.extracraftx.minecraft.beaconflight.config.Config;
import com.extracraftx.minecraft.beaconflight.events.EventHandler;
import com.extracraftx.minecraft.beaconflight.interfaces.FlyEffectable;
import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements FlyEffectable{
    public ServerPlayerEntityMixin(World world, GameProfile profile){
        super(world, profile);
    }

    private int flyTicksLeft = 0;

    @Override
    public void allowFlight(int ticks, boolean setFlying) {
        flyTicksLeft = Math.max(flyTicksLeft, ticks);
        abilities.allowFlying = true;
        if(setFlying)
            abilities.flying = true;
        sendAbilitiesUpdate();
    }

    @Override
    public void disallowFlight() {
        abilities.allowFlying = false;
        abilities.flying = false;
        sendAbilitiesUpdate();
        addPotionEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, Config.INSTANCE.slowFallingTime*20));
    }

    @Override
    public void tickFlight() {
        if(flyTicksLeft > 0){
            flyTicksLeft --;
            if(flyTicksLeft == 0)
                disallowFlight();
        }
    }

    @Override
    public void setFlightTicks(int ticks) {
        flyTicksLeft = ticks;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info){
        EventHandler.onPlayerTick(this);
    }

    @Inject(method = "setGameMode", at = @At("RETURN"))
    private void onSetGameMode(GameMode gameMode, CallbackInfo info){
        EventHandler.onSetGameMode(gameMode, this);
    }

    @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
    private void onWriteCustomDataToTag(CompoundTag tag, CallbackInfo info){
        tag.putInt("flyTicksLeft", flyTicksLeft);
    }

    @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
    private void onReadCustomDataFromTag(CompoundTag tag, CallbackInfo info){
        allowFlight(tag.getInt("flyTicksLeft"), false);
    }

    @Shadow
    public void sendAbilitiesUpdate(){}

}