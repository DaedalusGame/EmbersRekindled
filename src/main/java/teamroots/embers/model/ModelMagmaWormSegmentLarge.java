package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.entity.EntityMagmaWorm;
import teamroots.embers.util.Misc;

public class ModelMagmaWormSegmentLarge extends ModelBase {

    public static ModelMagmaWormSegmentLarge INSTANCE;
    //fields
    ModelRenderer head1;
    ModelRenderer stonehead1;
    ModelRenderer stonehead2;
    ModelRenderer head2;
    ModelRenderer head3;
    ModelRenderer head4;
    ModelRenderer head5;
    ModelRenderer head6;
    ModelRenderer head7;
    ModelRenderer head8;
    ModelRenderer head9;
    ModelRenderer head10;
    ModelRenderer head11;
    ModelRenderer head12;
    ModelRenderer head13;

    public ModelMagmaWormSegmentLarge() {
        textureWidth = 128;
        textureHeight = 128;
        head1 = new ModelRenderer(this, 64, 0);
        head1.addBox(-6F, -6F, -6F, 12, 12, 12);
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
        stonehead2 = new ModelRenderer(this, 16, 32);
        stonehead2.addBox(-2F, -2F, -2F, 4, 4, 4);
        stonehead2.setRotationPoint(0F, 0F, 6F);
        stonehead2.setTextureSize(128, 128);
        stonehead2.mirror = true;
        setRotation(stonehead2, 0F, 0F, 0F);
        head2 = new ModelRenderer(this, 0, 64);
        head2.addBox(-5F, -5F, -5F, 10, 10, 10);
        head2.setRotationPoint(0F, 0F, 11F);
        head2.setTextureSize(128, 128);
        head2.mirror = true;
        setRotation(head2, 0F, 0F, 0F);
        head3 = new ModelRenderer(this, 0, 16);
        head3.addBox(-3F, -3F, -3F, 6, 6, 6);
        head3.setRotationPoint(0F, 0F, 19F);
        head3.setTextureSize(128, 128);
        head3.mirror = true;
        setRotation(head3, 0F, 0F, 0F);
        head4 = new ModelRenderer(this, 0, 64);
        head4.addBox(-5F, 0F, -5F, 10, 10, 10);
        head4.setRotationPoint(1F, -2F, -1F);
        head4.setTextureSize(128, 128);
        head4.mirror = true;
        setRotation(head4, 1.963495F, 0.7853982F, -0.5235988F);
        head5 = new ModelRenderer(this, 0, 64);
        head5.addBox(-5F, 0F, -5F, 10, 10, 10);
        head5.setRotationPoint(1F, 2F, -1F);
        head5.setTextureSize(128, 128);
        head5.mirror = true;
        setRotation(head5, 1.178097F, 0.3926991F, -0.5235988F);
        head6 = new ModelRenderer(this, 0, 64);
        head6.addBox(-5F, 0F, -5F, 10, 10, 10);
        head6.setRotationPoint(-1F, -2F, -1F);
        head6.setTextureSize(128, 128);
        head6.mirror = true;
        setRotation(head6, 1.963495F, -0.7853982F, 0.5235988F);
        head7 = new ModelRenderer(this, 0, 64);
        head7.addBox(-5F, 0F, -5F, 10, 10, 10);
        head7.setRotationPoint(-1F, 2F, -1F);
        head7.setTextureSize(128, 128);
        head7.mirror = true;
        setRotation(head7, 1.178097F, -0.3926991F, 0.5235988F);
        head8 = new ModelRenderer(this, 0, 16);
        head8.addBox(-3F, -3F, -3F, 6, 6, 6);
        head8.setRotationPoint(0F, -6F, -3F);
        head8.setTextureSize(128, 128);
        head8.mirror = true;
        setRotation(head8, 0.7853982F, 0F, 0F);
        head9 = new ModelRenderer(this, 0, 16);
        head9.addBox(-3F, -3F, -3F, 6, 6, 6);
        head9.setRotationPoint(0F, 6F, -3F);
        head9.setTextureSize(128, 128);
        head9.mirror = true;
        setRotation(head9, -0.7853982F, 0F, 0F);
        head10 = new ModelRenderer(this, 0, 0);
        head10.addBox(-4F, 0F, -4F, 8, 8, 8);
        head10.setRotationPoint(-1F, -2F, 7F);
        head10.setTextureSize(128, 128);
        head10.mirror = true;
        setRotation(head10, 1.963495F, -0.7853982F, 0.5235988F);
        head11 = new ModelRenderer(this, 0, 0);
        head11.addBox(-4F, 0F, -4F, 8, 8, 8);
        head11.setRotationPoint(1F, -2F, 7F);
        head11.setTextureSize(128, 128);
        head11.mirror = true;
        setRotation(head11, 1.963495F, 0.7853982F, -0.5235988F);
        head12 = new ModelRenderer(this, 0, 0);
        head12.addBox(-4F, 0F, -4F, 8, 8, 8);
        head12.setRotationPoint(1F, 2.386667F, 6F);
        head12.setTextureSize(128, 128);
        head12.mirror = true;
        setRotation(head12, 1.178097F, 0.3926991F, -0.5235988F);
        head13 = new ModelRenderer(this, 0, 0);
        head13.addBox(-4F, 0F, -4F, 8, 8, 8);
        head13.setRotationPoint(-1F, 2.386667F, 6F);
        head13.setTextureSize(128, 128);
        head13.mirror = true;
        setRotation(head13, 1.178097F, -0.3926991F, 0.5235988F);
    }

    public Vec3d getPosFromIndex(EntityMagmaWorm guardian, int index, float partialTicks) {
        return guardian.getSegmentPosition(index,partialTicks);
    }

    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, int index) {
        float scale = scaleFactor;
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        GlStateManager.pushAttrib();
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
        head5.render(scale);
        head6.render(scale);
        head7.render(scale);
        head8.render(scale);
        head9.render(scale);
        head10.render(scale);
        head11.render(scale);
        head12.render(scale);
        head13.render(scale);
        GlStateManager.enableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
