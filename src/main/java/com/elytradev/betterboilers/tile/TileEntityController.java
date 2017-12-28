package com.elytradev.betterboilers.tile;

import com.elytradev.betterboilers.BBLog;
import com.elytradev.betterboilers.block.IBoilerBlock;
import com.elytradev.betterboilers.block.ModBlocks;
import com.elytradev.concrete.inventory.ConcreteFluidTank;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

public class TileEntityController extends TileEntity implements ITickable{

    private int totalScanned = 0;
    public boolean error = false;
    public String errorReason;
    public ConcreteFluidTank tankWater;
    public ConcreteFluidTank tankSteam;
    private int boilerBlockCount = 0;
    private int fireboxBlockCount = 0;
    public static final int RESCAN_TIME = 100;
    private int currentScanTime = 0;

    private static final int MAXIMUM_BLOCKS_PER_MULTIBLOCK = 1000;

    public TileEntityController() {
        this.tankWater = new ConcreteFluidTank(1000).withFillValidator((it)->(it.getFluid() == ModBlocks.FLUID_STEAM));
        this.tankSteam = new ConcreteFluidTank(500).withFillValidator((it)->false);
        tankWater.listen(this::markDirty);
        tankSteam.listen(this::markDirty);
    }

    public void update() {
        if (currentScanTime >= RESCAN_TIME) {
            BiPredicate<World, BlockPos> predicate = (world,pos)->world.getBlockState(pos).getBlock() instanceof IBoilerBlock;
            scanNetwork(predicate);
            currentScanTime = 0;
        }
        currentScanTime++;
    }

    public void scanNetwork(BiPredicate<World, BlockPos> predicate) {
        if (!hasWorld()) return;
        if (world.isRemote) return;
        Set<BlockPos> seen = new HashSet<>();
        List<BlockPos> members = new ArrayList<>();
        List<BlockPos> queue = new ArrayList<>();
        queue.add(getPos());
        boolean foundOtherController = false;
		/*//This code used to wipe existing registrations so that an orphaned valve doesn't continue to claim this controller
		for (BlockPos pos : networkMemberLocations) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntityBoilerPart) {
				((TileEntityBoilerPart)te).setController(null);
			}
		}*/

        int totalScanned = 0;

        int itr = 0;
        while (!queue.isEmpty()) {
            if (itr > MAXIMUM_BLOCKS_PER_MULTIBLOCK) {
                setControllerStatus(true, "multiblock too big");
                return;
            }
            BlockPos pos = queue.remove(0);
            seen.add(pos);
            if (predicate.test(world, pos)) {
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

        int minY = 255;
        for(BlockPos pos : members) minY = Math.min(pos.getY(), minY);
        if (this.pos.getY() != minY) {
            setControllerStatus(true, "misplaced controller");
            return;
        }

        for (BlockPos pos : members) {

            TileEntity te = world.getTileEntity(pos);
            if(world.getBlockState(pos).getBlock()==ModBlocks.CONTROLLER) {
                if (pos != this.getPos()) {
                    setControllerStatus(true, "multiple controllers");
                    return;
                }
            }
            if(world.getBlockState(pos).getBlock()==ModBlocks.BOILER || world.getBlockState(pos).getBlock()== ModBlocks.VENT || world.getBlockState(pos).getBlock()==ModBlocks.VALVE) {
                boilerBlockCount++;
                if (pos.getY() == minY) {
                    setControllerStatus(true, "misplaced boiler block, valve or vent");
                    return;
                }
            }
            if(world.getBlockState(pos).getBlock()==ModBlocks.FIREBOX || world.getBlockState(pos).getBlock()== ModBlocks.HATCH) {
                fireboxBlockCount++;
                if (pos.getY() != minY) {
                    setControllerStatus(true, "misplaced firebox block or hatch");
                    return;
                }
            }
            if (te != null && te instanceof TileEntityBoilerPart) {
                ((TileEntityBoilerPart)te).setController(this);
            }
        }
        totalScanned = itr;
        setControllerStatus(false, "no issues");
    }

    public void setControllerStatus(boolean error, String status) {
        BBLog.info(error + ", " + status);
    }

}