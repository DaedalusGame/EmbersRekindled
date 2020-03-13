package teamroots.embers.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModelAshenArmor extends ModelBiped {
    public static ModelAshenArmor HEAD;
    public static ModelAshenArmor BODY;
    public static ModelAshenArmor LEGS;
    public static ModelAshenArmor FEET;

    EntityEquipmentSlot slot;

    public double velocity = 0;

    private final ModelRenderer head;
    private final ModelRenderer head1;
    private final ModelRenderer head2;
    private final ModelRenderer head3;
    private final ModelRenderer head4;
    private final ModelRenderer head5;
    private final ModelRenderer head6;
    private final ModelRenderer head7;
    private final ModelRenderer head8;
    private final ModelRenderer head9;
    private final ModelRenderer head10;
    private final ModelRenderer head11;
    private final ModelRenderer head12;
    private final ModelRenderer head13;
    private final ModelRenderer head14;
    private final ModelRenderer head15;
    private final ModelRenderer chest;
    private final ModelRenderer front1;
    private final ModelRenderer front2;
    private final ModelRenderer front3;
    private final ModelRenderer neck;
    private final ModelRenderer armR;
    private final ModelRenderer shoulderR;
    private final ModelRenderer gloveR;
    private final ModelRenderer armL;
    private final ModelRenderer shoulderL;
    private final ModelRenderer gloveL;
    private final ModelRenderer legR;
    private final ModelRenderer kneeR;
    private final ModelRenderer legL;
    private final ModelRenderer kneeL;
    private final ModelRenderer legsTop;
    private final ModelRenderer legL1;
    private final ModelRenderer legR1;
    private final ModelRenderer cape;
    private final ModelRenderer cape1;
    private final ModelRenderer cape2;
    private final ModelRenderer cape3;
    private final ModelRenderer capegem1;
    private final ModelRenderer capegem2;
    private final ModelRenderer capegem3;
    private final ModelRenderer capegem4;
    private final ModelRenderer capegem5;
    private final ModelRenderer capegem6;
    private final ModelRenderer capegem7;
    private final ModelRenderer bootR;
    private final ModelRenderer bootR1;
    private final ModelRenderer bootR2;
    private final ModelRenderer bootL;
    private final ModelRenderer bootL1;
    private final ModelRenderer bootL2;

    public ModelAshenArmor(EntityEquipmentSlot slot) {
        textureWidth = 64;
        textureHeight = 64;

        this.slot = slot;

        head = new ModelRendererScale(this, 1.1f);
        head.setRotationPoint(0.0F, 0.0F, 0.0F);
        chest = new ModelRendererScale(this, 1.1f);
        chest.setRotationPoint(0.0F, 0.0F, 0.0F);
        armR = new ModelRenderer(this);
        armR.setRotationPoint(-5.0F, 2.0F, 0.0F);
        armL = new ModelRenderer(this);
        armL.setRotationPoint(5.0F, 2.0F, 0.0F);
        legsTop = new ModelRendererScale(this, 1.1f);
        legsTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        legR = new ModelRenderer(this);
        legR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        legL = new ModelRenderer(this);
        legL.setRotationPoint(1.9F, 12.0F, 0.0F);
        bootR = new ModelRenderer(this);
        bootR.setRotationPoint(-1.9F, 12.0F, 0.0F);
        bootL = new ModelRenderer(this);
        bootL.setRotationPoint(1.9F, 12.0F, 0.0F);
        cape = new ModelRendererScale(this, 1.1f);
        cape.setRotationPoint(0.0F, 0.0F, 0.0F);

        head1 = new ModelRenderer(this);
        head1.setRotationPoint(0.0F, 0.0F, 0.0F);
        head.addChild(head1);
        head1.cubeList.add(new ModelBox(head1, 20, 35, -2.0F, -6.0F, 3.0F, 4, 4, 2, 0.0F, true));

        head2 = new ModelRenderer(this);
        head2.setRotationPoint(0.0F, -4.0F, 4.0F);
        head.addChild(head2);
        head2.cubeList.add(new ModelBox(head2, 36, 32, -4.0F, -1.0F, -1.0F, 8, 2, 1, 0.0F, true));

        head3 = new ModelRenderer(this);
        head3.setRotationPoint(-3.0F, -4.0F, 0.0F);
        head.addChild(head3);
        head3.cubeList.add(new ModelBox(head3, 32, 35, -1.0F, -1.0F, -4.0F, 1, 2, 7, 0.0F, true));

        head4 = new ModelRenderer(this);
        head4.setRotationPoint(4.0F, -4.0F, 0.0F);
        head.addChild(head4);
        head4.cubeList.add(new ModelBox(head4, 32, 35, -1.0F, -1.0F, -4.0F, 1, 2, 7, 0.0F, true));

        head5 = new ModelRenderer(this);
        head5.setRotationPoint(0.0F, -4.0F, 4.0F);
        head.addChild(head5);
        head5.cubeList.add(new ModelBox(head5, 32, 37, -1.0F, -4.0F, -1.0F, 2, 5, 1, 0.0F, true));

        head6 = new ModelRenderer(this);
        head6.setRotationPoint(0.0F, -4.0F, 0.0F);
        head.addChild(head6);
        head6.cubeList.add(new ModelBox(head6, 32, 48, -1.0F, -4.0F, -4.0F, 2, 1, 7, 0.0F, true));

        head7 = new ModelRenderer(this);
        head7.setRotationPoint(1.0F, -5.0F, -4.0F);
        head.addChild(head7);
        head7.cubeList.add(new ModelBox(head7, 22, 50, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, true));

        head8 = new ModelRenderer(this);
        head8.setRotationPoint(4.0F, -5.0F, -4.0F);
        head.addChild(head8);
        head8.cubeList.add(new ModelBox(head8, 22, 50, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, true));

        head9 = new ModelRenderer(this);
        head9.setRotationPoint(2.5F, -6.0F, -4.0F);
        head.addChild(head9);
        head9.cubeList.add(new ModelBox(head9, 22, 48, -2.0F, 0.0F, -1.0F, 4, 1, 1, 0.0F, true));

        head10 = new ModelRenderer(this);
        head10.setRotationPoint(2.5F, -3.0F, -4.0F);
        head.addChild(head10);
        head10.cubeList.add(new ModelBox(head10, 22, 48, -2.0F, 0.0F, -1.0F, 4, 1, 1, 0.0F, true));

        head11 = new ModelRenderer(this);
        head11.setRotationPoint(-4.0F, -5.0F, -4.0F);
        head.addChild(head11);
        head11.cubeList.add(new ModelBox(head11, 22, 50, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, true));

        head12 = new ModelRenderer(this);
        head12.setRotationPoint(-2.5F, -6.0F, -4.0F);
        head.addChild(head12);
        head12.cubeList.add(new ModelBox(head12, 22, 48, -2.0F, 0.0F, -1.0F, 4, 1, 1, 0.0F, true));

        head13 = new ModelRenderer(this);
        head13.setRotationPoint(-1.0F, -5.0F, -4.0F);
        head.addChild(head13);
        head13.cubeList.add(new ModelBox(head13, 22, 50, -0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F, true));

        head14 = new ModelRenderer(this);
        head14.setRotationPoint(-2.5F, -3.0F, -4.0F);
        head.addChild(head14);
        head14.cubeList.add(new ModelBox(head14, 22, 48, -2.0F, 0.0F, -1.0F, 4, 1, 1, 0.0F, true));

        head15 = new ModelRenderer(this);
        head15.setRotationPoint(0.0F, -3.0F, -3.0F);
        head.addChild(head15);
        head15.cubeList.add(new ModelBox(head15, 32, 37, -1.0F, -4.0F, -1.0F, 2, 4, 1, 0.0F, true));

        front1 = new ModelRenderer(this);
        front1.setRotationPoint(-3.0F, 0.0F, -1.5F);
        setRotationAngle(front1, 0.0873F, -2.8798F, 0.0F);
        chest.addChild(front1);
        front1.cubeList.add(new ModelBox(front1, 16, 0, -2.0F, 0.0F, -1.0F, 4, 12, 2, 0.0F, true));

        front2 = new ModelRenderer(this);
        front2.setRotationPoint(3.0F, 0.0F, -1.5F);
        setRotationAngle(front2, 0.0873F, 2.8798F, 0.0F);
        chest.addChild(front2);
        front2.cubeList.add(new ModelBox(front2, 16, 0, -2.0F, 0.0F, -1.0F, 4, 12, 2, 0.0F, false));

        front3 = new ModelRenderer(this);
        front3.setRotationPoint(0.0F, 0.0F, -1.5F);
        setRotationAngle(front3, 0.0873F, 3.1416F, 0.0F);
        chest.addChild(front3);
        front3.cubeList.add(new ModelBox(front3, 32, 16, -2.5F, 0.0F, -1.0F, 5, 10, 2, 0.0F, true));

        neck = new ModelRenderer(this);
        neck.setRotationPoint(0.0F, -1.0F, 0.0F);
        chest.addChild(neck);
        neck.cubeList.add(new ModelBox(neck, 0, 24, -4.5F, 0.0F, -4.5F, 9, 2, 9, 0.0F, true));

        shoulderR = new ModelRenderer(this);
        shoulderR.setRotationPoint(0.0F, 0.0F, 0.0F);
        armR.addChild(shoulderR);
        shoulderR.cubeList.add(new ModelBox(shoulderR, 0, 35, -3.5F, -4.0F, -2.5F, 5, 5, 5, 0.0F, false));

        gloveR = new ModelRendererScale(this, 1.1f);
        gloveR.setRotationPoint(-1.0F, 8.0F, 0.0F);
        armR.addChild(gloveR);
        gloveR.cubeList.add(new ModelBox(gloveR, 48, 20, -2.0F, -3.0F, -2.0F, 4, 5, 4, 0.0F, true));

        shoulderL = new ModelRenderer(this);
        shoulderL.setRotationPoint(0.0F, 0.0F, 0.0F);
        armL.addChild(shoulderL);
        shoulderL.cubeList.add(new ModelBox(shoulderL, 16, 54, -1.5F, -4.0F, -2.5F, 5, 5, 5, 0.0F, false));

        gloveL = new ModelRendererScale(this, 1.1f);
        gloveL.setRotationPoint(1.0F, 8.0F, 0.0F);
        armL.addChild(gloveL);
        gloveL.cubeList.add(new ModelBox(gloveL, 48, 20, -2.0F, -3.0F, -2.0F, 4, 5, 4, 0.0F, true));

        kneeR = new ModelRendererScale(this, 1.1f);
        kneeR.setRotationPoint(-0.6F, 0.0F, -0.5F);
        setRotationAngle(kneeR, 0.0873F, 0.0873F, 0.0F);
        legR.addChild(kneeR);
        kneeR.cubeList.add(new ModelBox(kneeR, 0, 45, -2.5F, 1.0F, -2.5F, 5, 5, 5, 0.0F, true));

        kneeL = new ModelRendererScale(this, 1.1f);
        kneeL.setRotationPoint(0.6F, 0.0F, -0.5F);
        setRotationAngle(kneeL, 0.0873F, -0.0873F, 0.0F);
        legL.addChild(kneeL);
        kneeL.cubeList.add(new ModelBox(kneeL, 0, 45, -2.5F, 1.0F, -2.5F, 5, 5, 5, 0.0F, true));

        legL1 = new ModelRenderer(this);
        legL1.setRotationPoint(2.1F, 13.0F, -0.5F);
        setRotationAngle(legL1, 0.0F, -0.1745F, 0.0F);
        legsTop.addChild(legL1);
        legL1.cubeList.add(new ModelBox(legL1, 32, 0, -2.5F, -5.1867F, -2.0F, 5, 4, 5, 0.0F, true));

        legR1 = new ModelRenderer(this);
        legR1.setRotationPoint(-2.1F, 13.0F, -0.5F);
        setRotationAngle(legR1, 0.0F, 0.1745F, 0.0F);
        legsTop.addChild(legR1);
        legR1.cubeList.add(new ModelBox(legR1, 32, 0, -2.5F, -5.1867F, -2.0F, 5, 4, 5, 0.0F, true));

        cape1 = new ModelRenderer(this);
        cape1.setRotationPoint(0.0F, 0.0F, 3.0F);
        setRotationAngle(cape1, 0.2618F, 0.0F, 0.0F);
        cape.addChild(cape1);
        cape1.cubeList.add(new ModelBox(cape1, 0, 0, -3.0F, 0.0F, -1.0F, 6, 22, 2, 0.0F, true));

        cape2 = new ModelRenderer(this);
        cape2.setRotationPoint(3.0F, 0.0F, 1.5F);
        setRotationAngle(cape2, 0.2618F, 0.5236F, 0.0F);
        cape.addChild(cape2);
        cape2.cubeList.add(new ModelBox(cape2, 48, 40, -3.0F, 0.0F, -1.0F, 6, 22, 2, 0.0F, true));

        cape3 = new ModelRenderer(this);
        cape3.setRotationPoint(-3.0F, 0.0F, 1.5F);
        setRotationAngle(cape3, 0.2618F, -0.5236F, 0.0F);
        cape.addChild(cape3);
        cape3.cubeList.add(new ModelBox(cape3, 48, 40, -3.0F, 0.0F, -1.0F, 6, 22, 2, 0.0F, false));

        capegem1 = new ModelRenderer(this);
        capegem1.setRotationPoint(3.0F, 0.0F, 1.5F);
        setRotationAngle(capegem1, 0.2618F, 0.5236F, 0.0F);
        cape.addChild(capegem1);
        capegem1.cubeList.add(new ModelBox(capegem1, 0, 60, -1.0F, 16.0F, 0.0F, 2, 2, 2, 0.0F, true));

        capegem2 = new ModelRenderer(this);
        capegem2.setRotationPoint(3.0F, 0.0F, 1.5F);
        setRotationAngle(capegem2, 0.2618F, 0.5236F, 0.0F);
        cape.addChild(capegem2);
        capegem2.cubeList.add(new ModelBox(capegem2, 0, 60, -1.0F, 10.0F, 0.0F, 2, 2, 2, 0.0F, true));

        capegem3 = new ModelRenderer(this);
        capegem3.setRotationPoint(3.0F, 0.0F, 1.5F);
        setRotationAngle(capegem3, 0.2618F, 0.5236F, 0.0F);
        cape.addChild(capegem3);
        capegem3.cubeList.add(new ModelBox(capegem3, 0, 60, -1.0F, 4.0F, 0.0F, 2, 2, 2, 0.0F, true));

        capegem4 = new ModelRenderer(this);
        capegem4.setRotationPoint(0.0F, 0.0F, 3.0F);
        setRotationAngle(capegem4, 0.2618F, 0.0F, 0.0F);
        cape.addChild(capegem4);
        capegem4.cubeList.add(new ModelBox(capegem4, 0, 60, -1.0F, 2.0F, 0.0F, 2, 2, 2, 0.0F, true));

        capegem5 = new ModelRenderer(this);
        capegem5.setRotationPoint(-3.0F, 0.0F, 1.5F);
        setRotationAngle(capegem5, 0.2618F, -0.5236F, 0.0F);
        cape.addChild(capegem5);
        capegem5.cubeList.add(new ModelBox(capegem5, 0, 60, -1.0F, 4.0F, 0.0F, 2, 2, 2, 0.0F, true));

        capegem6 = new ModelRenderer(this);
        capegem6.setRotationPoint(-3.0F, 0.0F, 1.5F);
        setRotationAngle(capegem6, 0.2618F, -0.5236F, 0.0F);
        cape.addChild(capegem6);
        capegem6.cubeList.add(new ModelBox(capegem6, 0, 60, -1.0F, 10.0F, 0.0F, 2, 2, 2, 0.0F, true));

        capegem7 = new ModelRenderer(this);
        capegem7.setRotationPoint(-3.0F, 0.0F, 1.5F);
        setRotationAngle(capegem7, 0.2618F, -0.5236F, 0.0F);
        cape.addChild(capegem7);
        capegem7.cubeList.add(new ModelBox(capegem7, 0, 60, -1.0F, 16.0F, 0.0F, 2, 2, 2, 0.0F, true));

        bootR1 = new ModelRendererScale(this, 1.1f);
        bootR1.setRotationPoint(-0.1F, 12.0F, 0.0F);
        bootR.addChild(bootR1);
        bootR1.cubeList.add(new ModelBox(bootR1, 16, 14, -2.0F, -7.0F, -2.0F, 4, 6, 4, 0.0F, false));

        bootR2 = new ModelRendererScale(this, 1.1f);
        bootR2.setRotationPoint(-0.1F, 12.0F, 0.0F);
        bootR.addChild(bootR2);
        bootR2.cubeList.add(new ModelBox(bootR2, 48, 16, -2.0F, -3.0F, -3.0F, 4, 2, 1, 0.0F, false));

        bootL1 = new ModelRendererScale(this, 1.1f);
        bootL1.setRotationPoint(0.1F, 12.0F, 0.0F);
        bootL.addChild(bootL1);
        bootL1.cubeList.add(new ModelBox(bootL1, 16, 14, -2.0F, -7.0F, -2.0F, 4, 6, 4, 0.0F, true));

        bootL2 = new ModelRendererScale(this, 1.1f);
        bootL2.setRotationPoint(0.1F, 12.0F, 0.0F);
        bootL.addChild(bootL2);
        bootL2.cubeList.add(new ModelBox(bootL2, 48, 16, -2.0F, -3.0F, -3.0F, 4, 2, 1, 0.0F, true));
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity instanceof EntityArmorStand) {
            // Hack so helmets look right on armor stand
            netHeadYaw = 0;
        }

        head.showModel = slot == EntityEquipmentSlot.HEAD;
        chest.showModel = slot == EntityEquipmentSlot.CHEST;
        armR.showModel = slot == EntityEquipmentSlot.CHEST;
        armL.showModel = slot == EntityEquipmentSlot.CHEST;
        legsTop.showModel = slot == EntityEquipmentSlot.LEGS;
        legL.showModel = slot == EntityEquipmentSlot.LEGS;
        legR.showModel = slot == EntityEquipmentSlot.LEGS;
        bootL.showModel = slot == EntityEquipmentSlot.FEET;
        bootR.showModel = slot == EntityEquipmentSlot.FEET;

        bipedHeadwear.showModel = false;

        switch (slot) {
            case FEET:
                bipedLeftLeg = bootL;
                bipedRightLeg = bootR;
                break;
            case LEGS:
                bipedLeftLeg = legL;
                bipedRightLeg = legR;
                bipedBody = legsTop;
                break;
            case CHEST:
                bipedBody = chest;
                bipedLeftArm = armL;
                bipedRightArm = armR;
                break;
            case HEAD:
                bipedHead = head;
                break;
        }

        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale * 1.0f);

        renderCape(entity, scale);
    }

    public void renderCape(Entity entity, float scale) {
        GlStateManager.pushMatrix();
        if (this.cape != null) {
            this.setCapeRotation(entity);
            cape.showModel = slot == EntityEquipmentSlot.CHEST;
            if (this.cape.childModels.size() == 10 && cape.showModel) {
                for (int i = 1; i < 8; i++) {
                    this.cape.childModels.get(i + 2).showModel = false;
                }
                if (entity instanceof EntityLivingBase) {
                    ItemStack stack = ((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    if (stack.hasTagCompound()) {
                        for (int i = 1; i < 8; i++) {
                            this.cape.childModels.get(i + 2).showModel = slot == EntityEquipmentSlot.CHEST && stack.getTagCompound().hasKey("gem" + i);
                        }
                    }
                }
            }
            if (this.isChild) {
                float f = 2.0F;
                GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
                GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
                cape.render(scale);
            } else {
                if (entity.isSneaking()) {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                }

                cape.render(scale);
            }
        }
        GlStateManager.popMatrix();
    }

    private void setCapeRotation(Entity entity) {
        double hComp = Math.atan(6.28f * Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ) / 0.4f);
        double yComp = (Math.max(-0.8, Math.min(0.8, -entity.motionY)) + 0.4) / 0.6;
        this.velocity = velocity * 0.5 + (hComp * 0.5 + yComp * 0.5) * 0.5;
        cape.rotationPointX = bipedBody.rotationPointX;
        cape.rotationPointY = bipedBody.rotationPointY - 1;
        cape.rotationPointZ = bipedBody.rotationPointZ;
        setRotationAngle(cape, bipedBody.rotateAngleX + ((float) Math.toRadians(80.0f * velocity * 0.5f)), bipedBody.rotateAngleY, bipedBody.rotateAngleZ);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
