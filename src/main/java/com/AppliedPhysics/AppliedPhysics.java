package com.AppliedPhysics;

import com.AppliedPhysics.block.ModBlocks;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.CapabilityTemperatureHandler;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.HeatStorage;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.ITemperatureHandler;
import com.AppliedPhysics.gui.ModGuiHandler;
import com.AppliedPhysics.gui.PhysicsTab;
import com.AppliedPhysics.item.ModItems;
import com.AppliedPhysics.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

@Mod(modid = AppliedPhysics.MODID, name = AppliedPhysics.NAME, version = AppliedPhysics.VERSION)
public class AppliedPhysics {

    public static final String MODID = "applied_physics";
    public static final String NAME = "Applied Physics";
    public static final String VERSION = "0.0.1";

    @Mod.Instance(MODID)
    public static AppliedPhysics instance;

    public static SimpleNetworkWrapper network;
    private static Logger logger;

	@SidedProxy(serverSide = "com.AppliedPhysics.proxy.CommonProxy", clientSide = "com.AppliedPhysics.proxy.ClientProxy")
	public static CommonProxy proxy;

	public static final PhysicsTab PHYSICS_TAB = new PhysicsTab();

	public static final Material STEAM = new MaterialLiquid(MapColor.SNOW);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
	    MinecraftForge.EVENT_BUS.register(this);

	    CapabilityManager.INSTANCE.register(ITemperatureHandler.class, new HeatStorage(), new Factory());

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new ModGuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	    proxy.registerRenderers();

        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
    }

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			ModItems.register(event.getRegistry());
			ModBlocks.registerItemBlocks(event.getRegistry());
		}

		@SubscribeEvent
		public static void registerItems(ModelRegistryEvent event) {
			ModItems.registerModels();
			ModBlocks.registerModels();
		}

		@SubscribeEvent
		public static void registerBlocksAndFluids(RegistryEvent.Register<Block> event) {
			ModFluids.register();
			ModBlocks.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void registerTextures(final TextureStitchEvent.Pre e) {
			if(e.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
				System.out.println("is textureMapBlocks");
				e.getMap().registerSprite(ModFluids.STEAM.getStill());
				e.getMap().registerSprite(ModFluids.STEAM.getFlowing());
			}
		}
	}

	private static class Factory implements Callable<ITemperatureHandler> {
		@Override
		public ITemperatureHandler call() throws Exception {
			return new CapabilityTemperatureHandler();
		}
	}
}
