package com.extracraftx.minecraft.beaconflight.interfaces;

public interface FlyEffectable{
    public void allowFlight(int ticks, boolean setFlying);
    public void setFlightTicks(int ticks);
    public void disallowFlight();
    public void tickFlight();
}