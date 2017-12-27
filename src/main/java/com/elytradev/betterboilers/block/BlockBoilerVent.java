package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.tile.TileEntityBoilerVent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockBoilerVent extends BlockTileEntity<TileEntityBoilerVent> implements IBoilerBlock {

    protected String name;

    public BlockBoilerVent() {
        super(Material.IRON, "boiler_vent");

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityBoilerVent> getTileEntityClass() {
        return TileEntityBoilerVent.class;
    }

    @Override
    public TileEntityBoilerVent createTileEntity(World world, IBlockState state) {
        return new TileEntityBoilerVent();
    }
}