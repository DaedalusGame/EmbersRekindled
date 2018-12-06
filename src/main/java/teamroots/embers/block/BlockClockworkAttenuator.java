package teamroots.embers.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.tileentity.TileEntityClockworkAttenuator;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BlockClockworkAttenuator extends BlockBaseGauge {
    public static final String DIAL_TYPE = "work";

    public BlockClockworkAttenuator(Material material, String name, boolean addToTab) {
        super(material, name, addToTab);
    }

    @Override
    public List<String> getDisplayInfo(World world, BlockPos pos, IBlockState state) {
        List<String> text = super.getDisplayInfo(world, pos, state);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityClockworkAttenuator) {
            DecimalFormat multiplierFormat = Embers.proxy.getDecimalFormat("embers.decimal_format.attenuator_multiplier");
            double activeSpeed = ((TileEntityClockworkAttenuator) tile).activeSpeed;
            double inactiveSpeed = ((TileEntityClockworkAttenuator) tile).inactiveSpeed;
            boolean active = ((TileEntityClockworkAttenuator) tile).powered;
            text.add((active ? TextFormatting.GREEN : TextFormatting.DARK_GREEN)+I18n.format("embers.tooltip.attenuator.on", multiplierFormat.format(activeSpeed)));
            text.add((!active ? TextFormatting.RED : TextFormatting.DARK_RED)+I18n.format("embers.tooltip.attenuator.off", multiplierFormat.format(inactiveSpeed)));
        }
        return text;
    }

    @Override
    protected void getTEData(EnumFacing facing, ArrayList<String> text, TileEntity tileEntity) {

    }

    @Override
    public String getDialType() {
        return DIAL_TYPE;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityClockworkAttenuator();
    }
}
