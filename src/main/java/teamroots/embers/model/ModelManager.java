package teamroots.embers.model;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.model.ModelBase;

public class ModelManager {
	public static Map<String, ModelBase> models = new HashMap<String, ModelBase>();
	public static void init(){
		models.put("ancientGolem",new ModelGolem());
		models.put("beamCannon",new ModelBeamCannon());
		models.put("ashenCloak", new ModelArmorHolder("ashenCloak"));
	}
}
