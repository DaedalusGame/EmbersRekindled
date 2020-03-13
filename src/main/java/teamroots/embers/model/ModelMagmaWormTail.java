package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.util.Misc;

public class ModelMagmaWormTail extends ModelBase {

    public static ModelMagmaWormTail INSTANCE;
    //fields
    ModelRenderer head1;
    ModelRenderer stonehead1;
    ModelRenderer head2;
    ModelRenderer stonehead2;
    ModelRenderer crest1;
    ModelRenderer crest2;
    ModelRenderer crest3;
    ModelRenderer crest4;
    ModelRenderer crest5;
    ModelRenderer leg1;
    ModelRenderer leg2;
    ModelRenderer head3;
    ModelRenderer head4;

    public ModelMagmaWormTail() {
        textureWidth = 128;
        textureHeight = 128;
        head1 = new ModelRenderer(this, 0, 64);
        head1.addBox(-5F, -5F, -5F, 10, 10, 10);
        head1.setRotationPoint(0F, 0F, 0F);
        head1.setTextureSize(128, 128);
        head1.mirror = true;
        setRotation(head1, 0F, 0F, 0F);
        stonehead1 = new ModelRenderer(this, 16, 32);
        stonehead1.addBox(-3F, -3F, -3F, 6, 6, 6);
        stonehead1.setRotationPoint(0F, 0F, -1F);
        stonehead1.setTextureSize(128, 128);
        stonehead1.mirror = true;
        setRotation(stonehead1, 0F, 0F, 0F);
        head2 = new ModelRenderer(this, 0, 0);
        head2.addBox(-4F, -4F, -4F, 8, 8, 8);
        head2.setRotationPoint(0F, 0F, 9F);
        head2.setTextureSize(128, 128);
        head2.mirror = true;
        setRotation(head2, 0F, 0F, 0F);
        stonehead2 = new ModelRenderer(this, 16, 32);
        stonehead2.addBox(-2F, -2F, -2F, 4, 4, 4);
        stonehead2.setRotationPoint(0F, 0F, 6F);
        stonehead2.setTextureSize(128, 128);
        stonehead2.mirror = true;
        setRotation(stonehead2, 0F, 0F, 0F);
        crest1 = new ModelRenderer(this, 32, 0);
        crest1.addBox(-2F, 0F, -2F, 4, 8, 4);
        crest1.setRotationPoint(-2F, -3F, -1F);
        crest1.setTextureSize(128, 128);
        crest1.mirror = true;
        setRotation(crest1, 2.617994F, 0F, -0.5235988F);
        crest2 = new ModelRenderer(this, 32, 0);
        crest2.addBox(-2F, 0F, -2F, 4, 8, 4);
        crest2.setRotationPoint(2F, -3F, -1F);
        crest2.setTextureSize(128, 128);
        crest2.mirror = true;
        setRotation(crest2, 2.617994F, 0F, 0.5235988F);
        crest3 = new ModelRenderer(this, 48, 0);
        crest3.addBox(-2F, 0F, -2F, 4, 6, 4);
        crest3.setRotationPoint(3F, -1F, -2F);
        crest3.setTextureSize(128, 128);
        crest3.mirror = true;
        setRotation(crest3, 2.617994F, 0F, 1.308997F);
        crest4 = new ModelRenderer(this, 32, 0);
        crest4.addBox(-2F, 0F, -2F, 4, 8, 4);
        crest4.setRotationPoint(0F, -2F, 6F);
        crest4.setTextureSize(128, 128);
        crest4.mirror = true;
        setRotation(crest4, 2.094395F, 0F, 0F);
        crest5 = new ModelRenderer(this, 48, 0);
        crest5.addBox(-2F, 0F, -2F, 4, 6, 4);
        crest5.setRotationPoint(-3F, -1F, -2F);
        crest5.setTextureSize(128, 128);
        crest5.mirror = true;
        setRotation(crest5, 2.617994F, 0F, -1.308997F);
        leg1 = new ModelRenderer(this, 32, 0);
        leg1.addBox(-2F, 0F, -2F, 4, 8, 4);
        leg1.setRotationPoint(-2.5F, 3F, -1F);
        leg1.setTextureSize(128, 128);
        leg1.mirror = true;
        setRotation(leg1, 2.617994F, 0F, -2.617994F);
        leg2 = new ModelRenderer(this, 32, 0);
        leg2.addBox(-2F, 0F, -2F, 4, 8, 4);
        leg2.setRotationPoint(2.5F, 3F, -1F);
        leg2.setTextureSize(128, 128);
        leg2.mirror = true;
        setRotation(leg2, 2.617994F, 0F, 2.617994F);
        head3 = new ModelRenderer(this, 0, 16);
        head3.addBox(-3F, -3F, -3F, 6, 6, 6);
        head3.setRotationPoint(0F, 0F, 16F);
        head3.setTextureSize(128, 128);
        head3.mirror = true;
        setRotation(head3, 0F, 0F, 0F);
        head4 = new ModelRenderer(this, 32, 0);
        head4.addBox(-2F, 0F, -2F, 4, 8, 4);
        head4.setRotationPoint(0F, 0F, 19F);
        head4.setTextureSize(128, 128);
        head4.mirror = true;
        setRotation(head4, 1.570796F, 0F, 0F);
    }

    public Vec3d getPosFromIndex(EntityMagmaWorm guardian, int index, float partialTicks) {
        return guardian.getSegmentPosition(index,partialTicks);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, int index) {
        float scale = scaleFactor;
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        EntityMagmaWorm guardian = (EntityMagmaWorm) entity;
        Vec3d basePos = guardian.getHeadPosition(ageInTicks - (int) ageInTicks);
        float fade = guardian.getFade(ageInTicks - (int) ageInTicks);
        Vec3d pos1 = getPosFromIndex(guardian, index, ageInTicks - (int) ageInTicks);
        Vec3d pos2 = getPosFromIndex(guardian, index + 1, ageInTicks - (int) ageInTicks);
        float yaw = Misc.yawDegreesBetweenPoints(pos2.x, pos1.y, pos1.z, pos1.x, pos2.y, pos2.z);
        float pitch = Misc.pitchDegreesBetweenPoints(pos2.x, pos1.y, pos1.z, pos1.x, pos2.y, pos2.z);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-basePos.x + pos1.x, basePos.y - pos1.y, basePos.z - pos1.z);
        GlStateManager.rotate(yaw + 180, 0, 1, 0);
        GlStateManager.rotate(pitch, 1, 0, 0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1, 1, 1, 1.0f * fade);
        stonehead1.render(scale);
        stonehead2.render(scale);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        float lavaFade = guardian.getSegmentFade(index) * fade;
        GlStateManager.color(1 * lavaFade, 1 * lavaFade, 1 * lavaFade, 0.75f * lavaFade);
        head1.render(scale);
        head2.render(scale);
        head3.render(scale);
        head4.render(scale);
        leg1.render(scale);
        leg2.render(scale);
        crest1.render(scale);
        crest2.render(scale);
        crest3.render(scale);
        crest4.render(scale);
        crest5.render(scale);
        GlStateManager.enableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}