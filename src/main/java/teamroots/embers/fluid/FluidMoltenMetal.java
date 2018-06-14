package teamroots.embers.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;

import java.awt.Color;

public class FluidMoltenMetal extends Fluid {

    public FluidMoltenMetal(String fluidName, String resourceName) {
        super(fluidName, new ResourceLocation(Embers.MODID, "blocks/molten_" + resourceName + "_still"), new ResourceLocation(Embers.MODID, "blocks/molten_" + resourceName + "_flowing"));

        setViscosity(6000);
        setDensity(2000);
        setLuminosity(15);
        setTemperature(900);
        setUnlocalizedName(fluidName);
    }

    public FluidMoltenMetal(String fluidName) {
        this(fluidName, fluidName);
    }

    @Override
    public int getColor() {
        return Color.WHITE.getRGB();
    }
}
