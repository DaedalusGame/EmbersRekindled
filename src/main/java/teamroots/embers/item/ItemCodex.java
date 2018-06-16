package teamroots.embers.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.SoundManager;

public class ItemCodex extends ItemBase {

	public ItemCodex() {
		super("codex", true);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		world.playSound(null,player.posX, player.posY, player.posZ, SoundManager.CODEX_OPEN, SoundCategory.MASTER, 1.0f, 1.0f);
		player.openGui(Embers.instance, 0, world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
		return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
}
