package com.AppliedPhysics.block.TileEntityBlocks;

import com.AppliedPhysics.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockTileEntity<TE extends TileEntity> extends BlockBase {

	public BlockTileEntity(Material material, String name) {
		super(material, name);
	}
	
	public abstract Class<TE> getTileEntityClass();
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public abstract TE createTileEntity(World world, IBlockState state);

}