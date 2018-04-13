package com.AppliedPhysics.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static com.AppliedPhysics.AppliedPhysics.MODID;

public class PhysicsTab extends CreativeTabs {

	public PhysicsTab() {
		super(MODID);
//		setBackgroundImageName("item_search.png");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Items.BOOK);
	}
	
	@Override
	public boolean hasSearchBar() {
		return false;
	}

}