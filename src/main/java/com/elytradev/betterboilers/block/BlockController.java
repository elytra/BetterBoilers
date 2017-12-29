package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.tile.TileEntityController;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockController extends BlockTileEntity<TileEntityController> implements IBoilerBlock {

    protected String name;
//    public static PropertyBool ACTIVE = PropertyBool.create("active");
//    public static int STATE = (1<<2);

    public BlockController() {
        super(Material.ROCK, "controller");
//        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false));

        setCreativeTab(BetterBoilers.creativeTab);
    }

//    public BlockStateContainer createBlockState(){
//        return new BlockStateContainer(this, ACTIVE);
//    }
//
//    public int getMetaFromState(IBlockState state){
//        int meta = 0;
//        if(state.getValue(ACTIVE)){meta |= STATE;}
//        return meta;
//    }
//
//    public IBlockState getStateFromMeta(int meta){
//        boolean active = (meta & STATE) != 0;
//        return blockState.getBaseState().withProperty(ACTIVE, active);
//    }
//
//    @Override
//    public void updateTick(World world, BlockPos pos, IBlockState state)
//    {
//        if(state.getValue(ACTIVE)){
//                world.setBlockState(pos, state.withProperty(ACTIVE,true));
//        }
//
//    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !player.isSneaking()) {
            player.openGui(BetterBoilers.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
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