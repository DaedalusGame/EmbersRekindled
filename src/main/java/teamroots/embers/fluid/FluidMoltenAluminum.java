package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenAluminum extends Fluid {
	public FluidMoltenAluminum() {
		super("aluminum",new ResourceLocation(Embers.MODID+":blocks/molten_aluminum_still"),new ResourceLocation(Embers.MODID+":blocks/molten_aluminum_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.block_molten_aluminum);
		setUnlocalizedName("aluminum");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
