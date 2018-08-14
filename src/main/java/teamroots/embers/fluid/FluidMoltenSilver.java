package teamroots.embers.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

import java.awt.*;

public class FluidMoltenSilver extends Fluid {
	public FluidMoltenSilver() {
		super("silver",new ResourceLocation(Embers.MODID+":blocks/molten_silver_still"),new ResourceLocation(Embers.MODID+":blocks/molten_silver_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.block_molten_silver);
		setUnlocalizedName("silver");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
