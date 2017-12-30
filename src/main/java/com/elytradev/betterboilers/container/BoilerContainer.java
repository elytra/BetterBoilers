package com.elytradev.betterboilers.container;

import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.WBar;
import com.elytradev.concrete.inventory.gui.widget.WFluidBar;
import com.elytradev.concrete.inventory.gui.widget.WItemSlot;
import com.elytradev.concrete.inventory.gui.widget.WPanel;
import com.elytradev.concrete.inventory.gui.widget.WPlainPanel;
import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.tile.TileEntityController;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class BoilerContainer extends ConcreteContainer {

    private ResourceLocation waterBG = new ResourceLocation(BetterBoilers.modId,"textures/gui/water_bg.png");
    private ResourceLocation waterFG = new ResourceLocation(BetterBoilers.modId,"textures/gui/water_fg.png");
    private ResourceLocation steamBG = new ResourceLocation(BetterBoilers.modId,"textures/gui/steam_bg.png");
    private ResourceLocation steamFG = new ResourceLocation(BetterBoilers.modId,"textures/gui/steam_fg.png");
    private ResourceLocation fireBG = new ResourceLocation(BetterBoilers.modId,"textures/gui/fire_bg.png");
    private ResourceLocation fireFG = new ResourceLocation(BetterBoilers.modId,"textures/gui/fire_fg.png");
    private ResourceLocation arrowBG = new ResourceLocation(BetterBoilers.modId,"textures/gui/arrow_bg.png");
    private ResourceLocation arrowFG = new ResourceLocation(BetterBoilers.modId,"textures/gui/arrow_fg.png");

    public BoilerContainer(IInventory player, IInventory container, TileEntityController boiler) {
        super(player, container);
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        WItemSlot slotsFuel = WItemSlot.of(container, 0, 3, 1);
        WPanel playerInv = this.createPlayerInventoryPanel();
        WFluidBar tankWater = new WFluidBar(waterBG, waterFG, boiler.tankWater).withTooltip("%d/%d mB water");
        WFluidBar tankSteam = new WFluidBar(steamBG, steamFG, boiler.tankSteam).withTooltip("%d/%d mB steam");
        WBar fuelTicks0 = new WBar(fireBG, fireFG, container, 2, 3, WBar.Direction.UP);
        WBar fuelTicks1 = new WBar(fireBG, fireFG, container, 4, 5, WBar.Direction.UP);
        WBar fuelTicks2 = new WBar(fireBG, fireFG, container, 6, 7, WBar.Direction.UP);
        WBar progressTicks = new WBar(arrowBG, arrowFG, container, 0, 1, WBar.Direction.RIGHT);
        panel.add(slotsFuel, 9, 64);
        panel.add(playerInv, 0, 87);
        panel.add(tankWater, 11, 11, 50, 32);
        panel.add(tankSteam, 112, 23, 18, 48);
        panel.add(fuelTicks0, 11, 46, 14, 14);
        panel.add(fuelTicks1, 29, 46, 14, 14);
        panel.add(fuelTicks2, 47, 46, 14, 14);
        panel.add(progressTicks, 73, 37, 24, 17);
    }
}