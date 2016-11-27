package teamroots.embers.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageParticle;
import teamroots.embers.proxy.ClientProxy;

public class ParticleUtil {
	public static int counter = 0;
	
	public static void spawnParticlesFromPacket(MessageParticle message, World world){
		/*Particle particle = null;
		
		if (particle != null){
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}*/
	}
	
	public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b, float scale, int lifetime){
		if (Embers.proxy instanceof ClientProxy){
			counter ++;
			if (counter % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2*Minecraft.getMinecraft().gameSettings.particleSetting) == 0){
				ClientProxy.particleRenderer.addParticle(new ParticleGlow(world,x,y,z,vx,vy,vz,r,g,b, scale, lifetime));
			}
		}
	}
}
