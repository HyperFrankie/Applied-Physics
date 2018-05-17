package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import com.AppliedPhysics.ModFluids;
import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Tank;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid.FluidContainedForTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid.FluidTankInterface;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid.SpecificTypeFluidContainedForTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.HeatStack;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.HeatStackInterface;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.ITemperatureHandler;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.TemperatureFluid.TemperatureFluidContainedForTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;

public class TileEntityTank extends TileEntity implements FluidTankInterface, HeatStackInterface {

	public int capacity = 10000;
	public ArrayList<FluidContainedForTileEntity> tanks = new ArrayList(){{
		add(TemperatureFluidContainedForTileEntity.create(0,  TileEntityTank.this, null, capacity));
		add(new SpecificTypeFluidContainedForTileEntity(1, TileEntityTank.this, new FluidStack(ModFluids.STEAM, 0), capacity, false));
	}};
	public IBlockState tank_block;
	public EnumFacing facing;
	public int connections, extraWidth;
	public static double[] contentLayersNormal = new double[]{
			6 * (14),
			10 * (14),
			12 * (14),
			12 * (14),
			14 * (14),
			14 * (14),
			14 * (14),
			14 * (14),
			14 * (14),
			14 * (14),
			12 * (14),
			12 * (14),
			10 * (14),
			6 * (14)
	}, contentLayersConnectedOneSide = new double[]{
			6 * (14 + 1),
			10 * (14 + 1),
			12 * (14 + 1),
			12 * (14 + 1),
			14 * (14 + 1),
			14 * (14 + 1),
			14 * (14 + 1),
			14 * (14 + 1),
			14 * (14 + 1),
			14 * (14 + 1),
			12 * (14 + 1),
			12 * (14 + 1),
			10 * (14 + 1),
			6 * (14 + 1)
	}, contentLayersConnectedBoth = new double[]{
			6 * (14 + 2),
			10 * (14 + 2),
			12 * (14 + 2),
			12 * (14 + 2),
			14 * (14 + 2),
			14 * (14 + 2),
			14 * (14 + 2),
			14 * (14 + 2),
			14 * (14 + 2),
			14 * (14 + 2),
			12 * (14 + 2),
			12 * (14 + 2),
			10 * (14 + 2),
			6 * (14 + 2)
	};
	public double physicalSpace, physicalContent, content_height, content_width, content_length, minU, minV, maxU, maxV, minX, minZ, maxX, maxZ;
	public boolean shouldRender = false;

	@Override
	public void onLoad() {
		this.tank_block = getWorld().getBlockState(this.pos);
		this.facing = tank_block.getValue(Tank.FACING);
		this.connections = tank_block.getValue(Tank.CONNECTIONS);
		updateFluidValue(0);
		updateFluidValue(1);
	}

	@Override
	public void updateFluidValue(int id) {
		if(tanks.get(0).getFluidAmount() != 0) {
			this.extraWidth = connections == 3 ? 2 : (connections == 0 ? 0 : 1);
			this.content_length = 0.875 + (0.0625 * extraWidth);
			double[] contentLayers = extraWidth == 0 ? contentLayersNormal : (extraWidth == 1 ? contentLayersConnectedOneSide : contentLayersConnectedBoth);
			physicalSpace = 0.0;
			for (double d : contentLayers) {
				physicalSpace += d;
			}
			physicalContent = (double) tanks.get(0).getFluidAmount() / (double) tanks.get(0).getCapacity() * physicalSpace;
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
		if(still == null) {
			still = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(FluidRegistry.WATER.getStill().toString());
		}
		minU = still.getInterpolatedU(16.0 * minX);
		minV = still.getInterpolatedV(16.0 * minZ);
		maxU = still.getInterpolatedU(16.0 * maxX);
		maxV = still.getInterpolatedV(16.0 * maxZ);
		shouldRender = true;
	}

	@Override
	public void updateHeatStacks() {
		if(((TemperatureFluidContainedForTileEntity) tanks.get(0)).getTemperature() >= 373.2) {
			TemperatureFluidContainedForTileEntity te = ((TemperatureFluidContainedForTileEntity) tanks.get(0));
			HeatStack cooled = te.coolMaxTemp(te.getTemperature() - 373.2,false);
			if(cooled.getEnergy() > 1.0) {
				//MAKE SURE
				cooled = te.coolMaxEnergyWithoutUpdate((int) cooled.getEnergy(), true);
				FluidStack drained = tanks.get(0).drain((int) (cooled.getEnergy() / 100.0), true, false);
				if(drained != null) {
					FluidStack steam = new FluidStack(ModFluids.STEAM, drained.amount);
					tanks.get(1).fill(steam, true);
				} else {
				}
			}
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		System.out.println("                    ----------------writing to NBT----------------");
		compound.setDouble("content", content_height);
		tanks.get(0).writeToNBT(compound);
		tanks.get(1).writeToNBT(compound);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.content_height = compound.getDouble("content");
		tanks.get(0).readFromNBT(compound);
		tanks.get(1).readFromNBT(compound);
		super.readFromNBT(compound);
	}

	@Override
	public boolean hasFastRenderer() { return false; }

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != EnumFacing.UP) || capability == ITemperatureHandler.HEAT_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) ? (facing != EnumFacing.UP ? (T) tanks.get(0) : (T) tanks.get(1)) : (capability == ITemperatureHandler.HEAT_HANDLER_CAPABILITY) ? (T) ((TemperatureFluidContainedForTileEntity) tanks.get(0)) : super.getCapability(capability, facing);
	}
}