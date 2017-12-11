package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.BetterBoilers;
import com.elytradev.betterboilers.block.fluids.BlockSteam;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static BlockBase boiler = new BlockBase(Material.IRON, "boiler").setCreativeTab(BetterBoilers.creativeTab);
    public static BlockBase valve = new BlockBase(Material.IRON, "boiler_valve").setCreativeTab(BetterBoilers.creativeTab);
    public static BlockBase vent = new BlockBase(Material.IRON, "boiler_vent").setCreativeTab(BetterBoilers.creativeTab);
    public static BlockBase firebox = new BlockBase(Material.ROCK, "firebox").setCreativeTab(BetterBoilers.creativeTab);
    public static BlockBase controller = new BlockBase(Material.ROCK, "controller").setCreativeTab(BetterBoilers.creativeTab);


    public static Fluid fluidSteam = new Fluid("steam",
            new ResourceLocation("betterboilers", "blocks/fluids/steam_still"),
            new ResourceLocation("betterboilers", "blocks/fluids/steam_flowing"))
            .setDensity(-5) //lighter than air
            .setTemperature(373) //100ºF
            .setGaseous(true);

    public static IBlockBase[] allBlocks = {
            boiler, valve, vent, firebox, controller
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

        //GameRegistry.registerTileEntity(distiller.getTileEntityClass(), distiller.getRegistryName().toString());

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
