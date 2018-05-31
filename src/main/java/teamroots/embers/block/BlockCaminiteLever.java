package teamroots.embers.block;

import net.minecraft.block.BlockLever;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import teamroots.embers.Embers;

public class BlockCaminiteLever extends BlockLever implements IBlock, IModeledBlock {
	public Item itemBlock = null;

	public BlockCaminiteLever(String name, boolean addToTab) {
		super();
		setUnlocalizedName(name);
		setRegistryName(Embers.MODID+":"+name);
		if (addToTab){
			setCreativeTab(Embers.tab);
		}
		itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public Item getItemBlock() {
		return itemBlock;
	}

	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName().toString(),"inventory"));
	}
}
