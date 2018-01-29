package com.elytradev.betterboilers.tile.boiler;

import com.elytradev.betterboilers.tile.boiler.IBoilerPart;
import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerController;
import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerPart;

import javax.annotation.Nullable;

public class TileEntityFireboxBlock extends TileEntityBoilerPart implements IBoilerPart {
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
}