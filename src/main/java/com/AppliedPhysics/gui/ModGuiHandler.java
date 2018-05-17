package com.AppliedPhysics.gui;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityIndustrialFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

    public static final int INDUSTRIAL_FURNACE = 0;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
			case INDUSTRIAL_FURNACE:
				return new ContainerIndustrialFurnace(player.inventory, (TileEntityIndustrialFurnace) world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
			case INDUSTRIAL_FURNACE:
				if(world.getTileEntity(new BlockPos(x, y, z)) != null) {
					if(world.getTileEntity(new BlockPos(x, y, z)) instanceof TileEntityIndustrialFurnace) {
						return new GuiIndustrialFurnace((ContainerIndustrialFurnace) getServerGuiElement(ID, player, world, x, y, z), player.inventory);
					}
				}
            default:
                return null;
        }
    }
}