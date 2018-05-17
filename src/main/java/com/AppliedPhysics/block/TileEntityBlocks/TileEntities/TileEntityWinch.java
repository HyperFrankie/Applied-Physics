package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Winch;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.Catenary;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.Rope.DoublePoint;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class TileEntityWinch extends TileEntity {

	public IBlockState winch;
	public EnumFacing facing;
	public double ropeOutLength = 15.0;
	public Winch.EnumNumber totalRopeLength;
	Vec3d connectionPoint, connectedTo;
	public List<Vec3d> positionDataTop = new ArrayList<>();
	public List<BlockPos[]> positionDataArrayTop = new ArrayList<>(), positionDataArrayBottom = new ArrayList<>();
	public int amountOfPoints = 100;
	public int[][] uDataArrayTop = new int[amountOfPoints][4], vDataArrayTop = new int[amountOfPoints][4];
	Catenary rope;

	@Override
	public void onLoad() {
		this.winch = getWorld().getBlockState(this.pos);
		this.facing = winch.getValue(Winch.FACING);
		this.totalRopeLength = winch.getValue(Winch.TOTAL_ROPE_LENGTH);
		loadRope();
	}

	public void loadRope() {
		this.connectionPoint = new Vec3d(0.0, 0.0, 0.0);
		this.connectedTo = new Vec3d(10.0, 5.0, 1.0);
		double dx = connectedTo.squareDistanceTo(connectionPoint.x, connectedTo.y, connectionPoint.z);
		this.rope = new Catenary(new DoublePoint(0, connectionPoint.y), new DoublePoint(dx, connectedTo.y), 32.0);
		double difference = dx / ((double) amountOfPoints - 1.0);
		for(double x = connectionPoint.x; x <= dx; x += difference) {
			double y = rope.calculateY(x);
			System.out.println(y);
			Vec3d currentPos = new Vec3d((connectedTo.x - connectionPoint.x) / dx * x, y, (connectedTo.z - connectionPoint.z) / dx * x);
			positionDataTop.add(currentPos);
			System.out.println(currentPos);
		}
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