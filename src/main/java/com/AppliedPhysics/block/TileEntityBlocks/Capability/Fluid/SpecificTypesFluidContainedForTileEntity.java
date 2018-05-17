package com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class SpecificTypesFluidContainedForTileEntity extends FluidContainedForTileEntity {

	private ArrayList<FluidStack> fluidTypes;
	boolean exclude;

	public SpecificTypesFluidContainedForTileEntity(int id, FluidTankInterface te, ArrayList<FluidStack> fluidTypes, int capacity, boolean exclude) {
		super(id, te, capacity);
		this.fluidTypes = fluidTypes;
		this.exclude = exclude;
	}

	public SpecificTypesFluidContainedForTileEntity(int id, FluidTankInterface te, ArrayList<FluidStack> fluidTypes, @Nullable FluidStack fluidStack, int capacity, boolean exclude) {
		super(id, te, fluidStack, capacity);
		this.fluidTypes = fluidTypes;
		this.exclude = exclude;
	}

	public SpecificTypesFluidContainedForTileEntity(int id, FluidTankInterface te, ArrayList<FluidStack> fluidTypes, Fluid fluid, int amount, int capacity, boolean exclude) {
		super(id, te, fluid, amount, capacity);
		this.fluidTypes = fluidTypes;
		this.exclude = exclude;
	}

	@Override
	public boolean canDrainFluidType(@Nullable FluidStack fluid) {
		for(FluidStack f : fluidTypes) {
			if(fluid.isFluidEqual(f)) {
				return !exclude;
			}
		}
		return exclude;
	}

	@Override
	public boolean canFillFluidType(@Nullable FluidStack fluid) {
		for(FluidStack f : fluidTypes) {
			if(fluid.isFluidEqual(f)) {
				return !exclude;
			}
		}
		return exclude;
	}
}
