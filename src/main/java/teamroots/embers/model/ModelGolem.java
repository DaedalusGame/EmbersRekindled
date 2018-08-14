package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelGolem extends ModelBase
{
  //fields
    ModelRenderer legL;
    ModelRenderer legR;
    ModelRenderer body1;
    ModelRenderer body2;
    ModelRenderer armR;
    ModelRenderer fistR;
    ModelRenderer armL;
    ModelRenderer fistL;
    ModelRenderer head;
  
  public ModelGolem()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      legL = new ModelRenderer(this, 0, 0);
      legL.addBox(-2F, 0F, -2F, 4, 10, 4);
      legL.setRotationPoint(2F, 14F, 0F);
      legL.setTextureSize(64, 64);
      legL.mirror = true;
      setRotation(legL, 0F, 0F, 0F);
      legR = new ModelRenderer(this, 0, 0);
      legR.addBox(-2F, 0F, -2F, 4, 10, 4);
      legR.setRotationPoint(-2F, 14F, 0F);
      legR.setTextureSize(64, 64);
      legR.mirror = true;
      setRotation(legR, 0F, 0F, 0F);
      body1 = new ModelRenderer(this, 16, 16);
      body1.addBox(-4F, 0F, -2F, 8, 4, 4);
      body1.setRotationPoint(0F, 10F, 0F);
      body1.setTextureSize(64, 64);
      body1.mirror = true;
      setRotation(body1, 0F, 0F, 0F);
      body2 = new ModelRenderer(this, 16, 0);
      body2.addBox(-4.5F, 0F, -3F, 9, 8, 6);
      body2.setRotationPoint(0F, 2F, 0F);
      body2.setTextureSize(64, 64);
      body2.mirror = true;
      setRotation(body2, 0F, 0F, 0F);
      armR = new ModelRenderer(this, 48, 0);
      armR.addBox(-2F, 0F, -2F, 4, 12, 4);
      armR.setRotationPoint(-4.5F, 2.013333F, 0F);
      armR.setTextureSize(64, 64);
      armR.mirror = true;
      setRotation(armR, 0F, 0F, 0.3926991F);
      fistR = new ModelRenderer(this, 0, 32);
      fistR.addBox(-2F, 12F, -2.5F, 5, 5, 5);
      fistR.setRotationPoint(-4.5F, 2.013333F, 0F);
      fistR.setTextureSize(64, 64);
      fistR.mirror = true;
      setRotation(fistR, 0F, 0F, 0.3926991F);
      armL = new ModelRenderer(this, 48, 0);
      armL.addBox(-2F, 0F, -2F, 4, 12, 4);
      armL.setRotationPoint(4.5F, 2.013333F, 0F);
      armL.setTextureSize(64, 64);
      armL.mirror = true;
      setRotation(armL, 0F, 0F, -0.3926991F);
      fistL = new ModelRenderer(this, 0, 32);
      fistL.addBox(-3F, 12F, -2.5F, 5, 5, 5);
      fistL.setRotationPoint(4.5F, 2.013333F, 0F);
      fistL.setTextureSize(64, 64);
      fistL.mirror = false;
      setRotation(fistL, 0F, 0F, -0.3926991F);
      head = new ModelRenderer(this, 32, 32);
      head.addBox(-4F, -8F, -4F, 8, 8, 8);
      head.setRotationPoint(0F, 2F, 0F);
      head.setTextureSize(64, 64);
      head.mirror = true;
      setRotation(head, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
  {
	GlStateManager.pushMatrix();
    super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    legL.render(scale);
    legR.render(scale);
    body1.render(scale);
    body2.render(scale);
    armR.render(scale);
    fistR.render(scale);
    armL.render(scale);
    fistL.render(scale);
    head.render(scale);
    GlStateManager.popMatrix();
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
  {
    legR.rotateAngleX = (float)Math.toRadians(180f*(float) Math.sin(limbSwing*0.5)*limbSwingAmount*0.5);
    legL.rotateAngleX = -(float)Math.toRadians(180f*(float) Math.sin(limbSwing*0.5)*limbSwingAmount*0.5);
    armL.rotateAngleX = (float)Math.toRadians(180f*(float) Math.sin(limbSwing*0.5)*limbSwingAmount*0.5);
    armR.rotateAngleX = -(float)Math.toRadians(180f*(float) Math.sin(limbSwing*0.5)*limbSwingAmount*0.5);
    fistL.rotateAngleX = (float)Math.toRadians(180f*(float) Math.sin(limbSwing*0.5)*limbSwingAmount*0.5);
    fistR.rotateAngleX = -(float)Math.toRadians(180f*(float) Math.sin(limbSwing*0.5)*limbSwingAmount*0.5);
  }

}
