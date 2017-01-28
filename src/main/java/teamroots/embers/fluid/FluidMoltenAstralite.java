package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenAstralite extends Fluid {
	public FluidMoltenAstralite() {
		super("astralite",new ResourceLocation(Embers.MODID+":blocks/moltenAstraliteStill"),new ResourceLocation(Embers.MODID+":blocks/moltenAstraliteFlowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.block_molten_astralite);
		setUnlocalizedName("astralite");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
