package com.elytradev.betterboilers.util;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraftforge.energy.EnergyStorage;

/**
 * A version of forge energy that can be asked to automatically mark a TileEntity dirty when accessed
 */
public class ObservableEnergyStorage extends EnergyStorage implements ITransferRate {
    private ArrayList<Runnable> listeners = new ArrayList<>();
    private int ticksThisSegment = 0;
    private int ticksPerSegment = 10; //twice per second should be enough
    private int thisSegmentEnergy = 0;
    private float lastSegmentEnergyPerTick = 0.0f;
    private int pollTime = 1;

    public ObservableEnergyStorage(int capacity) {
        super(capacity);
    }

    public ObservableEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public ObservableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    private void markDirty() {
        for(Runnable r : listeners) {
            r.run();
        }
    }

    public void listen(@Nonnull Runnable r) {
        listeners.add(r);
    }

    /* Hook mutator methods only, to send events on a successful mutation */

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int result = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && result!=0) markDirty();
        return result;
    }

    public int generateEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, maxReceive);
        if (!simulate)
            energy += energyReceived;
        if (!simulate && energyReceived!=0) markDirty();
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract<0) return 0;
        int result = super.extractEnergy(maxExtract, simulate);
        if (!simulate && result!=0) {
            markDirty();
            thisSegmentEnergy+=result;
        }
        return result;
    }

    /* Extra methods beyond IEnergyStorage to measure bandwidth */
    @Override
    public int getMaxTransfer() {
        return maxExtract/pollTime;
    }

    public ObservableEnergyStorage withPollTime(int time) {
        pollTime=(time<=0) ? 1 : time;
        return this;
    }

    public void tick() {
        ticksThisSegment++;
        if (ticksThisSegment>=ticksPerSegment) {
            lastSegmentEnergyPerTick = thisSegmentEnergy/(float)ticksPerSegment;
            ticksThisSegment=0;
            thisSegmentEnergy = 0;
        }
    }

    @Override
    public float getCurTransfer() {
        return lastSegmentEnergyPerTick;
    }

}