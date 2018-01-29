package com.elytradev.betterboilers.tile.turbine;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class TileEntityTurbinePowerTap extends TileEntityTurbinePart implements ITurbinePart {
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
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (controller==null) return null; //!important
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) CapabilityEnergy.ENERGY.cast(controller.energyStorage);
        } else {
            return super.getCapability(capability, facing);
        }
    }
}
