package teamroots.embers.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import teamroots.embers.proxy.ClientProxy;

public class ParticleAsh extends Particle implements IEmberParticle {
    double width, height, depth;

    public ParticleAsh(World worldIn, double x1, double y1, double z1, double x2, double y2, double z2, float lifetime) {
        super(worldIn, x1,y1,z1,0,0,0);
        width = x2 - x1;
        height = y2 - y1;
        depth = z2 - z1;
        this.particleMaxAge = (int)(lifetime *0.5f);
    }

    @Override
    public void onUpdate() {
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        int particles = (int) Math.ceil(Math.sqrt(width * width + height * height + depth * depth));
        for(int i = 0; i < particles * 4; i++) {
            double x = this.posX + width * rand.nextDouble();
            double y = this.posY + height * rand.nextDouble();
            double z = this.posZ + depth * rand.nextDouble();
            double speed = rand.nextDouble() * 0.1 + 0.1;
            float sizeSmall = rand.nextFloat() * 1f + 1f;
            float sizeBig = rand.nextFloat() * 2f + 2f;
            ClientProxy.particleRenderer.addParticle(new ParticleSmoke(world,x,y,z,0,-speed,0,0f,0f,0f,0.7f,sizeSmall,20));
            ClientProxy.particleRenderer.addParticle(new ParticleSmoke(world,x,y,z,(rand.nextDouble()-0.5)*0.1,(rand.nextDouble()-0.5)*0.1,(rand.nextDouble()-0.5)*0.1,0f,0f,0f,0.2f,sizeBig,40));
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        //NOOP
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
