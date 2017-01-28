package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenUmberSteel extends Fluid {
	public FluidMoltenUmberSteel() {
		super("umber_steel",new ResourceLocation(Embers.MODID+":blocks/molten_umber_steel_still"),new ResourceLocation(Embers.MODID+":blocks/molten_umber_steel_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.block_molten_umber_steel);
		setUnlocalizedName("umberSteel");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
