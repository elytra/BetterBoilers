package com.elytradev.betterboilers.proxy;

import com.elytradev.betterboilers.BetterBoilers;
//import com.elytradev.betterboilers.client.RenderDistiller;
//import com.elytradev.opaline.tile.TileEntityDistiller;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDistiller.class, new RenderDistiller());
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(BetterBoilers.modId, id), "inventory"));
    }
}
