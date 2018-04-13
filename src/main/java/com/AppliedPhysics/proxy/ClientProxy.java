package com.AppliedPhysics.proxy;

import com.AppliedPhysics.AppliedPhysics;
import com.AppliedPhysics.item.ItemBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

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
//		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMiniBlock.class, new TESRMiniBlock());
	}

}