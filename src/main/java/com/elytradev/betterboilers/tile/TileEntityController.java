package com.elytradev.betterboilers.tile;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Set;

public class TileEntityController extends TileEntity implements ITickable{

    private int totalScanned = 0;
    public boolean error = false;
    public String errorReason;

    public void scanNetwork() {
        if (!hasWorld()) return;
        if (world.isRemote) return;
        Set<BlockPos> seen = Sets.newHashSet();
        List<TileEntityNetworkMember> members = Lists.newArrayList();
        List<BlockPos> queue = Lists.newArrayList(getPos());
        boolean foundOtherController = false;

        for (BlockPos pos : networkMemberLocations) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityNetworkMember) {
                ((TileEntityNetworkMember)te).setController(null);
            }
        }

        totalScanned = 0;
        networkMemberLocations.clear();
        memberTypes.clear();

        int itr = 0;
        while (!queue.isEmpty()) {
            if (itr > 100) {
                error = true;
                errorReason = "network_too_big";
                return;
            }
            BlockPos pos = queue.remove(0);
            seen.add(pos);
            TileEntity te = getWorld().getTileEntity(pos);
            if (te instanceof TileEntityNetworkMember) {
                for (EnumFacing ef : EnumFacing.VALUES) {
                    BlockPos p = pos.offset(ef);
                    if (seen.contains(p)) continue;
                    seen.add(p);
                    if (world.getTileEntity(p) == null) {
                        continue;
                    }
                    queue.add(p);
                }
                if (te != this) {
                    if (te instanceof TileEntityController) {
                        error = true;
                        ((TileEntityController) te).error = true;
                        CLog.debug("Found other controller");
                        foundOtherController = true;
                    }
                    if (!members.contains(te)) {
                        TileEntityNetworkMember tenm = (TileEntityNetworkMember) te;
                        members.add(tenm);
                        if (te instanceof TileEntityDriveBay) {
                            driveBays.add((TileEntityDriveBay)te);
                        } else if (te instanceof TileEntityInterface) {
                            interfaces.add((TileEntityInterface)te);
                        } else if (te instanceof TileEntityMemoryBay) {
                            memoryBays.add((TileEntityMemoryBay)te);
                        } else if (te instanceof TileEntityMicrowaveBeam) {
                            beams.add((TileEntityMicrowaveBeam)te);
                        }
                        networkMemberLocations.add(pos);
                        memberTypes.add(tenm.getClass());
                        consumedPerTick += tenm.getPotentialConsumedPerTick();
                    }
                }
            }
            itr++;
        }
        if (foundOtherController) {
            error = true;
            errorReason = "multiple_controllers";
        } else {
            error = false;
            errorReason = null;
        }
        totalScanned = itr;
        CLog.debug("Found "+members.size()+" network members");
    }
}
