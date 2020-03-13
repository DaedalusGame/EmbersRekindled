package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelRendererScale extends ModelRenderer {
    float scale;

    public ModelRendererScale(ModelBase model, float scale) {
        super(model);
        this.scale = scale;
    }

    @Override
    public void render(float scale) {
        super.render(scale * this.scale);
    }

    @Override
    public void postRender(float scale) {
        super.postRender(scale * this.scale);
    }

    @Override
    public void renderWithRotation(float scale) {
        super.renderWithRotation(scale * this.scale);
    }
}
