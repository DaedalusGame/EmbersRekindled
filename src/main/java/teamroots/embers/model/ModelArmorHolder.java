package teamroots.embers.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelArmorHolder extends ModelBiped{
	ModelRenderer head;
    ModelRenderer chest;
    ModelRenderer armR;
    ModelRenderer armL;
    ModelRenderer legR;
    ModelRenderer legL;
    ModelRenderer legsTop;
    ModelRenderer bootR;
    ModelRenderer bootL;
    ModelRenderer cape;
    
	public ModelArmorHolder(String name){
		super(0.0f,1.0f,64,64);
		this.textureHeight = 64;
		this.textureWidth = 64;
		this.isChild = false;
		if (name == "ashenCloak"){
			
			ModelRenderer cape1;
		    ModelRenderer cape2;
		    ModelRenderer cape3;
		    ModelRenderer neck;
		    ModelRenderer front1;
		    ModelRenderer front2;
		    ModelRenderer shoulderR;
		    ModelRenderer shoulderL;
		    ModelRenderer kneeL;
		    ModelRenderer kneeR;
		    ModelRenderer front3;
		    ModelRenderer legR1;
		    ModelRenderer legL1;
		    ModelRenderer bootR1;
		    ModelRenderer bootR2;
		    ModelRenderer bootL1;
		    ModelRenderer bootL2;
		    ModelRenderer gloveR;
		    ModelRenderer gloveL;
		    ModelRenderer head1;
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
		    ModelRenderer head14;
		    ModelRenderer head15;
		    ModelRenderer capegem1;
		    ModelRenderer capegem2;
		    ModelRenderer capegem3;
		    ModelRenderer capegem4;
		    ModelRenderer capegem5;
		    ModelRenderer capegem6;
		    ModelRenderer capegem7;cape1 = new ModelRenderer(this, 0, 0);
		      cape1.addBox(-3F, 0F, -1F, 6, 22, 2);
		      cape1.setRotationPoint(0F, 0F, 3F);
		      cape1.setTextureSize(64, 64);
		      cape1.mirror = true;
		      setRotation(cape1, 0.2617994F, 0F, 0F);
		      cape2 = new ModelRenderer(this, 48, 40);
		      cape2.addBox(-3F, 0F, -1F, 6, 22, 2);
		      cape2.setRotationPoint(3F, 0F, 1.5F);
		      cape2.setTextureSize(64, 64);
		      cape2.mirror = true;
		      setRotation(cape2, 0.2617994F, 0.5235988F, 0F);
		      cape3 = new ModelRenderer(this, 48, 40);
		      cape3.addBox(-3F, 0F, -1F, 6, 22, 2);
		      cape3.setRotationPoint(-3F, 0F, 1.5F);
		      cape3.setTextureSize(64, 64);
		      cape3.mirror = false;
		      setRotation(cape3, 0.2617994F, -0.5235988F, 0F);
		      neck = new ModelRenderer(this, 0, 24);
		      neck.addBox(-4.5F, 0F, -4.5F, 9, 2, 9);
		      neck.setRotationPoint(0F, -1F, 0F);
		      neck.setTextureSize(64, 64);
		      neck.mirror = true;
		      setRotation(neck, 0F, 0F, 0F);
		      front1 = new ModelRenderer(this, 16, 0);
		      front1.addBox(-2F, 0F, -1F, 4, 12, 2);
		      front1.setRotationPoint(-3F, 0F, -1.5F);
		      front1.setTextureSize(64, 64);
		      front1.mirror = true;
		      setRotation(front1, 0.0872665F, -2.879793F, 0F);
		      front2 = new ModelRenderer(this, 16, 0);
		      front2.addBox(-2F, 0F, -1F, 4, 12, 2);
		      front2.setRotationPoint(3F, 0F, -1.5F);
		      front2.mirror = false;
		      front2.setTextureSize(64, 64);
		      setRotation(front2, 0.0872665F, 2.879793F, 0F);
		      shoulderR = new ModelRenderer(this, 0, 35);
		      shoulderR.addBox(-2.5F, -2F, -2.5F, 5, 5, 5);
		      shoulderR.setRotationPoint(-6F, 0F, 0F);
		      shoulderR.setTextureSize(64, 64);
		      shoulderR.mirror = true;
		      setRotation(shoulderR, 0F, 0F, 0F);
		      shoulderL = new ModelRenderer(this, 16, 54);
		      shoulderL.addBox(-2.5F, -2F, -2.5F, 5, 5, 5);
		      shoulderL.setRotationPoint(6F, 0F, 0F);
		      shoulderL.setTextureSize(64, 64);
		      shoulderL.mirror = true;
		      setRotation(shoulderL, 0F, 0F, 0F);
		      kneeL = new ModelRenderer(this, 0, 45);
		      kneeL.addBox(-2.5F, 1F, -2.5F, 5, 5, 5);
		      kneeL.setRotationPoint(2.5F, 12F, -0.5F);
		      kneeL.setTextureSize(64, 64);
		      kneeL.mirror = true;
		      setRotation(kneeL, 0.0872665F, -0.0872665F, 0F);
		      kneeR = new ModelRenderer(this, 0, 45);
		      kneeR.addBox(-2.5F, 1F, -2.5F, 5, 5, 5);
		      kneeR.setRotationPoint(-2.5F, 12F, -0.5F);
		      kneeR.setTextureSize(64, 64);
		      kneeR.mirror = true;
		      setRotation(kneeR, 0.0872665F, 0.0872665F, 0F);
		      front3 = new ModelRenderer(this, 32, 16);
		      front3.addBox(-2.5F, 0F, -1F, 5, 10, 2);
		      front3.setRotationPoint(0F, 0F, -1.5F);
		      front3.setTextureSize(64, 64);
		      front3.mirror = true;
		      setRotation(front3, 0.0872665F, 3.141593F, 0F);
		      legR1 = new ModelRenderer(this, 32, 0);
		      legR1.addBox(-2.5F, -5.186666F, -2F, 5, 4, 5);
		      legR1.setRotationPoint(-2.1F, 13F, -0.5F);
		      legR1.setTextureSize(64, 64);
		      legR1.mirror = true;
		      setRotation(legR1, 0F, 0.1745329F, 0F);
		      legL1 = new ModelRenderer(this, 32, 0);
		      legL1.addBox(-2.5F, -5.186666F, -2F, 5, 4, 5);
		      legL1.setRotationPoint(2.1F, 13F, -0.5F);
		      legL1.setTextureSize(64, 64);
		      legL1.mirror = true;
		      setRotation(legL1, 0F, -0.1745329F, 0F);
		      bootL1 = new ModelRenderer(this, 16, 14);
		      bootL1.addBox(-2F, 6F, -2F, 4, 6, 4);
		      bootL1.setRotationPoint(2F, 11F, 0F);
		      bootL1.setTextureSize(64, 64);
		      bootL1.mirror = true;
		      setRotation(bootL1, 0F, 0F, 0F);
		      bootR2 = new ModelRenderer(this, 48, 16);
		      bootR2.addBox(-2F, 10F, -3F, 4, 2, 1);
		      bootR2.setRotationPoint(-2F, 11F, 0F);
		      bootR2.setTextureSize(64, 64);
		      bootR2.mirror = true;
		      setRotation(bootR2, 0F, 0F, 0F);
		      bootR1 = new ModelRenderer(this, 16, 14);
		      bootR1.addBox(-2F, 6F, -2F, 4, 6, 4);
		      bootR1.setRotationPoint(-2F, 11F, 0F);
		      bootR1.setTextureSize(64, 64);
		      bootR1.mirror = false;
		      setRotation(bootR1, 0F, 0F, 0F);
		      bootL2 = new ModelRenderer(this, 48, 16);
		      bootL2.addBox(-2F, 10F, -3F, 4, 2, 1);
		      bootL2.setRotationPoint(2F, 11F, 0F);
		      bootL2.setTextureSize(64, 64);
		      bootL2.mirror = true;
		      setRotation(bootL2, 0F, 0F, 0F);
		      gloveR = new ModelRenderer(this, 48, 20);
		      gloveR.addBox(-2F, 7F, -2F, 4, 5, 4);
		      gloveR.setRotationPoint(-6F, 0F, 0F);
		      gloveR.setTextureSize(64, 64);
		      gloveR.mirror = true;
		      setRotation(gloveR, 0F, 0F, 0F);
		      gloveL = new ModelRenderer(this, 48, 20);
		      gloveL.addBox(-2F, 7F, -2F, 4, 5, 4);
		      gloveL.setRotationPoint(6F, 0F, 0F);
		      gloveL.setTextureSize(64, 64);
		      gloveL.mirror = true;
		      setRotation(gloveL, 0F, 0F, 0F);
		      head1 = new ModelRenderer(this, 20, 35);
		      head1.addBox(-2F, -2F, -1F, 4, 4, 2);
		      head1.setRotationPoint(0F, -4F, 4F);
		      head1.setTextureSize(64, 64);
		      head1.mirror = true;
		      setRotation(head1, 0F, 0F, 0F);
		      head2 = new ModelRenderer(this, 36, 32);
		      head2.addBox(-4F, -1F, -1F, 8, 2, 1);
		      head2.setRotationPoint(0F, -4F, 4F);
		      head2.setTextureSize(64, 64);
		      head2.mirror = true;
		      setRotation(head2, 0F, 0F, 0F);
		      head3 = new ModelRenderer(this, 32, 35);
		      head3.addBox(-1F, -1F, -4F, 1, 2, 7);
		      head3.setRotationPoint(-3F, -4F, 0F);
		      head3.setTextureSize(64, 64);
		      head3.mirror = true;
		      setRotation(head3, 0F, 0F, 0F);
		      head4 = new ModelRenderer(this, 32, 35);
		      head4.addBox(-1F, -1F, -4F, 1, 2, 7);
		      head4.setRotationPoint(4F, -4F, 0F);
		      head4.setTextureSize(64, 64);
		      head4.mirror = true;
		      setRotation(head4, 0F, 0F, 0F);
		      head5 = new ModelRenderer(this, 32, 37);
		      head5.addBox(-1F, -4F, -1F, 2, 5, 1);
		      head5.setRotationPoint(0F, -4F, 4F);
		      head5.setTextureSize(64, 64);
		      head5.mirror = true;
		      setRotation(head5, 0F, 0F, 0F);
		      head6 = new ModelRenderer(this, 32, 48);
		      head6.addBox(-1F, -4F, -4F, 2, 1, 7);
		      head6.setRotationPoint(0F, -4F, 0F);
		      head6.setTextureSize(64, 64);
		      head6.mirror = true;
		      setRotation(head6, 0F, 0F, 0F);
		      head7 = new ModelRenderer(this, 22, 50);
		      head7.addBox(-0.5F, 0F, -1F, 1, 2, 1);
		      head7.setRotationPoint(1F, -5F, -4F);
		      head7.setTextureSize(64, 64);
		      head7.mirror = true;
		      setRotation(head7, 0F, 0F, 0F);
		      head8 = new ModelRenderer(this, 22, 50);
		      head8.addBox(-0.5F, 0F, -1F, 1, 2, 1);
		      head8.setRotationPoint(4F, -5F, -4F);
		      head8.setTextureSize(64, 64);
		      head8.mirror = true;
		      setRotation(head8, 0F, 0F, 0F);
		      head9 = new ModelRenderer(this, 22, 48);
		      head9.addBox(-2F, 0F, -1F, 4, 1, 1);
		      head9.setRotationPoint(2.5F, -6F, -4F);
		      head9.setTextureSize(64, 64);
		      head9.mirror = true;
		      setRotation(head9, 0F, 0F, 0F);
		      head10 = new ModelRenderer(this, 22, 48);
		      head10.addBox(-2F, 0F, -1F, 4, 1, 1);
		      head10.setRotationPoint(2.5F, -3F, -4F);
		      head10.setTextureSize(64, 64);
		      head10.mirror = true;
		      setRotation(head10, 0F, 0F, 0F);
		      head11 = new ModelRenderer(this, 22, 50);
		      head11.addBox(-0.5F, 0F, -1F, 1, 2, 1);
		      head11.setRotationPoint(-4F, -5F, -4F);
		      head11.setTextureSize(64, 64);
		      head11.mirror = true;
		      setRotation(head11, 0F, 0F, 0F);
		      head12 = new ModelRenderer(this, 22, 48);
		      head12.addBox(-2F, 0F, -1F, 4, 1, 1);
		      head12.setRotationPoint(-2.5F, -6F, -4F);
		      head12.setTextureSize(64, 64);
		      head12.mirror = true;
		      setRotation(head12, 0F, 0F, 0F);
		      head13 = new ModelRenderer(this, 22, 50);
		      head13.addBox(-0.5F, 0F, -1F, 1, 2, 1);
		      head13.setRotationPoint(-1F, -5F, -4F);
		      head13.setTextureSize(64, 64);
		      head13.mirror = true;
		      setRotation(head13, 0F, 0F, 0F);
		      head14 = new ModelRenderer(this, 22, 48);
		      head14.addBox(-2F, 0F, -1F, 4, 1, 1);
		      head14.setRotationPoint(-2.5F, -3F, -4F);
		      head14.setTextureSize(64, 64);
		      head14.mirror = true;
		      setRotation(head14, 0F, 0F, 0F);
		      head15 = new ModelRenderer(this, 32, 37);
		      head15.addBox(-1F, -4F, -1F, 2, 4, 1);
		      head15.setRotationPoint(0F, -3F, -3F);
		      head15.setTextureSize(64, 64);
		      head15.mirror = true;
		      setRotation(head15, 0F, 0F, 0F);
		      capegem1 = new ModelRenderer(this, 0, 60);
		      capegem1.addBox(-1F, 16F, 0F, 2, 2, 2);
		      capegem1.setRotationPoint(3F, 0F, 1.5F);
		      capegem1.setTextureSize(64, 64);
		      capegem1.mirror = true;
		      setRotation(capegem1, 0.2617994F, 0.5235988F, 0F);
		      capegem2 = new ModelRenderer(this, 0, 60);
		      capegem2.addBox(-1F, 10F, 0F, 2, 2, 2);
		      capegem2.setRotationPoint(3F, 0F, 1.5F);
		      capegem2.setTextureSize(64, 64);
		      capegem2.mirror = true;
		      setRotation(capegem2, 0.2617994F, 0.5235988F, 0F);
		      capegem3 = new ModelRenderer(this, 0, 60);
		      capegem3.addBox(-1F, 4F, 0F, 2, 2, 2);
		      capegem3.setRotationPoint(3F, 0F, 1.5F);
		      capegem3.setTextureSize(64, 64);
		      capegem3.mirror = true;
		      setRotation(capegem3, 0.2617994F, 0.5235988F, 0F);
		      capegem4 = new ModelRenderer(this, 0, 60);
		      capegem4.addBox(-1F, 2F, 0F, 2, 2, 2);
		      capegem4.setRotationPoint(0F, 0F, 3F);
		      capegem4.setTextureSize(64, 64);
		      capegem4.mirror = true;
		      setRotation(capegem4, 0.2617994F, 0F, 0F);
		      capegem5 = new ModelRenderer(this, 0, 60);
		      capegem5.addBox(-1F, 4F, 0F, 2, 2, 2);
		      capegem5.setRotationPoint(-3F, 0F, 1.5F);
		      capegem5.setTextureSize(64, 64);
		      capegem5.mirror = true;
		      setRotation(capegem5, 0.2617994F, -0.5235988F, 0F);
		      capegem6 = new ModelRenderer(this, 0, 60);
		      capegem6.addBox(-1F, 10F, 0F, 2, 2, 2);
		      capegem6.setRotationPoint(-3F, 0F, 1.5F);
		      capegem6.setTextureSize(64, 64);
		      capegem6.mirror = true;
		      setRotation(capegem6, 0.2617994F, -0.5235988F, 0F);
		      capegem7 = new ModelRenderer(this, 0, 60);
		      capegem7.addBox(-1F, 16F, 0F, 2, 2, 2);
		      capegem7.setRotationPoint(-3F, 0F, 1.5F);
		      capegem7.setTextureSize(64, 64);
		      capegem7.mirror = true;
		      setRotation(capegem7, 0.2617994F, -0.5235988F, 0F);
		      
		      head = new ModelRenderer(this);
		      this.head.addChild(head1);
		      this.head.addChild(head2);
		      this.head.addChild(head3);
		      this.head.addChild(head4);
		      this.head.addChild(head5);
		      this.head.addChild(head6);
		      this.head.addChild(head7);
		      this.head.addChild(head8);
		      this.head.addChild(head9);
		      this.head.addChild(head10);
		      this.head.addChild(head11);
		      this.head.addChild(head12);
		      this.head.addChild(head13);
		      this.head.addChild(head14);
		      this.head.addChild(head15);

		      chest = new ModelRenderer(this);
		      this.chest.addChild(front1);
		      this.chest.addChild(front2);
		      this.chest.addChild(front3);
		      this.chest.addChild(neck);
		      
		      cape = new ModelRenderer(this);
		      this.cape.addChild(cape1);
		      this.cape.addChild(cape2);
		      this.cape.addChild(cape3);
		      this.cape.addChild(capegem1);
		      this.cape.addChild(capegem2);
		      this.cape.addChild(capegem3);
		      this.cape.addChild(capegem4);
		      this.cape.addChild(capegem5);
		      this.cape.addChild(capegem6);
		      this.cape.addChild(capegem7);

		      armL = new ModelRenderer(this);
		      this.armL.addChild(shoulderL);
		      this.armL.addChild(gloveL);

		      armR = new ModelRenderer(this);
		      this.armR.addChild(shoulderR);
		      this.armR.addChild(gloveR);
		      
		      legsTop = new ModelRenderer(this);
		      this.legsTop.addChild(legL1);
		      this.legsTop.addChild(legR1);

		      legL = new ModelRenderer(this);
		      this.legL.addChild(kneeL);
		      //this.legL.addChild(legL1);

		      legR = new ModelRenderer(this);
		      this.legR.addChild(kneeR);
		      //this.legR.addChild(legR1);

		      bootL = new ModelRenderer(this);
		      this.bootL.addChild(bootL1);
		      this.bootL.addChild(bootL2);

		      bootR = new ModelRenderer(this);
		      this.bootR.addChild(bootR1);
		      this.bootR.addChild(bootR2);
		      
		    for (int i = 0; i < head.childModels.size(); i ++){
		    	head.childModels.get(i).rotationPointY += 0.5;
		    }
		      
		    for (int i = 0; i < armR.childModels.size(); i ++){
				armR.childModels.get(i).rotationPointX += 6;
		    }
		    for (int i = 0; i < armL.childModels.size(); i ++){
				armL.childModels.get(i).rotationPointX -= 6;
		    }
		      
		    for (int i = 0; i < legR.childModels.size(); i ++){
		    	legR.childModels.get(i).rotationPointY -= 12;
		    }
		    for (int i = 0; i < legL.childModels.size(); i ++){
		    	legL.childModels.get(i).rotationPointY -= 12;
		    }
		    for (int i = 0; i < bootR.childModels.size(); i ++){
		    	bootR.childModels.get(i).rotationPointY -= 12;
		    }
		    for (int i = 0; i < bootL.childModels.size(); i ++){
		    	bootL.childModels.get(i).rotationPointY -= 12;
			}
		}
	}
	
	public static void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
