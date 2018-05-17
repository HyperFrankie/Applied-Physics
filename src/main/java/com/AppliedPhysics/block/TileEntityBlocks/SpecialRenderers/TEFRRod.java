package com.AppliedPhysics.block.TileEntityBlocks.SpecialRenderers;

import com.AppliedPhysics.block.TileEntityBlocks.Blocks.Rod;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityRod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;

import java.util.HashMap;
import java.util.Map;

public class TEFRRod extends TileEntitySpecialRenderer<TileEntityRod> {

	protected static BlockRendererDispatcher blockRenderer;
	Map<EnumFacing, IBakedModel> models = new HashMap<>();

	@Override
	public void render(TileEntityRod te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		System.out.println("rendering");
		if(blockRenderer == null) {
			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		}
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = getWorld().getBlockState(pos);
		if(models.isEmpty()) {
			for(Object o : Arrays.asList(new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.UP})) {
				EnumFacing f = (EnumFacing) o;
				models.put(f, blockRenderer.getBlockModelShapes().getModelForState(state.withProperty(Rod.FACING, f)));
			}
		}

		BufferBuilder buffer = Tessellator.getInstance().getBuffer();

		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		buffer.setTranslation(x + 1, y, z);
		buffer.endVertex();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		EnumFacing f = state.getValue(Rod.FACING);
		IBakedModel model = models.get(f);
		GlStateManager.rotate(te.rotation, (f == EnumFacing.EAST ? 1.0f : 0.0f), (f == EnumFacing.UP ? 1.0f : 0.0f), (f == EnumFacing.NORTH ? 1.0f : 0.0f));
		blockRenderer.getBlockModelRenderer().renderModel(getWorld(), model, state, pos, buffer, true);

		buffer.setTranslation(0, 0, 0);
		Tessellator.getInstance().draw();
		GlStateManager.rotate(0.0f, 0.0f, 0.0f, 0.0f);
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

//	@Override
//	public void renderTileEntityFast(TileEntityTank te1, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
//
////		if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) != null) {
////			if (Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos()) instanceof TileEntityRod) {
////				TileEntityRod te = (TileEntityRod) Minecraft.getMinecraft().getIntegratedServer().getWorld(Minecraft.getMinecraft().world.provider.getDimension()).getTileEntity(te1.getPos());
//		if(blockRenderer == null) {
//			blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
//		}
//		BlockPos pos = new BlockPos(x, y, z);
//		IBlockState state = getWorld().getBlockState(pos);
//		if(models.isEmpty()) {
//			for(Object o : Arrays.asList(new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.UP})) {
//				EnumFacing f = (EnumFacing) o;
//				models.put(f, blockRenderer.getBlockModelShapes().getModelForState(state.withProperty(Rod.FACING, f)));
//			}
//		}
//		GlStateManager.enableRescaleNormal();
//		GlStateManager.pushMatrix();
//		buffer.setTranslation(x, y, z);
//		EnumFacing f = state.getValue(Rod.FACING);
//		IBakedModel model = models.get(f);
//		blockRenderer.getBlockModelRenderer().renderModel(getWorld(), model, state, pos, buffer, true);
//
////			}
////		}
//	}
}
