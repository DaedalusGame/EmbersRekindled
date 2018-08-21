package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;

import java.util.ArrayList;

public class BlockEmberGauge extends BlockBaseGauge {
	public static final String DIAL_TYPE = "ember";

	public BlockEmberGauge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab); 
	}

	@Override
	protected void getTEData(EnumFacing facing, ArrayList<String> text, TileEntity tileEntity) {
		if (tileEntity.hasCapability(EmbersCapabilities.EMBER_CAPABILITY, facing)){
			IEmberCapability handler = tileEntity.getCapability(EmbersCapabilities.EMBER_CAPABILITY, facing);
			if (handler != null){
				text.add(formatEmber(handler.getEmber(), handler.getEmberCapacity()));
			}
		}
	}

	public static String formatEmber(double ember, double emberCapacity) {
		return I18n.format("embers.tooltip.emberdial.ember", ember, emberCapacity);
	}

	@Override
	public String getDialType() {
		return DIAL_TYPE;
	}
}
