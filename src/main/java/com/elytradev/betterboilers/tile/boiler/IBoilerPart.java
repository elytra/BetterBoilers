package com.elytradev.betterboilers.tile.boiler;

import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerController;

public interface IBoilerPart {

    public void setController(TileEntityBoilerController controller);

    public TileEntityBoilerController getController();

}
