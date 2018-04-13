package com.AppliedPhysics.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

    public static final int SAVEBLOCKGUI = 0;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
//			case SAVEBLOCKGUI:
//				return new ContainerSaveModel(player, (TileEntityMiniBlock) world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
//			case SAVEBLOCKGUI:
//				if(world.getTileEntity(new BlockPos(x, y, z)) != null) {
//					if(world.getTileEntity(new BlockPos(x, y, z)) instanceof TileEntityMiniBlock) {
//						return new GuiSaveModel((ContainerSaveModel) getServerGuiElement(ID, player, world, x, y, z), player, (TileEntityMiniBlock) world.getTileEntity(new BlockPos(x, y, z)));
//					}
//				}
            default:
                return null;
        }
    }
}