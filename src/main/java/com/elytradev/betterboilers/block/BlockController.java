package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.tile.TileEntityController;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockController extends BlockTileEntity<TileEntityController> implements IBoilerBlock {

    protected String name;

    public BlockController() {
        super(Material.ROCK, "controller");

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityController> getTileEntityClass() {
        return TileEntityController.class;
    }

    @Override
    public TileEntityController createTileEntity(World world, IBlockState state) {
        return new TileEntityController();
    }
}