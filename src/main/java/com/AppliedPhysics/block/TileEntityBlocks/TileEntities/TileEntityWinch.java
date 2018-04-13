package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Winch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityWinch extends TileEntity {

	public IBlockState winch;
	public EnumFacing facing;
	public double ropeOutLength = 0.0;
	public Winch.EnumNumber totalRopeLength;

	@Override
	public void onLoad() {
//		if(world.isRemote) {
//			AppliedPhysics.network.sendToServer(new PacketRequestUpdatePulley(this));
//		}
		this.winch = getWorld().getBlockState(this.pos);
		this.facing = winch.getValue(Winch.FACING);
		this.totalRopeLength = winch.getValue(Winch.TOTAL_ROPE_LENGTH);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setDouble("ropeOutLength", ropeOutLength);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.ropeOutLength = compound.getDouble("ropeOutLength");
		super.readFromNBT(compound);
	}

}