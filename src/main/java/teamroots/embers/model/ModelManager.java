package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
	public static Map<String, ModelBase> models = new HashMap<String, ModelBase>();
	public static void init(){
		models.put("ancientGolem",new ModelGolem());
		models.put("beamCannon",new ModelBeamCannon());
		models.put("ashenCloak", new ModelArmorHolder("ashenCloak"));

		ModelNull.instance = new ModelNull();
		ModelMagmaWormHead.instance = new ModelMagmaWormHead();
		ModelMagmaWormSegment.instance = new ModelMagmaWormSegment();
		ModelMagmaWormSegmentLarge.instance = new ModelMagmaWormSegmentLarge();
		ModelMagmaWormSegmentFirst.instance = new ModelMagmaWormSegmentFirst();
		ModelMagmaWormTail.instance = new ModelMagmaWormTail();

	}
}
