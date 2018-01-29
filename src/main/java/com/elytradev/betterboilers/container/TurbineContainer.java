package com.elytradev.betterboilers.container;

import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineController;
import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.*;
import com.elytradev.betterboilers.BetterBoilers;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class TurbineContainer extends ConcreteContainer {

    public TurbineContainer(IInventory player, IInventory container, TileEntityTurbineController turbine) {
        super(player, container);
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        WPanel playerInv = this.createPlayerInventoryPanel();
        panel.add(playerInv, 0, 87);
    }
}
