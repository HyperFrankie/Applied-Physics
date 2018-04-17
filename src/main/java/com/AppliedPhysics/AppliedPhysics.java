package com.AppliedPhysics;

import com.AppliedPhysics.block.ModBlocks;
import com.AppliedPhysics.gui.ModGuiHandler;
import com.AppliedPhysics.gui.PhysicsTab;
import com.AppliedPhysics.item.ModItems;
import com.AppliedPhysics.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

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



	static {
		FluidRegistry.enableUniversalBucket();
	}

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
	    MinecraftForge.EVENT_BUS.register(this);

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
	}
}
