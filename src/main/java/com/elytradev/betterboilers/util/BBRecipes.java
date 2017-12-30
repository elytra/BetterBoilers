package com.elytradev.betterboilers.util;

import com.elytradev.betterboilers.block.ModBlocks;
import com.elytradev.betterboilers.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class BBRecipes {

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {

        IForgeRegistry<IRecipe> r = event.getRegistry();

        // Crafting bench recipes

        recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.BOILER, 16),
                "bbb", "bib", "bbb",
                'b', "plateBrass",
                'i', new ItemStack(Items.IRON_INGOT)
                ));
        if (OreDictionary.getOres("plateBrass").isEmpty()) {
            if (OreDictionary.getOres("ingotBrass").isEmpty()) {
                recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.BOILER, 16),
                        "bbb", "bib", "bbb",
                        'b', "ingotBrass",
                        'i', new ItemStack(Items.IRON_INGOT)
                ));
            } else {
                recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.BOILER, 16),
                        "bib", "iii", "bib",
                        'b', new ItemStack(Items.GOLD_INGOT),
                        'i', new ItemStack(Items.IRON_INGOT)
                ));
            }
        }
        recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.VALVE, 1),
                " u ", "ibi", " u ",
                'b', new ItemStack(ModBlocks.BOILER),
                'i', new ItemStack(Items.IRON_INGOT),
                'u', new ItemStack(Items.BUCKET)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.VENT, 1),
                " u ", "ibi", " u ",
                'b', new ItemStack(ModBlocks.BOILER),
                'i', new ItemStack(Items.IRON_INGOT),
                'u', new ItemStack(Blocks.IRON_BARS)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.FIREBOX, 8),
                " b ", "bib", " b ",
                'b', new ItemStack(Items.BRICK),
                'i', new ItemStack(Items.NETHERBRICK)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.HATCH, 1),
                " u ", "ibi", " u ",
                'b', new ItemStack(ModBlocks.FIREBOX),
                'i', new ItemStack(Items.IRON_INGOT),
                'u', new ItemStack(Blocks.IRON_BARS)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:blocks"), new ItemStack(ModBlocks.CONTROLLER, 1),
                "ibi", "bub", "ibi",
                'b', new ItemStack(ModBlocks.FIREBOX),
                'i', new ItemStack(Items.IRON_INGOT),
                'u', new ItemStack(Items.BLAZE_POWDER)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("betterboilers:items"), new ItemStack(ModItems.INSPECTOR, 1),
                "i  ", "uii", "i b",
                'b', new ItemStack(Items.BOOK),
                'i', new ItemStack(Items.IRON_INGOT),
                'u', new ItemStack(Items.FLINT)
        ));
    }

    public static <T extends IRecipe> T recipe(IForgeRegistry<IRecipe> registry, T t) {
        t.setRegistryName(new ResourceLocation(t.getRecipeOutput().getItem().getRegistryName()+"_"+t.getRecipeOutput().getItemDamage()));
        registry.register(t);
        return t;
    }

}
