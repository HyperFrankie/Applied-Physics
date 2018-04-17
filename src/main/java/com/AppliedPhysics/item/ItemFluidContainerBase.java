package com.AppliedPhysics.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFluidContainerBase extends ItemBase {

	public int capacity;
	public Fluid[] subFluids;

	public ItemFluidContainerBase(String name, int capacity, Fluid... subFluids) {
		super(name);
		this.subFluids = subFluids;
		this.capacity = capacity;
		setMaxStackSize(1);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
	}

	@Override
	public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new FluidHandlerVariableLevel(stack, capacity);
	}

	@Override
	public void getSubItems(@Nullable final CreativeTabs tab, final NonNullList subItems) {
		if (!this.isInCreativeTab(tab)) return;

		subItems.add(new ItemStack(this));

		for (Fluid f : subFluids) {
			final FluidStack fluidStack = new FluidStack(f, capacity);
			final ItemStack stack = new ItemStack(this);
			final IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (fluidHandler != null) {
				final int fluidFillAmount = fluidHandler.fill(fluidStack, true);
				if (fluidFillAmount == fluidStack.amount) {
					final ItemStack filledStack = fluidHandler.getContainer();
					subItems.add(filledStack);
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		String unlocalizedName = this.getUnlocalizedNameInefficiently(stack);
		IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
		FluidStack fluidStack = fluidHandler.getTankProperties()[0].getContents();

		String s = "";
		if (fluidStack == null || fluidStack.amount <= 0) {
			s += "Simple Tank";
		} else {
			s += "Simple " + fluidStack.getFluid().getLocalizedName(fluidStack) + " Tank";
		}
		return s;
	}
}
