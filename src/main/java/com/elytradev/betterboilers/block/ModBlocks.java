package com.elytradev.betterboilers.block;

import com.elytradev.betterboilers.block.fluids.BlockSteam;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    //public static BlockDistiller distiller = new BlockDistiller().setCreativeTab(Opaline.creativeTab);

    public static Fluid fluidSteam = new Fluid("steam",
            new ResourceLocation("steam", "blocks/fluids/steam_still"),
            new ResourceLocation("steam", "blocks/fluids/steam_flowing"))
            .setDensity(1) //the density of normal water
            .setTemperature(373) //100ºF
            .setRarity(EnumRarity.UNCOMMON);

    public static IBlockBase[] allBlocks = {
            //distiller
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
