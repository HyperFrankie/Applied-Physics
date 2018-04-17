package com.AppliedPhysics.item;

import com.AppliedPhysics.ModFluids;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

//	public static ItemBase 			shadow_ingot = 				new ItemBase("shadow_ingot");
//	public static ItemArmorBase 	armor_helmet_shadow = 		new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.HEAD,  "shadow_helmet");
//	public static ItemArmorBase 	armor_chestplate_shadow = 	new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.CHEST, "shadow_chestplate");
//	public static ItemArmorBase 	armor_leggings_shadow = 	new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.LEGS,  "shadow_leggings");
//	public static ItemArmorBase 	armor_boots_shadow = 		new ItemArmorBase(	Main.shadowArmorMaterial, EntityEquipmentSlot.FEET,  "shadow_boots");
//	public static ItemSwordBase 	shadow_sword = 				new ItemSwordBase(	Main.shadowToolMaterial,	"shadow_sword");
//	public static ItemPickaxeBase 	shadow_pickaxe = 			new ItemPickaxeBase(Main.shadowToolMaterial,	"shadow_pickaxe");
//	public static ItemAxeBase 		shadow_axe = 				new ItemAxeBase(	Main.shadowToolMaterial,	"shadow_axe");
//	public static ItemShovelBase 	shadow_shovel = 			new ItemShovelBase(	Main.shadowToolMaterial,	"shadow_shovel");
//	public static ItemHoeBase 		shadow_hoe = 				new ItemHoeBase(	Main.shadowToolMaterial,	"shadow_hoe");
//	public static ItemChiselBase 	chisel_wood = 				new ItemChiselBase(	Main.chiselWood,			"wood_chisel");
//	public static ItemChiselBase 	chisel_stone = 				new ItemChiselBase(	Main.chiselStone,			"stone_chisel");
//	public static ItemChiselBase 	chisel_iron = 				new ItemChiselBase(	Main.chiselIron,			"iron_chisel");
//	public static ItemChiselBase 	chisel_diamond = 			new ItemChiselBase(	Main.chiselDiamond,			"diamond_chisel");
//	public static ItemChiselBase 	chisel_obsidian = 			new ItemChiselBase(	Main.chiselObsidian,		"obsidian_chisel");
//	public static SaveModelBase		save_model = 				new SaveModelBase(	Main.shadowToolMaterial, 	"save_model");
//	public static Bit				bit = 						new Bit("Bit");
	public static final ItemFluidContainerBase SIMPLE_TANK = new ItemFluidContainerBase("simple_tank", 10000, ModFluids.STEAM, FluidRegistry.WATER, FluidRegistry.LAVA);

	public static void register(IForgeRegistry<Item> registry) {
		registry.registerAll(
				SIMPLE_TANK
				);
	}

	public static void registerModels() {
		SIMPLE_TANK.registerItemModel();
		ModelLoaderRegistry.registerLoader(FluidTankModel.CustomModelLoader.INSTANCE);
		ModelLoader.setCustomMeshDefinition(SIMPLE_TANK, stack -> FluidTankModel.LOCATION);
		ModelBakery.registerItemVariants(SIMPLE_TANK, FluidTankModel.LOCATION);
	}

}
