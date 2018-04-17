package com.AppliedPhysics.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandlerSpecificType extends FluidHandlerItemStack {

	protected FluidStack empty;
	protected Fluid fluidType;

	public FluidHandlerSpecificType(ItemStack itemFluidContainer, int containerCapacity, Fluid fluidType) {
		super(itemFluidContainer, containerCapacity);

		this.fluidType = fluidType;
		this.empty = new FluidStack(fluidType, 0);
		if (getFluid() == null) {
			setContainerToEmpty();
		}
	}

	public FluidHandlerSpecificType(ItemStack itemFluidContainer, int containerCapacity, FluidStack fluid) {
		super(itemFluidContainer, containerCapacity);
		this.fluidType = fluid.getFluid();
		this.empty = new FluidStack(fluidType, 0);
		if (getFluid() == null) {
			setContainerToEmpty();
		}
	}

	@Override
	protected void setContainerToEmpty() {
		setFluid(empty.copy());
		container.getTagCompound().removeTag(FLUID_NBT_KEY);
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		return fluid.getFluid() == fluidType;
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluid) {
		return fluid.getFluid() == fluidType;
	}
}