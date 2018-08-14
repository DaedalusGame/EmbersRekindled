package teamroots.embers.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageParticle;
import teamroots.embers.proxy.ClientProxy;

import java.util.Random;

public class ParticleUtil {
	public static Random random = new Random();
	public static int counter = 0;
	
	public static void spawnParticlesFromPacket(MessageParticle message, World world){
		/*Particle particle = null;
		
		if (particle != null){
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}*/
	}
	
	public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleGlow(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleGlowThroughBlocks(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleGlowThroughBlocks(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleGlow(world,x,y,z,vx,vy,vz,r,g,b,1.0f, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleLineGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleLineGlow(world,x,y,z,vx,vy,vz,r,g,b, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleTyrfing(World world, float x, float y, float z, float vx, float vy, float vz, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleTyrfing(world,x,y,z,vx,vy,vz, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleStar(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleStar(world,x,y,z,vx,vy,vz,r,g,b, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleSpark(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleSpark(world,x,y,z,vx,vy,vz,r,g,b, scale, lifetime));
			}
		}
	}
	
	public static void spawnParticleSmoke(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float a, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter += random.nextInt(3);
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleSmoke(world,x,y,z,vx,vy,vz,r,g,b,a, scale, lifetime));
			}
		}
	}
}
