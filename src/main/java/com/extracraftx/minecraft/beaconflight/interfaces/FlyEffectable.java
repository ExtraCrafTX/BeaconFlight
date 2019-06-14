package com.extracraftx.minecraft.beaconflight.interfaces;

public interface FlyEffectable{
    public void allowFlight(int ticks);
    public void setFlightTicks(int ticks);
    public void disallowFlight();
    public void tickFlight();
}