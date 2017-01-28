package teamroots.embers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.tileentity.TileEntityKnowledgeTable;

public class ItemGolemsEye extends ItemBase {
	public ItemGolemsEye() {
		super("golems_eye", true);
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		if (oldStack != ItemStack.EMPTY && newStack != ItemStack.EMPTY){
			return slotChanged || oldStack.getItemDamage() != newStack.getItemDamage() || oldStack.getItem() != newStack.getItem();
		}
		return slotChanged;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean selected){
		if (selected && world.isRemote){
			RayTraceResult result = entity.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
			if (result != null){
				ItemStack test = ItemStack.EMPTY;
				if (result.typeOfHit == RayTraceResult.Type.BLOCK){
					if (world.getTileEntity(result.getBlockPos()) instanceof TileEntityKnowledgeTable){
						TileEntityKnowledgeTable table = ((TileEntityKnowledgeTable)world.getTileEntity(result.getBlockPos()));
						if (table.inventory.getStackInSlot(0) != ItemStack.EMPTY){
							test = table.inventory.getStackInSlot(0);
						}
					}
					if (test == ItemStack.EMPTY){
						test = new ItemStack(world.getBlockState(result.getBlockPos()).getBlock(),1,world.getBlockState(result.getBlockPos()).getBlock().getMetaFromState(world.getBlockState(result.getBlockPos())));
					}
				}
				if (test != ItemStack.EMPTY){
					if (test.getItem() != null){
						if (ResearchManager.researches.get(test.getItem().getRegistryName().toString()) != null && stack.getItemDamage() <= 4){
							if (stack.getItemDamage() < 4){
								stack.setItemDamage(stack.getItemDamage()+1);
							}
							return;
						}
					}
				}
			}
		}
		if (stack.getItemDamage() > 0){
			stack.setItemDamage(stack.getItemDamage()-1);
		}
	}
	
	@Override
	public void initModel(){
		ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName().toString()),
				new ModelResourceLocation(getRegistryName().toString()+"_opening1"),
				new ModelResourceLocation(getRegistryName().toString()+"_opening2"),
				new ModelResourceLocation(getRegistryName().toString()+"_opening3"),
				new ModelResourceLocation(getRegistryName().toString()+"_opening4"));
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName().toString()+"_opening1"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(getRegistryName().toString()+"_opening2"));
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(getRegistryName().toString()+"_opening3"));
		ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation(getRegistryName().toString()+"_opening4"));
	}
}
