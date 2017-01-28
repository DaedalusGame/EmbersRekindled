package teamroots.embers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import teamroots.embers.util.EmberInventoryUtil;
import teamroots.embers.world.EmberWorldData;

public class ItemEmberGauge extends ItemBase {

	public ItemEmberGauge() {
		super("ember_detector", true);
		this.setMaxStackSize(1);
	}
}
