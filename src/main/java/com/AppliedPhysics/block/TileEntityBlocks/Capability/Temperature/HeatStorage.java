package com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class HeatStorage implements Capability.IStorage<ITemperatureHandler> {

	@Override
	public NBTBase writeNBT(Capability<ITemperatureHandler> capability, ITemperatureHandler instance, EnumFacing side) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setDouble("temperature", instance.getTemperature());
		tagCompound.setDouble("energy", instance.getEnergy());
		tagCompound.setDouble("warmthCoefficient", instance.getWarmthCoefficient());
		return tagCompound;
	}


	@Override
	public void readNBT(Capability<ITemperatureHandler> capability, ITemperatureHandler instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tagCompound = (NBTTagCompound) nbt;
		instance.setTemperature(tagCompound.getDouble("temperature"));
		instance.setEnergy(tagCompound.getDouble("energy"));
		instance.setWarmthCoefficient(tagCompound.getDouble("warmthCoefficient"));
	}

}
