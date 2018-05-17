package com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityWinch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class TEFRWinch extends TileEntitySpecialRenderer<TileEntityWinch> {
	@Override
	public void render(TileEntityWinch te1, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//	@Override
//	public void renderTileEntityFast(TileEntityWinch te1, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
		if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) != null) {
			if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) instanceof TileEntityWinch) {
				TileEntityWinch te = (TileEntityWinch) Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos());
//				int s = te.positionDataArrayTop.size();
				int s = te.positionDataTop.size();
				BufferBuilder buffer = Tessellator.getInstance().getBuffer();
				GlStateManager.pushMatrix();
				GlStateManager.pushAttrib();
				buffer.setTranslation(x, y, z);

				buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
				GL11.glPointSize(10);
				GL11.glLineWidth(2);
				buffer.color(255, 0, 0, 255);

//				buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
//				buffer.color(255, 0, 0, 255);
//				for(int i = 0; i < s; i++) {
//					BlockPos[] vertexData = te.positionDataArrayTop.get(i);
//					for(int j = 0; j < 4; j++) {
//						BlockPos p = vertexData[j];
//						buffer.pos(p.getX(), p.getY(), p.getZ()).tex(te.uDataArrayTop[i][j], te.vDataArrayTop[i][j]).endVertex();
//					}
//					buffer.endVertex();
//				}
				for(int i = 0; i < s - 2; i++) {
					Vec3d pos0 = te.positionDataTop.get(i);
					Vec3d pos1 = te.positionDataTop.get(i + 1);
					Vec3d pos2 = te.positionDataTop.get(i + 2);
					buffer.pos(
							pos0.x
//									+ te.getPos().getX()
							, pos0.y
//									+ te.getPos().getY()
							, pos0.z
//									+ te.getPos().getZ()
					);
					buffer.pos(
							pos1.x
//									+ te.getPos().getX()
							, pos1.y
//									+ te.getPos().getY()
							, pos1.z
//									+ te.getPos().getZ()
					);
					buffer.pos(
							pos2.x
//									+ te.getPos().getX()
							, pos2.y
//									+ te.getPos().getY()
							, pos2.z
//									+ te.getPos().getZ()
					);
//					buffer.pos(0, 0, 0).endVertex();
//					buffer.pos(1, 1, 1).endVertex();
				}
				buffer.endVertex();

				Tessellator.getInstance().draw();
				buffer.setTranslation(0, 0, 0);
				GlStateManager.popAttrib();
				GlStateManager.popMatrix();
			}
		}
	}

}
