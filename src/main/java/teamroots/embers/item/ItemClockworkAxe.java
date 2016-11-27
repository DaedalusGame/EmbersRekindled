package teamroots.embers.item;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import teamroots.embers.Embers;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEmberBurstFX;
import teamroots.embers.util.EmberInventoryUtil;

public class ItemClockworkAxe extends ItemTool implements IModeledItem, IEmberChargedTool {

	public static ToolMaterial materialClockworkAxe = EnumHelper.addToolMaterial(Embers.MODID+":clockworkAxe", 3, -1, 12.0f, 5.0f, 15);
	
	public ItemClockworkAxe(String name, boolean addToTab) {
		super(materialClockworkAxe,Sets.newHashSet(new Block[]{Blocks.PLANKS}));
		setUnlocalizedName(name);
		setRegistryName(Embers.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Embers.tab);
		}
		setHarvestLevel("axe",this.toolMaterial.getHarvestLevel());
		this.damageVsEntity = 8.0f;
		this.attackSpeed = -3.0f;
		GameRegistry.register(this);
	}
	
	public float getProperEfficiency(){
		return this.efficiencyOnProperMaterial;
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state){
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
        		? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand){
		if (EmberInventoryUtil.getEmberTotal(player) >= 25.0 && stack.getTagCompound().getInteger("cooldown") <= 0 || player.capabilities.isCreativeMode){
			double posX = player.posX;
			double posY = player.posY+player.getEyeHeight();
			double posZ = player.posZ;
			boolean doContinue = true;
			for (double i = 0; i < 40.0 && doContinue; i ++){
				for (int j = 0; j < 5; j ++){
					posX += 0.1f*player.getLookVec().xCoord;
					posY += 0.1f*player.getLookVec().yCoord;
					posZ += 0.1f*player.getLookVec().zCoord;
				}
				IBlockState state = world.getBlockState(new BlockPos(posX,posY,posZ));
				if (state.isFullCube() && state.isOpaqueCube() && state.getBlockHardness(world, new BlockPos(posX,posY,posZ)) > 0){
					doContinue = false;
					BlockPos pos = new BlockPos(posX,posY,posZ);
					if (!world.isRemote){
						PacketHandler.INSTANCE.sendToAll(new MessageEmberBurstFX(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5));
					}
					for (int xx = -3; xx < 4; xx ++){
						for (int yy = -3; yy < 4; yy ++){
							for (int zz = -3; zz < 4; zz ++){
								BlockPos newPos = pos.add(xx,yy,zz);
								if (world.getBlockState(newPos).getMaterial() == Material.WOOD && world.getBlockState(newPos).getBlockHardness(world, new BlockPos(posX,posY,posZ)) > 0){
									IBlockState tempState = world.getBlockState(newPos);
									tempState.getBlock().onBlockHarvested(world, newPos, tempState, player);
									world.destroyBlock(newPos, true);
									world.notifyBlockUpdate(newPos, state, Blocks.AIR.getDefaultState(), 3);
								}
							}
						}
					}
					stack.getTagCompound().setInteger("cooldown",80);
					EmberInventoryUtil.removeEmber(player, 25.0);
				}
			}
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL,stack);
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
	
	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack){
		return this.getHarvestLevel(stack, state.getBlock().getHarvestTool(state)) >= state.getBlock().getHarvestLevel(state);
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
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
	}
}
