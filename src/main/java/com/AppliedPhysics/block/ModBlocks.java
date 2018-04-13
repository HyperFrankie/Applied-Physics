package com.AppliedPhysics.block;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Tank;
import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Winch;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

	//	public static BlockCounter counter = new BlockCounter();
	public static BlockBase basicBlock = new BlockBase(Material.ROCK, "basic_block");
	public static Winch winch = new Winch();
	public static Tank tank = new Tank();

	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				basicBlock,
				winch,
				tank
		);
		GameRegistry.registerTileEntity(winch.getTileEntityClass(), winch.getRegistryName().toString());
		GameRegistry.registerTileEntity(tank.getTileEntityClass(), tank.getRegistryName().toString());
	}

	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				basicBlock.createItemBlock(basicBlock.getRegistryName()),
				winch.createItemBlock(winch.getRegistryName()),
				tank.createItemBlock(tank.getRegistryName())
		);
	}

	public static void registerModels() {
		basicBlock.registerItemModel(Item.getItemFromBlock(basicBlock));
		winch.registerItemModel(Item.getItemFromBlock(winch));
		tank.registerItemModel(Item.getItemFromBlock(tank));
	}

}