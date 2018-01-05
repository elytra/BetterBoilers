package com.elytradev.betterboilers;

import com.elytradev.betterboilers.container.BoilerContainer;
import com.elytradev.betterboilers.tile.TileEntityBoilerController;
import com.elytradev.betterboilers.util.BBConfig;
import com.elytradev.concrete.inventory.IContainerInventoryHolder;
import com.elytradev.concrete.inventory.gui.client.ConcreteGui;
import com.elytradev.betterboilers.block.ModBlocks;
import com.elytradev.betterboilers.client.BBTab;
import com.elytradev.betterboilers.item.ModItems;
import com.elytradev.betterboilers.proxy.CommonProxy;
import com.elytradev.betterboilers.util.BBRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


@Mod(modid = BetterBoilers.modId, name = BetterBoilers.name, version = BetterBoilers.version)
public class BetterBoilers {
    public static final String modId = "betterboilers";
    public static final String name  = "Better Boilers";
    public static final String version = "@VERSION@";
    public static BBConfig config;

    @Mod.Instance(modId)
    public static BetterBoilers instance;

    public static final BBTab creativeTab = new BBTab();

    static {
        FluidRegistry.enableUniversalBucket();
    }

    @SidedProxy(serverSide = "com.elytradev.betterboilers.proxy.CommonProxy", clientSide = "com.elytradev.betterboilers.proxy.ClientProxy")
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BBLog.info("oooh, steamy! " + name + " is loading!");
        MinecraftForge.EVENT_BUS.register(BBRecipes.class);
        config = BBConfig.createConfig(event);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
            public static final int BOILER = 0;

            @Nullable
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    case BOILER:
                        return new BoilerContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityBoilerController)world.getTileEntity(new BlockPos(x,y,z)));

                    default:
                        return null;
                }

            }

            @Nullable
            @Override
            @SideOnly(Side.CLIENT)
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    case BOILER:
                        BoilerContainer boilerContainer = new BoilerContainer(
                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
                                (TileEntityBoilerController)world.getTileEntity(new BlockPos(x,y,z)));
                        return new ConcreteGui(boilerContainer);
                    default:
                        return null;
                }

            }
        });
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());
        //MinecraftForge.EVENT_BUS.register(LightHandler.class);
        ModItems.registerOreDict(); // register oredict
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }
    }
}
