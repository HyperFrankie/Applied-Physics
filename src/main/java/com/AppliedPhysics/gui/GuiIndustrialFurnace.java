package com.AppliedPhysics.gui;

import com.AppliedPhysics.AppliedPhysics;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiIndustrialFurnace extends GuiContainer {

	ContainerIndustrialFurnace container;
	public static ResourceLocation BG_TEXTURE = new ResourceLocation(AppliedPhysics.MODID, "textures/gui/industrial_furnace_without_slots.png"), BG_TEXTURE_SLOTS = new ResourceLocation(AppliedPhysics.MODID, "textures/gui/industrial_furnace_slots.png");

	public GuiIndustrialFurnace(ContainerIndustrialFurnace container, InventoryPlayer playerInv) {
		super(container);
		this.container = container;
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		BG_TEXTURE = new ResourceLocation(AppliedPhysics.MODID, "textures/gui/industrial_furnace_without_slots.png");
//		BG_TEXTURE_SLOTS = new ResourceLocation(AppliedPhysics.MODID, "textures/gui/industrial_furnace_slots.png");
		GlStateManager.color(1, 1, 1, 1);
		int x = (width - this.getXSize()) / 2;
		int y = (height - this.getYSize()) / 2;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
		mc.getTextureManager().bindTexture(BG_TEXTURE_SLOTS);
		drawTexturedModalRect(x, y , 0, 0, this.getXSize(), this.getYSize());
		GL11.glPopMatrix();

		mc.getTextureManager().bindTexture(BG_TEXTURE);
		drawTexturedModalRect(x, y, 0, 0, this.getXSize(), this.getYSize());

//		System.out.println(this.container.furnace);
//		System.out.println(this.container.furnace.isBurning());

		if(this.container.furnace.isBurning()) {
//			System.out.println("is burning so drawing textures for when burning");
			int k = this.getBurnScaled(12);
			drawTexturedModalRect(x + 56, y + 6 + 12 - k, 176, 12 - k, 14, k + 1);

			int l = this.getBurnScaled(23);
			drawTexturedModalRect(x + 76, y + 23, 176, 14, l + 1, 16);
		}
	}

	private int getBurnScaled(double pixels) {
		double i = this.container.furnace.getField(1);

		if (i == 0.0) {
			i = 200.0;
		}

//		System.out.println("Math.floor result: Math.floor(" + ((double) this.container.furnace.getField(0)) + " * " + pixels + " / " + i + ") results in " + Math.floor(((double) this.container.furnace.getField(0)) * pixels / i));
		return (int) Math.floor(((double) this.container.furnace.getField(0)) * pixels / i);
	}
}
