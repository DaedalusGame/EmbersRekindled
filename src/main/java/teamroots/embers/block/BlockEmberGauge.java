package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.api.block.IDial;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdateRequest;
import teamroots.embers.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class BlockEmberGauge extends BlockBaseGauge {
	public BlockEmberGauge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab); 
	}

	@Override
	protected void getTEData(EnumFacing facing, ArrayList<String> text, TileEntity tileEntity) {
		if (tileEntity.hasCapability(EmbersCapabilities.EMBER_CAPABILITY, facing)){
			IEmberCapability handler = tileEntity.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing);
			if (handler != null){
				text.add(I18n.format("embers.tooltip.emberdial.ember",handler.getEmber(),handler.getEmberCapacity()));
			}
		}
	}

	@Override
	public String getDialType() {
		return "ember";
	}
}
