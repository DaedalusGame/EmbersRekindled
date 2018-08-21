package teamroots.embers.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;

import java.awt.*;

public class FluidSteam extends Fluid {

    public FluidSteam() {
        super("steam", new ResourceLocation(Embers.MODID, "blocks/fluid_steam_still"), new ResourceLocation(Embers.MODID, "blocks/fluid_steam_flowing"));

        setViscosity(200);
        setDensity(-1000);
        setTemperature(750);
        setGaseous(true);
        setUnlocalizedName("steam");
    }

    @Override
    public int getColor() {
        return Color.WHITE.getRGB();
    }
}
