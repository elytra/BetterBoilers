package com.elytradev.betterboilers.tile.turbine;

import com.elytradev.betterboilers.util.FluidAccess;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityTurbinePressureValve extends TileEntityTurbinePart implements ITurbinePart {
    private TileEntityTurbineController controller;

    @Override
    @Nullable
    public TileEntityTurbineController getController() {
        return this.controller;
    }

    @Override
    public void setController(TileEntityTurbineController controller) {
        this.controller = controller;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (controller==null) return false; //!important
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (controller==null) return null; //!important
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) FluidAccess.insertOnly(controller.getTankSteam());
        } else {
            return super.getCapability(capability, facing);
        }
    }

}
