package com.elytradev.betterboilers.block.turbine;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineChamber;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockTurbineChamber extends BlockTileEntity<TileEntityTurbineChamber> implements ITurbineBlock {

    protected String name;

    public BlockTurbineChamber() {
        super(Material.IRON, "chamber");

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityTurbineChamber> getTileEntityClass() {
        return TileEntityTurbineChamber.class;
    }

    @Override
    public TileEntityTurbineChamber createTileEntity(World world, IBlockState state) {
        return new TileEntityTurbineChamber();
    }
}
