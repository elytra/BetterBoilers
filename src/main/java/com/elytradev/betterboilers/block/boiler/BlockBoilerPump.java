package com.elytradev.betterboilers.block.boiler;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerPump;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BlockBoilerPump extends BlockTileEntity<TileEntityBoilerPump> implements IBoilerBlock {

    protected String name;
    public static PropertyBool IS_BRASS= PropertyBool.create("is_brass");

    public BlockBoilerPump() {
        super(Material.IRON, "boiler_pump");
        this.setDefaultState(blockState.getBaseState().withProperty(IS_BRASS, true));

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public Class<TileEntityBoilerPump> getTileEntityClass() {
        return TileEntityBoilerPump.class;
    }

    @Override
    public TileEntityBoilerPump createTileEntity(World world, IBlockState state) {
        return new TileEntityBoilerPump();
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
        boolean hasBrass = !OreDictionary.getOres("plateBrass").isEmpty() || !OreDictionary.getOres("ingotBrass").isEmpty();
        return this.getDefaultState().withProperty(IS_BRASS, hasBrass);
    }
}