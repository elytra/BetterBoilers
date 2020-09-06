package com.elytradev.betterboilers.container;

import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerController;
import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.*;
import com.elytradev.betterboilers.BetterBoilers;
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
    private ResourceLocation fireABG = new ResourceLocation(BetterBoilers.modId,"textures/gui/single_fire_bg.png");
    private ResourceLocation fireAFG = new ResourceLocation(BetterBoilers.modId,"textures/gui/single_fire_fg.png");
    private static ResourceLocation[] pumpAnim = new ResourceLocation[13];
    static {
        for (int i = 0; i <= 12; i++) {
            pumpAnim[i] = new ResourceLocation(BetterBoilers.modId, "textures/gui/pump/" + i + ".png");
        }
    }

    public BoilerContainer(IInventory player, IInventory container, TileEntityBoilerController boiler) {
        super(player, container);
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        //Need to make this more configurable
        WItemSlot slotsFuel = WItemSlot.of(container, 0, 3, 1);
        WPanel playerInv = this.createPlayerInventoryPanel();
        WFluidBar tankWater = new WFluidBar(waterBG, waterFG, boiler.tankWater).withTooltip("%d/%d mB water");
        WFluidBar tankSteam = new WFluidBar(steamBG, steamFG, boiler.tankSteam).withTooltip("%d/%d mB steam");
        WBar fuelTicks0 = new WBar(fireBG, fireFG, container, 2, 3, WBar.Direction.UP);
        WBar fuelTicks1 = new WBar(fireBG, fireFG, container, 4, 5, WBar.Direction.UP);
        WBar fuelTicks2 = new WBar(fireBG, fireFG, container, 6, 7, WBar.Direction.UP);
        WBar progressTicks = new WBar(arrowBG, arrowFG, container, 0, 1, WBar.Direction.RIGHT);
        WImage waterBar = new WImage(new ResourceLocation(BetterBoilers.modId, "textures/gui/water_bar.png"));
        WImage steamBar = new WImage(new ResourceLocation(BetterBoilers.modId, "textures/gui/steam_bar.png"));
        WImage waterBar1 = new WImage(new ResourceLocation(BetterBoilers.modId, "textures/gui/water_bar.png"));
        WImage steamBar1 = new WImage(new ResourceLocation(BetterBoilers.modId, "textures/gui/steam_bar.png"));
        WAnimation pump = new WAnimation( 150, pumpAnim);
        panel.add(slotsFuel, 9, 64);
        panel.add(playerInv, 0, 87);
        panel.add(tankWater, 11, 15, 50, 24);
        panel.add(waterBar, 11, 11, 50, 4);
        panel.add(waterBar1, 11, 39, 50, 4);
        panel.add(tankSteam, 112, 27, 18, 40);
        panel.add(steamBar, 112, 23, 18, 4);
        panel.add(steamBar1, 112, 67, 18, 4);
        panel.add(fuelTicks0, 11, 46, 14, 14);
        panel.add(fuelTicks1, 29, 46, 14, 14);
        panel.add(fuelTicks2, 47, 46, 14, 14);

        if (boiler.pumpCount == 0) {
            panel.add(progressTicks, 75, 37, 24, 17);
        } else {
            panel.add(pump, 75, 37, 24, 17);
        }
    }
}