package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Winch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityRod extends TileEntity {

	public IBlockState rod;
	public EnumFacing facing;

	@Override
	public void onLoad() {
//		if(world.isRemote) {
//			AppliedPhysics.network.sendToServer(new PacketRequestUpdatePulley(this));
//		}
		this.rod = getWorld().getBlockState(this.pos);
		this.facing = rod.getValue(Winch.FACING);
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
