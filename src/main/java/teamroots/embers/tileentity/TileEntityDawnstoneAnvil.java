package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.item.IFilter;
import teamroots.embers.api.tile.IBin;
import teamroots.embers.api.tile.IHammerable;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageAnvilSparksFX;
import teamroots.embers.network.message.MessageStamperFX;
import teamroots.embers.recipe.DawnstoneAnvilRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.util.FilterUtil;
import teamroots.embers.util.Misc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileEntityDawnstoneAnvil extends TileEntity implements ITileEntityBase, IHammerable, ISpecialFilter {
	int ticksExisted = 0;
	int progress = 0;
	public ItemStackHandler inventory = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
        	TileEntityDawnstoneAnvil.this.markDirty();
        }

		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
			return 1; //CURSED
		}
	};
	Random random = new Random();
	
	public TileEntityDawnstoneAnvil(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		tag.setTag("inventory", inventory.serializeNBT());
		tag.setInteger("progress", progress);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		inventory.deserializeNBT(tag.getCompoundTag("inventory"));
		progress = tag.getInteger("progress");
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
			return (T)this.inventory;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (heldItem.isEmpty() && hand == EnumHand.MAIN_HAND) {
			boolean doContinue = true;
			for (int i = 1; i >= 0 && doContinue; i --){
				if (!inventory.getStackInSlot(i).isEmpty() && !world.isRemote){
					world.spawnEntity(new EntityItem(world,player.posX,player.posY,player.posZ,inventory.getStackInSlot(i)));
					inventory.setStackInSlot(i, ItemStack.EMPTY);
					doContinue = false;
					progress = 0;
					markDirty();
					return true;
				}
			}
		}
		else if (heldItem.getItem() == RegistryManager.tinker_hammer){
			onHit();
			return true;
		}
		else if (!heldItem.isEmpty() && hand == EnumHand.MAIN_HAND){
			ItemStack stack = heldItem.copy();
			ItemStack stack2 = heldItem.copy();
			stack2.setCount(1);
			boolean doContinue = true;
			for (int i = 0; i < 2 && doContinue; i ++){
				if (inventory.getStackInSlot(i).isEmpty()){
					this.inventory.insertItem(i,stack2,false);
					doContinue = false;
					player.getHeldItem(hand).shrink(1);
					if (player.getHeldItem(hand).getCount() == 0){
						player.setHeldItem(hand, ItemStack.EMPTY);
					}
					progress = 0;
					markDirty();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		Misc.spawnInventoryInWorld(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, inventory);
		world.setTileEntity(pos, null);
	}
	
	public boolean isValid(ItemStack stack1, ItemStack stack2){
		/*if (stack1.getItem() instanceof ItemTool || stack1.getItem() instanceof ItemSword || stack1.getItem() instanceof ItemArmor){
			if (!ItemModUtil.hasHeat(stack1) && stack2.getItem() == RegistryManager.ancient_motive_core){
				return true;
			}
			else if (ItemModUtil.hasHeat(stack1) && ItemModUtil.modifierRegistry.containsKey(stack2.getItem()) && ItemModUtil.getLevel(stack1) > ItemModUtil.getTotalModLevel(stack1) && ItemModUtil.isModValid(stack1,stack2)){
				return true;
			}
			else if (ItemModUtil.hasHeat(stack1) && stack1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && stack2.isEmpty()) {
				return true;
			}
		}
		if (stack1.getItem().getIsRepairable(stack1,stack2)
				|| stack1.getItem().isRepairable() && stack2.getItem() == RegistryManager.isolated_materia){
			return true;
		}
		if (!Misc.getRepairItem(stack1).isEmpty() && stack1.getItem().getIsRepairable(stack1, Misc.getRepairItem(stack1)) && Misc.getResourceCount(stack1) != -1 && stack2.isEmpty()){
			return true;
		}*/
		DawnstoneAnvilRecipe recipe = RecipeRegistry.getDawnstoneAnvilRecipe(stack1,stack2);

		return recipe != null;
	}
	
	public ItemStack[] getResult(ItemStack stack1, ItemStack stack2){
		/*if (stack1.getItem() instanceof ItemTool || stack1.getItem() instanceof ItemSword || stack1.getItem() instanceof ItemArmor){
			if ((!ItemModUtil.hasHeat(stack1) || !ItemModUtil.hasModifier(stack1, ItemModUtil.modifierRegistry.get(RegistryManager.ancient_motive_core).name)) && stack2.getItem() == RegistryManager.ancient_motive_core){
				ItemModUtil.checkForTag(stack1);
				ItemModUtil.addModifier(stack1, stack2.copy());
				ItemStack result = stack1.copy();
				inventory.setStackInSlot(1, ItemStack.EMPTY);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
				markDirty();
				return new ItemStack[]{result};
			}
			else if (ItemModUtil.hasHeat(stack1) && ItemModUtil.modifierRegistry.containsKey(stack2.getItem())){
				ItemModUtil.checkForTag(stack1);
				ItemModUtil.addModifier(stack1, stack2);
				ItemStack result = stack1.copy();
				inventory.setStackInSlot(1, ItemStack.EMPTY);
				inventory.setStackInSlot(0, ItemStack.EMPTY);
				markDirty();
				return new ItemStack[]{result};
			}
			else if (ItemModUtil.hasHeat(stack1) && stack2.isEmpty()){
				if (stack1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).tagCount() > 0){
					List<ItemStack> stacks = new ArrayList<>();
					for (int i = 0; i < stack1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).tagCount(); i ++){
						ItemStack s = new ItemStack(stack1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i).getCompoundTag("item"));
						if (ItemModUtil.modifierRegistry.get(s.getItem()) != null && ItemModUtil.modifierRegistry.get(s.getItem()).countTowardsTotalLevel){
							for (int j = 0; j < stack1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i).getInteger("level"); j ++){
								stacks.add(new ItemStack(stack1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).getTagList("modifiers", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i).getCompoundTag("item")));
							}
						}
					}
					stack1.getTagCompound().getCompoundTag(ItemModUtil.HEAT_TAG).setTag("modifiers", new NBTTagList());
					stacks.add(stack1.copy());
					inventory.setStackInSlot(0, ItemStack.EMPTY);
					inventory.setStackInSlot(1, ItemStack.EMPTY);
					markDirty();
					return stacks.toArray(new ItemStack[stacks.size()]);
				}
			}
		}
		if (stack1.getItem().getIsRepairable(stack1, stack2) || stack1.getItem().isRepairable() && stack2.getItem() == RegistryManager.isolated_materia){
			inventory.setStackInSlot(1, ItemStack.EMPTY);
			inventory.getStackInSlot(0).setItemDamage(Math.max(0, inventory.getStackInSlot(0).getItemDamage() - inventory.getStackInSlot(0).getMaxDamage()));
			ItemStack result = inventory.getStackInSlot(0).copy();
			inventory.setStackInSlot(0, ItemStack.EMPTY);
			markDirty();
			return new ItemStack[]{result};
		}
		if (stack1.getItem().getIsRepairable(stack1, Misc.getRepairItem(stack1)) && Misc.getResourceCount(stack1) != -1 && stack2.isEmpty()){
			int resourceAmount = Misc.getResourceCount(stack1);
			inventory.setStackInSlot(0, ItemStack.EMPTY);
			markDirty();
			return new ItemStack[]{new ItemStack(Misc.getRepairItem(stack1).getItem(),resourceAmount,Misc.getRepairItem(stack1).getItemDamage())};		
		}
		return new ItemStack[]{};*/
		DawnstoneAnvilRecipe recipe = RecipeRegistry.getDawnstoneAnvilRecipe(stack1,stack2);
		if(recipe != null) {
			inventory.setStackInSlot(1, ItemStack.EMPTY);
			inventory.setStackInSlot(0, ItemStack.EMPTY);
			markDirty();
			List<ItemStack> results = recipe.getResult(this, stack1, stack2);
			return results.toArray(new ItemStack[results.size()]);
		}
		return new ItemStack[0];
	}

	@Override
	public void markDirty() {
		super.markDirty();
		Misc.syncTE(this);
	}

	public void onHit(){
		if (isValid(inventory.getStackInSlot(0),inventory.getStackInSlot(1))){
			progress += 1;
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.25f, 2.0f+random.nextFloat(), false);
			if (progress > 40){
				progress = 0;
				ItemStack[] results = getResult(inventory.getStackInSlot(0),inventory.getStackInSlot(1));
				for (int i = 0; i < results.length; i ++){
					ItemStack result = results[i];
					TileEntity bin = getWorld().getTileEntity(getPos().down());
					if (bin instanceof IBin){
						ItemStack remainder = ((TileEntityBin) bin).getInventory().insertItem(0, result, false);
						if (!remainder.isEmpty() && !getWorld().isRemote){
							EntityItem item = new EntityItem(getWorld(),getPos().getX()+0.5,getPos().getY()+1.0625f,getPos().getZ()+0.5,remainder);
							getWorld().spawnEntity(item);
						}
						bin.markDirty();
						markDirty();
					}
					else if (!world.isRemote){
						EntityItem item = new EntityItem(getWorld(),getPos().getX()+0.5,getPos().getY()+1.0625f,getPos().getZ()+0.5,result);
						getWorld().spawnEntity(item);
					}
				}
				if (!getWorld().isRemote){
					PacketHandler.INSTANCE.sendToAll(new MessageStamperFX(getPos().getX()+0.5,getPos().getY()+1.0625,getPos().getZ()+0.5));
				}
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 0.95f+random.nextFloat()*0.1f, false);
			}
			markDirty();
			if (!getWorld().isRemote){
				PacketHandler.INSTANCE.sendToAll(new MessageAnvilSparksFX(getPos().getX()+0.5,getPos().getY()+1.0625,getPos().getZ()+0.5));
			}
		}
	}

	@Override
	public void onHit(TileEntity hammer) {
		progress = 40;
		onHit();
	}

	@Override
	public boolean isValid() {
		return isValid(inventory.getStackInSlot(0),inventory.getStackInSlot(1));
	}

	@Override
	public IFilter getSpecialFilter() {
		return FilterUtil.FILTER_NOT_EXISTING;
	}
}
