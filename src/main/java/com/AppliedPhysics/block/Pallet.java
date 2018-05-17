package com.AppliedPhysics.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Pallet extends BlockBase {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST}));
	public static final PropertyBool CONNECTED = PropertyBool.create("connected");
	public AxisAlignedBB
			AABBLower_nor = new AxisAlignedBB(0.0625,   0,      0,       1,      0.125, 1),
			AABBUpper_nor = new AxisAlignedBB(0,        0.0625, 0.0625,  1,      0.0625, 0.9375),
			AABBLower_rot = new AxisAlignedBB(0,        0,      0.0625,  1,      0.0625, 0.9375),
			AABBUpper_rot = new AxisAlignedBB(0.0625,   0.0625, 0,       1,      0.125, 1);
	public static List<AxisAlignedBB> rayBoxesNor = new ArrayList<AxisAlignedBB>() {{
		add(new AxisAlignedBB(0, 0.0625, 0, 1, 0.125, 0.1875));
		add(new AxisAlignedBB(0, 0.0625, 0.25, 1, 0.125, 0.4375));
		add(new AxisAlignedBB(0, 0.0625, 0.5, 1, 0.125, 0.75));
		add(new AxisAlignedBB(0, 0.0625, 0.8125, 1, 0.125, 1));
		add(new AxisAlignedBB(0.0625, 0, 0, 0.25, 0.0625, 1));
		add(new AxisAlignedBB(0.75, 0, 0, 0.9375, 0.0625, 1));
	}};
	public static List<AxisAlignedBB> rayBoxesNorCon = new ArrayList<AxisAlignedBB>() {{
		add(new AxisAlignedBB(0, 0.0625, 0, 1, 0.125, 0.1875));
		add(new AxisAlignedBB(0, 0.0625, 0.25, 1, 0.125, 0.4375));
		add(new AxisAlignedBB(0, 0.0625, 0.5, 1, 0.125, 0.6875));
		add(new AxisAlignedBB(0, 0.0625, 0.75, 1, 0.125, 0.9375));
		add(new AxisAlignedBB(0, 0, 0.0625, 1, 0.0625, 0.25));
		add(new AxisAlignedBB(0, 0, 0.75, 1, 0.0625, 0.9375));
	}};
	public static List<AxisAlignedBB> rayBoxesRot = new ArrayList<AxisAlignedBB>() {{
		add(new AxisAlignedBB(0, 0.0625, 0, 0.1875, 0.125, 1));
		add(new AxisAlignedBB(0.25, 0.0625, 0, 0.5, 0.125, 1));
		add(new AxisAlignedBB(0.5625, 0.0625, 0, 0.75, 0.125, 1));
		add(new AxisAlignedBB(0.8125, 0.0625, 0, 1, 0.125, 1));
		add(new AxisAlignedBB(0, 0, 0.0625, 1, 0.0625, 0.25));
		add(new AxisAlignedBB(0, 0, 0.75, 1, 0.0625, 0.9375));
	}};
	public static List<AxisAlignedBB> rayBoxesRotCon = new ArrayList<AxisAlignedBB>() {{
			add(new AxisAlignedBB(0.0625, 0.0625, 0, 0.25, 0.125, 1));
			add(new AxisAlignedBB(0.3125, 0.0625, 0, 0.5, 0.125, 1));
			add(new AxisAlignedBB(0.5625, 0.0625, 0, 0.75, 0.125, 1));
			add(new AxisAlignedBB(0.8125, 0.0625, 0, 1, 0.125, 1));
			add(new AxisAlignedBB(0, 0, 0.0625, 1, 0.0625, 0.25));
			add(new AxisAlignedBB(0, 0, 0.75, 1, 0.0625, 0.9375));
	}};

	public Pallet() {
		super(Material.WOOD, "pallet");
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(CONNECTED, false));
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		if (!isActualState) {
			state = state.getActualState(worldIn, pos);
		}

		if(state == null) {
			collidingBoxes.add(getBoundingBox(state, worldIn, pos));
		}
		if(state.getPropertyKeys().isEmpty()) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBLower_nor);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBUpper_nor);
		} else if(state.getPropertyKeys().contains(FACING)) {
			if(state.getValue(FACING) == EnumFacing.NORTH) {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBLower_nor);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBUpper_nor);
			} else {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBLower_rot);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBUpper_rot);
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 0.125, 1).offset(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return getBoundingBox(state, worldIn, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, CONNECTED);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing f = EnumFacing.fromAngle(placer.rotationYawHead);
		f = Arrays.asList(new EnumFacing[]{EnumFacing.WEST, EnumFacing.SOUTH}).contains(f) ? f.getOpposite() : f;
		System.out.println(f);
		IBlockState potentialConnection = world.getBlockState(pos.offset(f.getOpposite()));
		System.out.println(potentialConnection);
		if(potentialConnection.getBlock() instanceof Pallet) {
			System.out.println("pallet to potentially connect with found");
			if(Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(potentialConnection.getValue(FACING))) {
				System.out.println("pallet found has the same orientation");
				return this.blockState.getBaseState()
						.withProperty(FACING, f)
						.withProperty(CONNECTED, true);
			}
		}
		return this.blockState.getBaseState().withProperty(FACING, f).withProperty(CONNECTED, false);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
//		System.out.println("BUD");
		if(pos.offset(state.getValue(FACING).getOpposite()).equals(fromPos)) {
//			System.out.println("update block has effect on connection");
			if(worldIn.getBlockState(fromPos).getBlock() instanceof Pallet) {
				if(worldIn.getBlockState(fromPos).getValue(FACING) == state.getValue(FACING)) {
					if(!state.getValue(CONNECTED)) {
						worldIn.setBlockState(pos, state.withProperty(CONNECTED, true), 11);
					}
				}
			} else {
				if(state.getValue(CONNECTED)) {
					worldIn.setBlockState(pos, state.withProperty(CONNECTED, false), 11);
				}
			}
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (state.getPropertyKeys().isEmpty()) {
			return 0;
		} else {
			return (state.getValue(FACING) == EnumFacing.NORTH ? 0 : 2) + (state.getValue(CONNECTED) ? 1 : 0);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.blockState.getBaseState()
				.withProperty(FACING, (meta - (meta % 2) == 0) ? EnumFacing.NORTH : EnumFacing.EAST)
				.withProperty(CONNECTED, (meta % 2 == 0) ? false : true);
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
		return ray(pos, start, end, worldIn);
	}

	@Nullable
	@Override
	protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
		return ray(pos, start, end, null);
	}

	@Nullable
	protected RayTraceResult ray(BlockPos pos, Vec3d start, Vec3d end, @Nullable World world) {
		RayTraceResult raytraceresultNearest = null;
		RayTraceResult raytraceresult = null;
		List<AxisAlignedBB> bbs;
		if(world == null) {
			bbs = rayBoxesNor;
		} else {
			if(world.getBlockState(pos).getBlock() instanceof Pallet) {
				if(!world.getBlockState(pos).getPropertyKeys().isEmpty()) {
					bbs = (world.getBlockState(pos).getValue(FACING) == EnumFacing.NORTH) ?
							((world.getBlockState(pos).getValue(CONNECTED)) ? rayBoxesNorCon : rayBoxesNor) :
							((world.getBlockState(pos).getValue(CONNECTED)) ? rayBoxesRotCon : rayBoxesRot);
				} else {
					bbs = rayBoxesNor;
				}
			} else {
				bbs = rayBoxesNor;
			}
		}
		for(int i = 0; i < bbs.size(); i++) {
			Vec3d startVec = start.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
			Vec3d endVec = end.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
			raytraceresult = bbs.get(i).calculateIntercept(startVec, endVec);
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
