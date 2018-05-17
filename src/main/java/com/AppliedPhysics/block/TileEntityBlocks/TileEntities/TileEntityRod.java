package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRod extends TileEntity {

	public IBlockState rod;
	public float rotation = 0;

	@Override
	public void onLoad() {
		this.rod = getWorld().getBlockState(this.pos);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

}
