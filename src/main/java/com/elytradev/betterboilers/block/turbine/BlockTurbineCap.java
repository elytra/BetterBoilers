package com.elytradev.betterboilers.block.turbine;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineCap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockTurbineCap extends BlockTileEntity<TileEntityTurbineCap> implements ITurbineBlock {

    protected String name;

    public BlockTurbineCap() {
        super(Material.IRON, "cap");

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityTurbineCap> getTileEntityClass() {
        return TileEntityTurbineCap.class;
    }

    @Override
    public TileEntityTurbineCap createTileEntity(World world, IBlockState state) {
        return new TileEntityTurbineCap();
    }
}
