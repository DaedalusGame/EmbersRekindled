package teamroots.embers.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberBurstFX;
import teamroots.embers.util.EmberInventoryUtil;

public class ItemGrandhammer extends ItemTool implements IModeledItem, IEmberChargedTool {

	public static ToolMaterial materialGrandhammer = EnumHelper.addToolMaterial(Embers.MODID+":grandhammer", 1, -1, 6.0f, 6.0f, 15);
	
	public ItemGrandhammer(String name, boolean addToTab) {
		super(materialGrandhammer,Sets.newHashSet(new Block[]{Blocks.STONE}));
		setUnlocalizedName(name);
		setRegistryName(Embers.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Embers.tab);
		}
		setHarvestLevel("pickaxe",this.toolMaterial.getHarvestLevel());
		setHarvestLevel("axe",this.toolMaterial.getHarvestLevel());
		setHarvestLevel("shovel",this.toolMaterial.getHarvestLevel());
		this.damageVsEntity = 10.0f;
		this.attackSpeed = -3.0f;
		GameRegistry.register(this);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().getInteger("cooldown") > 0){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack){
		if (stack.hasTagCompound()){
			if (stack.getTagCompound().getInteger("cooldown") > 0){
				return (stack.getTagCompound().getInteger("cooldown"))/160.0f;
			}
		}
		return 0.0;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
		if (EmberInventoryUtil.getEmberTotal(player) >= 25.0 && stack.getTagCompound().getInteger("cooldown") <= 0 || player.capabilities.isCreativeMode){
			double posX = player.posX+1.5*player.getLookVec().xCoord;
			double posY = player.posY+player.getEyeHeight()+1.5*player.getLookVec().yCoord;
			double posZ = player.posZ+1.5*player.getLookVec().zCoord;
			boolean doContinue = true;
			List<EntityLivingBase> rawEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX-3.0,posY-3.0,posZ-3.0,posX+3.0,posY+3.0,posZ+3.0));
			ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
			for (int i = 0; i < rawEntities.size(); i ++){
				if (rawEntities.get(i).getUniqueID().compareTo(player.getUniqueID()) != 0){
					entities.add(rawEntities.get(i));
				}
			}
			if (entities.size() > 0){
				for (int i = 0; i < entities.size(); i ++){
					entities.get(i).setFire(1);
					entities.get(i).attackEntityFrom(DamageSource.causePlayerDamage(player), 11.0f);
					entities.get(i).setLastAttacker(player);
					entities.get(i).setRevengeTarget(player);
					entities.get(i).knockBack(player, 1.0f, player.posX-entities.get(i).posX, player.posZ-entities.get(i).posZ);
					if (!world.isRemote){
						PacketHandler.INSTANCE.sendToAll(new MessageEmberBurstFX(entities.get(i).posX,entities.get(i).posY+entities.get(i).getEyeHeight()/1.5,entities.get(i).posZ));
					}
				}
				stack.getTagCompound().setInteger("cooldown",160);
				EmberInventoryUtil.removeEmber(player, 25.0);
			}
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL,stack);
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state){
        if (stack.hasTagCompound()){
        	if (!stack.getTagCompound().getBoolean("poweredOn")){
        		return 0;
        	}
        }
		return this.efficiencyOnProperMaterial;
    }
	
	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack){
		return this.getHarvestLevel(stack, state.getBlock().getHarvestTool(state)) >= state.getBlock().getHarvestLevel(state);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living){
		stack.getTagCompound().setBoolean("didUse", true);
		return true;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		if (oldStack.hasTagCompound() && newStack.hasTagCompound()){
			return slotChanged || oldStack.getTagCompound().getBoolean("poweredOn") != newStack.getTagCompound().getBoolean("poweredOn");
		}
		return slotChanged;
	}
	
	@Override
	public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack){
		if (oldStack.hasTagCompound() && newStack.hasTagCompound()){
			return oldStack.getTagCompound().getBoolean("poweredOn") != newStack.getTagCompound().getBoolean("poweredOn");
		}
		return false;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("tickCount", 0);
			stack.getTagCompound().setBoolean("poweredOn", false);
			stack.getTagCompound().setBoolean("didUse", false);
			stack.getTagCompound().setInteger("cooldown", 0);
		}
		else {
			if (stack.getTagCompound().getInteger("cooldown") > 0){
				stack.getTagCompound().setInteger("cooldown", stack.getTagCompound().getInteger("cooldown")-1);
			}
			if (entity instanceof EntityPlayer){
				stack.getTagCompound().setInteger("tickCount", stack.getTagCompound().getInteger("tickCount")+1);
				if (stack.getTagCompound().getInteger("tickCount") >= 5){
					stack.getTagCompound().setInteger("tickCount", 0);
					if (EmberInventoryUtil.getEmberTotal(((EntityPlayer)entity)) > 5.0){
						if (!stack.getTagCompound().getBoolean("poweredOn")){
							stack.getTagCompound().setBoolean("poweredOn", true);
						}
					}
					else {
						if (stack.getTagCompound().getBoolean("poweredOn")){
							stack.getTagCompound().setBoolean("poweredOn", false);
						}
					}
				}
				if (stack.getTagCompound().getBoolean("didUse")){
					stack.getTagCompound().setBoolean("didUse", false);
					EmberInventoryUtil.removeEmber(((EntityPlayer)entity), 5.0);
					if (EmberInventoryUtil.getEmberTotal((EntityPlayer)entity) < 5.0){
						stack.getTagCompound().setBoolean("poweredOn", false);
					}
				}
			}
		}
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
