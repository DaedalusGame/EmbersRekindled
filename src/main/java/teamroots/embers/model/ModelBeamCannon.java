package teamroots.embers.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import teamroots.embers.tileentity.TileEntityBeamCannon;
import teamroots.embers.util.Misc;

public class ModelBeamCannon extends ModelBase
{//fields
    ModelRenderer base;
    ModelRenderer disc1;
    ModelRenderer disc2;
    ModelRenderer disc3;
    ModelRenderer disc4;
  
  public ModelBeamCannon()
  {
    textureWidth = 32;
    textureHeight = 32;
    
      base = new ModelRenderer(this, 0, 0);
      base.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5);
      base.setRotationPoint(0F, 16F, 0F);
      base.setTextureSize(32, 32);
      base.mirror = true;
      setRotation(base, 0F, 0F, 0F);
      disc1 = new ModelRenderer(this, 0, 10);
      disc1.addBox(-3F, -10F, -3F, 6, 2, 6);
      disc1.setRotationPoint(0F, 16F, 0F);
      disc1.setTextureSize(32, 32);
      disc1.mirror = true;
      setRotation(disc1, 0F, 0F, 0F);
      disc2 = new ModelRenderer(this, 0, 18);
      disc2.addBox(-2F, -12F, -2F, 4, 10, 4);
      disc2.setRotationPoint(0F, 16F, 0F);
      disc2.setTextureSize(32, 32);
      disc2.mirror = true;
      setRotation(disc2, 0F, 0F, 0F);
      disc3 = new ModelRenderer(this, 0, 10);
      disc3.addBox(-3F, -4F, -3F, 6, 2, 6);
      disc3.setRotationPoint(0F, 16F, 0F);
      disc3.setTextureSize(32, 32);
      disc3.mirror = true;
      setRotation(disc3, 0F, 0F, 0F);
      disc4 = new ModelRenderer(this, 0, 10);
      disc4.addBox(-3F, -7F, -3F, 6, 2, 6);
      disc4.setRotationPoint(0F, 16F, 0F);
      disc4.setTextureSize(32, 32);
      disc4.mirror = true;
      setRotation(disc4, 0F, 0F, 0F);
  }
  
  public void render(TileEntityBeamCannon tile, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
  {
	GlStateManager.pushMatrix();
	float yaw = 0;
	float pitch = 0;
	if (tile.target != null){
		yaw = Misc.yawDegreesBetweenPoints(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), tile.target.getX(), tile.target.getY(), tile.target.getZ());
		pitch = Misc.pitchDegreesBetweenPoints(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), tile.target.getX(), tile.target.getY(), tile.target.getZ());
		base.rotateAngleY = (float)Math.toRadians(yaw);
		base.rotateAngleX = (float)Math.toRadians(pitch);
		disc1.rotateAngleY = (float)Math.toRadians(yaw);
		disc1.rotateAngleX = (float)Math.toRadians(pitch);
		disc2.rotateAngleY = (float)Math.toRadians(yaw);
		disc2.rotateAngleX = (float)Math.toRadians(pitch);
		disc3.rotateAngleY = (float)Math.toRadians(yaw);
		disc3.rotateAngleX = (float)Math.toRadians(pitch);
		disc4.rotateAngleY = (float)Math.toRadians(yaw);
		disc4.rotateAngleX = (float)Math.toRadians(pitch);
	}
	base.render(scale);
    disc1.render(scale);
    disc2.render(scale);
    disc3.render(scale);
    disc4.render(scale);
    GlStateManager.popMatrix();
  }
  
  public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
  {
	GlStateManager.pushMatrix();
	float speed = (float)Math.min(0.25f, ((new Vec3d(entity.motionX,0,entity.motionZ)).lengthVector() * 4.0f));
    super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    base.render(scale);
    disc1.render(scale);
    disc2.render(scale);
    disc3.render(scale);
    disc4.render(scale);
    GlStateManager.popMatrix();
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
