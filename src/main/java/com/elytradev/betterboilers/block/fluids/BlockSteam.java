package com.elytradev.betterboilers.block.fluids;

import com.elytradev.betterboilers.BetterBoilers;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockSteam extends BlockFluidClassic {
    protected String name;

    //This needs to be a fluid, a gas and an energy type. Will move this out to the API for compat abstraction later - Gaiyamato
    public BlockSteam(Fluid fluid, String name) {
        super(fluid, Material.WATER);
        this.quantaPerBlock= 8;
        this.quantaPerBlockFloat = 8f;
        this.tickRate = 3;
        this.temperature = 373;
        this.density = -5;

        this.name = name;

        setUnlocalizedName(BetterBoilers.modId + ".fluid." + name);
        setRegistryName(name);
    }
}
