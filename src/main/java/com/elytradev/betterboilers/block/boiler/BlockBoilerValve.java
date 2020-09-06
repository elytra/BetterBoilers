package com.elytradev.betterboilers.block.boiler;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerValve;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

//Needs to become a ConnectedBlock so that pipes etc. visually connect to it.
public class BlockBoilerValve extends BlockTileEntity<TileEntityBoilerValve> implements IBoilerBlock {

    protected String name;
    public static PropertyBool IS_BRASS= PropertyBool.create("is_brass");

    public BlockBoilerValve() {
        super(Material.IRON, "boiler_valve");
        this.setDefaultState(blockState.getBaseState().withProperty(IS_BRASS, true));

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

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, IS_BRASS);
    }

    public int getMetaFromState(IBlockState state){
        return state.getValue(IS_BRASS) ? 1 : 0;
    }

    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(IS_BRASS, meta == 1);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(IS_BRASS, BetterBoilers.hasBrass);
    }
}