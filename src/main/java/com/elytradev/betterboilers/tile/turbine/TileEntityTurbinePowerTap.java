package com.elytradev.betterboilers.tile.turbine;

import com.elytradev.betterboilers.util.BBConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityTurbinePowerTap extends TileEntityTurbinePart implements ITurbinePart, ITickable {
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

    public void update() {
        if (world.isRemote || !hasWorld() || !hasController()) return;
        for (EnumFacing side : EnumFacing.values()) {
            TileEntity tile = world.getTileEntity(getPos().offset(side));
            if (tile == null || !tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) {
                continue;
            }

            IEnergyStorage cap = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
            int energyTransfer = controller.energyStorage.extractEnergy(BBConfig.turbineOut, true);
            if (energyTransfer != 0) {
                int qty = cap.receiveEnergy(BBConfig.turbineOut, false);
                if (qty > 0) controller.energyStorage.extractEnergy(qty, false);
            }
        }
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
