package com.AppliedPhysics;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;

import static net.minecraftforge.fluids.FluidUtil.getFluidContained;

public class AppliedPhysicsUtils {

	public static boolean canStackBurn(ItemStack burnStack, ItemStack outputSlot) {
		if(!isStackFuel(burnStack)) {
			return false;
		} else {
			if(burnStack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack = getOutputItem(burnStack);
				if(itemstack.isEmpty()) {
					return true;
				} else {
					if(outputSlot.isEmpty()) {
						return true;
					} else if(!outputSlot.isItemEqual(itemstack)) {
						return false;
					} else if(outputSlot.getCount() + itemstack.getCount() <= outputSlot.getMaxStackSize() && outputSlot.getCount() + itemstack.getCount() <= outputSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
						return true;
					} else {
						return outputSlot.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
					}
				}
			}
		}
	}

	public static boolean isStackFuel(ItemStack burnStack) {
		if(hasFluidAndNotNullOrEmpty(burnStack)) {
			return true;
		}
		if(burnStack.getItem().getItemBurnTime(burnStack) == -1) {
			return TileEntityFurnace.getItemBurnTime(burnStack) > 0;
		}
		return burnStack.getItem().getItemBurnTime(burnStack) > 0;
	}

	public static ItemStack getOutputItem(ItemStack burnStack) {
		if(hasFluidAndNotNullOrEmpty(burnStack)) {
			ItemStack copyStack = burnStack.copy();
			FluidActionResult r = FluidUtil.tryEmptyContainer(copyStack, new VoidFluidHandler(), getFluidContained(copyStack).amount, null, true);
			ItemStack resultStack = ItemStack.EMPTY;
			if (r.getResult() != null && r.getResult().getItem() != Items.AIR) {
				resultStack = r.getResult();
			}
			return resultStack;
		} else if(burnStack.getItem() instanceof ItemBlock) {
			if(((ItemBlock) burnStack.getItem()).getBlock().equals(Blocks.LOG) || ((ItemBlock) burnStack.getItem()).getBlock().equals(Blocks.LOG2)) {
				return new ItemStack(Items.COAL, 1, 1);
			}
		}
		return ItemStack.EMPTY;
	}

	public static double energyProduced(ItemStack burnStack) {
		if(burnStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			return energyInFluid(getFluidContained(burnStack));
		} else {
			return burnStack.getItem().getItemBurnTime(burnStack);
		}
	}

	public static double energyInFluid(FluidStack fluidStack) {
		if(fluidStackIsUsable(fluidStack)) {
			double amount = ((double) fluidStack.amount) / 1000.0;
			double temp = fluidStack.getFluid().getTemperature();
			double density = ((double) fluidStack.getFluid().getDensity());
			return (long) (density * amount * temp);
		}
		return 0;
	}

	public static int getBurnTime(ItemStack burnStack) {
		int burnTime;
		if(burnStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			if(getFluidContained(burnStack).getFluid() != null) {
				double amount = ((double) getFluidContained(burnStack).amount) / 1000.0;
				double density = ((double) getFluidContained(burnStack).getFluid().getDensity());
				burnTime = (int) (amount * density * 10.0);
			} else {
				burnTime = 0;
			}
		} else {
			if(burnStack.getItem().getItemBurnTime(burnStack) == -1) {
				burnTime = TileEntityFurnace.getItemBurnTime(burnStack);
			} else {
				burnTime = burnStack.getItem().getItemBurnTime(burnStack);
			}
		}
		return burnTime;
	}

//	public static boolean canItemOutputIntoSlot(ItemStack burnStack, ItemStack outputSlot, boolean withOutputItemCheck) {
//		if(outputSlot.isEmpty()) {
//			return true;
//		} else if(
//				outputSlot.getCount() < outputSlot.getMaxStackSize() ||
//						getOutputItem(burnStack).isEmpty() ||
//						getOutputItem(burnStack).) {
//
//		}
//		return (outputSlot.getCount() != outputSlot.getMaxStackSize() || outputSlot.isEmpty()) &&
//				((getOutputItem(burnStack).isEmpty() || getOutputItem(burnStack)) || !withOutputItemCheck) ||
//				areItemStackTagsEqual(burnStack, outputSlot);
//
//
//	}

	public static boolean hasFluidAndNotNullOrEmpty(ItemStack stack) {
		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			if(getFluidContained(stack) != null) {
				return fluidStackIsUsable(getFluidContained(stack));
			}
		}
		return false;
	}

	public static boolean fluidStackIsUsable(FluidStack fluidStack) {
		if(fluidStack != null) {
			if(fluidStack.getFluid() != null) {
				return true;
			}
		}
		return false;
	}

	public static FluidStack getFluidIfUsableOrEmptyWaterStack(FluidStack fluidStack) {
		if(fluidStackIsUsable(fluidStack)) {
			return fluidStack;
		}
		return new FluidStack(FluidRegistry.WATER, 0);
	}

	public static FluidStack getFluidIfUsableOr1000mbWaterStack(FluidStack fluidStack) {
		if(fluidStackIsUsable(fluidStack)) {
			return fluidStack;
		}
		return new FluidStack(FluidRegistry.WATER, 1000);
	}

	public static boolean shouldReturnResultImmediately(ItemStack burnStack, ItemStack outputSlot) {
		if(hasFluidAndNotNullOrEmpty(burnStack)) {
			return true;
		}
		return false;
	}
}
