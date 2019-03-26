package com.elytradev.betterboilers.tile.boiler;

import com.elytradev.betterboilers.tile.boiler.IBoilerPart;
import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerController;
import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerPart;
import com.elytradev.concrete.inventory.ValidatedItemHandlerView;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityBoilerFireboxHatch extends TileEntityBoilerPart implements IBoilerPart {
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
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (controller==null) return null; //!important
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) new ValidatedItemHandlerView(controller.getInv());
        } else {
            return super.getCapability(capability, facing);
        }
    }

}
