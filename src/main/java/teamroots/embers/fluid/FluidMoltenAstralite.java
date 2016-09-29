package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenAstralite extends Fluid {
	public FluidMoltenAstralite() {
		super("moltenAstralite",new ResourceLocation(Embers.MODID+":blocks/moltenAstraliteStill"),new ResourceLocation(Embers.MODID+":blocks/moltenAstraliteFlowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.blockMoltenAstralite);
		setUnlocalizedName("moltenAstralite");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
