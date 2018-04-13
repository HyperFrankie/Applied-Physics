package com.AppliedPhysics.block.TileEntityBlocks;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityRod;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class RodBase extends BlockTileEntity<TileEntityRod> {

	public enum EnumNumber implements IStringSerializable {
		r1,
		r2,
		r3;

		public static String[] names = new String[values().length];
		static
		{
			for(RodBase.EnumNumber num : values())
				names[num.ordinal()] = num.toString();
		}

		public String getName()
		{
			return toString();
		}
	}

	public static final PropertyEnum<RodBase.EnumNumber> ROTATION_STATE = PropertyEnum.create("rotation_state", RodBase.EnumNumber.class);

	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.VALUES));

	public RodBase(Material material, String name) {
		super(material, name);
		System.out.println(this.blockState);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.UP)
				.withProperty(ROTATION_STATE, RodBase.EnumNumber.r1));
	}

	public int getMetaFromState(IBlockState state) {
		if (state.getPropertyKeys().isEmpty()) {
			return 0;
		}
		else {
			return state.getValue(this.FACING).getIndex();
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROTATION_STATE, FACING);
	}

	@Override
	public Class<TileEntityRod> getTileEntityClass() {
		return TileEntityRod.class;
	}

	@Override
	public TileEntityRod createTileEntity(World world, IBlockState state) { return new TileEntityRod(); }

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

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) { return new AxisAlignedBB(0, 0, 0, 1, 1, 1); }

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) { return getBoundingBox(state, worldIn, pos).offset(pos); }
}
