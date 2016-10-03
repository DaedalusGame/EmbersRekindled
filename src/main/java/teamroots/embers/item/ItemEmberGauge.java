package teamroots.embers.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import teamroots.embers.util.Vec2i;
import teamroots.embers.world.EmberWorldData;

public class ItemEmberGauge extends ItemBase {

	public ItemEmberGauge() {
		super("emberDetector", true);
		this.setMaxStackSize(1);
	}
}
