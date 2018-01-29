package com.elytradev.betterboilers.block.turbine;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineRotor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockTurbineRotor extends BlockTileEntity<TileEntityTurbineRotor> implements ITurbineBlock {

    protected String name;

    public BlockTurbineRotor() {
        super(Material.IRON, "turbine_rotor");

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityTurbineRotor> getTileEntityClass() {
        return TileEntityTurbineRotor.class;
    }

    @Override
    public TileEntityTurbineRotor createTileEntity(World world, IBlockState state) {
        return new TileEntityTurbineRotor();
    }
}
