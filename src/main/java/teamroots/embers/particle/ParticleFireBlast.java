package teamroots.embers.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import teamroots.embers.ConfigManager;

import java.awt.*;

public class ParticleFireBlast extends Particle implements IEmberParticle {
    Color color;

    protected ParticleFireBlast(World worldIn, double posXIn, double posYIn, double posZIn, Color color, float scale, int lifetime) {
        super(worldIn, posXIn, posYIn, posZIn);
        this.color = color;
        this.particleScale = scale;
        this.particleMaxAge = lifetime;

        this.canCollide = ConfigManager.enableParticleCollisions;
    }

    @Override
    public void onUpdate(){
        super.onUpdate();
        float lifeCoeff = (float)particleAge / particleMaxAge;
        float flameSize = particleScale * 1.5f;
        for (int i = 0; i < 60; i++) {
            double yaw = rand.nextDouble() * Math.PI * 2;
            double pitch = rand.nextDouble() * Math.PI * 2;
            float dist = 0.2f;
            float dx = (float) (Math.sin(yaw) * Math.cos(pitch));
            float dy = (float) (Math.sin(pitch));
            float dz = (float) (Math.cos(yaw) * Math.cos(pitch));

            float endDist = particleScale * 2.0f * (float)Math.sqrt(1-lifeCoeff);
            int lifetime = 5 + rand.nextInt(45);
            float velocityFactor = endDist / lifetime;
            float scale = flameSize * (float)Math.sqrt(lifeCoeff);

            velocityFactor *= -1;
            ParticleUtil.spawnParticleGlow(world, (float)posX + dx * dist, (float)posY - 0.1f + dy * dist, (float)posZ + dz * dist, dx * velocityFactor, dy * velocityFactor + particleScale/lifetime, dz * velocityFactor, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 255f, scale, lifetime);
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
