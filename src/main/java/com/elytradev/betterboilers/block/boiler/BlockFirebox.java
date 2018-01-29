package com.elytradev.betterboilers.block.boiler;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.boiler.TileEntityFireboxBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockFirebox extends BlockTileEntity<TileEntityFireboxBlock> implements IBoilerBlock {

    protected String name;

    public BlockFirebox(Material material, String name) {
        super(material, name);

        setCreativeTab(BetterBoilers.creativeTab);
    }
    @Override
    public Class<TileEntityFireboxBlock> getTileEntityClass() {
        return TileEntityFireboxBlock.class;
    }

    @Override
    public TileEntityFireboxBlock createTileEntity(World world, IBlockState state) {
        return new TileEntityFireboxBlock();
    }
}
