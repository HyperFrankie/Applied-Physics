package com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public interface ITemperatureHandler {

	@CapabilityInject(ITemperatureHandler.class)
	public static Capability<ITemperatureHandler> HEAT_HANDLER_CAPABILITY = null;

	@Nullable
	HeatStack heat(HeatStack resource, boolean doHeat);

	@Nullable
	HeatStack heatMaxEnergy(double maxEnergyDifference, boolean doHeat);

	@Nullable
	HeatStack heatMaxEnergyWithResource(HeatStack resource, double maxEnergyDifference, boolean doHeat);

	@Nullable
	HeatStack heatMaxTemp(double maxTempDifference, boolean doHeat);

	@Nullable
	HeatStack heatMaxTempWithResource(HeatStack resource, double maxTempDifference, boolean doHeat);






	@Nullable
	HeatStack cool(HeatStack destination, boolean doHeat);

	@Nullable
	HeatStack coolMaxEnergy(double maxEnergyDifference, boolean doHeat);

	@Nullable
	HeatStack coolMaxEnergyWithDestination(HeatStack destination, double maxEnergyDifference, boolean doHeat);

	@Nullable
	HeatStack coolMaxTemp(double maxTempDifference, boolean doHeat);

	@Nullable
	HeatStack coolMaxTempWithDestination(HeatStack destination, double maxTempDifference, boolean doHeat);

	double getTemperature();

	double getEnergy();

	double getWarmthCoefficient();

	void setTemperature(double temperature);

	boolean setEnergy(double energy);

	void setWarmthCoefficient(double warmthCoefficient);
}
