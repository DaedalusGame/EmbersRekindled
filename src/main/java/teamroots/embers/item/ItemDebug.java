package teamroots.embers.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDebug extends ItemBase {
	public ItemDebug() {
		super("debug", false);
	}
	
	@Override
	public EnumActionResult onItemUse(
	EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ){
		if (Blocks.TORCH.canPlaceBlockOnSide(world, pos, face)){
			((ItemBlock)Item.getItemFromBlock(Blocks.TORCH)).onItemUse(player, world, pos, hand, face, hitX, hitY, hitZ);
		}
		return EnumActionResult.SUCCESS;
	}
}
