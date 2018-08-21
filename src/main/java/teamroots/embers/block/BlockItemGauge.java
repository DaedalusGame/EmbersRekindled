package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import teamroots.embers.api.block.IDial;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdateRequest;
import teamroots.embers.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class BlockItemGauge extends BlockBaseGauge {
	public BlockItemGauge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	protected void getTEData(EnumFacing facing, ArrayList<String> text, TileEntity tileEntity) {
		if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)){
			IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
			if (handler != null){
				for (int i = 0; i < handler.getSlots(); i++){
					ItemStack stack = handler.getStackInSlot(i);
					String item;
					if (!stack.isEmpty())
						item = I18n.format("embers.tooltip.itemdial.item", stack.getCount(), stack.getDisplayName());
					else
						item = I18n.format("embers.tooltip.itemdial.noitem");
					text.add(I18n.format("embers.tooltip.itemdial.slot",i,item));
				}
			}
		}
	}

	@Override
	public String getDialType() {
		return "item";
	}
}
