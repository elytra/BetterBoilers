package com.elytradev.betterboilers.block.fluids;

import com.elytradev.betterboilers.BetterBoilers;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockSteam extends BlockFluidClassic {
    protected String name;

    public BlockSteam(Fluid fluid, String name) {
        super(fluid, Material.WATER);
        this.quantaPerBlock= 8;
        this.quantaPerBlockFloat = 8f;
        this.tickRate = 7;
        this.temperature = 373;

        this.name = name;

        setUnlocalizedName(BetterBoilers.modId + ".fluid." + name);
        setRegistryName(name);
    }
}
