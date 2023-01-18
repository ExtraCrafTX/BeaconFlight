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

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements FlyEffectable{
    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    private int flyTicksLeft = 0;
    private float xpCounter;

    @Override
    public void allowFlight(int ticks, boolean setFlying) {
        flyTicksLeft = Math.max(flyTicksLeft, ticks);
        if(Config.INSTANCE.xpDrainRate == 0 || totalExperience > 0){
            getAbilities().allowFlying = true;
            if(setFlying)
                getAbilities().flying = true;
            sendAbilitiesUpdate();
        }
    }

    @Override
    public void disallowFlight() {
        // if in creative mode dont disallow flight
        if(getAbilities().creativeMode)
            return;
        getAbilities().allowFlying = false;
        getAbilities().flying = false;
        sendAbilitiesUpdate();
        // if player is wearing elytra dont add slow falling effect
        if(!getEquippedStack(EquipmentSlot.CHEST).getItem().equals(Items.ELYTRA)) {
            addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, Config.INSTANCE.slowFallingTime*20));
        }
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