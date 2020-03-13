package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelNull extends ModelBase {

    public static ModelNull INSTANCE;

    public ModelNull() {
        textureWidth = 128;
        textureHeight = 128;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}
}