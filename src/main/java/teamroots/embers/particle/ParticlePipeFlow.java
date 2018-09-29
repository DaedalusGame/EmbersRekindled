package teamroots.embers.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import teamroots.embers.util.Misc;

public class ParticlePipeFlow extends Particle implements IEmberParticle{
	public float colorR = 0;
	public float colorG = 0;
	public float colorB = 0;
	public float initAlpha = 0;
	public ResourceLocation texture = new ResourceLocation("embers:entity/particle_smoke");
	public ParticlePipeFlow(World worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime) {
		super(worldIn, x,y,z,0,0,0);
		this.colorR = r;
		this.colorG = g;
		this.colorB = b;
		if (this.colorR > 1.0){
			this.colorR = this.colorR/255.0f;
		}
		if (this.colorG > 1.0){
			this.colorG = this.colorG/255.0f;
		}
		if (this.colorB > 1.0){
			this.colorB = this.colorB/255.0f;
		}
		this.setRBGColorF(colorR, colorG, colorB);
		this.particleMaxAge = (int)((float)lifetime*0.5f);
		this.particleScale = scale;
		this.motionX = vx*2.0f;
		this.motionY = vy*2.0f;
		this.motionZ = vz*2.0f;
		this.canCollide = false;
		this.initAlpha = a;
		this.particleAngle = rand.nextFloat()*2.0f*(float)Math.PI;
	    TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
	    this.setParticleTexture(sprite);
	}
	
	@Override
	public int getBrightnessForRender(float pTicks){
		return 255;
	}
	
	@Override
	public boolean shouldDisableDepth(){
		return true;
	}
	
	@Override
	public int getFXLayer(){
		return 1;
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		if (Misc.random.nextInt(6) == 0){
			this.particleAge ++;
		}
		float lifeCoeff = (float)this.particleAge/(float)this.particleMaxAge;
		this.particleAlpha = initAlpha*(float)Math.sin((1.0-lifeCoeff)*Math.PI);
		this.prevParticleAngle = particleAngle;
		if(particleAge >= particleMaxAge)
			setRBGColorF(0,0,0);
		particleAngle += 1.0f;
	}

	@Override
	public boolean alive() {
		return this.particleAge < this.particleMaxAge;
	}

	@Override
	public boolean isAdditive() {
		return true;
	}

	@Override
	public boolean renderThroughBlocks() {
		return false;
	}
}
