package com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature;

import javax.annotation.Nullable;

public class TileEntityHeatContained extends CapabilityTemperatureHandler {

	private HeatStackInterface te;

	public TileEntityHeatContained(HeatStackInterface te) {
		this(te, 300, 3000, 10);
	}

	public TileEntityHeatContained(HeatStackInterface te, double temperature, double energy, double warmthCoefficient) {
		super(temperature, energy, warmthCoefficient);
		this.te = te;
	}

	@Nullable
	@Override
	public HeatStack heat(HeatStack resource, boolean doHeat) {
		onContentsChanged();
		return super.heat(resource, doHeat);
	}

	@Nullable
	@Override
	public HeatStack heatMaxEnergy(double maxEnergyDifference, boolean doHeat) {
		onContentsChanged();
		return super.heatMaxEnergy(maxEnergyDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack heatMaxEnergyWithResource(HeatStack resource, double maxEnergyDifference, boolean doHeat) {
		onContentsChanged();
		return super.heatMaxEnergyWithResource(resource, maxEnergyDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack heatMaxTemp(double maxTempDifference, boolean doHeat) {
		onContentsChanged();
		return super.heatMaxTemp(maxTempDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack heatMaxTempWithResource(HeatStack resource, double maxTempDifference, boolean doHeat) {
		onContentsChanged();
		return super.heatMaxTempWithResource(resource, maxTempDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack cool(HeatStack destination, boolean doHeat) {
		onContentsChanged();
		return super.cool(destination, doHeat);
	}

	@Nullable
	@Override
	public HeatStack coolMaxEnergy(double maxEnergyDifference, boolean doHeat) {
		onContentsChanged();
		return super.coolMaxEnergy(maxEnergyDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack coolMaxEnergyWithDestination(HeatStack destination, double maxEnergyDifference, boolean doHeat) {
		onContentsChanged();
		return super.coolMaxEnergyWithDestination(destination, maxEnergyDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack coolMaxTemp(double maxTempDifference, boolean doHeat) {
		onContentsChanged();
		return super.coolMaxTemp(maxTempDifference, doHeat);
	}

	@Nullable
	@Override
	public HeatStack coolMaxTempWithDestination(HeatStack destination, double maxTempDifference, boolean doHeat) {
		onContentsChanged();
		return super.coolMaxTempWithDestination(destination, maxTempDifference, doHeat);
	}

	@Override
	public double getTemperature() {
		return super.getTemperature();
	}

	@Override
	public double getEnergy() {
		return super.getEnergy();
	}

	@Override
	public double getWarmthCoefficient() {
		return super.getWarmthCoefficient();
	}

	public HeatStackInterface getTe() {
		return te;
	}

	@Override
	public void setTemperature(double energy) {
		onContentsChanged();
		super.setTemperature(energy);
	}

	@Override
	public boolean setEnergy(double energy) {
		onContentsChanged();
		return super.setEnergy(energy);
	}

	@Override
	public void setWarmthCoefficient(double warmthCoefficient) {
		onContentsChanged();
		super.setWarmthCoefficient(warmthCoefficient);
	}

	public void onContentsChanged() {
		te.updateHeatStacks();
	}

}
