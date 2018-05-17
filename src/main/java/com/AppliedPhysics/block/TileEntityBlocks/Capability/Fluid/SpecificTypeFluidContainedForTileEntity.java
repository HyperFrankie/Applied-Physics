package com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class SpecificTypeFluidContainedForTileEntity extends FluidContainedForTileEntity {

	private FluidStack fluidType;
	boolean exclude;

	public SpecificTypeFluidContainedForTileEntity(int id, FluidTankInterface te, FluidStack fluidType, int capacity, boolean exclude) {
		super(id, te, capacity);
		this.fluidType = fluidType;
		this.exclude = exclude;
	}

	public SpecificTypeFluidContainedForTileEntity(int id, FluidTankInterface te, FluidStack fluidType, @Nullable FluidStack fluidStack, int capacity, boolean exclude) {
		super(id, te, fluidStack, capacity);
		this.fluidType = fluidType;
		this.exclude = exclude;
	}

	public SpecificTypeFluidContainedForTileEntity(int id, FluidTankInterface te, FluidStack fluidType, Fluid fluid, int amount, int capacity, boolean exclude) {
		super(id, te, fluid, amount, capacity);
		this.fluidType = fluidType;
		this.exclude = exclude;
	}

	@Override
	public boolean canDrainFluidType(@Nullable FluidStack fluid) {
		if(fluid.isFluidEqual(fluidType)) {
			return !exclude;
		}
		return exclude;
	}

	@Override
	public boolean canFillFluidType(@Nullable FluidStack fluid) {
		if(fluid.isFluidEqual(fluidType)) {
			return !exclude;
		}
		return exclude;
	}
}
