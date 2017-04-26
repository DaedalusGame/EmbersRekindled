package teamroots.embers.lighting;

import java.util.ArrayList;

import org.lwjgl.opengl.GL20;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import teamroots.embers.util.ShaderUtil;
import teamroots.embers.lighting.Light;

public class LightManager {
	public static ArrayList<Light> lights = new ArrayList<Light>();
	
	public static int maxLights = 100;
	
	public static void addLight(Light l){
		for (int i = 0; i < 1; i ++){
			if (lights.size() < maxLights && l != null){
				lights.add(l);
			}
		}
	}
	
	public static void uploadLights(){
		int max = GL20.glGetUniformLocation(ShaderUtil.currentProgram, "lightCount");
		GL20.glUniform1i(max, lights.size());
		for (int i = 0; i < lights.size(); i ++){
			if (i < lights.size()){
				Light l = lights.get(i);
				int pos = GL20.glGetUniformLocation(ShaderUtil.currentProgram, "lights["+i+"].position");
				GL20.glUniform3f(pos, l.x, l.y, l.z);
				int color = GL20.glGetUniformLocation(ShaderUtil.currentProgram, "lights["+i+"].color");
				GL20.glUniform4f(color, l.r, l.g, l.b, l.a);
				int radius = GL20.glGetUniformLocation(ShaderUtil.currentProgram, "lights["+i+"].radius");
				GL20.glUniform1f(radius, l.radius);
			}
			else {
				int pos = GL20.glGetUniformLocation(ShaderUtil.currentProgram, "lights["+i+"].position");
				GL20.glUniform3f(pos, 0, 0, 0);
				int color = GL20.glGetUniformLocation(ShaderUtil.currentProgram, "lights["+i+"].color");
				GL20.glUniform4f(color, 0, 0, 0, 0);
				int radius = GL20.glGetUniformLocation(ShaderUtil.currentProgram, "lights["+i+"].radius");
				GL20.glUniform1f(radius, 0);
			}
		}
	}
	
	public static void update(World world){
		for (Entity e : world.getLoadedEntityList()){
			if (e instanceof ILightProvider){
				addLight(((ILightProvider)e).provideLight());
			}
		}
		for (TileEntity t : world.loadedTileEntityList){
			if (t instanceof ILightProvider){
				addLight(((ILightProvider)t).provideLight());
			}
		}
		uploadLights();
		lights.clear();
	}
}
