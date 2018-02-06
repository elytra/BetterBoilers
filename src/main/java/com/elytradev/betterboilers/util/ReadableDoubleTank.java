package com.elytradev.betterboilers.util;

import javax.annotation.Nullable;

import com.elytradev.concrete.inventory.ConcreteFluidTank;

import com.google.common.base.Preconditions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * IFluidHandler capability which provides two tanks.
 */
public class ReadableDoubleTank implements IFluidHandler {
    private FluidTank a;
    private FluidTank b;

    public ReadableDoubleTank(FluidTank a, FluidTank b) {
        this.a = Preconditions.checkNotNull(a);
        this.b = Preconditions.checkNotNull(b);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return FluidTankProperties.convert(new FluidTankInfo[]{a.getInfo(), b.getInfo()});
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    @Nullable
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    @Nullable
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    private static boolean validateFill(FluidTank tank, FluidStack stack) {
        return false;
    }
}