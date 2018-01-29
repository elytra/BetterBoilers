package com.elytradev.betterboilers.item;

import com.elytradev.betterboilers.tile.boiler.TileEntityBoilerController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemInspector extends ItemBase {

    public ItemInspector() {
        super("inspector");
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (player.isSneaking()) {
            if (te instanceof TileEntityBoilerController) {
                TileEntityBoilerController controller = (TileEntityBoilerController)te;
                if (!world.isRemote) {
                    player.sendMessage(controller.errorReason);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
}
