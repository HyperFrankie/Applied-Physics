package com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class TEFRTankFluid extends TileEntitySpecialRenderer<TileEntityTank> {

	@Override
	public void render(TileEntityTank te1, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//		System.out.println("rendering");
//	@Override
//	public void renderTileEntityAt(TileEntityTank te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
		if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) != null) {
//			System.out.println("there is a TileEntity at pos");
			if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) instanceof TileEntityTank) {
//				System.out.println("TileEntity is instanceof TileEntityTank");
				TileEntityTank te = (TileEntityTank) Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos());
//				System.out.println("shouldRender: " + te.shouldRender);
				if (te.shouldRender) {
					BufferBuilder ver = Tessellator.getInstance().getBuffer();
					GlStateManager.enableRescaleNormal();
					GlStateManager.pushMatrix();
					ver.setTranslation(x, y, z);
					ver.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					ver.pos(te.maxX, te.content_height, te.maxZ).tex(te.minU, te.minV).endVertex();
					ver.pos(te.maxX, te.content_height, te.minZ).tex(te.minU, te.maxV).endVertex();
					ver.pos(te.minX, te.content_height, te.minZ).tex(te.maxU, te.maxV).endVertex();
					ver.pos(te.minX, te.content_height, te.maxZ).tex(te.maxU, te.minV).endVertex();
					Tessellator.getInstance().draw();
					ver.setTranslation(0, 0, 0);
					GlStateManager.popMatrix();
					GlStateManager.disableRescaleNormal();
					GlStateManager.disableBlend();
				}
			}
		}
	}
}
