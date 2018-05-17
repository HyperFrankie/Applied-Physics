package com.AppliedPhysics.block.TileEntityBlocks.Blocks;

import com.AppliedPhysics.block.TileEntityBlocks.BlockTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityRod;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rod extends BlockTileEntity<TileEntityRod> {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.UP));
	public enum EnumRotation implements IStringSerializable {
		r0,
		r1,
		r2,
		r3;

		public static String[] names = new String[values().length];
		static {
			for(Rod.EnumRotation num : values())
				names[num.ordinal()] = num.toString();
		}

		public String getName()
		{
			return toString();
		}
	}
	public static final PropertyEnum<Rod.EnumRotation> ROTATION = PropertyEnum.create("rotation", Rod.EnumRotation.class);

	public AxisAlignedBB selectedBoxNorth = new AxisAlignedBB(0.375, 0.375, 0, 0.625, 0.625, 1);
	public AxisAlignedBB selectedBoxEast = new AxisAlignedBB(0, 0.375, 0.375, 1, 0.625, 0.625);
	public AxisAlignedBB selectedBoxUp = new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
	public AxisAlignedBB defaultBB = new AxisAlignedBB(0.375, 0.375, 0.375, 0.625, 0.625, 0.625);
	public static List<AxisAlignedBB> rayBoxesNorth = new ArrayList<AxisAlignedBB>() {{
		add(new AxisAlignedBB(0.373,    0.4375, 0,  0.625,  0.5625, 1));
		add(new AxisAlignedBB(0.4375,   0.373,  0,  0.5625, 0.4375, 1));
		add(new AxisAlignedBB(0.4375,   0.5625, 0,  0.5625, 0.625, 1));
	}};
	public static List<AxisAlignedBB> rayBoxesEast = new ArrayList<AxisAlignedBB>()	{{
		add(new AxisAlignedBB(0,    0.4375, 0.373,  1,  0.5625, 0.625));
		add(new AxisAlignedBB(0,    0.373,  0.4375, 1,  0.4375, 0.5625));
		add(new AxisAlignedBB(0,    0.5625, 0.4375, 1,  0.625, 0.5625));
	}};
	public static List<AxisAlignedBB> rayBoxesUp = new ArrayList<AxisAlignedBB>() {{
		add(new AxisAlignedBB(0.373,    0, 0.4375,  0.625,  1, 0.5625));
		add(new AxisAlignedBB(0.4375,   0, 0.373,   0.5625, 1, 0.4375));
		add(new AxisAlignedBB(0.4375,   0, 0.5625,  0.5625, 1, 0.625));
	}};

	public Rod(Material material, String name) {
		super(material, name);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(ROTATION, EnumRotation.r0));
	}

	@Override
	public TileEntityRod createTileEntity(World world, IBlockState state) {
		return new TileEntityRod();
	}

	@Override
	public Class<TileEntityRod> getTileEntityClass() {
		return TileEntityRod.class;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		List<AxisAlignedBB> bbs = rayBoxesNorth;
		if(worldIn.getBlockState(pos).getBlock() instanceof Rod) {
			if(!worldIn.getBlockState(pos).getPropertyKeys().isEmpty()) {
				EnumFacing f = worldIn.getBlockState(pos).getValue(FACING);
				bbs = (f == EnumFacing.NORTH) ? rayBoxesNorth : (f == EnumFacing.EAST ? rayBoxesEast : rayBoxesUp);
			}
		}
		for(AxisAlignedBB bb : bbs) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, bb);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(state.getBlock() instanceof Rod) {
			if (!state.getPropertyKeys().isEmpty()) {
				EnumFacing f = state.getValue(FACING);
				return ((f == EnumFacing.NORTH) ? selectedBoxNorth.offset(pos) : (f == EnumFacing.EAST ? selectedBoxEast.offset(pos) : selectedBoxUp.offset(pos)));
			}
		}
		return defaultBB.offset(pos);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return getBoundingBox(state, worldIn, pos);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ROTATION);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing f;
		boolean isPlacerVisualDirection = false;
		if(placer != null && !placer.isSneaking()) {
			isPlacerVisualDirection = true;
			if(placer.rotationPitch < -55 || placer.rotationPitch > 55) {
				f = EnumFacing.UP;
			} else {
				EnumFacing playerFacing = EnumFacing.fromAngle(placer.rotationYawHead);
				f = (FACING.getAllowedValues().contains(playerFacing)) ? playerFacing : playerFacing.getOpposite();
			}
		} else {
			f = facing;
			System.out.println(f);
			f = (FACING.getAllowedValues().contains(f)) ? f : f.getOpposite();
			System.out.println(f);
		}

		IBlockState potentialRod0 = world.getBlockState(pos.offset(f));
		IBlockState potentialRod1 = world.getBlockState(pos.offset(f.getOpposite()));
		EnumRotation rotation = EnumRotation.r0;
		if(potentialRod0.getBlock() instanceof Rod) {
			if(potentialRod0.getValue(FACING) == f) {
				if(potentialRod1.getBlock() instanceof Rod) {
					if(potentialRod1.getValue(FACING) == f) {
						IBlockState s = isPlacerVisualDirection ? world.getBlockState(pos.offset(EnumFacing.fromAngle(placer.rotationYawHead))) : world.getBlockState(pos.offset(facing));
						rotation = (s.getValue(ROTATION) != null) ? s.getValue(ROTATION) : EnumRotation.r0;
					}
				} else {
					rotation = potentialRod0.getValue(ROTATION);
				}
			}
		} else if(potentialRod1.getBlock() instanceof Rod) {
			if(potentialRod1.getValue(FACING) == f) {
				rotation = potentialRod1.getValue(ROTATION);
			}
		}

		return this.blockState.getBaseState()
				.withProperty(FACING, f)
				.withProperty(ROTATION, rotation);
	}

//	@Override
//	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
//		System.out.println("BUD at " + pos);
//		boolean rotation = (worldIn.getBlockState(pos).getValue(ROTATION));
//		if(!(
//				state.getValue(ROTATION) == rotation
//		)) {
//			worldIn.setBlockState(pos, this.blockState.getBaseState()
//					.withProperty(NORTH, north)
//					.withProperty(EAST, east)
//					.withProperty(SOUTH, south)
//					.withProperty(WEST, west), 11);
//		}
//	}

	@Override
	public int getMetaFromState(IBlockState state) {
		EnumFacing f = state.getValue(FACING);
		int r = state.getValue(ROTATION).ordinal();
		return ((f == EnumFacing.NORTH ? 0 : (f == EnumFacing.EAST ? 1 : 2)) * ROTATION.getAllowedValues().size()) + r;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		int facingNum = (meta - (meta % ROTATION.getAllowedValues().size())) / ROTATION.getAllowedValues().size();
		return super.getStateFromMeta(meta)
				.withProperty(FACING, (facingNum == 0 ? EnumFacing.NORTH : (facingNum == 1 ? EnumFacing.EAST : EnumFacing.UP)))
				.withProperty(ROTATION, EnumRotation.values()[meta % ROTATION.getAllowedValues().size()]);
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
		List<AxisAlignedBB> rayBoxes = rayBoxes = rayBoxesNorth;
		if(world == null) {
			rayBoxes = rayBoxesNorth;
		} else {
			if(world.getBlockState(pos).getBlock() instanceof Rod) {
				if(!world.getBlockState(pos).getPropertyKeys().isEmpty()) {
					EnumFacing f = world.getBlockState(pos).getValue(FACING);
					rayBoxes = (f == EnumFacing.NORTH) ? rayBoxesNorth : (f == EnumFacing.EAST ? rayBoxesEast : rayBoxesUp);
				}
			}
		}
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
