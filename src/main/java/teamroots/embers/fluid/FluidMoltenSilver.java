package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenSilver extends Fluid {
	public FluidMoltenSilver() {
		super("silver",new ResourceLocation(Embers.MODID+":blocks/moltenSilverStill"),new ResourceLocation(Embers.MODID+":blocks/moltenSilverFlowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.blockMoltenSilver);
		setUnlocalizedName("silver");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
