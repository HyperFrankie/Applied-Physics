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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			ItemStack stack = playerIn.getHeldItem(hand);
			System.out.println(stack);
			if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, facing)) {
				System.out.println("has fluid handler item capability");
				FluidTank tank = ((FluidTank) ((TileEntityTank) worldIn.getTileEntity(pos)).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing));
				if(tank != null) {
					if(FluidUtil.getFluidContained(stack) != null) {
						System.out.println("fluid in item != null");
						if(FluidUtil.getFluidContained(stack).amount <= tank.getCapacity() - tank.getFluidAmount() || !(stack.getItem() instanceof UniversalBucket)) {
							System.out.println("item fluid amount < than the empty space in the tank || the !(ItemStack.getItem() instanceof UniversalBucket)");
							FluidActionResult r = FluidUtil.tryEmptyContainer(stack, tank, tank.getCapacity() - tank.getFluidAmount(), playerIn, true);
							if(r.isSuccess()) {
								playerIn.setHeldItem(hand, r.getResult());
								System.out.println("r.getResult(): " + r.getResult());
								System.out.println("tank fluid: " + ((tank.getFluidAmount() != 0) ? tank.getFluid().getUnlocalizedName() : "empty"));
								System.out.println("tank level: " + tank.getFluidAmount());
								worldIn.getTileEntity(pos).markDirty();
							} else {
								System.out.println("no success");
							}
							return true;
						} else if(FluidUtil.getFluidContained(stack).amount == 0 && (tank.getFluidAmount() >= 1000 || !(stack.getItem() instanceof UniversalBucket))) {
							System.out.println("item fluid amount == 0 && either the fluid in the tank is >= 1000 || !(ItemStack.getItem() instanceof UniversalBucket)");
							FluidActionResult r = FluidUtil.tryFillContainer(stack, tank, tank.getFluidAmount(), playerIn,true);
							if(r.isSuccess()) {
								playerIn.setHeldItem(hand, r.getResult());
								System.out.println("r.getResult(): " + r.getResult());
								System.out.println("tank fluid: " + ((tank.getFluidAmount() != 0) ? tank.getFluid().getUnlocalizedName() : "empty"));
								System.out.println("tank level: " + tank.getFluidAmount());
								worldIn.getTileEntity(pos).markDirty();
							} else {
								System.out.println("no success");
							}
							return true;
						}
					}
					System.out.println("fluid in item == null || can't fill tank any further");
					FluidActionResult r = FluidUtil.tryFillContainer(stack, tank, tank.getFluidAmount(), playerIn,true);
//				    FluidStack s = FluidUtil.tryFluidTransfer(FluidUtil.getFluidHandler(stack), tank, tank.getFluidAmount(), true);
					if(r.isSuccess()) {
						playerIn.setHeldItem(hand, r.getResult());
						System.out.println("r.getResult(): " + r.getResult());
						System.out.println("tank fluid: " + ((tank.getFluidAmount() != 0) ? tank.getFluid().getUnlocalizedName() : "empty"));
						System.out.println("tank level: " + tank.getFluidAmount());
						worldIn.getTileEntity(pos).markDirty();
					} else {
						System.out.println("no success");
					}
				}
				return true;
			}
			return true;
		}
		return true;
	}

	@Nonnull
	public static FluidActionResult tryFillContainer(@Nonnull ItemStack container, IFluidHandler fluidSource, int maxAmount, @Nullable EntityPlayer player, boolean doFill) {
		ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
		IFluidHandlerItem containerFluidHandler = FluidUtil.getFluidHandler(containerCopy);
		if (containerFluidHandler != null) {
			FluidStack simulatedTransfer = FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, false);
			System.out.println(simulatedTransfer);
			if (simulatedTransfer != null) {
				if (doFill) {
					FluidUtil.tryFluidTransfer(containerFluidHandler, fluidSource, maxAmount, true);
					if (player != null) {
						SoundEvent soundevent = simulatedTransfer.getFluid().getFillSound(simulatedTransfer);
						player.world.playSound(null, player.posX, player.posY + 0.5, player.posZ, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
				else {
					containerFluidHandler.fill(simulatedTransfer, true);
				}

				ItemStack resultContainer = containerFluidHandler.getContainer();
				return new FluidActionResult(resultContainer);
			}
		}
		return FluidActionResult.FAILURE;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromAngleOrFacing(world, placer, null, pos);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		IBlockState stateAfterUpdate = getStateFromAngleOrFacing(worldIn, null, state.getValue(FACING).getOpposite(), pos);
		if(stateAfterUpdate.getValue(CONNECTIONS) != state.getValue(CONNECTIONS)) {
			worldIn.setBlockState(pos, state.withProperty(CONNECTIONS, stateAfterUpdate.getValue(CONNECTIONS)), 11);
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
		} else {
			return ((state.getValue(Tank.FACING).getIndex() - 2) * CONNECTIONS.getAllowedValues().size()) + state.getValue(this.CONNECTIONS);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta)
				.withProperty(FACING, EnumFacing.getFront((meta - (meta % CONNECTIONS.getAllowedValues().size())) / CONNECTIONS.getAllowedValues().size() + 2))
				.withProperty(CONNECTIONS, meta % CONNECTIONS.getAllowedValues().size());
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

	private IBlockState getStateFromAngleOrFacing(World world, @Nullable EntityLivingBase placer, @Nullable EnumFacing blockFacing, BlockPos pos) {
		EnumFacing f;
		if(placer != null) {
			f = EnumFacing.fromAngle(placer.rotationYawHead);
		} else {
			f = blockFacing;
		}
		IBlockState target = world.getBlockState(pos);
		IBlockState backFromTarget = world.getBlockState(pos.offset(f.getOpposite()));
		IBlockState frontFromTarget = world.getBlockState(pos.offset(f));
		if(backFromTarget.getBlock() instanceof Tank && frontFromTarget.getBlock() instanceof Tank) {
			if(
					Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(backFromTarget.getValue(FACING))
							&& Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(frontFromTarget.getValue(FACING))
					) {
				return this.blockState.getBaseState()
						.withProperty(FACING, f.getOpposite())
						.withProperty(CONNECTIONS, 3);
			}
		}
//		System.out.println("not both instanceof tank");
		if(backFromTarget.getBlock() instanceof Tank) {
//			System.out.println("backFromTarget instanceof tank");
			if(Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(backFromTarget.getValue(FACING))) {
//				System.out.println("aligned over the same axis");
				return this.blockState.getBaseState()
						.withProperty(FACING, f.getOpposite())
						.withProperty(CONNECTIONS, 1);
			}
		}
		if(frontFromTarget.getBlock() instanceof Tank) {
//			System.out.println("frontFromTarget instanceof tank");
			if(Arrays.asList(new EnumFacing[]{f, f.getOpposite()}).contains(frontFromTarget.getValue(FACING))) {
//				System.out.println("aligned over the same axis");
				return this.blockState.getBaseState()
						.withProperty(FACING, f.getOpposite())
						.withProperty(CONNECTIONS, 2);
			}
		}
		return this.blockState.getBaseState().withProperty(FACING, f.getOpposite()).withProperty(CONNECTIONS, 0);
	}

}
