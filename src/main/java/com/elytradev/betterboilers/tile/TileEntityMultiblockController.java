package com.elytradev.betterboilers.tile;

import com.elytradev.betterboilers.block.boiler.BlockBoilerController;
import com.elytradev.betterboilers.block.ModBlocks;
import com.elytradev.betterboilers.block.turbine.BlockTurbineController;
import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerController;
import com.elytradev.betterboilers.tile.turbine.TileEntityTurbineController;
import com.elytradev.betterboilers.util.BBConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

public abstract class TileEntityMultiblockController extends TileEntity {

    protected int getMaxBlocksPerMultiblock() { return BBConfig.defaultMaxMultiblock; }
    protected String status;
    public TextComponentTranslation errorReason;
    public enum ControllerStatus { ACTIVE, ERRORED }

    public void scanNetwork(BiPredicate<World, BlockPos> membership, BiPredicate<World, List<BlockPos>> validator) {
        if (!hasWorld()) return;
        if (world.isRemote) return;
        Set<BlockPos> seen = new HashSet<>();
        List<BlockPos> members = new ArrayList<>();
        List<BlockPos> queue = new ArrayList<>();
        queue.add(getPos());

        onDisassemble(world, members);

        int itr = 0;
        while (!queue.isEmpty()) {
            if (itr > getMaxBlocksPerMultiblock()) {
                if (world.getBlockState(this.getPos()).getBlock() == ModBlocks.BOILER_CONTROLLER) {
                    setControllerStatus(TileEntityBoilerController.ControllerStatus.ERRORED, "msg.bb.tooBig");
                } else {
                    setControllerStatus(TileEntityTurbineController.ControllerStatus.ERRORED, "msg.bb.tooBig");
                }
                return;
            }
            BlockPos pos = queue.remove(0);
            seen.add(pos);
            if (membership.test(world, pos)) {
                //TODO: Replace with generalized neighbor function?
                for (EnumFacing ef : EnumFacing.VALUES) {
                    BlockPos p = pos.offset(ef);
                    if (seen.contains(p)) continue;
                    seen.add(p);
                    queue.add(p);
                }

                if (!members.contains(pos)) {
                    //TODO: This is where we would do early checks, like "is this another controller?" or "is this malformed?"
                    //(world,pos)->world.getBlock(pos) instanceof IBoilerBlock
                    members.add(pos);
                }
            }
            itr++;
        }

        if (!validator.test(world, members)) {
            if (world.getBlockState(this.getPos()).getBlock() == ModBlocks.BOILER_CONTROLLER) {
                setControllerStatus(TileEntityBoilerController.ControllerStatus.ERRORED, status);
            } else {
                setControllerStatus(TileEntityTurbineController.ControllerStatus.ERRORED, status);
            }
            return;
        }

        onAssemble(world, members);
        if (world.getBlockState(this.getPos()).getBlock() == ModBlocks.BOILER_CONTROLLER) {
            setControllerStatus(TileEntityBoilerController.ControllerStatus.ACTIVE, "msg.bb.noIssue");
        } else {
            setControllerStatus(TileEntityTurbineController.ControllerStatus.ACTIVE, "msg.bb.noIssue");
        }
    }

    public void setControllerStatus( ControllerStatus state, String status) {
        errorReason = new TextComponentTranslation(status);
        if (state == ControllerStatus.ERRORED) {
            if (world.getBlockState(this.getPos()).getBlock() == ModBlocks.BOILER_CONTROLLER) {
                world.setBlockState(this.getPos(), ModBlocks.BOILER_CONTROLLER.getDefaultState().withProperty(BlockBoilerController.ACTIVE, false));
            } else {
                world.setBlockState(this.getPos(), ModBlocks.TURBINE_CONTROLLER.getDefaultState().withProperty(BlockTurbineController.ACTIVE, false));
            }
        } else {
            if (world.getBlockState(this.getPos()).getBlock() == ModBlocks.BOILER_CONTROLLER) {
                world.setBlockState(this.getPos(), ModBlocks.BOILER_CONTROLLER.getDefaultState().withProperty(BlockBoilerController.ACTIVE, true));
            } else {
                world.setBlockState(this.getPos(), ModBlocks.TURBINE_CONTROLLER.getDefaultState().withProperty(BlockTurbineController.ACTIVE, true));
            }
        }
    }

    public abstract void onAssemble(World world, List<BlockPos> blocks);

    public abstract void onDisassemble(World world, List<BlockPos> blocks);

}
