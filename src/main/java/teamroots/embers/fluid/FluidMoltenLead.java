package teamroots.embers.fluid;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;

public class FluidMoltenLead extends Fluid {
	public FluidMoltenLead() {
		super("lead",new ResourceLocation(Embers.MODID+":blocks/molten_lead_still"),new ResourceLocation(Embers.MODID+":blocks/molten_lead_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManager.block_molten_lead);
		setUnlocalizedName("lead");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
