package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenDawnstone extends Fluid {
	public FluidMoltenDawnstone() {
		super("dawnstone",new ResourceLocation(Embers.MODID+":blocks/moltenDawnstoneStill"),new ResourceLocation(Embers.MODID+":blocks/moltenDawnstoneFlowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.blockMoltenDawnstone);
		setUnlocalizedName("dawnstone");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
