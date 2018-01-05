package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import net.minecraft.block.material.Material;

public class FireboxBlock extends BlockBase implements IBoilerBlock {

    protected String name;

    public FireboxBlock(Material material, String name) {
        super(material, name);

        setCreativeTab(BetterBoilers.creativeTab);
    }
}
