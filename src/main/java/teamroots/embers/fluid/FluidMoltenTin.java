package teamroots.embers.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

import java.awt.*;

public class FluidMoltenTin extends Fluid {
	public FluidMoltenTin() {
		super("tin",new ResourceLocation(Embers.MODID+":blocks/molten_tin_still"),new ResourceLocation(Embers.MODID+":blocks/molten_tin_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.block_molten_tin);
		setUnlocalizedName("tin");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
