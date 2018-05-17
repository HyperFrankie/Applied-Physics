package com.AppliedPhysics.gui;

import com.AppliedPhysics.block.TileEntityBlocks.TileEntities.TileEntityIndustrialFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * This is a class that creates the server side Container for an inventory GUI
 * @author lenar_000
 *
 */
public class ContainerIndustrialFurnace extends Container {

//	private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

	public static int offsetX = 0, offsetY = 0, extraWidth = 0;
	public BlockPos pos;
	public TileEntityIndustrialFurnace furnace;

	/**
	 * This is a method that creates the server side ContainerIndustrialFurnace, an extension of Container, for an inventory GUI
	 * @param playerInv
	 * @param furnace
	 */
	public ContainerIndustrialFurnace(InventoryPlayer playerInv, final TileEntityIndustrialFurnace furnace) {
		this.pos = furnace.getPos();
		this.furnace = furnace;
		IItemHandler inventory = furnace.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

		addSlotToContainer(new SlotItemHandler(inventory, 0, offsetX + 56, offsetY + 23) {	//Furnace input slot
			@Override
			public void onSlotChanged() {
				furnace.markDirty();
				furnace.inputStackChanged = true;
			}
		});

		addSlotToContainer(new SlotItemHandler(inventory, 1, offsetX + 104, offsetY + 23) {	//Furnace output slot
			@Override
			public void onSlotChanged() {
				furnace.markDirty();
				furnace.outputStackChanged = true;
			}
		});

		for (int k = 0; k < 9; k++) {									//Hotbar
			addSlotToContainer(new Slot(playerInv, k, offsetX + 8 + k * 18, offsetY + 117));
		}

		for (int i = 0; i < 3; i++) {									//Main inventory
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, offsetX + 8 + j * 18, offsetY + 59 + i * 18));
			}
		}

//		for (int k = 0; k < 4; ++k) {									//Armor
//			final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
//			this.addSlotToContainer(new Slot(playerInv, 36 + (3 - k), offsetX + 8, offsetY + k * 18) {
//				/**
//				 * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1
//				 * in the case of armor slots)
//				 */
//				public int getSlotStackLimit() {
//					return 1;
//				}
//				/**
//				 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace
//				 * fuel.
//				 */
//				public boolean isItemValid(ItemStack stack) {
//					return stack.getItem().isValidArmor(stack, entityequipmentslot, playerInv.player);
//				}
//				/**
//				 * Return whether this slot's stack can be taken from this slot.
//				 */
//				public boolean canTakeStack(EntityPlayer playerIn) {
//					ItemStack itemstack = this.getStack();
//					return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.canTakeStack(playerIn);
//				}
//				@Nullable
//				@SideOnly(Side.CLIENT)
//				public String getSlotTexture() {
//					return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
//				}
//			});
//		}

//		addSlotToContainer(new Slot(playerInv, 40, offsetX + 80, offsetY + 54) {			//Offhand slot
//			@Nullable
//			@SideOnly(Side.CLIENT)
//			public String getSlotTexture()
//			{
//				return "minecraft:items/empty_armor_slot_shield";
//			}
//		});
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) { return true; }

	/**Method that checks whether the given ItemStack can be inserted into one of the slots in range:
	 * <pre>
	 * startIndex - endIndex.
	 *
	 * startIndex = slot index + 1
	 * endIndex = slot index +2
	 *
	 * Range 1 - 10     are Hotbar slots
	 * Range 10 - 37    are Main inventory slots
	 * Range 37 - 41    are Armor slots
	 * Range 41 - 42    is Offhand slot
	 * </pre>
	 */
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if(!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots - 1, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
