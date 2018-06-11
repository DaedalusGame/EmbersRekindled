package teamroots.embers.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialUnpushable extends Material {

	public MaterialUnpushable() {
		super(MapColor.STONE);
		setRequiresTool();
		setImmovableMobility();
	}
}
