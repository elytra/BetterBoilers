package com.elytradev.betterboilers.block.turbine;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.turbine.TileEntityTurbinePowerTap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockTurbinePowerTap extends BlockTileEntity<TileEntityTurbinePowerTap> implements ITurbineBlock {

    protected String name;

    public BlockTurbinePowerTap() {
        super(Material.IRON, "power_tap");

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityTurbinePowerTap> getTileEntityClass() {
        return TileEntityTurbinePowerTap.class;
    }

    @Override
    public TileEntityTurbinePowerTap createTileEntity(World world, IBlockState state) {
        return new TileEntityTurbinePowerTap();
    }
}
