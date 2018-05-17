package com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature;

import com.AppliedPhysics.AppliedPhysicsUtils;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class CapabilityTemperatureHandler implements ITemperatureHandler {

	public HeatStack heatStack = new HeatStack(0, 10, false);

	public CapabilityTemperatureHandler() {
		this(FluidRegistry.WATER.getTemperature(), AppliedPhysicsUtils.energyInFluid(new FluidStack(FluidRegistry.WATER, 1000)), (double) (FluidRegistry.WATER.getTemperature()) / AppliedPhysicsUtils.energyInFluid(new FluidStack(FluidRegistry.WATER, 1000)));
	}

	public CapabilityTemperatureHandler(double temperature, double energy, double warmthCoefficient) {
		heatStack.setTemperature(temperature);
		heatStack.setEnergy(energy);
		heatStack.setWarmthCoefficient(warmthCoefficient);
	}

	@Nullable
	@Override
	public HeatStack heat(HeatStack resource, boolean doHeat) {
		if(doHeat) {
			heatStack.setEnergy(heatStack.getEnergy() + resource.getEnergy());
			heatStack.setTemperature(heatStack.getEnergy() + (resource.getEnergy() / heatStack.getWarmthCoefficient()));
		}
		return new HeatStack(resource.getEnergy(), heatStack.getWarmthCoefficient(), false);
	}

	@Nullable
	@Override
	public HeatStack heatMaxEnergy(double maxEnergyDifference, boolean doHeat) {
		if(doHeat) {
			heatStack.setEnergy(heatStack.getEnergy() + maxEnergyDifference);
			heatStack.setTemperature(heatStack.getTemperature() + (maxEnergyDifference / heatStack.getWarmthCoefficient()));
		}
		return new HeatStack(maxEnergyDifference, heatStack.getWarmthCoefficient(), false);
	}

	@Nullable
	@Override
	public HeatStack heatMaxEnergyWithResource(HeatStack resource, double maxEnergyDifference, boolean doHeat) {
		double energyDifference = Math.min(resource.getEnergy(), maxEnergyDifference);
		if(doHeat) {
			heatStack.setEnergy(heatStack.getEnergy() + energyDifference);
			heatStack.setEnergy(heatStack.getEnergy() + (energyDifference / heatStack.getWarmthCoefficient()));
		}
		return new HeatStack(energyDifference, heatStack.getWarmthCoefficient(), false);
	}

	@Nullable
	@Override
	public HeatStack heatMaxTemp(double maxTempDifference, boolean doHeat) {
		if(doHeat) {
			heatStack.setTemperature(heatStack.getTemperature() + maxTempDifference);
			heatStack.setEnergy(heatStack.getEnergy() + (maxTempDifference * heatStack.getWarmthCoefficient()));
		}
		return new HeatStack(maxTempDifference, heatStack.getWarmthCoefficient(), true);
	}

	@Nullable
	@Override
	public HeatStack heatMaxTempWithResource(HeatStack resource, double maxTempDifference, boolean doHeat) {
		double tempDifference = Math.min(resource.getTemperature(), maxTempDifference);
		if(doHeat) {
			heatStack.setTemperature(heatStack.getTemperature() + tempDifference);
			heatStack.setEnergy(heatStack.getEnergy() + (tempDifference * heatStack.getWarmthCoefficient()));
		}
		return new HeatStack(maxTempDifference, heatStack.getWarmthCoefficient(), true);
	}










	@Nullable
	@Override
	public HeatStack cool(HeatStack destination, boolean doHeat) {
		if(doHeat) {
			double oldEnergy = new Double(heatStack.getEnergy());
			double oldTemp = new Double(heatStack.getTemperature());
			heatStack.setEnergy(0);
			heatStack.setTemperature(0);
			return new HeatStack(oldTemp, oldEnergy);
		}
		return new HeatStack(heatStack.getTemperature(), heatStack.getEnergy());
	}

	@Nullable
	@Override
	public HeatStack coolMaxEnergy(double maxEnergyDifference, boolean doHeat) {
		double energyDifference = Math.min(maxEnergyDifference, heatStack.getEnergy());
		if(doHeat) {
			heatStack.setEnergy(heatStack.getEnergy() - energyDifference);
			heatStack.setTemperature(heatStack.getTemperature() - (energyDifference / heatStack.getWarmthCoefficient()));
		}
		return new HeatStack(energyDifference, heatStack.getWarmthCoefficient(), false);
	}

	@Nullable
	@Override
	public HeatStack coolMaxEnergyWithDestination(HeatStack destination, double maxEnergyDifference, boolean doHeat) {
		return coolMaxEnergy(maxEnergyDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack coolMaxTemp(double maxTempDifference, boolean doHeat) {
		double tempDifference = Math.min(heatStack.getTemperature(), maxTempDifference);
		if(doHeat) {
			heatStack.setTemperature(heatStack.getTemperature() - tempDifference);
			heatStack.setEnergy(heatStack.getEnergy() - (tempDifference * heatStack.getWarmthCoefficient()));
			return new HeatStack(heatStack.getTemperature(), heatStack.getEnergy());
		}
		return new HeatStack(tempDifference, heatStack.getWarmthCoefficient(), true);
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
	public void setTemperature(double temperature) { heatStack.setTemperature(temperature); }

	@Override
	public boolean setEnergy(double energy) { heatStack.setEnergy(energy); return true;}

	@Override
	public void setWarmthCoefficient(double warmthCoefficient) {
		heatStack.setWarmthCoefficient(warmthCoefficient);
	}
}