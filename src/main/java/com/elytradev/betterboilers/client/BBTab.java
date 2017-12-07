package com.elytradev.betterboilers.client;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

public class BBTab extends CreativeTabs {
    public BBTab() {
        super(BetterBoilers.modId);
        //setBackgroundImageName("betterboilers.png");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.logoFake);
    }
}
