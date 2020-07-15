package teamroots.embers.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;

import java.awt.*;

public class FluidGas extends Fluid {
    public FluidGas() {
        super("gas_dwarf", new ResourceLocation(Embers.MODID, "blocks/fluid_gas_dwarf_still"), new ResourceLocation(Embers.MODID, "blocks/fluid_gas_dwarf_flowing"));

        setViscosity(200);
        setDensity(-1500);
        setTemperature(950);
        setGaseous(true);
        setUnlocalizedName("gas_dwarf");
        setColor(Color.WHITE);
    }
}
