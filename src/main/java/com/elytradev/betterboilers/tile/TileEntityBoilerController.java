package com.elytradev.betterboilers.tile;

import com.elytradev.betterboilers.block.IBoilerBlock;
import com.elytradev.betterboilers.block.ModBlocks;
import com.elytradev.concrete.inventory.*;
import com.google.common.base.Predicates;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;

public class TileEntityBoilerController extends TileEntityControllerBase implements ITickable, IContainerInventoryHolder, IBoilerPart {

    private int totalScanned = 0;
    public ConcreteFluidTank tankWater;
    public ConcreteFluidTank tankSteam;
    public ConcreteItemStorage inv;
    private int boilerBlockCount = 0;
    private int fireboxBlockCount = 0;
    private static final int RESCAN_TIME = 100;
    private int currentScanTime = 100;

    private int currentProcessTime;
    private static final int PROCESS_LENGTH = 1000;
    private int[] currentFuelTime = new int[3];
    private int[] maxFuelTime = new int[3];

    protected int getMaxBlocksPerMultiblock() { return 1000; }

    public TileEntityBoilerController getController() {
        return this;
    }

    public void setController(TileEntityBoilerController controller) {
    }

    public TileEntityBoilerController() {
        this.inv = new ConcreteItemStorage(3).withValidators(Validators.FURNACE_FUELS,
                Validators.FURNACE_FUELS,
                Validators.FURNACE_FUELS)
                .withName(ModBlocks.CONTROLLER.getUnlocalizedName() + ".name");;
        this.tankWater = new ConcreteFluidTank(1000).withFillValidator((it)->(it.getFluid() == FluidRegistry.WATER));
        this.tankSteam = new ConcreteFluidTank(500).withFillValidator((it)->false);
        tankWater.listen(this::markDirty);
        tankSteam.listen(this::markDirty);
    }

    public void update() {
        if (currentScanTime >= RESCAN_TIME) {
            BiPredicate<World, BlockPos> members = (world,pos)->world.getBlockState(pos).getBlock() instanceof IBoilerBlock;
            scanNetwork(members, this::isValid);
            currentScanTime = 0;
        }
        currentScanTime++;

        if (!world.isRemote) {
            if (canProcessFluid()) {
                if (consumeFuel(0)) currentProcessTime += fireboxBlockCount;
                if (consumeFuel(1)) currentProcessTime += fireboxBlockCount;
                if (consumeFuel(2)) currentProcessTime += fireboxBlockCount;
                if (tankWater.getFluidAmount() == 0) currentProcessTime = 0;
                if (currentProcessTime >= PROCESS_LENGTH) {
                    tankWater.drain(100, true);
                    tankSteam.fill(new FluidStack(ModBlocks.FLUID_STEAM, 50), true);
                    currentProcessTime = 0;
                }
            }
        }
    }

    public boolean isValid(World world, List<BlockPos> blocks) {
        int minY = 255;
        for(BlockPos pos : blocks) minY = Math.min(pos.getY(), minY);
        if (this.pos.getY() != minY) {
            status = "msg.bb.badController";
            return false;
        }

        for (BlockPos pos : blocks) {
            if (world.getTileEntity(pos) instanceof TileEntityControllerBase) {
                if (pos != this.getPos()) {
                    status = "msg.bb.tooManyControllers";
                    return false;
                }
            }
            if (world.getBlockState(pos).getBlock() == ModBlocks.BOILER
                    || world.getBlockState(pos).getBlock() == ModBlocks.VENT
                    || world.getBlockState(pos).getBlock() == ModBlocks.VALVE) {
                boilerBlockCount++;
                if (pos.getY() == minY) {
                    status = "msg.bb.badBoiler";
                    return false;
                }
            }
            if (world.getBlockState(pos).getBlock() == ModBlocks.FIREBOX
                    || world.getBlockState(pos).getBlock() == ModBlocks.HATCH) {
                fireboxBlockCount++;
                if (pos.getY() != minY) {
                    status = "msg.bb.badFirebox";
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onAssemble(World world, List<BlockPos> blocks) {
        boilerBlockCount = 0;
        fireboxBlockCount = 0;
        for (BlockPos pos : blocks) {
            if (world.getBlockState(pos).getBlock() == ModBlocks.BOILER
                    || world.getBlockState(pos).getBlock() == ModBlocks.VENT
                    || world.getBlockState(pos).getBlock() == ModBlocks.VALVE) {
                boilerBlockCount++;
            }
            if (world.getBlockState(pos).getBlock() == ModBlocks.FIREBOX
                    || world.getBlockState(pos).getBlock() == ModBlocks.HATCH
                    || world.getBlockState(pos).getBlock() == ModBlocks.CONTROLLER) {
                fireboxBlockCount++;
            }
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te instanceof TileEntityBoilerPart) {
                ((TileEntityBoilerPart)te).setController(this);
            }
        }
        tankWater.setCapacity(1000*boilerBlockCount);
        tankSteam.setCapacity(500*boilerBlockCount);
    }

    @Override
    public void onDisassemble(World world, List<BlockPos> blocks) {
        for (BlockPos pos : blocks) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBoilerPart) {
                ((TileEntityBoilerPart) te).setController(null);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setInteger("BoilerCount", boilerBlockCount);
        tag.setInteger("FireboxCount", fireboxBlockCount);
        tag.setIntArray("CurrentFuelTime", currentFuelTime);
        tag.setIntArray("MaxFuelTime", maxFuelTime);
        tag.setTag("WaterTank", tankWater.writeToNBT(new NBTTagCompound()));
        tag.setTag("SteamTank", tankSteam.writeToNBT(new NBTTagCompound()));
        tag.setTag("Inventory", inv.serializeNBT());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        boilerBlockCount = compound.getInteger("BoilerCount");
        fireboxBlockCount = compound.getInteger("FireboxCount");
        tankWater.setCapacity(1000 * boilerBlockCount);
        tankSteam.setCapacity(500 * boilerBlockCount);
        currentFuelTime = compound.getIntArray("CurrentFuelTime");
        maxFuelTime = compound.getIntArray("MaxFuelTime");
        tankWater.readFromNBT(compound.getCompoundTag("WaterTank"));
        tankSteam.readFromNBT(compound.getCompoundTag("SteamTank"));
        inv.deserializeNBT(compound.getCompoundTag("Inventory"));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        // again, I've copy-pasted this like 12 times, should probably go into Concrete
        if (!hasWorld() || getWorld().isRemote) return;
        WorldServer ws = (WorldServer)getWorld();
        Chunk c = getWorld().getChunkFromBlockCoords(getPos());
        SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
        for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
            if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                player.connection.sendPacket(packet);
            }
        }
    }

    private boolean canProcessFluid() {
        FluidStack tankDrained = tankWater.drain(100, false);
        int tankFilled = tankSteam.fill(new FluidStack(ModBlocks.FLUID_STEAM, 50), false);
        return (tankDrained != null && tankFilled == 50);
    }

    private boolean consumeFuel(int slot) {
        if (currentFuelTime[slot] <= 0) {
            ItemStack usedFuel = inv.extractItem(slot, 1, false);
            if (!usedFuel.isEmpty()) {
                int newFuelTicks = TileEntityFurnace.getItemBurnTime(usedFuel);
                maxFuelTime[slot] = newFuelTicks;
                currentFuelTime[slot] = newFuelTicks;
            } else {
                return false;
            }
        }
        currentFuelTime[slot] -= fireboxBlockCount;
        return true;
    }

    @Override
    public IInventory getContainerInventory() {
        ValidatedInventoryView view = new ValidatedInventoryView(inv);
        if(world.isRemote) {
            return view;
        }
        else {
            return view.withField(0, () -> currentProcessTime)
                    .withField(1, () -> PROCESS_LENGTH)
                    .withField(2, () -> currentFuelTime[0])
                    .withField(3, () -> maxFuelTime[0])
                    .withField(4, () -> currentFuelTime[1])
                    .withField(5, () -> maxFuelTime[1])
                    .withField(6, () -> currentFuelTime[2])
                    .withField(7, () -> maxFuelTime[2]);

        }
    }

    public ConcreteFluidTank getTankWater() {
        return tankWater;
    }

    public ConcreteFluidTank getTankSteam() {
        return tankSteam;
    }

    public ConcreteItemStorage getInv() {
        return inv;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        } else {
            return super.hasCapability(capability, facing);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) new ValidatedItemHandlerView(inv);
        } else {
            return super.getCapability(capability, facing);
        }
    }

}