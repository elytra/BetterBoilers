package com.elytradev.betterboilers.tile;

import com.elytradev.betterboilers.BBLog;
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

    private static final int MAXIMUM_BLOCKS_PER_MULTIBLOCK = 1000;
    private transient Set<BlockPos> networkMemberLocations = Sets.newHashSet();

    public void update() {};

    @Override
    public boolean hasController() {
        return true;
    }

    @Override
    public TileEntityController getController() {
        return this;
    }

    @Override
    public void setController(TileEntityController controller) {}

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
        boolean error = false;

        int itr = 0;
        while (!queue.isEmpty()) {
            if (itr > MAXIMUM_BLOCKS_PER_MULTIBLOCK) {
                error = true;
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

        for (BlockPos pos : members) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te instanceof TileEntityBoilerPart) {
                ((TileEntityBoilerPart)te).setController(this);
            }
        }
        totalScanned = itr;
    }

    public void onNetworkPatched(TileEntityBoilerPart part) {
        if (totalScanned == 0) return;
        if (networkMemberLocations.add(part.getPos())) {
            totalScanned++;
            if (totalScanned > 100) {
                error = true;
                errorReason = "network_too_big";
            }
        }
    }

    public boolean knowsOfMemberAt(BlockPos pos) {
        return networkMemberLocations.contains(pos);
    }

    @Override
    public BlockPos getPosition() {
        return getPos();
    }

    @Override
    public double getX() {
        return getPos().getX()+0.5;
    }

    @Override
    public double getY() {
        return getPos().getY()+0.5;
    }

    @Override
    public double getZ() {
        return getPos().getZ()+0.5;
    }

}
