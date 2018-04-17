package com.AppliedPhysics;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
	public static Fluid STEAM = createFluid("steam", 100, 1000);

	private static Fluid createFluid(String fluidName, int density, int viscosity) {
		return new Fluid(
				fluidName,
				new ResourceLocation(AppliedPhysics.MODID + ":" + fluidName + "_still"),
				new ResourceLocation(AppliedPhysics.MODID + ":" + fluidName + "_flowing")
		)
					.setDensity(density)
					.setViscosity(viscosity);
	}

	public static void register() {
		STEAM = createAndRegister(STEAM);
	}

	public static Fluid createAndRegister(Fluid fluid) {
		if (FluidRegistry.registerFluid(fluid)) {
			return fluid;
		} else return FluidRegistry.getFluid(fluid.getName());
	}
}