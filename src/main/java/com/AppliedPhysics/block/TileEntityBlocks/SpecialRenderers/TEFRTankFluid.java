package com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityTank;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class TEFRTankFluid extends TileEntitySpecialRenderer<TileEntityTank> {

	@Override
	public void render(TileEntityTank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//	@Override
//	public void renderTileEntityAt(TileEntityTank te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
		if(te.shouldRender) {
			BufferBuilder ver = Tessellator.getInstance().getBuffer();
			GlStateManager.enableRescaleNormal();
			GlStateManager.pushMatrix();
			ver.setTranslation(x, y, z);
			ver.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

//			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//			buffer.pos(te.maxX, te.content_height, te.maxZ).tex(te.maxU, te.minV).endVertex();
//			buffer.pos(te.maxX, te.content_height, te.minZ).tex(te.maxU, te.maxV).endVertex();
//			buffer.pos(te.minX, te.content_height, te.minZ).tex(te.minU, te.maxV).endVertex();
//			buffer.pos(te.minX, te.content_height, te.maxZ).tex(te.minU, te.minV).endVertex();
//			System.out.println("after buffer.pos");
//			ver.pos(1, 1, 1).tex(te.maxU, te.minV).endVertex();
//			ver.pos(1, 1, 0).tex(te.maxU, te.maxV).endVertex();
//			ver.pos(0, 1, 0).tex(te.minU, te.maxV).endVertex();
//			ver.pos(0, 1, 1).tex(te.minU, te.minV).endVertex();
			System.out.println("...");
			ver.pos(1, 0.9, 1).tex(0.25, 0.0).endVertex();
			ver.pos(1, 0.9, 0).tex(0.0, 0.25).endVertex();
			ver.pos(0, 0.9, 0).tex(0.0, 0.25).endVertex();
			ver.pos(0, 0.9, 1).tex(0.25, 0.0).endVertex();
			ver.setTranslation(0, 0, 0);

			Tessellator.getInstance().draw();
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}
}
