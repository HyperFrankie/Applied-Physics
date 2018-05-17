package com.AppliedPhysics.proxy;

import com.AppliedPhysics.AppliedPhysics;
import com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers.TEFRRod;
import com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers.TEFRTankFluid;
import com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers.TEFRWinch;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityRod;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityTank;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityWinch;
import com.AppliedPhysics.item.ItemBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	public void registerItemRenderer(ItemBase item, int meta, String id) {
		// TODO Auto-generated method stub
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(AppliedPhysics.MODID + ":" + id, "inventory"));
	}

	public void registerItemRenderer(Item item, int meta, String id) {
		// TODO Auto-generated method stub
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(AppliedPhysics.MODID + ":" + id, "inventory"));
	}
	
	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}
	
	@Override
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TEFRTankFluid());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRod.class, new TEFRRod());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWinch.class, new TEFRWinch());
	}

	public void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
	}

}