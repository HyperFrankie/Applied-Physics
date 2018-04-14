package com.AppliedPhysics.block.TileEntityBlocks.Blocks;

import com.AppliedPhysics.block.TileEntityBlocks.BlockTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityTank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			System.out.println("block activated");
			Block block = state.getBlock();
			ItemStack stack = playerIn.getHeldItemMainhand();
			System.out.println(stack.toString());
			if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, facing)) {
				System.out.println("has fluid handler item capability");
				FluidTank tank = ((TileEntityTank) worldIn.getTileEntity(pos)).tank;
				IFluidHandlerItem c = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, facing);
				if(FluidUtil.getFluidContained(playerIn.getHeldItemMainhand()) != null) {
					if(!(FluidUtil.getFluidContained(stack).amount > tank.getCapacity() - tank.getFluidAmount())) {
						FluidActionResult r = FluidUtil.tryEmptyContainer(stack, tank, tank.getCapacity() - tank.getFluidAmount(), playerIn, true);
						if(r.isSuccess()) {
							playerIn.inventory.setInventorySlotContents(playerIn.inventory.getSlotFor(playerIn.getHeldItemMainhand()), r.getResult());
							System.out.println("r.getResult(): " + r.getResult());
							System.out.println("tank fluid: " + ((tank.getFluidAmount() != 0) ? tank.getFluid().getUnlocalizedName() : "empty"));
							System.out.println("tank level: " + tank.getFluidAmount());
							((TileEntityTank) worldIn.getTileEntity(pos)).updateFluidValue();
//						System.out.println("fluid in item after tryEmpty: " + FluidUtil.getFluidContained(playerIn.getHeldItemMainhand()).amount);
						} else {
							System.out.println("no success");
						}
						return true;
					} else if(FluidUtil.getFluidContained(stack).amount == 0) {
						FluidActionResult r = FluidUtil.tryFillContainer(stack, tank, tank.getCapacity() - tank.getFluidAmount(), playerIn,true);
						if(r.isSuccess()) {
							playerIn.inventory.setInventorySlotContents(playerIn.inventory.getSlotFor(playerIn.getHeldItemMainhand()), r.getResult());
							System.out.println("r.getResult(): " + r.getResult());
							System.out.println("tank fluid: " + ((tank.getFluidAmount() != 0) ? tank.getFluid().getUnlocalizedName() : "empty"));
							System.out.println("tank level: " + tank.getFluidAmount());
							((TileEntityTank) worldIn.getTileEntity(pos)).updateFluidValue();
//						System.out.println("fluid in item after tryFill: " + FluidUtil.getFluidContained(playerIn.getHeldItemMainhand()).amount);
						} else {
							System.out.println("no success");
						}
						return true;
					}
				}
				System.out.println("fluid contained is null or can't fill tank any further");
				FluidActionResult r = FluidUtil.tryFillContainer(stack, tank, tank.getCapacity() - tank.getFluidAmount(), playerIn,true);
				if(r.isSuccess()) {
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.getSlotFor(playerIn.getHeldItemMainhand()), r.getResult());
					System.out.println("r.getResult(): " + r.getResult());
					System.out.println("tank fluid: " + ((tank.getFluidAmount() != 0) ? tank.getFluid().getUnlocalizedName() : "empty"));
					System.out.println("tank level: " + tank.getFluidAmount());
					((TileEntityTank) worldIn.getTileEntity(pos)).updateFluidValue();
//				System.out.println("fluid in item after tryFill: " + FluidUtil.getFluidContained(playerIn.getHeldItemMainhand()).amount);
				} else {
					System.out.println("no success");
				}
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing f = EnumFacing.fromAngle(placer.rotationYawHead);
		System.out.println(f);
		IBlockState target = world.getBlockState(pos);
		IBlockState backFromTarget = world.getBlockState(pos.offset(f.getOpposite()));
		System.out.println(backFromTarget);
		IBlockState frontFromTarget = world.getBlockState(pos.offset(f));
		System.out.println(frontFromTarget);
		if(
				backFromTarget.getBlock() instanceof Tank
				&& frontFromTarget.getBlock() instanceof Tank
				) {
			System.out.println("both instanceof tank");
			if(
					Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(backFromTarget.getValue(FACING))
				&& Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(frontFromTarget.getValue(FACING))
					) {
				System.out.println("both aligned over the same axis");
				return this.blockState.getBaseState()
						.withProperty(FACING, f.getOpposite())
						.withProperty(CONNECTIONS, 3);
			}
		}
		System.out.println("not both instanceof tank");
		if(backFromTarget.getBlock() instanceof Tank) {
			System.out.println("backFromTarget instanceof tank");
			if(Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(backFromTarget.getValue(FACING))) {
				System.out.println("aligned over the same axis");
				return this.blockState.getBaseState()
						.withProperty(FACING, f.getOpposite())
						.withProperty(CONNECTIONS, 1);
			}
		}
		if(frontFromTarget.getBlock() instanceof Tank) {
			System.out.println("frontFromTarget instanceof tank");
			if(Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(frontFromTarget.getValue(FACING))) {
				System.out.println("aligned over the same axis");
				return this.blockState.getBaseState()
						.withProperty(FACING, f.getOpposite())
						.withProperty(CONNECTIONS, 2);
			}
		}
	return this.blockState.getBaseState().withProperty(FACING, f.getOpposite()).withProperty(CONNECTIONS, 0);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		EnumFacing f = state.getValue(FACING).getOpposite();
		System.out.println(f);
		IBlockState backFromTarget = worldIn.getBlockState(pos.offset(f.getOpposite()));
		System.out.println(backFromTarget);
		IBlockState frontFromTarget = worldIn.getBlockState(pos.offset(f));
		System.out.println(frontFromTarget);
		if(
				backFromTarget.getBlock() instanceof Tank
						&& frontFromTarget.getBlock() instanceof Tank
				) {
			if (
					Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(backFromTarget.getValue(FACING))
							&& Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(frontFromTarget.getValue(FACING))
					) {
				if(state.getValue(CONNECTIONS) != 3) {
					worldIn.setBlockState(pos, state.withProperty(CONNECTIONS, 3), 11);
				}
			}
		} else if(backFromTarget.getBlock() instanceof Tank) {
			if (Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(backFromTarget.getValue(FACING))) {
				if(state.getValue(CONNECTIONS) != 1) {
					worldIn.setBlockState(pos, state.withProperty(CONNECTIONS, 1), 11);
				}
			}
		} else if(frontFromTarget.getBlock() instanceof Tank) {
			if (Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(frontFromTarget.getValue(FACING))) {
				if(state.getValue(CONNECTIONS) != 2) {
					worldIn.setBlockState(pos, state.withProperty(CONNECTIONS, 2), 11);
				}
			}
		} else {
			if(state.getValue(CONNECTIONS) != 0) {
				worldIn.setBlockState(pos, state.withProperty(CONNECTIONS, 0), 11);
			}
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		if(worldIn.getTileEntity(pos) != null) {
			if(worldIn.getTileEntity(pos) instanceof TileEntityTank) {
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
				.withProperty(FACING, EnumFacing.getFront((meta - (meta % 4)) / CONNECTIONS.getAllowedValues().size() + 2))
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
