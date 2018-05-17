package com.AppliedPhysics.block.TileEntityBlocks.Blocks;

import com.AppliedPhysics.AppliedPhysics;
import com.AppliedPhysics.block.TileEntityBlocks.BlockTileEntity;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.ITemperatureHandler;
import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityIndustrialFurnace;
import com.AppliedPhysics.gui.ModGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class IndustrialFurnace extends BlockTileEntity<TileEntityIndustrialFurnace> {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public IndustrialFurnace(Material material, String name) {
		super(material, name);
		setDefaultState(blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH));
		blockParticleGravity = 1.0F;
		lightOpacity = 20; // cast a light shadow
		setTickRandomly(false);
		useNeighborBrightness = false;
	}

	@Override
	protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING); }


	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing f = EnumFacing.fromAngle(placer.rotationYawHead);
		System.out.println(f);
		return this.blockState.getBaseState()
				.withProperty(FACING, f);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
//		System.out.println("BUD in furnace : 00");
		if(world.getTileEntity(pos) != null) {
//			System.out.println("BUD in furnace : 01");
			if(world.getTileEntity(pos) instanceof TileEntityIndustrialFurnace) {
//				System.out.println("BUD in furnace : 02");
				for(EnumFacing f : EnumFacing.VALUES) {
//					System.out.println("BUD in furnace : 03");
					if(pos.offset(f).equals(neighbor)) {
//						System.out.println("BUD in furnace : 04");
						if(world.getTileEntity(neighbor) != null) {
//							System.out.println("BUD in furnace : 05");
							if(world.getTileEntity(neighbor).getCapability(ITemperatureHandler.HEAT_HANDLER_CAPABILITY, f.getOpposite()) != null) {
//								System.out.println("BUD in furnace : 06");
								((TileEntityIndustrialFurnace) world.getTileEntity(pos)).connectedCapableBlocks.put(world.getTileEntity(neighbor), f);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(AppliedPhysics.instance,
				ModGuiHandler.INDUSTRIAL_FURNACE,
				worldIn,
				pos.getX(),
				pos.getY(),
				pos.getZ());
		return true;
	}

	@Nullable
	@Override
	public TileEntityIndustrialFurnace createTileEntity(World world, IBlockState state) {
		return new TileEntityIndustrialFurnace();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if(!hasTileEntity) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityIndustrialFurnace) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityIndustrialFurnace) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}
//		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
		Minecraft.getMinecraft().world.removeTileEntity(pos);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(FACING).getIndex() - 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.getFront(meta + 2));
	}

	@Override
	public Class<TileEntityIndustrialFurnace> getTileEntityClass() {
		return TileEntityIndustrialFurnace.class;
	}
}
