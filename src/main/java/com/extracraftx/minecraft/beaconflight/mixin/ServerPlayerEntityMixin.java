package com.extracraftx.minecraft.beaconflight.mixin;

import com.extracraftx.minecraft.beaconflight.config.Config;
import com.extracraftx.minecraft.beaconflight.events.EventHandler;
import com.extracraftx.minecraft.beaconflight.interfaces.FlyEffectable;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements FlyEffectable{

    public ServerPlayerEntityMixin(World world, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(world, blockPos, f, gameProfile);
    }

    private int flyTicksLeft = 0;
    private float xpCounter;

    @Override
    public void allowFlight(int ticks, boolean setFlying) {
        flyTicksLeft = Math.max(flyTicksLeft, ticks);
        if(Config.INSTANCE.xpDrainRate == 0 || totalExperience > 0){
            PlayerAbilities abilities = getAbilities();
            abilities.allowFlying = true;
            if(setFlying)
                abilities.flying = true;
            sendAbilitiesUpdate();
        }
    }

    @Override
    public void disallowFlight() {
        PlayerAbilities abilities = getAbilities();
        abilities.allowFlying = false;
        abilities.flying = false;
        sendAbilitiesUpdate();
        if (Config.INSTANCE.slowFallingTime > 0)
            addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, Config.INSTANCE.slowFallingTime*20));
    }

    @Override
    public void tickFlight() {
        if(flyTicksLeft > 0){
            if(Config.INSTANCE.xpDrainRate != 0){
                if(getAbilities().flying){
                    xpCounter += Config.INSTANCE.xpDrainRate;
                    addExperience(-(int)Math.floor(xpCounter));
                    xpCounter %= 1;
                    if(Config.INSTANCE.xpDrainRate > 0 && totalExperience == 0)
                        disallowFlight();
                }
                if(totalExperience > 0)
                    allowFlight(flyTicksLeft, false);
            }
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

    @Inject(method = "changeGameMode", at = @At("HEAD"))
    private void onSetGameMode(GameMode gameMode, CallbackInfoReturnable<Boolean> cir){
        EventHandler.onSetGameMode(gameMode, this);
    }
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void onWriteCustomDataToTag(NbtCompound tag, CallbackInfo info){
        tag.putInt("flyTicksLeft", flyTicksLeft);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void onReadCustomDataFromTag(NbtCompound tag, CallbackInfo info){
        allowFlight(tag.getInt("flyTicksLeft"), false);
    }

    @Shadow
    public void sendAbilitiesUpdate(){}

}