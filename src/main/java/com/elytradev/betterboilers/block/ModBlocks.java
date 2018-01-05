package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.block.fluids.BlockSteam;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final BoilerBlock BOILER = new BoilerBlock(Material.IRON, "boiler");
    public static final BlockBoilerValve VALVE = new BlockBoilerValve();
    public static final BlockBoilerVent VENT = new BlockBoilerVent();
    public static final BlockBoilerPump PUMP = new BlockBoilerPump();
    public static final FireboxBlock FIREBOX = new FireboxBlock(Material.ROCK, "firebox");
    public static final BlockFireboxHatch HATCH = new BlockFireboxHatch();
    public static final BlockController CONTROLLER = new BlockController();

    public static final Fluid FLUID_STEAM = new Fluid("steam",
            new ResourceLocation("betterboilers", "blocks/fluids/steam_still"),
            new ResourceLocation("betterboilers", "blocks/fluids/steam_flowing"))
            .setDensity(-5) //lighter than air
            .setTemperature(373) //100ºF
            .setGaseous(true);

    public static IBlockBase[] allBlocks = {
            BOILER, VALVE, VENT, PUMP, FIREBOX, HATCH, CONTROLLER
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

        GameRegistry.registerTileEntity(VALVE.getTileEntityClass(), VALVE.getRegistryName().toString());
        GameRegistry.registerTileEntity(VENT.getTileEntityClass(), VENT.getRegistryName().toString());
        GameRegistry.registerTileEntity(PUMP.getTileEntityClass(), PUMP.getRegistryName().toString());
        GameRegistry.registerTileEntity(HATCH.getTileEntityClass(), HATCH.getRegistryName().toString());
        GameRegistry.registerTileEntity(CONTROLLER.getTileEntityClass(), CONTROLLER.getRegistryName().toString());

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
