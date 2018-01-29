package com.elytradev.betterboilers.block.turbine;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineGasket;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockTurbineGasket extends BlockTileEntity<TileEntityTurbineGasket> implements ITurbineBlock {

    protected String name;

    public BlockTurbineGasket() {
        super(Material.IRON, "turbine_gasket");

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityTurbineGasket> getTileEntityClass() {
        return TileEntityTurbineGasket.class;
    }

    @Override
    public TileEntityTurbineGasket createTileEntity(World world, IBlockState state) {
        return new TileEntityTurbineGasket();
    }
}
