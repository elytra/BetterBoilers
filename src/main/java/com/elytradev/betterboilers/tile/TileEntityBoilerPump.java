package com.elytradev.betterboilers.tile;

import com.elytradev.betterboilers.util.BBConfig;
import com.elytradev.betterboilers.util.FluidAccess;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntityBoilerPump extends TileEntityBoilerPart implements IBoilerPart, ITickable {
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

    public void update() {
        if (world.isRemote || !hasWorld() || !hasController()) return;
        for (EnumFacing side : EnumFacing.values()) {
            TileEntity tile = world.getTileEntity(getPos().offset(side));
            if (tile == null || !tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())) {
                return;
            }

            IFluidHandler cap = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
            FluidStack drain = controller.getTankSteam().drain(BBConfig.pumpDrain, false);
            if (drain != null) {
                int qty = cap.fill(drain, true);
                if (qty > 0) controller.getTankSteam().drain(qty, true);
            }
        }
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
            return (T) FluidAccess.extractOnly(controller.getTankSteam());
        } else {
            return super.getCapability(capability, facing);
        }
    }

}
