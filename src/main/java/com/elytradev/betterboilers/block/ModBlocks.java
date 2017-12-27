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

    public static BoilerBlock boiler = new BoilerBlock(Material.IRON, "boiler");
    public static BlockBoilerValve valve = new BlockBoilerValve();
    public static BlockBoilerVent vent = new BlockBoilerVent();
    public static BoilerBlock firebox = new BoilerBlock(Material.ROCK, "firebox");
    public static BlockFireboxHatch hatch = new BlockFireboxHatch();
    public static BlockController controller = new BlockController();


    public static Fluid fluidSteam = new Fluid("steam",
            new ResourceLocation("betterboilers", "blocks/fluids/steam_still"),
            new ResourceLocation("betterboilers", "blocks/fluids/steam_flowing"))
            .setDensity(-5) //lighter than air
            .setTemperature(373) //100ºF
            .setGaseous(true);

    public static IBlockBase[] allBlocks = {
            boiler, valve, vent, firebox, hatch, controller
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.toBlock());
        }

        FluidRegistry.registerFluid(ModBlocks.fluidSteam);
        BlockSteam steam = new BlockSteam(fluidSteam, "fluid_steam");
        registry.register(steam);
        fluidSteam.setBlock(steam);
        FluidRegistry.addBucketForFluid(ModBlocks.fluidSteam);

        GameRegistry.registerTileEntity(valve.getTileEntityClass(), valve.getRegistryName().toString());
        GameRegistry.registerTileEntity(vent.getTileEntityClass(), vent.getRegistryName().toString());
        GameRegistry.registerTileEntity(hatch.getTileEntityClass(), hatch.getRegistryName().toString());
        GameRegistry.registerTileEntity(controller.getTileEntityClass(), controller.getRegistryName().toString());

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
