package teamroots.embers.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teamroots.embers.Embers;
import teamroots.embers.model.*;

public class RenderMagmaWorm extends RenderLiving<EntityMagmaWorm> {

    public RenderMagmaWorm(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
    }

    public static class Factory implements IRenderFactory<EntityMagmaWorm> {

        @Override
        public RenderMagmaWorm createRenderFor(RenderManager manager) {
            return new RenderMagmaWorm(manager, ModelNull.INSTANCE, 0.5f);
        }
    }

    @Override
    public void renderModel(EntityMagmaWorm entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        boolean flag = !entity.isInvisible() || this.renderOutlines;
        boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture(entity)) {
                return;
            }
            if (flag1) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            for (int i = 1; i < entity.getSegments(); i++) {
                if (i == entity.getSegments() -1) {
                    ModelMagmaWormTail.INSTANCE.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, i);
                }
                else if (i == 1) {
                    ModelMagmaWormSegmentFirst.INSTANCE.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, i);
                }
                else if (i == 2) {
                    ModelMagmaWormSegmentLarge.INSTANCE.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, i);
                }
                else {
                    ModelMagmaWormSegment.INSTANCE.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, i);
                }
            }
            ModelMagmaWormHead.INSTANCE.render(entity,limbSwing,limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (flag1) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    @Override
    public boolean shouldRender(EntityMagmaWorm entity, ICamera camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMagmaWorm entity) {
        return new ResourceLocation(Embers.MODID, "textures/entity/magma_worm.png");
    }
}