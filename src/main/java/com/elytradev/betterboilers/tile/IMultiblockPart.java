package com.elytradev.betterboilers.tile;

import net.minecraft.tileentity.TileEntity;

public interface IMultiblockPart<T extends TileEntityMultiblockController> {
    void setController(TileEntityMultiblockController controller);
}
