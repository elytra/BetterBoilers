package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.tile.TileEntityBoilerValve;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockBoilerValve extends BlockTileEntity<TileEntityBoilerValve> implements IBoilerBlock {

    protected String name;

    public BlockBoilerValve() {
        super(Material.IRON, "boiler_valve");

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityBoilerValve> getTileEntityClass() {
        return TileEntityBoilerValve.class;
    }

    @Override
    public TileEntityBoilerValve createTileEntity(World world, IBlockState state) {
        return new TileEntityBoilerValve();
    }
}