package com.elytradev.betterboilers.tile.boiler;

import javax.annotation.Nullable;

import com.elytradev.betterboilers.util.FluidAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.minecraft.util.EnumFacing;

public class TileEntityBoilerValve extends TileEntityBoilerPart implements IBoilerPart {
    private TileEntityBoilerController controller;

    @Override
    @Nullable
    public TileEntityBoilerController getController() {
        return this.controller;
    }

    @Override
    public void setController(TileEntityBoilerController controller) {
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
            return (T) FluidAccess.insertOnly(controller.getTankWater());
        } else {
            return super.getCapability(capability, facing);
        }
    }

}
