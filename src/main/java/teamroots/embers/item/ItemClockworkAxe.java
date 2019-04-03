package teamroots.embers.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import teamroots.embers.Embers;
import teamroots.embers.api.item.IEmberChargedTool;
import teamroots.embers.util.EmberInventoryUtil;

public class ItemClockworkAxe extends ItemTool implements IModeledItem, IEmberChargedTool {

	public static ToolMaterial materialClockworkAxe = EnumHelper.addToolMaterial(Embers.MODID+":clockworkAxe", 3, -1, 16.0f, 5.0f, 15);
	
	public ItemClockworkAxe(String name, boolean addToTab) {
		super(materialClockworkAxe,Sets.newHashSet(new Block[]{Blocks.PLANKS}));
		setUnlocalizedName(name);
		setRegistryName(Embers.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Embers.tab);
		}
		setHarvestLevel("axe",this.toolMaterial.getHarvestLevel());
		this.attackDamage = 8.0f;
		this.attackSpeed = -3.0f;
	}

	public float getProperEfficiency(){
		return this.efficiency;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state){
        Material material = state.getMaterial();
        if (stack.hasTagCompound()){
        	if (!stack.getTagCompound().getBoolean("poweredOn")){
        		return 0;
        	}
        }
        return material != Material.WOOD 
        		&& material != Material.PLANTS 
        		&& material != Material.VINE 
                && material != Material.CACTUS
                && material != Material.CARPET
                && material != Material.CLOTH 
                && material != Material.WEB
                && material != Material.GOURD
        		? super.getDestroySpeed(stack, state) : this.efficiency;
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
				return (stack.getTagCompound().getInteger("cooldown"))/80.0f;
			}
		}
		return 0.0;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living){
		stack.getTagCompound().setBoolean("didUse", true);
		return true;
	}
	
	/*@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack){
		if (state.getBlock().getHarvestLevel(state) <= 3 && state.getBlock().getHarvestTool(state) != null || state.getBlock().getHarvestLevel(state) < 1){
			String tool = state.getBlock().getHarvestTool(state);
			if (tool != null){
				return tool.compareTo("axe") == 0;
			}
		}
		return false;
	}*/
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		if (oldStack.hasTagCompound() && newStack.hasTagCompound()){
			return slotChanged || oldStack.getTagCompound().getBoolean("poweredOn") != newStack.getTagCompound().getBoolean("poweredOn") || newStack.getItem() != oldStack.getItem();
		}
		return slotChanged || newStack.getItem() != oldStack.getItem();
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
			stack.getTagCompound().setInteger("cooldown", 0);
			stack.getTagCompound().setBoolean("poweredOn", false);
			stack.getTagCompound().setBoolean("didUse", false);
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
	public boolean isEnchantable(ItemStack stack){
		return true;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant){
		return enchant.type != EnumEnchantmentType.BREAKABLE && enchant.type == EnumEnchantmentType.WEAPON || enchant.type == EnumEnchantmentType.DIGGER;
	}
	
	@Override
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}

	@Override
	public boolean hasEmber(ItemStack stack) {
		if (stack.hasTagCompound()){
			return stack.getTagCompound().getBoolean("poweredOn");
		}
		return false;
	}
}
