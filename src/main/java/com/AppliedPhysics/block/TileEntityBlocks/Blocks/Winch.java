package com.AppliedPhysics.block.TileEntityBlocks.Blocks;

import com.AppliedPhysics.block.TileEntityBlocks.BlockTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityWinch;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class Winch extends BlockTileEntity<TileEntityWinch> {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.VALUES));

	public enum EnumNumber implements IStringSerializable {
		n1,
		n2,
		n3,
		n4,
		n5,
		n6;

		public static String[] names = new String[values().length];
		static
		{
			for(EnumNumber num : values())
				names[num.ordinal()] = num.toString();
		}

		public String getName()
		{
			return toString();
		}
	}

	public static final PropertyEnum<EnumNumber> TOTAL_ROPE_LENGTH = PropertyEnum.create("total_rope_length", EnumNumber.class);

	public Winch(Material material, String name) {
		super(material, name);
		System.out.println(this.blockState);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.UP)
				.withProperty(TOTAL_ROPE_LENGTH, EnumNumber.n1));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		if(placer.rotationPitch < -55 || placer.rotationPitch > 55) {
			return this.blockState.getBaseState().withProperty(FACING, placer.rotationPitch < -55 ? EnumFacing.DOWN : EnumFacing.UP);
		}
		if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
			return this.blockState.getBaseState().withProperty(FACING, EnumFacing.fromAngle(placer.rotationYawHead).getOpposite());
		}
		return this.blockState.getBaseState().withProperty(FACING, facing);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if(worldIn.getTileEntity(pos) != null) {
			if(worldIn.getTileEntity(pos) instanceof TileEntityWinch) {
				return state.withProperty(TOTAL_ROPE_LENGTH, ((TileEntityWinch) worldIn.getTileEntity(pos)).totalRopeLength);
			}
		}
		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TOTAL_ROPE_LENGTH, FACING);
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
	public Class<TileEntityWinch> getTileEntityClass() {
		return TileEntityWinch.class;
	}

	@Override
	public TileEntityWinch createTileEntity(World world, IBlockState state) {
		return new TileEntityWinch();
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

//	@Override
//	public BlockRenderLayer getBlockLayer() {
//		return BlockRenderLayer.TRANSLUCENT;
//	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = this.getActualState(state, source, pos);
		if(state.getBlock() instanceof Winch) {
			if(state.getProperties().containsKey("facing")) {
				EnumFacing f = source.getBlockState(pos).getValue(Winch.FACING);
				return new AxisAlignedBB(
						!Arrays.asList(EnumFacing.HORIZONTALS).contains(f) ? 0.0625: 0,
						Arrays.asList(EnumFacing.HORIZONTALS).contains(f) ? 0.0625: 0,
						0.0625,
						0.9375,
						Arrays.asList(EnumFacing.HORIZONTALS).contains(f) ? 0.9375: 1.0,
						0.9375
				);
			}
		}
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return getBoundingBox(state, worldIn, pos).offset(pos);
	}
}