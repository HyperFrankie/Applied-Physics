package com.AppliedPhysics.block;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.IndustrialFurnace;
import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Rod;
import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Tank;
import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Winch;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	public static BlockBase basicBlock =                    new BlockBase(Material.ROCK, "basic_block");
	public static Winch wood_winch =                        new Winch(Material.WOOD, "wood_winch");
	public static Winch steel_winch =                       new Winch(Material.IRON, "steel_winch");
	public static Tank tank =                               new Tank();
	public static Pallet pallet =                           new Pallet();
	public static IronWalkway ironWalkway =                 new IronWalkway(Material.WOOD, "iron_walkway");
	public static IndustrialFurnace industrial_furnace =    new IndustrialFurnace(Material.ROCK, "industrial_furnace");
	public static Rod rod_wood =                            new Rod(Material.WOOD, "wooden_rod");
	public static Rod rod_steel =                           new Rod(Material.IRON, "steel_rod");

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				basicBlock,
				wood_winch,
				steel_winch,
				tank,
				pallet,
				ironWalkway,
				industrial_furnace,
				rod_wood,
				rod_steel
		);
		GameRegistry.registerTileEntity(wood_winch.getTileEntityClass(), wood_winch.getRegistryName().toString());
		GameRegistry.registerTileEntity(tank.getTileEntityClass(), tank.getRegistryName().toString());
		GameRegistry.registerTileEntity(industrial_furnace.getTileEntityClass(), industrial_furnace.getRegistryName().toString());
		GameRegistry.registerTileEntity(rod_wood.getTileEntityClass(), rod_wood.getRegistryName().toString());
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				basicBlock.createItemBlock(basicBlock.getRegistryName()),
				wood_winch.createItemBlock(wood_winch.getRegistryName()),
				steel_winch.createItemBlock(steel_winch.getRegistryName()),
				tank.createItemBlock(tank.getRegistryName()),
				pallet.createItemBlock(pallet.getRegistryName()),
				ironWalkway.createItemBlock(ironWalkway.getRegistryName()),
				industrial_furnace.createItemBlock(industrial_furnace.getRegistryName()),
				rod_wood.createItemBlock(rod_wood.getRegistryName()),
				rod_steel.createItemBlock(rod_steel.getRegistryName())
		);
	}

	public static void registerModels() {
		basicBlock.registerItemModel(Item.getItemFromBlock(basicBlock));
		wood_winch.registerItemModel(Item.getItemFromBlock(wood_winch));
		steel_winch.registerItemModel(Item.getItemFromBlock(steel_winch));
		tank.registerItemModel(Item.getItemFromBlock(tank));
		pallet.registerItemModel(Item.getItemFromBlock(pallet));
		ironWalkway.registerItemModel(Item.getItemFromBlock(ironWalkway));
		industrial_furnace.registerItemModel(Item.getItemFromBlock(industrial_furnace));
		rod_wood.registerItemModel(Item.getItemFromBlock(rod_wood));
		rod_steel.registerItemModel(Item.getItemFromBlock(rod_steel));
	}

}