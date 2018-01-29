package com.elytradev.betterboilers.block.turbine;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.BlockTileEntity;
import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineController;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTurbineController extends BlockTileEntity<TileEntityTurbineController> implements ITurbineBlock {

    protected String name;
    public static PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockTurbineController() {
        super(Material.ROCK, "turbine_controller");
//        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false));

        setCreativeTab(BetterBoilers.creativeTab);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, ACTIVE);
    }

    public int getMetaFromState(IBlockState state){
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    public IBlockState getStateFromMeta(int meta){
        return getDefaultState().withProperty(ACTIVE, meta == 1);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !player.isSneaking()) {
            player.openGui(BetterBoilers.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public Class<TileEntityTurbineController> getTileEntityClass() {
        return TileEntityTurbineController.class;
    }

    @Override
    public TileEntityTurbineController createTileEntity(World world, IBlockState state) {
        return new TileEntityTurbineController();
    }
}