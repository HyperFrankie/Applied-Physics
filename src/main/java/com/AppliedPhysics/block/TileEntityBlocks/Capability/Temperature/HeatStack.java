package com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature;

import com.AppliedPhysics.AppliedPhysicsUtils;
import com.sun.istack.internal.NotNull;
import net.minecraftforge.fluids.FluidStack;

public class HeatStack {

	private double temperature;
	private double energy;
	private double warmthCoefficient;

	/**
	 * WARNING!! When used with temperature value 0, this will cause an error, for the energy value is divided
	 * by the temperature value, which would thus result in division by 0.
	 * @param temperature
	 * @param energy
	 */
	public HeatStack(double temperature, double energy) {
		this.temperature = temperature;
		this.energy = energy;
		this.warmthCoefficient = energy / temperature;
	}

	public HeatStack(double temperature, double energy, double warmthCoefficient) {
		this.temperature = temperature;
		this.energy = energy;
		this.warmthCoefficient = warmthCoefficient;
	}

	public HeatStack(double value, double warmthCoefficient, boolean isFirstArgumentTemperature) {
		this(isFirstArgumentTemperature ? value : value / warmthCoefficient, !isFirstArgumentTemperature ? value : value * warmthCoefficient, warmthCoefficient);
	}

	public HeatStack(@NotNull FluidStack fluidStack) {
		this(
				AppliedPhysicsUtils.energyInFluid(AppliedPhysicsUtils.getFluidIfUsableOrEmptyWaterStack(fluidStack)),
				AppliedPhysicsUtils.energyInFluid(AppliedPhysicsUtils.getFluidIfUsableOr1000mbWaterStack(fluidStack)) /
								AppliedPhysicsUtils.getFluidIfUsableOr1000mbWaterStack(fluidStack).getFluid().getTemperature()
				, false
		);
	}

	public void setWarmthCoefficient(double warmthCoefficient) {
		this.warmthCoefficient = warmthCoefficient;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getWarmthCoefficient() {
		return warmthCoefficient;
	}

	public double getEnergy() {
		return energy;
	}

	public double getTemperature() {
		return temperature;
	}
}
