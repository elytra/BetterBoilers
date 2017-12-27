package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.tile.TileEntityFireboxHatch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockFireboxHatch extends BlockTileEntity<TileEntityFireboxHatch> implements IBoilerBlock {

    protected String name;

    public BlockFireboxHatch() {
        super(Material.ROCK, "firebox_hatch");

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityFireboxHatch> getTileEntityClass() {
        return TileEntityFireboxHatch.class;
    }

    @Override
    public TileEntityFireboxHatch createTileEntity(World world, IBlockState state) {
        return new TileEntityFireboxHatch();
    }
}
