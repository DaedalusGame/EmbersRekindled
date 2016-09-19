package teamroots.embers.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import teamroots.embers.Embers;
import teamroots.embers.network.message.MessageParticle;
import teamroots.embers.proxy.ClientProxy;

public class ParticleUtil {
	public static void spawnParticlesFromPacket(MessageParticle message, World world){
		/*Particle particle = null;
		
		if (particle != null){
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}*/
	}
	
	public static void spawnParticleGlow(World world, float x, float y, float z, float vx, float vy, float vz, float r, float g, float b){
		if (Embers.proxy instanceof ClientProxy){
			((ClientProxy)Embers.proxy).particleRenderer.addParticle(new ParticleGlow(world,x,y,z,vx,vy,vz,r,g,b));
		}
	}
}
