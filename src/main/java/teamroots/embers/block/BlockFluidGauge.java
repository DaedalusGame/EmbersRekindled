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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import teamroots.embers.api.block.IDial;
import teamroots.embers.api.tile.IExtraDialInformation;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdateRequest;
import teamroots.embers.tileentity.TileEntityEmberGauge;
import teamroots.embers.tileentity.TileEntityFluidGauge;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockFluidGauge extends BlockBaseGauge {

	public static final String DIAL_TYPE = "fluid";

	public BlockFluidGauge(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	protected void getTEData(EnumFacing facing, ArrayList<String> text, TileEntity tileEntity) {
		if (tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)){
			IFluidHandler handler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
			if (handler != null){
				for (IFluidTankProperties property : handler.getTankProperties()) {
					FluidStack contents = property.getContents();
					int capacity = property.getCapacity();
					text.add(formatFluidStack(contents, capacity));
				}
			}
		}
	}

	public static String formatFluidStack(FluidStack contents, int capacity) {
		if (contents != null)
            return I18n.format("embers.tooltip.fluiddial.fluid",contents.getFluid().getLocalizedName(contents),contents.amount, capacity);
        else
            return I18n.format("embers.tooltip.fluiddial.nofluid", capacity);
	}

	@Override
	public String getDialType() {
		return DIAL_TYPE;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityFluidGauge();
	}
}
