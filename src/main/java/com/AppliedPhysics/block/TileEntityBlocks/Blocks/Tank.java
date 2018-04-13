package com.AppliedPhysics.block.TileEntityBlocks.Blocks;

import com.AppliedPhysics.block.TileEntityBlocks.BlockTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityTank;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityWinch;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class Tank extends BlockTileEntity<TileEntityTank> {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.HORIZONTALS));
	public static final PropertyInteger CONNECTIONS = PropertyInteger.create("connections", 0, 3);

	public Tank() {
		super(Material.IRON, "tank");
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(CONNECTIONS, 0));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
	return this.blockState.getBaseState().withProperty(FACING, EnumFacing.fromAngle(placer.rotationYawHead).getOpposite());
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if(worldIn.getTileEntity(pos) != null) {
			if(worldIn.getTileEntity(pos) instanceof TileEntityWinch) {
				return state
						.withProperty(FACING, ((TileEntityTank) worldIn.getTileEntity(pos)).facing)
						.withProperty(CONNECTIONS, ((TileEntityTank) worldIn.getTileEntity(pos)).connections);
			}
		}
		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, CONNECTIONS);
	}

	public int getMetaFromState(IBlockState state) {
		if (state.getPropertyKeys().isEmpty()) {
			return 0;
		}
		else {
			return ((state.getValue(Tank.FACING).getIndex() - 2) * CONNECTIONS.getAllowedValues().size()) + state.getValue(this.CONNECTIONS);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta)
				.withProperty(FACING, EnumFacing.getFront(meta - (meta % 4) / CONNECTIONS.getAllowedValues().size()))
				.withProperty(CONNECTIONS, meta % 4);
	}

	@Override
	public Class<TileEntityTank> getTileEntityClass() { return TileEntityTank.class; }

	@Override
	public TileEntityTank createTileEntity(World world, IBlockState state) {
		return new TileEntityTank();
	}

	@Override
	public boolean isTranslucent(IBlockState i) {
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
