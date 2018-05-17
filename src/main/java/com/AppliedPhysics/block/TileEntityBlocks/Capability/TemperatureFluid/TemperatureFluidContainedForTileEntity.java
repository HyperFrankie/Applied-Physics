package com.AppliedPhysics.block.TileEntityBlocks.Capability.TemperatureFluid;

import com.AppliedPhysics.AppliedPhysicsUtils;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid.FluidContainedForTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Fluid.FluidTankInterface;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.HeatStack;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.HeatStackInterface;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.ITemperatureHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class TemperatureFluidContainedForTileEntity extends FluidContainedForTileEntity implements ITemperatureHandler {

	private TileEntity te;
	public int id;
	private HeatStack heatStack;

	public static TemperatureFluidContainedForTileEntity create(int id, TileEntity te, @Nullable FluidStack fluidStack, int fluidCapacity) {
		if(te instanceof FluidTankInterface && te instanceof HeatStackInterface) {
			return new TemperatureFluidContainedForTileEntity(id, te, fluidCapacity, fluidStack);
		}
		return null;
	}

	private TemperatureFluidContainedForTileEntity(int id, TileEntity te, int capacity, @Nullable FluidStack fluidStack) {
		super(id, (FluidTankInterface) te, fluidStack, capacity);
		this.id = id;
		this.te = te;
		this.heatStack = new HeatStack(fluidStack);
	}

	@Nullable
	@Override
	public HeatStack heat(HeatStack resource, boolean doHeat) {
		if(getFluidAmount() != 0) {
			if(doHeat) {
				heatStack.setEnergy(heatStack.getEnergy() + resource.getEnergy());
				heatStack.setTemperature(heatStack.getTemperature() + (resource.getEnergy() / heatStack.getWarmthCoefficient() / getAmountInBuckets()));
				updateHeatStacks();
			}
			return new HeatStack(resource.getEnergy(), heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	@Override
	public HeatStack heatMaxEnergy(double maxEnergyDifference, boolean doHeat) {
		if(getFluidAmount() != 0) {
			if(doHeat) {
				heatStack.setEnergy(heatStack.getEnergy() + maxEnergyDifference);
				heatStack.setTemperature(heatStack.getTemperature() + (maxEnergyDifference / heatStack.getWarmthCoefficient() / getAmountInBuckets()));
				updateHeatStacks();
			}
			return new HeatStack(maxEnergyDifference, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	@Override
	public HeatStack heatMaxEnergyWithResource(HeatStack resource, double maxEnergyDifference, boolean doHeat) {
		if(getFluidAmount() != 0) {
			double energyDifference = Math.min(resource.getEnergy(), maxEnergyDifference);
			if(doHeat) {
				heatStack.setEnergy(heatStack.getEnergy() + energyDifference);
				heatStack.setTemperature(heatStack.getTemperature() + (energyDifference / heatStack.getWarmthCoefficient() / getAmountInBuckets()));
				updateHeatStacks();
			}
			return new HeatStack(energyDifference, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	@Override
	public HeatStack heatMaxTemp(double maxTempDifference, boolean doHeat) {
		if(getFluidAmount() != 0) {
			if(doHeat) {
				heatStack.setTemperature(heatStack.getTemperature() + maxTempDifference);
				heatStack.setEnergy(heatStack.getEnergy() + (maxTempDifference * heatStack.getWarmthCoefficient() * getAmountInBuckets()));
				updateHeatStacks();
			}
			return new HeatStack(maxTempDifference, heatStack.getWarmthCoefficient() * getAmountInBuckets(), true);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	@Override
	public HeatStack heatMaxTempWithResource(HeatStack resource, double maxTempDifference, boolean doHeat) {
		if(getFluidAmount() != 0) {
			double tempDifference = Math.min(resource.getTemperature(), maxTempDifference);
			if(doHeat) {
				heatStack.setTemperature(heatStack.getTemperature() + tempDifference);
				heatStack.setEnergy(heatStack.getEnergy() + (tempDifference * heatStack.getWarmthCoefficient() * getAmountInBuckets()));
				updateHeatStacks();
			}
			return new HeatStack(maxTempDifference, heatStack.getWarmthCoefficient() * getAmountInBuckets(), true);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}










	@Nullable
	@Override
	public HeatStack cool(HeatStack destination, boolean doHeat) {
		if(getFluidAmount() != 0) {
			if(doHeat) {
				double oldEnergy = new Double(heatStack.getEnergy());
				double oldTemp = new Double(heatStack.getTemperature());
				heatStack.setEnergy(0);
				heatStack.setTemperature(0);
				updateHeatStacks();
				return new HeatStack(oldTemp, oldEnergy, heatStack.getWarmthCoefficient());
			}
			return new HeatStack(heatStack.getTemperature(), heatStack.getEnergy(), heatStack.getWarmthCoefficient());
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	@Override
	public HeatStack coolMaxEnergy(double maxEnergyDifference, boolean doHeat) {
		if(getFluidAmount() != 0) {
			double energyDifference = Math.min(maxEnergyDifference, heatStack.getEnergy());
			if(doHeat) {
				heatStack.setEnergy(heatStack.getEnergy() - energyDifference);
				heatStack.setTemperature(heatStack.getTemperature() - (energyDifference / heatStack.getWarmthCoefficient() / getAmountInBuckets()));
				updateHeatStacks();
			}
			return new HeatStack(energyDifference, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	public HeatStack coolMaxEnergyWithoutUpdate(double maxEnergyDifference, boolean doHeat) {
		if(getFluidAmount() != 0) {
			double energyDifference = Math.min(maxEnergyDifference, heatStack.getEnergy());
			if(doHeat) {
				heatStack.setEnergy(heatStack.getEnergy() - energyDifference);
				heatStack.setTemperature(heatStack.getTemperature() - (energyDifference / heatStack.getWarmthCoefficient() / getAmountInBuckets()));
			}
			return new HeatStack(energyDifference, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	@Override
	public HeatStack coolMaxEnergyWithDestination(HeatStack destination, double maxEnergyDifference, boolean doHeat) {
		return coolMaxEnergy(maxEnergyDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack coolMaxTemp(double maxTempDifference, boolean doHeat) {
		if(getFluidAmount() != 0) {
			double tempDifference = Math.min(heatStack.getTemperature(), maxTempDifference);
			if(doHeat) {
				heatStack.setTemperature(heatStack.getTemperature() - tempDifference);
				heatStack.setEnergy(heatStack.getEnergy() - (tempDifference * heatStack.getWarmthCoefficient() * getAmountInBuckets()));
				updateHeatStacks();
			}
			return new HeatStack(tempDifference, heatStack.getWarmthCoefficient() * getAmountInBuckets(), true);
		}
		return new HeatStack(0, heatStack.getWarmthCoefficient() * getAmountInBuckets(), false);
	}

	@Nullable
	@Override
	public HeatStack coolMaxTempWithDestination(HeatStack destination, double maxTempDifference, boolean doHeat) {
		return coolMaxTemp(maxTempDifference, doHeat);
	}

	@Override
	public double getTemperature() {
		return heatStack.getTemperature();
	}

	@Override
	public double getEnergy() {
		return heatStack.getEnergy();
	}

	@Override
	public double getWarmthCoefficient() {
		return heatStack.getWarmthCoefficient();
	}

	@Override
	public void setTemperature(double temperature) {
		heatStack.setTemperature(temperature);
		heatStack.setEnergy(heatStack.getTemperature() * heatStack.getWarmthCoefficient() * getAmountInBuckets());
	}

	@Override
	public boolean setEnergy(double energy) {
		heatStack.setEnergy(energy);
		heatStack.setTemperature(heatStack.getEnergy() / heatStack.getWarmthCoefficient() / getAmountInBuckets());
		return true;
	}

	/**
	 * This doesn't do a thing for the warmthCoefficient is set by the fluid. Use {@linkplain #setWarmthCoefficient(Fluid)} instead
	 * @param warmthCoefficient
	 */
	@Override
	public void setWarmthCoefficient(double warmthCoefficient) {}

	/**
	 * Set's the warmthCoefficient according to the {@linkplain Fluid} akjsdfl
	 * @param fluid
	 */
	public void setWarmthCoefficient(Fluid fluid) {
		heatStack.setWarmthCoefficient(AppliedPhysicsUtils.energyInFluid(AppliedPhysicsUtils.getFluidIfUsableOr1000mbWaterStack(new FluidStack(fluid, 1000))) /
				AppliedPhysicsUtils.getFluidIfUsableOr1000mbWaterStack(new FluidStack(fluid, 1000)).getFluid().getTemperature());
	}

	private void updateHeatStacks() {
		((HeatStackInterface) te).updateHeatStacks();
	}













	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if(doFill) {
			int filledAmount = super.fill(resource, doFill);
			if(filledAmount != 0) {
				setWarmthCoefficient(getFluid().getFluid());
				setEnergy(getEnergy() + AppliedPhysicsUtils.energyInFluid(new FluidStack(resource, filledAmount)));
			}
			return filledAmount;
		} else {
			return super.fill(resource, doFill);
		}
	}

	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if(doDrain) {
			FluidStack drainedStack = super.drain(maxDrain, doDrain);
			if(drainedStack != null) {
				if(drainedStack.amount != 0) {
					setEnergy(getEnergy() - AppliedPhysicsUtils.energyInFluid(drainedStack));
				}
			}
			return drainedStack;
		} else {
			return super.drain(maxDrain, doDrain);
		}
	}

	@Nullable
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if(doDrain) {
			FluidStack drainedStack = super.drain(resource, doDrain);
			if(drainedStack != null) {
				if(drainedStack.amount != 0) {
					setEnergy(getEnergy() - AppliedPhysicsUtils.energyInFluid(drainedStack));
				}
			}
			return drainedStack;
		} else {
			return super.drain(resource, doDrain);
		}
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return super.getTankProperties();
	}

	public double getAmountInBuckets() {
		return ((double) getFluidAmount()) / 1000.0;
	}
}
