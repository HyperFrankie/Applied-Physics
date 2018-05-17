package com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class FluidContainedForTileEntity extends FluidTank {

	FluidTankInterface te;
	public int id;

	public FluidContainedForTileEntity(int id, FluidTankInterface te, int capacity) {
		super(null, capacity);
		this.te = te;
		this.id = id;
	}

	public FluidContainedForTileEntity(int id, FluidTankInterface te, @Nullable FluidStack fluidStack, int capacity) {
		super(fluidStack, capacity);
		this.te = te;
		this.id = id;
	}

	public FluidContainedForTileEntity(int id, FluidTankInterface te, Fluid fluid, int amount, int capacity) {
		super(new FluidStack(fluid, amount), capacity);
		this.te = te;
		this.id = id;
	}

	public int fill(FluidStack resource, boolean doFill, boolean doUpdate)
	{
		if (!canFillFluidType(resource))
		{
			return 0;
		}

		return fillInternal(resource, doFill, doUpdate);
	}

	public int fillInternal(FluidStack resource, boolean doFill, boolean doUpdate)
	{
		if (resource == null || resource.amount <= 0)
		{
			return 0;
		}

		if (!doFill)
		{
			if (fluid == null)
			{
				return Math.min(capacity, resource.amount);
			}

			if (!fluid.isFluidEqual(resource))
			{
				return 0;
			}

			return Math.min(capacity - fluid.amount, resource.amount);
		}

		if (fluid == null)
		{
			fluid = new FluidStack(resource, Math.min(capacity, resource.amount));

			if(doUpdate) {
				onContentsChanged();
			}

			if (tile != null)
			{
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, fluid.amount));
			}
			return fluid.amount;
		}

		if (!fluid.isFluidEqual(resource))
		{
			return 0;
		}
		int filled = capacity - fluid.amount;

		if (resource.amount < filled)
		{
			fluid.amount += resource.amount;
			filled = resource.amount;
		}
		else
		{
			fluid.amount = capacity;
		}

		if(doUpdate) {
			onContentsChanged();
		}

		if (tile != null)
		{
			FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile.getWorld(), tile.getPos(), this, filled));
		}
		return filled;
	}

	public FluidStack drain(FluidStack resource, boolean doDrain, boolean doUpdate) {
		if (!canDrainFluidType(getFluid())) {
			return null;
		}
		return drainInternal(resource, doDrain, doUpdate);
	}

	public FluidStack drain(int maxDrain, boolean doDrain, boolean doUpdate) {
		if (!canDrainFluidType(fluid)) {
			return null;
		}
		return drainInternal(maxDrain, doDrain, doUpdate);
	}

	@Nullable
	public FluidStack drainInternal(FluidStack resource, boolean doDrain, boolean doUpdate) {
		if (resource == null || !resource.isFluidEqual(getFluid())) {
			return null;
		}
		return drainInternal(resource.amount, doDrain);
	}

	@Nullable
	public FluidStack drainInternal(int maxDrain, boolean doDrain, boolean doUpdate) {
		if (fluid == null || maxDrain <= 0) {
			return null;
		}

		int drained = maxDrain;
		if (fluid.amount < drained) {
			drained = fluid.amount;
		}

		FluidStack stack = new FluidStack(fluid, drained);
		if (doDrain) {
			fluid.amount -= drained;
			if (fluid.amount <= 0) {
				fluid = null;
			}

			if(doUpdate) {
				onContentsChanged();
			}

			if (tile != null) {
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorld(), tile.getPos(), this, drained));
			}
		}
		return stack;
	}

	@Override
	protected void onContentsChanged() {
		super.onContentsChanged();
		te.updateFluidValue(id);
	}
}
