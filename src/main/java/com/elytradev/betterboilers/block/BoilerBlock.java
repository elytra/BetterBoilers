package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import net.minecraft.block.material.Material;

public class BoilerBlock extends BlockBase implements IBoilerBlock {

    protected String name;

    public BoilerBlock(Material material, String name) {
        super(material, name);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(BetterBoilers.creativeTab);
    }
}
