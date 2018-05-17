package com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityIndustrialFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TEFRIndustrialFurnace extends FastTESRBlockLoc<TileEntityIndustrialFurnace> {

	@Override
	public void renderTileEntityFast(TileEntityIndustrialFurnace te1, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
		if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) != null) {
			if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) instanceof TileEntityIndustrialFurnace) {
				TileEntityIndustrialFurnace te = (TileEntityIndustrialFurnace) Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos());
				if (te.isBurning()) {
					if(te.getCurrentBurnItem().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) != null) {

					}
//					GlStateManager.enableRescaleNormal();
//					GlStateManager.pushMatrix();
//					buffer.setTranslation(x, y, z);
//					buffer.pos(te.maxX, te.content_height, te.maxZ).tex(te.minU, te.minV).endVertex();
//					buffer.pos(te.maxX, te.content_height, te.minZ).tex(te.minU, te.maxV).endVertex();
//					buffer.pos(te.minX, te.content_height, te.minZ).tex(te.maxU, te.maxV).endVertex();
//					buffer.pos(te.minX, te.content_height, te.maxZ).tex(te.maxU, te.minV).endVertex();
//					buffer.endVertex();
//					GlStateManager.popMatrix();
//					GlStateManager.disableRescaleNormal();

				}
			}
		}
	}

}
