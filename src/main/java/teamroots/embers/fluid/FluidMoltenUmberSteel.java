package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenUmberSteel extends Fluid {
	public FluidMoltenUmberSteel() {
		super("moltenUmberSteel",new ResourceLocation(Embers.MODID+":blocks/moltenUmberSteelStill"),new ResourceLocation(Embers.MODID+":blocks/moltenUmberSteelFlowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.blockMoltenUmberSteel);
		setUnlocalizedName("moltenUmberSteel");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
