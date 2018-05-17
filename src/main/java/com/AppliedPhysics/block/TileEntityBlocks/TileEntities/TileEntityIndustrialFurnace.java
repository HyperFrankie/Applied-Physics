package com.AppliedPhysics.block.TileEntityBlocks.TileEntities;

import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.HeatStack;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.HeatStackInterface;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.ITemperatureHandler;
import com.AppliedPhysics.block.TileEntityBlocks.Capability.Temperature.TileEntityHeatContained;
import com.AppliedPhysics.gui.ContainerIndustrialFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.AppliedPhysics.AppliedPhysicsUtils.*;

public class TileEntityIndustrialFurnace extends TileEntityLockable implements ISidedInventory, ITickable, HeatStackInterface {

	public enum slotEnum {
		INPUT_SLOT, OUTPUT_SLOT
	}
	private static final int[] slotsTop = new int[] {slotEnum.INPUT_SLOT.ordinal()};
	private static final int[] slotsBottom = new int[] {slotEnum.OUTPUT_SLOT.ordinal()};
	private ItemStackHandler inventory = new ItemStackHandler(2) {
		@Override
		protected void onContentsChanged(int slot) {
			if(slot == slotEnum.INPUT_SLOT.ordinal()) {
				inputStackChanged = true;
			} else {
				outputStackChanged = true;
			}
			markDirty();
		}
	};
	public java.util.Map<TileEntity, EnumFacing> connectedCapableBlocks = new HashMap<>();
	private int currentBurnTime;
	private double currentItemEnergy;
	private int currentItemBurnTime;
	private ItemStack currentBurnItem = ItemStack.EMPTY;
	private String customName;
	private boolean currentlyBurning = false, currentlyPaused = false, resultReturnedImmediately = false;
	public boolean inputStackChanged = true, outputStackChanged;
	ArrayList<TileEntityHeatContained> heatStacks = new ArrayList(){{
		add(new TileEntityHeatContained(TileEntityIndustrialFurnace.this));
	}};

	@Override
	public void onLoad() {
		super.onLoad();
		for(EnumFacing f : Arrays.asList(new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST})) {
			if(getWorld().getTileEntity(this.getPos().offset(f)) != null) {
				if(getWorld().getTileEntity(this.getPos().offset(f)).getCapability(ITemperatureHandler.HEAT_HANDLER_CAPABILITY, f.getOpposite()) != null) {
					connectedCapableBlocks.put(getWorld().getTileEntity(this.getPos().offset(f)), f);
				}
			}
		}
	}

	@Override
	public boolean shouldRefresh(World parWorld, BlockPos parPos, IBlockState parOldState, IBlockState parNewState) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inventory.getSlots();
	}

	public ItemStack decrStackSize(int index, int count) {
		ItemStackHandler stackHandler = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
		if (stackHandler.getStackInSlot(index) != ItemStack.EMPTY) {
			ItemStack itemstack;
			if (stackHandler.getStackInSlot(index).getCount() <= count) {
				itemstack = stackHandler.getStackInSlot(index);
				stackHandler.setStackInSlot(index, ItemStack.EMPTY);
				return itemstack;
			} else {
				itemstack = stackHandler.getStackInSlot(index).splitStack(count);
				if (stackHandler.getStackInSlot(index).getCount() == 0) {
					stackHandler.setStackInSlot(index, ItemStack.EMPTY);
				}
				return itemstack;
			}
		} else {
			return ItemStack.EMPTY;
		}
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStackHandler stackHandler = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));

		if(stack == null) {
			stack = ItemStack.EMPTY;
		}

		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}

		stackHandler.setStackInSlot(index, stack);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).getStackInSlot(index).copy();
		this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).extractItem(index, getInventoryStackLimit(), false);
		return stack;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).getStackInSlot(index);
	}

	@Override
	public boolean isEmpty() {
		boolean isEmpty = true;
		ItemStackHandler sh = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
		for(int i = 0; i < sh.getSlots(); i++) {
			if(!sh.getStackInSlot(i).isEmpty()) {
				isEmpty = false;
			}
		}
		return isEmpty;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public String getName() {
		return hasCustomName() ? customName : "container.industrial_furnace";
	}

	@Override
	public boolean hasCustomName() {
		return customName != null && customName.length() > 0;
	}

	public void setCustomInventoryName(String parCustomName) {
		customName = parCustomName;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)).serializeNBT());
		if(currentBurnItem != null) {
			compound.setTag("currentBurnItem", currentBurnItem.serializeNBT());
		}
		compound.setInteger("burnTime", currentBurnTime);
		compound.setInteger("currentItemBurnTime", currentItemBurnTime);
		compound.setDouble("currentItemEnergy", currentItemEnergy);
		compound.setBoolean("currentlyPaused", currentlyPaused);
		compound.setBoolean("currentlyBurning", currentlyBurning);
		compound.setBoolean("resultReturnedImmediately", resultReturnedImmediately);

		if(hasCustomName()) {
			compound.setString("CustomName", customName);
		}
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)).deserializeNBT(compound.getCompoundTag("inventory"));
		if(compound.getTag("currentBurnItem") != null) {
			currentBurnItem.deserializeNBT((NBTTagCompound) compound.getTag("currentBurnItem"));
		}
		currentBurnTime =           compound.getInteger ("burnTime");
		currentItemBurnTime =       compound.getInteger ("currentItemBurnTime");
		currentItemEnergy =         compound.getDouble  ("currentItemEnergy");
		currentlyPaused =           compound.getBoolean ("currentlyPaused");
		currentlyBurning =          compound.getBoolean ("currentlyBurning");
		resultReturnedImmediately = compound.getBoolean ("resultReturnedImmediately");

		if (compound.hasKey("CustomName")) {
			customName = compound.getString("CustomName");
		}
		super.readFromNBT(compound);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return currentlyBurning;
	}

	public void setBurning(boolean burning) {
		currentlyBurning = burning;
	}

	public boolean isPaused() {
		return currentlyPaused;
	}

	public void setPaused(boolean paused) {
		currentlyPaused = paused;
	}

	private void startBurningItem(boolean shouldReturnResultImmediately) {
		ItemStackHandler sh = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
		if(!sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()).isEmpty()) {
			currentBurnItem = sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()).copy();
			currentItemBurnTime = getBurnTime(currentBurnItem);
			currentItemEnergy = energyProduced(currentBurnItem);
			currentBurnTime = 0;
			decrStackSize(slotEnum.INPUT_SLOT.ordinal(), 1);
			setBurning(true);
			if(shouldReturnResultImmediately) {
				resultReturnedImmediately = true;

				returnResult();
			}
			markDirty();
		}
	}

	private void finishBurningItem() {
		ItemStackHandler sh = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
		if(!resultReturnedImmediately) {
			returnResult();
		} else {
			resultReturnedImmediately = false;
		}
		setBurning(false);
		System.out.println("attempt to start burning next item: " + sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()));
		if(canStackBurn(sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()), sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal()))) {
			startBurningItem(shouldReturnResultImmediately(sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()), sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal())));
		} else {
			currentBurnItem = null;
			currentItemBurnTime = 0;
			currentItemEnergy = 0;
			currentBurnTime = 0;
		}
	}

	private void returnResult() {
		ItemStackHandler sh = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
		if(sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal()).isEmpty()) {
			sh.setStackInSlot(slotEnum.OUTPUT_SLOT.ordinal(), getOutputItem(currentBurnItem));
		} else {
			if(getOutputItem(currentBurnItem).isEmpty()) {

			} else if(getOutputItem(currentBurnItem).isItemEqual(getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal()))) {
				ItemStack s = getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal()).copy();
				s.setCount(s.getCount() + 1);
				sh.setStackInSlot(slotEnum.OUTPUT_SLOT.ordinal(), s);
			}
		}
	}

	@Override
	public void update() {
		if(outputStackChanged) {
			if(isBurning()) {
				ItemStackHandler sh = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
				if(canStackBurn(currentBurnItem, sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal()))) {
					setPaused(false);
				} else {
					setPaused(true);
				}
				outputStackChanged = false;
			}
		}

		if(isBurning() && !isPaused()) {
			currentBurnTime++;
			heat(1);
			distributeHeat();
			if(currentItemBurnTime == currentBurnTime) {
				finishBurningItem();
			}
		}

		if(inputStackChanged || outputStackChanged) {
			if(!isBurning()) {
				ItemStackHandler sh = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
				if(canStackBurn(sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()), sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal()))) {
					startBurningItem(shouldReturnResultImmediately(sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()), sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal())));
				}
			}
			inputStackChanged = false;
		}

//		System.out.println("CHANGE SOME STUFF OVER HERE");

//		if(outputStackChanged) {
//			if(!isBurning()) {
//				ItemStackHandler sh = ((ItemStackHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH));
//				if(canStackBurn(sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()), sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal()))) {
//					startBurningItem(shouldReturnResultImmediately(sh.getStackInSlot(slotEnum.INPUT_SLOT.ordinal()), sh.getStackInSlot(slotEnum.OUTPUT_SLOT.ordinal())));
//				}
//				outputStackChanged = false;
//			}
//		}
	}

	public void distributeHeat() {
		double energy = heatStacks.get(0).getEnergy();

		for(Object o : connectedCapableBlocks.keySet().toArray()) {
			TileEntity te = (TileEntity) o;
			EnumFacing f = connectedCapableBlocks.get(te);
			if(te.getCapability(ITemperatureHandler.HEAT_HANDLER_CAPABILITY, f.getOpposite()) != null) {
				HeatStack heated = te.getCapability(ITemperatureHandler.HEAT_HANDLER_CAPABILITY, f.getOpposite()).heatMaxEnergyWithResource(heatStacks.get(0).heatStack, energy / 6, true);
				heatStacks.get(0).coolMaxEnergy(heated.getEnergy(), true);
			}
		}
	}

	public void heat(int timeDifference) {
		double energy = (double) currentItemEnergy / (double) currentItemBurnTime * (double) timeDifference;
		this.heatStacks.get(0).heatMaxEnergy(energy, true);
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {}

	@Override
	public void closeInventory(EntityPlayer playerIn) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == slotEnum.INPUT_SLOT.ordinal();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? slotsBottom : slotsTop;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int parSlotIndex, ItemStack parStack,
	                              EnumFacing parFacing) {
		return true;
	}

	@Override
	public String getGuiID() {
		return "applied_physics:industrial_furnace";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerIndustrialFurnace(playerInventory, this);
	}

	@Override
	public int getField(int id) {
		switch (id) {
			case 0:
				return currentBurnTime;
			case 1:
				return currentItemBurnTime;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
			case 0:
				currentBurnTime = value;
			case 1:
				currentItemBurnTime = value;
			default:
				break;
		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	public ItemStack getCurrentBurnItem() {
		return currentBurnItem;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.getSlots(); ++i) {
			inventory.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public void updateHeatStacks() {

	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == ITemperatureHandler.HEAT_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : (capability == ITemperatureHandler.HEAT_HANDLER_CAPABILITY) ? (T) heatStacks.get(0) : super.getCapability(capability, facing);
	}
}
