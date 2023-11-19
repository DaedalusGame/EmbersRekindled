package teamroots.embers.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import teamroots.embers.EventManager;
import teamroots.embers.config.ConfigMain;
import teamroots.embers.util.Misc;

public class ParticleTyrfing extends Particle implements IEmberParticle{
	public float colorR = 0;
	public float colorG = 0;
	public float colorB = 0;
	public float initScale = 0;
	public int phase = Misc.random.nextInt(360);
	public ResourceLocation texture = new ResourceLocation("embers:entity/particle_mote");
	public ParticleTyrfing(World worldIn, double x, double y, double z, double vx, double vy, double vz, float scale, int lifetime) {
		super(worldIn, x,y,z,0,0,0);
		float timerSine = ((float)Math.sin(8.0*Math.toRadians(EventManager.ticks % 360))+1.0f)/2.0f;
		this.particleRed = (0.25f*timerSine);
		this.particleGreen = 0.0625f;
		this.particleBlue = (0.125f+0.125f*timerSine);
		this.setRBGColorF(particleRed, particleGreen, particleBlue);
		this.particleMaxAge = lifetime;
		this.particleScale = scale;
		this.initScale = scale;
		this.motionX = vx;
		this.motionY = vy;
		this.motionZ = vz;
		phase = Misc.random.nextInt(360);
		this.particleAngle = 2.0f*(float)Math.PI;
	    TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
	    this.setParticleTexture(sprite);

		this.canCollide = ConfigMain.CLIENT_CATEGORY.enableParticleCollisions;
	}
	/*
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rotX, float rotZ, float rotYZ, float rotXY, float rotXZ){
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		super.renderParticle(buffer, entity, partialTicks, rotX, rotZ, rotYZ, rotXY, rotXZ);
	}*/

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
		this.particleScale = initScale-initScale*lifeCoeff;
		this.particleAlpha = 1.0f-lifeCoeff;
		this.prevParticleAngle = particleAngle;
		float timerSine = ((float)Math.sin(8.0*Math.toRadians(EventManager.ticks % 360 + phase))+1.0f)/2.0f;
		this.particleRed = (0.25f*timerSine);
		this.particleGreen = 0.0625f;
		this.particleBlue = (0.125f+0.125f*timerSine);
		particleAngle += 1.0f;
	}

	@Override
	public boolean alive() {
		return this.particleAge < this.particleMaxAge;
	}

	@Override
	public boolean isAdditive() {
		return false;
	}

	@Override
	public boolean renderThroughBlocks() {
		return false;
	}
}
