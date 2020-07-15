package teamroots.embers.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;

import java.awt.*;

public class FluidOilDwarven extends Fluid {
    public FluidOilDwarven() {
        super("oil_dwarf", new ResourceLocation(Embers.MODID, "blocks/fluid_oil_dwarf_still"), new ResourceLocation(Embers.MODID, "blocks/fluid_oil_dwarf_flowing"));

        setViscosity(1000);
        setDensity(2000);
        setTemperature(300);
        setGaseous(true);
        setUnlocalizedName("oil_dwarf");
        setColor(Color.WHITE);
    }
}
