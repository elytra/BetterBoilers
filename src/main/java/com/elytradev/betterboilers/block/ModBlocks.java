package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.block.boiler.*;
import com.elytradev.betterboilers.block.fluids.BlockSteam;
import com.elytradev.betterboilers.block.turbine.*;
import com.elytradev.betterboilers.tile.boiler.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final BlockBoiler BOILER = new BlockBoiler(Material.IRON, "boiler");
    public static final BlockBoilerValve VALVE = new BlockBoilerValve();
    public static final BlockBoilerVent VENT = new BlockBoilerVent();
    public static final BlockBoilerPump PUMP = new BlockBoilerPump();
    public static final BlockFirebox FIREBOX = new BlockFirebox(Material.ROCK, "firebox");
    public static final BlockFireboxHatch HATCH = new BlockFireboxHatch();
    public static final BlockBoilerController BOILER_CONTROLLER = new BlockBoilerController();
    public static final BlockTurbineChamber CHAMBER = new BlockTurbineChamber();
    public static final BlockTurbineCap CAP = new BlockTurbineCap();
    public static final BlockTurbineRotor ROTOR = new BlockTurbineRotor();
    public static final BlockTurbineGasket GASKET = new BlockTurbineGasket();
    public static final BlockTurbinePressureValve PRESSURE_VALVE = new BlockTurbinePressureValve();
    public static final BlockTurbinePowerTap POWER_TAP = new BlockTurbinePowerTap();
    public static final BlockTurbineController TURBINE_CONTROLLER = new BlockTurbineController();

    public static final Fluid FLUID_STEAM = new Fluid("steam",
            new ResourceLocation("betterboilers", "blocks/fluids/steam_still"),
            new ResourceLocation("betterboilers", "blocks/fluids/steam_flowing"))
            .setDensity(-5) //lighter than air
            .setTemperature(373) //100ºF
            .setGaseous(true);

    public static IBlockBase[] allBlocks = {
            BOILER, VALVE, VENT, PUMP, FIREBOX, HATCH, BOILER_CONTROLLER, CHAMBER, CAP, ROTOR, GASKET, PRESSURE_VALVE, POWER_TAP, TURBINE_CONTROLLER
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.toBlock());
        }

        FluidRegistry.registerFluid(ModBlocks.FLUID_STEAM);
        BlockSteam steam = new BlockSteam(FLUID_STEAM, "fluid_steam");
        registry.register(steam);
        FLUID_STEAM.setBlock(steam);
        FluidRegistry.addBucketForFluid(ModBlocks.FLUID_STEAM);

        GameRegistry.registerTileEntity(BOILER.getTileEntityClass(), BOILER.getRegistryName().toString());
        GameRegistry.registerTileEntity(VALVE.getTileEntityClass(), VALVE.getRegistryName().toString());
        GameRegistry.registerTileEntity(VENT.getTileEntityClass(), VENT.getRegistryName().toString());
        GameRegistry.registerTileEntity(PUMP.getTileEntityClass(), PUMP.getRegistryName().toString());
        GameRegistry.registerTileEntity(FIREBOX.getTileEntityClass(), FIREBOX.getRegistryName().toString());
        GameRegistry.registerTileEntity(HATCH.getTileEntityClass(), HATCH.getRegistryName().toString());
        GameRegistry.registerTileEntity(BOILER_CONTROLLER.getTileEntityClass(), BOILER_CONTROLLER.getRegistryName().toString());
        GameRegistry.registerTileEntity(CHAMBER.getTileEntityClass(), CHAMBER.getRegistryName().toString());
        GameRegistry.registerTileEntity(CAP.getTileEntityClass(), CAP.getRegistryName().toString());
        GameRegistry.registerTileEntity(ROTOR.getTileEntityClass(), ROTOR.getRegistryName().toString());
        GameRegistry.registerTileEntity(GASKET.getTileEntityClass(), GASKET.getRegistryName().toString());
        GameRegistry.registerTileEntity(PRESSURE_VALVE.getTileEntityClass(), PRESSURE_VALVE.getRegistryName().toString());
        GameRegistry.registerTileEntity(POWER_TAP.getTileEntityClass(), POWER_TAP.getRegistryName().toString());
        GameRegistry.registerTileEntity(TURBINE_CONTROLLER.getTileEntityClass(), TURBINE_CONTROLLER.getRegistryName().toString());

    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.createItemBlock());
        }

    }

    public static void registerModels() {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            block.registerItemModel(Item.getItemFromBlock(block.toBlock()));
        }
    }
}
