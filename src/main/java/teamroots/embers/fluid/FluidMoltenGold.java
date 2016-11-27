package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenGold extends Fluid {
	public FluidMoltenGold() {
		super("gold",new ResourceLocation(Embers.MODID+":blocks/moltenGoldStill"),new ResourceLocation(Embers.MODID+":blocks/moltenGoldFlowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.blockMoltenGold);
		setUnlocalizedName("gold");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
