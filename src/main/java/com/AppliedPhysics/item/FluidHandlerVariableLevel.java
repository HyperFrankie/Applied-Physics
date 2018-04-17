package com.AppliedPhysics.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * FluidHandlerItemStackSimple is a template capability provider for ItemStacks.
 * Data is stored directly in the vanilla NBT, in the same way as the old ItemFluidContainer.
 *
 * This implementation only allows item containers to be fully filled or emptied, similar to vanilla buckets.
 */
public class FluidHandlerVariableLevel extends FluidHandlerItemStackSimple {

	public FluidHandlerVariableLevel(@Nonnull ItemStack container, int capacity) {
		super(container, capacity);
	}

	@Nonnull
	@Override
	public ItemStack getContainer()
	{
		return container;
	}

	@Nullable
	public FluidStack getFluid() {
		NBTTagCompound tagCompound = container.getTagCompound();
		if (tagCompound == null || !tagCompound.hasKey(FLUID_NBT_KEY)) {
			return null;
		}
		return FluidStack.loadFluidStackFromNBT(tagCompound.getCompoundTag(FLUID_NBT_KEY));
	}

	protected void setFluid(FluidStack fluid) {
		if (!container.hasTagCompound()) {
			container.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound fluidTag = new NBTTagCompound();
		fluid.writeToNBT(fluidTag);
		container.getTagCompound().setTag(FLUID_NBT_KEY, fluidTag);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return new IFluidTankProperties[] { new FluidTankProperties(getFluid(), capacity) };
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		System.out.println("=========== doing fill with resource " + resource + " and doFill set to " + doFill + " ===========");
		if (container.getCount() != 1 || resource == null || resource.amount <= 0 || !canFillFluidType(resource)) {
			return 0;
		}

		FluidStack contained = getFluid();
		if (contained == null || contained.getFluid().equals(resource.getFluid())) {
			int emptyCapacity = capacity;
			if(contained != null) { emptyCapacity -= contained.amount; }
			int fillAmount = Math.min(emptyCapacity, resource.amount);
			if (doFill) {
				FluidStack filled = resource.copy();
				int newAmount = fillAmount;
				if(contained != null) { newAmount += contained.amount; }
				filled.amount = newAmount;
				setFluid(filled);
			}
			return fillAmount;
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		System.out.println("=========== doing drain with resource " + resource + " and doDrain set to " + doDrain + " ===========");
		if (container.getCount() != 1 || resource == null || resource.amount <= 0 || !resource.isFluidEqual(getFluid())) {
			return null;
		}
		return drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		System.out.println("=========== doing drain with maxDrain " + maxDrain + " and doDrain set to " + doDrain + " ===========");
		if (container.getCount() != 1 || maxDrain <= 0) {
			System.out.println("returning null stage 1");
			return null;
		}

		FluidStack contained = getFluid();
		if (contained == null || contained.amount <= 0 || !canDrainFluidType(contained)) {
			System.out.println("returning null stage 2");
			return null;
		}

		final int drainAmount = Math.min(contained.amount, maxDrain);
		FluidStack drained = contained.copy();
		drained.amount = drainAmount;

		if (doDrain) {
			System.out.println("doDrain is set to true");
			if(contained.amount - drainAmount == 0) {
				System.out.println("level in item is 0, going to set to empty");
				setContainerToEmpty();
			} else {
				System.out.println("level in item is " + (contained.amount - drainAmount));
				FluidStack newF = contained.copy();
				newF.amount = contained.amount - drainAmount;
				setFluid(newF);
			}
		}
		return drained;
	}

	public boolean canFillFluidType(FluidStack fluid)
	{
		return true;
	}

	public boolean canDrainFluidType(FluidStack fluid)
	{
		return true;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (T) this : null;
	}

	/**
	 * Destroys the container item when it's emptied.
	 */
	public static class Consumable extends net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple {
		public Consumable(ItemStack container, int capacity) {
			super(container, capacity);
		}

		@Override
		protected void setContainerToEmpty() {
			super.setContainerToEmpty();
			container.shrink(1);
		}
	}

	/**
	 * Swaps the container item for a different one when it's emptied.
	 */
	public static class SwapEmpty extends net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple {
		protected final ItemStack emptyContainer;

		public SwapEmpty(ItemStack container, ItemStack emptyContainer, int capacity) {
			super(container, capacity);
			this.emptyContainer = emptyContainer;
		}

		@Override
		protected void setContainerToEmpty() {
			super.setContainerToEmpty();
			container = emptyContainer;
		}
	}
}