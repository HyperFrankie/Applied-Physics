package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Tank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityTank extends TileEntity {

	public IBlockState tank;
	public EnumFacing facing;
	public int connections;
	public double content;

	@Override
	public void onLoad() {
		this.tank = getWorld().getBlockState(this.pos);
		this.facing = tank.getValue(Tank.FACING);
		this.connections = tank.getValue(Tank.CONNECTIONS);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setDouble("content", content);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.content = compound.getDouble("content");
		super.readFromNBT(compound);
	}

}