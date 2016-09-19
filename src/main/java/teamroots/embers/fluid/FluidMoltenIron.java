package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenIron extends Fluid {
	public FluidMoltenIron() {
		super("moltenIron",new ResourceLocation(Embers.MODID+":blocks/fluidMetalBase"),new ResourceLocation(Embers.MODID+":blocks/fluidMetalFlowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.blockMoltenIron);
		setUnlocalizedName("moltenIron");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
