package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Tank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityTank extends TileEntity {

	public int capacity = 10000;
	public FluidTank tank = new FluidTank(capacity);
	public IBlockState tank_block;
	public EnumFacing facing;
	public int connections, extraWidth;
	public double[] contentLayers;
	public double physicalSpace, physicalContent, content_height, content_width, content_length, minU, minV, maxU, maxV, minX, minZ, maxX, maxZ;
	public boolean shouldRender = false;

	@Override
	public void onLoad() {
		this.tank_block = getWorld().getBlockState(this.pos);
		this.facing = tank_block.getValue(Tank.FACING);
		this.connections = tank_block.getValue(Tank.CONNECTIONS);
		updateFluidValue();
	}

	public void updateFluidValue() {
		if(tank.getFluidAmount() != 0) {
			this.extraWidth = connections == 3 ? 2 : (connections == 0 ? 0 : 1);
			this.content_length = 0.875 + (0.0625 * extraWidth);
			this.contentLayers = new double[]{
					6 * (14 + extraWidth),
					10 * (14 + extraWidth),
					12 * (14 + extraWidth),
					12 * (14 + extraWidth),
					14 * (14 + extraWidth),
					14 * (14 + extraWidth),
					14 * (14 + extraWidth),
					14 * (14 + extraWidth),
					14 * (14 + extraWidth),
					14 * (14 + extraWidth),
					12 * (14 + extraWidth),
					12 * (14 + extraWidth),
					10 * (14 + extraWidth),
					6 * (14 + extraWidth)
			};
			physicalSpace = 0.0;
			for (double d : contentLayers) {
				physicalSpace += d;
			}
			physicalContent = (double) tank.getFluidAmount() / (double) tank.getCapacity() * physicalSpace;
			System.out.println("physical content is " + physicalContent + " of a total of" + physicalSpace);
			double i = physicalContent;
			content_height = 0.0625;
			for (int l = 0; l < contentLayers.length && i != -1; l++) {
				double d = contentLayers[l];
				if (d < i) {
					i -= d;
					content_height += 0.0625;
				} else {
					content_height += 0.0625 * (i / d);
					content_width = 0.0625 * d / (14 + extraWidth);
					i = -1;
				}
			}
			System.out.println(content_height);
			System.out.println(content_width);
			System.out.println(content_length);
			createFace();
		} else {
			shouldRender = false;
		}
	}

	private void createFace() {
		if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
			minX = 0.5 - (content_width / 2);
			maxX = 1.0 - minX;
			minZ = 0.0625 - ((
							(facing == EnumFacing.SOUTH && connections == 1) ||
							(facing == EnumFacing.NORTH && connections == 2) ||
							connections == 3)
					? 0.0625 : 0.0);
			maxZ = 0.9375 + ((
							(facing == EnumFacing.SOUTH && connections == 2) ||
							(facing == EnumFacing.NORTH && connections == 1) ||
							connections == 3)
					? 0.0625 : 0.0);
		} else {
			minZ = 0.5 - (content_width / 2);
			maxZ = 1.0 - minZ;
			minX = 0.0625 - ((
							(facing == EnumFacing.WEST && connections == 1) ||
							(facing == EnumFacing.EAST && connections == 2) ||
							connections == 3)
					? 0.0625 : 0.0);
			maxX = 0.9375 + ((
							(facing == EnumFacing.WEST && connections == 2) ||
							(facing == EnumFacing.EAST && connections == 1) ||
							connections == 3)
					? 0.0625 : 0.0);
		}
		TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(((FluidTank) this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)).getFluid().getFluid().getStill().toString());
		minU = still.getInterpolatedU(16.0 * minX);
		minV = still.getInterpolatedV(16.0 * minZ);
		maxU = still.getInterpolatedU(16.0 * maxX);
		maxV = still.getInterpolatedV(16.0 * maxZ);
		shouldRender = true;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		System.out.println("                    ----------------writing to NBT----------------");
		compound.setDouble("content", content_height);
		tank.writeToNBT(compound);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.content_height = compound.getDouble("content");
		tank.readFromNBT(compound);
		super.readFromNBT(compound);
	}

//	@Override
//	public boolean hasFastRenderer() { return false; }

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != EnumFacing.UP) || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			if(facing == EnumFacing.UP) {

			} else {
				return (T) tank;
			}
		return super.getCapability(capability, facing);
	}
}