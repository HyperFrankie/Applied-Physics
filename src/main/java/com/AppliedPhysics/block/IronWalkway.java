package com.AppliedPhysics.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IronWalkway extends BlockBase {

	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");
	public AxisAlignedBB bb = new AxisAlignedBB(0, 0, 0, 1, 0.0625, 1);
	public static List<AxisAlignedBB> rayBoxes = new ArrayList<AxisAlignedBB>()	{{
			add(new AxisAlignedBB(0,        0, 0, 0.0625,   0.0625, 1));
			add(new AxisAlignedBB(0.1875,   0, 0, 0.25,     0.0625, 1));
			add(new AxisAlignedBB(0.375,    0, 0, 0.4375,   0.0625, 1));
			add(new AxisAlignedBB(0.5625,   0, 0, 0.625,    0.0625, 1));
			add(new AxisAlignedBB(0.75,     0, 0, 0.8125,   0.0625, 1));
			add(new AxisAlignedBB(0.9375,   0, 0, 1,        0.0625, 1));
			add(new AxisAlignedBB(0, 0, 0,     1,   0.0625, 0.0625));
			add(new AxisAlignedBB(0, 0, 0.1875,1,   0.0625, 0.25));
			add(new AxisAlignedBB(0, 0, 0.375, 1,   0.0625, 0.4375));
			add(new AxisAlignedBB(0, 0, 0.5625,1,   0.0625, 0.625));
			add(new AxisAlignedBB(0, 0, 0.75,  1,   0.0625, 0.8125));
			add(new AxisAlignedBB(0, 0, 0.9375,1,   0.0625, 1));
	}};

	public IronWalkway(Material material, String name) {
		super(material, name);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(NORTH, false)
				.withProperty(EAST, false)
				.withProperty(SOUTH, false)
				.withProperty(WEST, false));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, bb);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bb.offset(pos);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return getBoundingBox(state, worldIn, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.blockState.getBaseState()
				.withProperty(NORTH, world.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() instanceof IronWalkway)
				.withProperty(EAST, world.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() instanceof IronWalkway)
				.withProperty(SOUTH, world.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() instanceof IronWalkway)
				.withProperty(WEST, world.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() instanceof IronWalkway);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return this.blockState.getBaseState()
				.withProperty(NORTH, worldIn.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() instanceof IronWalkway)
				.withProperty(EAST, worldIn.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() instanceof IronWalkway)
				.withProperty(SOUTH, worldIn.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() instanceof IronWalkway)
				.withProperty(WEST, worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() instanceof IronWalkway);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		System.out.println("BUD at " + pos);
		boolean north = (worldIn.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() instanceof IronWalkway),
				east = (worldIn.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() instanceof IronWalkway),
				south = (worldIn.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() instanceof IronWalkway),
				west = (worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() instanceof IronWalkway);
		if(!(
						state.getValue(NORTH) == north &&
						state.getValue(EAST) == east &&
						state.getValue(SOUTH) == south &&
						state.getValue(WEST) == west
		)) {
			worldIn.setBlockState(pos, this.blockState.getBaseState()
					.withProperty(NORTH, north)
					.withProperty(EAST, east)
					.withProperty(SOUTH, south)
					.withProperty(WEST, west), 11);
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.blockState.getBaseState()
				.withProperty(NORTH, false)
				.withProperty(EAST, false)
				.withProperty(SOUTH, false)
				.withProperty(WEST, false);
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

	@Nullable
	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		return rayTrace(pos, start, end, new AxisAlignedBB(0, 0, 0, 1, 1, 1));
	}

	@Nullable
	@Override
	protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
		RayTraceResult raytraceresultNearest = null;
		RayTraceResult raytraceresult = null;
		for(int i = 0; i < rayBoxes.size(); i++) {
			Vec3d startVec = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
			Vec3d endVec = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
			raytraceresult = rayBoxes.get(i).calculateIntercept(startVec, endVec);
			raytraceresult = raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), raytraceresult.sideHit, pos);

			if(raytraceresultNearest == null) {
				raytraceresultNearest = raytraceresult;
			} else if(raytraceresult != null && raytraceresultNearest != null) {
				if(raytraceresult.hitVec.distanceTo(start) < raytraceresultNearest.hitVec.distanceTo(start)) {
					raytraceresultNearest = raytraceresult;
				}
			}
		}
		return raytraceresultNearest;
	}
}
