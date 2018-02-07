package com.elytradev.betterboilers.container;

import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineController;
import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.*;
import com.elytradev.betterboilers.BetterBoilers;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class TurbineContainer extends ConcreteContainer {

    private ResourceLocation steamBG = new ResourceLocation(BetterBoilers.modId,"textures/gui/steam_bg.png");
    private ResourceLocation steamFG = new ResourceLocation(BetterBoilers.modId,"textures/gui/steam_fg.png");
    private ResourceLocation energyBG = new ResourceLocation(BetterBoilers.modId,"textures/gui/energy_bg.png");
    private ResourceLocation energyFG = new ResourceLocation(BetterBoilers.modId,"textures/gui/energy_fg.png");

    public TurbineContainer(IInventory player, IInventory container, TileEntityTurbineController turbine) {
        super(player, container);
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        WPanel playerInv = this.createPlayerInventoryPanel();
        WFluidBar tankSteam = new WFluidBar(steamBG, steamFG, turbine.tankSteam).withTooltip("%d/%d mB steam");
        WBar energyBar = new WBar(energyBG, energyFG, container, 0, 1, WBar.Direction.UP).withTooltip("%d/%d RF");
        WImage steamBar = new WImage(new ResourceLocation(BetterBoilers.modId, "textures/gui/steam_bar.png"));
        WImage steamBar1 = new WImage(new ResourceLocation(BetterBoilers.modId, "textures/gui/steam_bar.png"));
        panel.add(playerInv, 0, 87);
        panel.add(tankSteam, 38, 26, 18, 40);
        panel.add(energyBar, 108, 22, 18, 48);
        panel.add(steamBar, 38, 22, 18, 4);
        panel.add(steamBar1, 38, 66, 18, 4);
    }
}
