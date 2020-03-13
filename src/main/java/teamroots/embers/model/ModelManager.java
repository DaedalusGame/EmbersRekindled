package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
	public static Map<String, ModelBase> models = new HashMap<String, ModelBase>();

	public static void init(){
		models.put("ancientGolem",new ModelGolem());
		models.put("beamCannon",new ModelBeamCannon());
		models.put("ashenCloak", new ModelArmorHolder("ashenCloak"));

		ModelAshenArmor.HEAD = new ModelAshenArmor(EntityEquipmentSlot.HEAD);
		ModelAshenArmor.BODY = new ModelAshenArmor(EntityEquipmentSlot.CHEST);
		ModelAshenArmor.LEGS = new ModelAshenArmor(EntityEquipmentSlot.LEGS);
		ModelAshenArmor.FEET = new ModelAshenArmor(EntityEquipmentSlot.FEET);

		ModelNull.INSTANCE = new ModelNull();
		ModelMagmaWormHead.INSTANCE = new ModelMagmaWormHead();
		ModelMagmaWormSegment.INSTANCE = new ModelMagmaWormSegment();
		ModelMagmaWormSegmentLarge.INSTANCE = new ModelMagmaWormSegmentLarge();
		ModelMagmaWormSegmentFirst.INSTANCE = new ModelMagmaWormSegmentFirst();
		ModelMagmaWormTail.INSTANCE = new ModelMagmaWormTail();

	}
}
