package com.elytradev.betterboilers.tile.boiler;

import com.elytradev.betterboilers.block.boiler.BlockBoilerController;
import com.elytradev.betterboilers.block.boiler.IBoilerBlock;
import com.elytradev.betterboilers.block.ModBlocks;
import com.elytradev.betterboilers.tile.TileEntityMultiblockController;
import com.elytradev.betterboilers.util.BBConfig;
import com.elytradev.betterboilers.util.FluidAccess;
import com.elytradev.betterboilers.util.ReadableDoubleTank;
import com.elytradev.concrete.inventory.*;
import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;

public class TileEntityBoilerController extends TileEntityMultiblockController implements ITickable, IContainerInventoryHolder, IBoilerPart {

    public ConcreteFluidTank tankWater;
    public ConcreteFluidTank tankSteam;
    private ReadableDoubleTank cap;
    public ConcreteItemStorage inv;
    private int boilerBlockCount = 0;
    private int fireboxBlockCount = 0;
    public int pumpCount = 0;
    private static final int RESCAN_TIME = 100;
    private int currentScanTime = 100;

    private int currentProcessTime;
    private static final int PROCESS_LENGTH = BBConfig.ticksToBoil;
    private int[] currentFuelTime = new int[3];
    private int[] maxFuelTime = new int[3];
    private int fuelThisTick = 0;

    protected int getMaxBlocksPerMultiblock() { return BBConfig.defaultMaxMultiblock; }

    public TileEntityBoilerController getController() {
        return this;
    }

    public void setController(TileEntityBoilerController controller) {
    }

    public TileEntityBoilerController() {
        this.inv = new ConcreteItemStorage(3).withValidators(Validators.FURNACE_FUELS,
                Validators.FURNACE_FUELS,
                Validators.FURNACE_FUELS)
                .withName(ModBlocks.BOILER_CONTROLLER.getUnlocalizedName() + ".name");
        this.tankWater = new ConcreteFluidTank(1000).withFillValidator((it)->(it.getFluid() == FluidRegistry.WATER));
        this.tankSteam = new ConcreteFluidTank(500).withFillValidator((it)->false);
        this.cap = new ReadableDoubleTank(tankWater, tankSteam);
        tankWater.listen(this::markDirty);
        tankSteam.listen(this::markDirty);
        inv.listen(this::markDirty);
    }

    public void update() {
        if (currentScanTime >= RESCAN_TIME) {
            BiPredicate<World, BlockPos> members = (world,pos)->world.getBlockState(pos).getBlock() instanceof IBoilerBlock;
            scanNetwork(members, this::isValid);
            currentScanTime = 0;
        }
        currentScanTime++;

        if (!world.isRemote) {
            if (pumpCount == 0) {
                if (canProcessFluid()) {
                    if (consumeFuel(0)) currentProcessTime += fireboxBlockCount;
                    if (consumeFuel(1)) currentProcessTime += fireboxBlockCount;
                    if (consumeFuel(2)) currentProcessTime += fireboxBlockCount;
                    if (tankWater.getFluidAmount() == 0) currentProcessTime = 0;
                    if (currentProcessTime >= PROCESS_LENGTH) {
                        tankWater.drain(2 * BBConfig.steamPerBoil, true);
                        tankSteam.fill(new FluidStack(ModBlocks.FLUID_STEAM, BBConfig.steamPerBoil), true);
                        currentProcessTime -= PROCESS_LENGTH;
                    }
                }
            } else {
                if (canPumpFluid()) {
                    fuelThisTick = 0;
                    if (consumeFuel(0)) fuelThisTick++;
                    if (consumeFuel(1)) fuelThisTick++;
                    if (consumeFuel(2)) fuelThisTick++;
                    double fuelPerTick = (double) BBConfig.steamPerBoil / (double) BBConfig.ticksToBoil;
                    int toProcess = (int) Math.ceil(fireboxBlockCount * fuelThisTick * fuelPerTick * BBConfig.pumpMultiplier); //WHY THE FUCK DOES MATH.CEIL RETURN A DOUBLE WHAT THE ACTUAL FUCK
                    tankWater.drain(2 * toProcess, true);
                    tankSteam.fill(new FluidStack(ModBlocks.FLUID_STEAM, toProcess), true);
                }
            }
        }
    }

    public boolean isValid(World world, List<BlockPos> blocks) {
        int minY = 255;
        int validBlockCount = 0;
        for(BlockPos pos : blocks) minY = Math.min(pos.getY(), minY);
        //controller must be on the bottom layer
        if (this.pos.getY() != minY) {
            status = "msg.bb.badBoilerController";
            return false;
        }

        for (BlockPos pos : blocks) {
            //cannot have more than one controller
            if (world.getTileEntity(pos) instanceof TileEntityMultiblockController) {
                if (pos != this.getPos()) {
                    status = "msg.bb.tooManyControllers";
                    return false;
                }
                validBlockCount++;
            }
            //boiler blocks (including valve, vent, and pump) must not be on bottom layer
            if (world.getBlockState(pos).getBlock() == ModBlocks.BOILER
                    || world.getBlockState(pos).getBlock() == ModBlocks.VENT
                    || world.getBlockState(pos).getBlock() == ModBlocks.VALVE
                    || world.getBlockState(pos).getBlock() == ModBlocks.PUMP) {
                if (pos.getY() == minY) {
                    status = "msg.bb.badBoiler";
                    return false;
                }
                validBlockCount++;
            }
            //firebox blocks (including hatches) must only be on bottom layer
            if (world.getBlockState(pos).getBlock() == ModBlocks.FIREBOX
                    || world.getBlockState(pos).getBlock() == ModBlocks.HATCH) {
                if (pos.getY() != minY) {
                    status = "msg.bb.badFirebox";
                    return false;
                }
                validBlockCount++;
            }
        }
        //must be at least minimum size
        if (validBlockCount < BBConfig.defaultMinMultiblock) {
            status = "msg.bb.tooSmall";
            return false;
        }
        //ok!
        return true;
    }

    @Override
    public void onAssemble(World world, List<BlockPos> blocks) {
        boilerBlockCount = 0;
        fireboxBlockCount = 0;
        pumpCount = 0;
        for (BlockPos pos : blocks) {
            if (world.getBlockState(pos).getBlock() == ModBlocks.BOILER
                    || world.getBlockState(pos).getBlock() == ModBlocks.VENT
                    || world.getBlockState(pos).getBlock() == ModBlocks.VALVE) {
                boilerBlockCount++;
            }
            if (world.getBlockState(pos).getBlock() == ModBlocks.FIREBOX
                    || world.getBlockState(pos).getBlock() == ModBlocks.HATCH
                    || world.getBlockState(pos).getBlock() == ModBlocks.BOILER_CONTROLLER) {
                fireboxBlockCount++;
            }
            if (world.getBlockState(pos).getBlock() == ModBlocks.PUMP) {
                boilerBlockCount++;
                pumpCount++;
            }
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te instanceof TileEntityBoilerPart) {
                ((TileEntityBoilerPart)te).setController(this);
            }
        }
        tankWater.setCapacity(1000*boilerBlockCount); //TODO: might be causing voiding issues
        tankSteam.setCapacity(500*boilerBlockCount); //TODO: might be causing voiding issues
        markDirty();
    }

    @Override
    public void onDisassemble(World world, List<BlockPos> blocks) {
        for (BlockPos pos : blocks) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityBoilerPart) {
                ((TileEntityBoilerPart) te).setController(null);
            }
        }
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setInteger("BoilerCount", boilerBlockCount);
        tag.setInteger("FireboxCount", fireboxBlockCount);
        tag.setInteger("PumpCount", pumpCount);
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
        pumpCount = compound.getInteger("PumpCount");
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
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
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
        FluidStack tankDrained = tankWater.drain(2*BBConfig.steamPerBoil, false);
        int tankFilled = tankSteam.fill(new FluidStack(ModBlocks.FLUID_STEAM, BBConfig.steamPerBoil), false);
        return (tankDrained != null && tankFilled == BBConfig.steamPerBoil);
    }

    private boolean canPumpFluid() {
        // for when there are pump blocks present
        double fuelPerTick = (double) BBConfig.steamPerBoil / (double) BBConfig.ticksToBoil;
        int toProcess = (int)Math.ceil(fireboxBlockCount * 3 * fuelPerTick * BBConfig.pumpMultiplier); // mult by 3 for max number of active fuel sources at a time
        FluidStack tankDrained = tankWater.drain(2*toProcess, false);
        int tankFilled = tankSteam.fill(new FluidStack(ModBlocks.FLUID_STEAM, toProcess), false);
        return (tankDrained != null && tankFilled == toProcess);
    }

    private boolean consumeFuel(int slot) {
        if (currentFuelTime[slot] <= 0) {
            ItemStack usedFuel = inv.extractItem(slot, 1, false);
            if (!usedFuel.isEmpty()) {
                int newFuelTicks = 5 * TileEntityFurnace.getItemBurnTime(usedFuel);
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
        } else
            return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) new ValidatedItemHandlerView(inv);
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) cap;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        if (oldState.getBlock()==newState.getBlock()) return false;
        else return super.shouldRefresh(world, pos, oldState, newState);
    }

    public void setControllerStatus(ControllerStatus state, String status) {
        //TODO: potential candidate for inventory-clobbering
        errorReason = new TextComponentTranslation(status);
        if (state == ControllerStatus.ERRORED) {
            world.setBlockState(this.getPos(), ModBlocks.BOILER_CONTROLLER.getDefaultState().withProperty(BlockBoilerController.ACTIVE, false));
        } else {
            world.setBlockState(this.getPos(), ModBlocks.BOILER_CONTROLLER.getDefaultState().withProperty(BlockBoilerController.ACTIVE, true));
        }
    }

}
