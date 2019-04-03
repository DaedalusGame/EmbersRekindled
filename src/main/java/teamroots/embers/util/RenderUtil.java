package teamroots.embers.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import teamroots.embers.EventManager;

import java.awt.*;
import java.util.regex.Pattern;

public class RenderUtil {
	public static int lightx = 0xF000F0;
	public static int lighty = 0xF000F0;
    public static final float root2over2 = (float)Math.sqrt(2.0f)/2.0f;
	public static final Pattern COLOR_CODE_MATCHER = Pattern.compile("§[0-9a-f]");

	public static void renderWavyEmberLine(BufferBuilder b, double x1, double y1, double x2, double y2, double thickness){
		renderWavyEmberLine(b,x1,y1,x2,y2,thickness,1.0,new Color(255,64,16));
	}
    
    public static void renderWavyEmberLine(BufferBuilder b, double x1, double y1, double x2, double y2, double thickness, double density, Color color){
		double dx = x2-x1;
		double dy = y2-y1;
    	double angleRads = Math.atan2(y2-y1, x2-x1);
		double dist = Math.sqrt(dx*dx+dy*dy);
    	double orthoX = Math.cos(angleRads+(Math.PI/2.0));
    	double orthoY = Math.sin(angleRads+(Math.PI/2.0));
    	double rayX = Math.cos(angleRads);
    	double rayY = Math.sin(angleRads);
		for (int i = 0; i <= 10; i ++){
    		float coeff = (float)i / 10f;
    		double thickCoeff = Math.min(1.0, 1.4f*MathHelper.sqrt(2.0f*(0.5f-Math.abs((coeff-0.5f)))));
			//double thickCoeff = 1.0+0.25*Math.sin(coeff*Math.PI*2.0f);
    		double tx = x1*(1.0f-coeff) + x2*coeff;
    		double ty = NoiseGenUtil.interpolate((float)y1, (float)y2, coeff);
			float tick = Minecraft.getMinecraft().getRenderPartialTicks()+EventManager.ticks;
    		int offX = (int)(6f*tick);
			int offZ = (int)(6f*tick);
			float sine = (float)Math.sin(coeff*Math.PI*2.0f + 0.25f*(tick)) + 0.25f*(float)Math.sin(coeff*Math.PI*3.47f + 0.25f*(tick));
    		float sineOff = (4.0f + (float)thickness)/3.0f;
			float densityCoeff = (float)(0.5+0.5*Math.sin(coeff*Math.PI*2.0*dist*0.01 + tick * 0.2));
    		float minusDensity = (float)density * densityCoeff * EmberGenUtil.getEmberDensity(1, offX+(int)(tx-thickness*orthoX*thickCoeff), offZ+(int)(ty-thickness*orthoY*thickCoeff));
    		float plusDensity = (float)density * densityCoeff * EmberGenUtil.getEmberDensity(1, offX+(int)(tx-thickness*orthoX*thickCoeff), offZ+(int)(ty-thickness*orthoY*thickCoeff));
    		b.pos(tx-thickness*(0.5f+minusDensity)*orthoX*thickCoeff-thickCoeff*orthoX*sine*sineOff, ty-thickness*(0.5f+minusDensity)*orthoY*thickCoeff-thickCoeff*orthoY*sine*sineOff, 0).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, (float)Math.pow(0.5f*(float)Math.max(0,thickCoeff-0.4f)*minusDensity,1)).endVertex();
    		b.pos(tx+thickness*(0.5f+plusDensity)*orthoX*thickCoeff-thickCoeff*orthoX*sine*sineOff, ty+thickness*(0.5f+plusDensity)*orthoY*thickCoeff-thickCoeff*orthoY*sine*sineOff, 0).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, (float)Math.pow(0.5f*(float)Math.max(0,thickCoeff-0.4f)*plusDensity,1)).endVertex();
    	}
    }

	public static void renderHighlightCircle(BufferBuilder b, double x1, double y1, double thickness){
		renderHighlightCircle(b,x1,y1,thickness,0,new Color(255,64,16));
	}

    public static void renderHighlightCircle(BufferBuilder b, double x1, double y1, double thickness, double z, Color color){
    	for (int i = 0; i < 40; i ++){
    		float coeff = (float)i / 40f;
    		int i2 = i+1;
    		if (i2 == 40){
    			i2 = 0;
    		}
    		float coeff2 = (float)(i2) / 40f;
    		double angle = Math.PI * 2.0 * coeff;
    		double angle2 = Math.PI * 2.0 * coeff2;
			float tick = Minecraft.getMinecraft().getRenderPartialTicks()+EventManager.ticks;
			double calcAngle2 = angle2;
			float density1 = EmberGenUtil.getEmberDensity(4, (int)x1+(int)(480.0*angle), (int)y1+4*(int)tick + (int)(4.0f*thickness));
			float density2 = EmberGenUtil.getEmberDensity(4, (int)x1+(int)(480.0*calcAngle2), (int)y1+4*(int)tick + (int)(4.0f*thickness));
			double tx = x1 + Math.sin(angle+0.03125f*tick)*(thickness - (thickness * 0.5f * density1));
    		double ty = y1 + Math.cos(angle+0.03125f*tick)*(thickness - (thickness * 0.5f * density1));
    		double tx2 = x1 + Math.sin(angle2+0.03125f*tick)*(thickness - (thickness * 0.5f * density2));
    		double ty2 = y1 + Math.cos(angle2+0.03125f*tick)*(thickness - (thickness * 0.5f * density2));
    		b.pos(x1, y1, z).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f).endVertex();
    		b.pos(tx, ty, z).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.0f).endVertex();
    		b.pos(tx2, ty2, z).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.0f).endVertex();
        }
    }
    
    public static void renderRadiantCircle(BufferBuilder b, double x, double y, double radius){
		for (int i = 0; i < 20; i ++){
    		double angle = Math.PI * 2.0 * ((double)i)/20.0;
    		double angle2 = Math.PI * 2.0 * ((double)(i+1))/20.0;
			float tick = Minecraft.getMinecraft().getRenderPartialTicks()+EventManager.ticks;
			double radiusBonus = (double)EmberGenUtil.getEmberDensity(4, (int)(20.0*angle), (int)tick);
    		double tx = x + Math.cos(angle)*40.0;//(radius+radius*0.1*radiusBonus);
			double ty = y + Math.sin(angle)*40.0;//(radius+radius*0.1*radiusBonus);
    		double tx2 = x + Math.cos(angle2)*40.0;//(radius+radius*0.1*radiusBonus);
			double ty2 = y + Math.sin(angle2)*40.0;//(radius+radius*0.1*radiusBonus);
	    	b.pos(x, y, 0.0).color(1.0f, 0.25f, 0.0625f, 1.0f).endVertex();
	    	b.pos(tx, ty, 0.0).color(1.0f, 0.25f, 0.0625f, 1.0f).endVertex();
			b.pos(tx2, ty2, 0.0).color(1.0f, 0.25f, 0.0625f, 1.0f).endVertex();
    	}
    }
    
	public static void renderBeam(BufferBuilder buf, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a, float radius, double angle){
		double yaw = Misc.yawDegreesBetweenPoints(x1, y1, z1, x2, y2, z2);
        double pitch = Misc.pitchDegreesBetweenPoints(x1, y1, z1, x2, y2, z2);
        
        float yawCos = MathHelper.cos((float)Math.toRadians(yaw));
        float yawSin = MathHelper.sin((float)Math.toRadians(yaw));
        float pitchCos = MathHelper.cos((float)Math.toRadians(pitch));
        float pitchSin = MathHelper.sin((float)Math.toRadians(pitch));
        float angCos = MathHelper.cos((float)Math.toRadians(angle));
        float angSin = MathHelper.sin((float)Math.toRadians(angle));
        
        float dxh = radius*yawCos*pitchCos;
        float dyh = 0;
        float dzh = radius*-yawSin*pitchCos;
        
        float dxv = radius*0.5f*-yawSin*pitchSin;
        float dyv = radius*0.5f*pitchCos;
        float dzv = radius*0.5f*-yawCos*pitchSin;
        
        float dx = dxh * angCos + dxv * angSin;
        float dy = dyh * angCos + dyv * angSin;
        float dz = dzh * angCos + dzv * angSin;
        float dx2 = dxh * -angSin + dxv * angCos;
        float dy2 = dyh * -angSin + dyv * angCos;
        float dz2 = dzh * -angSin + dzv * angCos;
        
        double distX = x2-x1;
        double distY = y2-y1;
        double distZ = z2-z1;
        
        buf.pos(x1-dx, y1-dy, z1-dz).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
        buf.pos(x1+dx, y1+dy, z1+dz).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
        buf.pos(x1+distX*0.1f+dx, y1+distY*0.1f+dy, z1+distZ*0.1f+dz).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.1f-dx, y1+distY*0.1f-dy, z1+distZ*0.1f-dz).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        
        buf.pos(x1-dx2, y1-dy2, z1-dz2).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
        buf.pos(x1+dx2, y1+dy2, z1+dz2).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
        buf.pos(x1+distX*0.1f+dx2, y1+distY*0.1f+dy2, z1+distZ*0.1f+dz2).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.1f-dx2, y1+distY*0.1f-dy2, z1+distZ*0.1f-dz2).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        
        buf.pos(x1+distX*0.1f-dx, y1+distY*0.1f-dy, z1+distZ*0.1f-dz).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.1f+dx, y1+distY*0.1f+dy, z1+distZ*0.1f+dz).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.9f+dx, y1+distY*0.9f+dy, z1+distZ*0.9f+dz).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.9f-dx, y1+distY*0.9f-dy, z1+distZ*0.9f-dz).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        
        buf.pos(x1+distX*0.1f-dx2, y1+distY*0.1f-dy2, z1+distZ*0.1f-dz2).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.1f+dx2, y1+distY*0.1f+dy2, z1+distZ*0.1f+dz2).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.9f+dx2, y1+distY*0.9f+dy2, z1+distZ*0.9f+dz2).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.9f-dx2, y1+distY*0.9f-dy2, z1+distZ*0.9f-dz2).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        
        buf.pos(x1+distX*0.9f-dx, y1+distY*0.9f-dy, z1+distZ*0.9f-dz).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.9f+dx, y1+distY*0.9f+dy, z1+distZ*0.9f+dz).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX+dx, y1+distY+dy, z1+distZ+dz).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
        buf.pos(x1+distX-dx, y1+distY-dy, z1+distZ-dz).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
        
        buf.pos(x1+distX*0.9f-dx2, y1+distY*0.9f-dy2, z1+distZ*0.9f-dz2).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX*0.9f+dx2, y1+distY*0.9f+dy2, z1+distZ*0.9f+dz2).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
        buf.pos(x1+distX+dx2, y1+distY+dy2, z1+distZ+dz2).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
        buf.pos(x1+distX-dx2, y1+distY-dy2, z1+distZ-dz2).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, 0).endVertex();
	
	}
	
	public static void renderAlchemyCircle(BufferBuilder buf, double x, double y, double z, float r, float g, float b, float a, double radius, double angle){
		double sign = 1;
		if (Minecraft.getMinecraft().player.posY+Minecraft.getMinecraft().player.getEyeHeight() < y){
			sign = -1;
		}
		int lightx = 0xF000F0;
        int lighty = 0xF000F0;
        for (double i = 0; i < 360; i += 10){
			double tx = Math.sin(Math.toRadians(i+angle));
			double tz = Math.cos(Math.toRadians(i+angle));
			double tx2 = Math.sin(Math.toRadians(i+angle+10));
			double tz2 = Math.cos(Math.toRadians(i+angle+10));
			buf.pos(x+radius*tx, y, z+radius*tz).tex(0, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
			buf.pos(x+(radius+0.25)*tx, y, z+(radius+0.25)*tz).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
			buf.pos(x+(radius+0.25)*tx2, y, z+(radius+0.25)*tz2).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
			buf.pos(x+radius*tx2, y, z+radius*tz2).tex(1, 0).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		}
		double ax = (radius+0.24)*Math.sin(Math.toRadians(0+angle));
		double az = (radius+0.24)*Math.cos(Math.toRadians(0+angle));
		double adx = (0.1875)*Math.cos(Math.toRadians(0+angle));
		double adz = (0.1875)*-Math.sin(Math.toRadians(0+angle));
		double bx = (radius+0.24)*Math.sin(Math.toRadians(120+angle));
		double bz = (radius+0.24)*Math.cos(Math.toRadians(120+angle));
		double bdx = (0.1875)*Math.cos(Math.toRadians(120+angle));
		double bdz = (0.1875)*-Math.sin(Math.toRadians(120+angle));
		double cx = (radius+0.24)*Math.sin(Math.toRadians(240+angle));
		double cz = (radius+0.24)*Math.cos(Math.toRadians(240+angle));
		double cdx = (0.1875)*Math.cos(Math.toRadians(240+angle));
		double cdz = (0.1875)*-Math.sin(Math.toRadians(240+angle));
		buf.pos(x+(ax-adx), y+0.00005*sign, z+(az-adz)).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(ax+adx), y+0.00005*sign, z+(az+adz)).tex(0, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(bx-bdx), y+0.00005*sign, z+(bz-bdz)).tex(1, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(bx+bdx), y+0.00005*sign, z+(bz+bdz)).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();

		buf.pos(x+(bx-bdx), y+0.0001*sign, z+(bz-bdz)).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(bx+bdx), y+0.0001*sign, z+(bz+bdz)).tex(0, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx-cdx), y+0.0001*sign, z+(cz-cdz)).tex(1, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx+cdx), y+0.0001*sign, z+(cz+cdz)).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();

		buf.pos(x+(ax-adx), y+0.00015*sign, z+(az-adz)).tex(0, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(ax+adx), y+0.00015*sign, z+(az+adz)).tex(0, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx-cdx), y+0.00015*sign, z+(cz-cdz)).tex(1, 1).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
		buf.pos(x+(cx+cdx), y+0.00015*sign, z+(cz+cdz)).tex(1, 0.5).lightmap(lightx, lighty).color(r, g, b, a).endVertex();
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param textures
	 * 
	 * Order the textures and inversions like so: up (pos Y), down (neg Y), north (neg Z), south (pos Z), west (neg X), east (pos X)
	 */
	public static void addBox(BufferBuilder b, double x1, double y1, double z1, double x2, double y2, double z2, StructUV[] textures, int[] inversions){
		//BOTTOM FACE
		b.pos(x1, y1, z1).tex(textures[0].minU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x1, y1, z2).tex(textures[0].maxU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z2).tex(textures[0].maxU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z1).tex(textures[0].minU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		//TOP FACE
		b.pos(x1, y2, z1).tex(textures[1].minU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x1, y2, z2).tex(textures[1].maxU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z2).tex(textures[1].maxU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z1).tex(textures[1].minU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		//NORTH FACE
		b.pos(x1, y1, z1).tex(textures[2].minU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y1, z1).tex(textures[2].maxU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y2, z1).tex(textures[2].maxU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x1, y2, z1).tex(textures[2].minU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		//SOUTH FACE
		b.pos(x1, y1, z2).tex(textures[3].minU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y1, z2).tex(textures[3].maxU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y2, z2).tex(textures[3].maxU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x1, y2, z2).tex(textures[3].minU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		//WEST FACE
		b.pos(x1, y1, z1).tex(textures[4].minU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y1, z2).tex(textures[4].maxU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z2).tex(textures[4].maxU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z1).tex(textures[4].minU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		//EAST FACE
		b.pos(x2, y1, z1).tex(textures[5].minU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y1, z2).tex(textures[5].maxU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z2).tex(textures[5].maxU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z1).tex(textures[5].minU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
	}
	
	public static void addBoxWithSprite(BufferBuilder b, double x1, double y1, double z1, double x2, double y2, double z2, TextureAtlasSprite sprite, StructUV[] textures, int[] inversions){
		float spriteW = sprite.getMaxU()-sprite.getMinU();
		float spriteH = sprite.getMaxV()-sprite.getMinV();
		
		//BOTTOM FACE
		b.pos(x1, y1, z1).tex(sprite.getMinU()+textures[0].minU*spriteW,sprite.getMinV()+textures[0].minV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x1, y1, z2).tex(sprite.getMinU()+textures[0].maxU*spriteW,sprite.getMinV()+textures[0].minV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z2).tex(sprite.getMinU()+textures[0].maxU*spriteW,sprite.getMinV()+textures[0].maxV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		b.pos(x2, y1, z1).tex(sprite.getMinU()+textures[0].minU*spriteW,sprite.getMinV()+textures[0].maxV*spriteH).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		//TOP FACE
		b.pos(x1, y2, z1).tex(sprite.getMinU()+textures[1].minU*spriteW,sprite.getMinV()+textures[1].minV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x1, y2, z2).tex(sprite.getMinU()+textures[1].maxU*spriteW,sprite.getMinV()+textures[1].minV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z2).tex(sprite.getMinU()+textures[1].maxU*spriteW,sprite.getMinV()+textures[1].maxV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		b.pos(x2, y2, z1).tex(sprite.getMinU()+textures[1].minU*spriteW,sprite.getMinV()+textures[1].maxV*spriteH).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		//NORTH FACE
		b.pos(x1, y1, z1).tex(sprite.getMinU()+textures[2].minU*spriteW,sprite.getMinV()+textures[2].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y1, z1).tex(sprite.getMinU()+textures[2].maxU*spriteW,sprite.getMinV()+textures[2].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x2, y2, z1).tex(sprite.getMinU()+textures[2].maxU*spriteW,sprite.getMinV()+textures[2].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		b.pos(x1, y2, z1).tex(sprite.getMinU()+textures[2].minU*spriteW,sprite.getMinV()+textures[2].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		//SOUTH FACE
		b.pos(x1, y1, z2).tex(sprite.getMinU()+textures[3].minU*spriteW,sprite.getMinV()+textures[3].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y1, z2).tex(sprite.getMinU()+textures[3].maxU*spriteW,sprite.getMinV()+textures[3].minV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x2, y2, z2).tex(sprite.getMinU()+textures[3].maxU*spriteW,sprite.getMinV()+textures[3].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		b.pos(x1, y2, z2).tex(sprite.getMinU()+textures[3].minU*spriteW,sprite.getMinV()+textures[3].maxV*spriteH).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		//WEST FACE
		b.pos(x1, y1, z1).tex(sprite.getMinU()+textures[4].minU*spriteW,sprite.getMinV()+textures[4].minV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y1, z2).tex(sprite.getMinU()+textures[4].maxU*spriteW,sprite.getMinV()+textures[4].minV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z2).tex(sprite.getMinU()+textures[4].maxU*spriteW,sprite.getMinV()+textures[4].maxV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		b.pos(x1, y2, z1).tex(sprite.getMinU()+textures[4].minU*spriteW,sprite.getMinV()+textures[4].maxV*spriteH).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		//EAST FACE
		b.pos(x2, y1, z1).tex(sprite.getMinU()+textures[5].minU*spriteW,sprite.getMinV()+textures[5].minV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y1, z2).tex(sprite.getMinU()+textures[5].maxU*spriteW,sprite.getMinV()+textures[5].minV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z2).tex(sprite.getMinU()+textures[5].maxU*spriteW,sprite.getMinV()+textures[5].maxV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		b.pos(x2, y2, z1).tex(sprite.getMinU()+textures[5].minU*spriteW,sprite.getMinV()+textures[5].maxV*spriteH).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
	}
	

	public static void addBoxExt(BufferBuilder b, double x1, double y1, double z1, double x2, double y2, double z2, StructUV[] textures, int[] inversions, boolean[] faceToggles){
		//BOTTOM FACE
		if (faceToggles[0]){
			b.pos(x1, y1, z1).tex(textures[0].minU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
			b.pos(x1, y1, z2).tex(textures[0].maxU,textures[0].minV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
			b.pos(x2, y1, z2).tex(textures[0].maxU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
			b.pos(x2, y1, z1).tex(textures[0].minU,textures[0].maxV).color(255, 255, 255, 255).normal(0, -1*inversions[0], 0).endVertex();
		}
		//TOP FACE
		if (faceToggles[1]){
			b.pos(x1, y2, z1).tex(textures[1].minU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
			b.pos(x1, y2, z2).tex(textures[1].maxU,textures[1].minV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
			b.pos(x2, y2, z2).tex(textures[1].maxU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
			b.pos(x2, y2, z1).tex(textures[1].minU,textures[1].maxV).color(255, 255, 255, 255).normal(0, 1*inversions[1], 0).endVertex();
		}
		//NORTH FACE
		if (faceToggles[2]){
			b.pos(x1, y1, z1).tex(textures[2].minU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
			b.pos(x2, y1, z1).tex(textures[2].maxU,textures[2].minV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
			b.pos(x2, y2, z1).tex(textures[2].maxU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
			b.pos(x1, y2, z1).tex(textures[2].minU,textures[2].maxV).color(255, 255, 255, 255).normal(0, 0, -1*inversions[2]).endVertex();
		}
		//SOUTH FACE
		if (faceToggles[3]){
			b.pos(x1, y1, z2).tex(textures[3].minU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
			b.pos(x2, y1, z2).tex(textures[3].maxU,textures[3].minV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
			b.pos(x2, y2, z2).tex(textures[3].maxU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
			b.pos(x1, y2, z2).tex(textures[3].minU,textures[3].maxV).color(255, 255, 255, 255).normal(0, 0, 1*inversions[3]).endVertex();
		}
		//WEST FACE
		if (faceToggles[4]){
			b.pos(x1, y1, z1).tex(textures[4].minU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
			b.pos(x1, y1, z2).tex(textures[4].maxU,textures[4].minV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
			b.pos(x1, y2, z2).tex(textures[4].maxU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
			b.pos(x1, y2, z1).tex(textures[4].minU,textures[4].maxV).color(255, 255, 255, 255).normal(-1*inversions[4], 0, 0).endVertex();
		}
		//EAST FACE
		if (faceToggles[5]){
			b.pos(x2, y1, z1).tex(textures[5].minU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
			b.pos(x2, y1, z2).tex(textures[5].maxU,textures[5].minV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
			b.pos(x2, y2, z2).tex(textures[5].maxU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
			b.pos(x2, y2, z1).tex(textures[5].minU,textures[5].maxV).color(255, 255, 255, 255).normal(1*inversions[5], 0, 0).endVertex();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void drawQuadGui(BufferBuilder BufferBuilder, double zLevel, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double minU, double minV, double maxU, double maxV){
		float f = 0.00390625F;
        float f1 = 0.00390625F;
        BufferBuilder.pos((double)x1 + 0.0F, (double)y1 + 0.0F, zLevel).tex(minU,maxV).endVertex();
        BufferBuilder.pos((double)x2 + 0.0F, (double)y2 + 0.0F, zLevel).tex(maxU, maxV).endVertex();
        BufferBuilder.pos((double)x3 + 0.0F, (double)y3 + 0.0F, zLevel).tex(maxU, minV).endVertex();
        BufferBuilder.pos((double)x4 + 0.0F, (double)y4 + 0.0F, zLevel).tex(minU, minV).endVertex();
    }

	@SideOnly(Side.CLIENT)
	public static void drawTexturedModalRect(int x, int y, double zLevel, double minU, double minV, double maxU, double maxV, int widthIn, int heightIn)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder = tessellator.getBuffer();
        BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        BufferBuilder.pos((double)(x + 0), (double)(y + heightIn), (double)zLevel).tex(minU, maxV).endVertex();
        BufferBuilder.pos((double)(x + widthIn), (double)(y + heightIn), (double)zLevel).tex(maxU, maxV).endVertex();
        BufferBuilder.pos((double)(x + widthIn), (double)(y + 0), (double)zLevel).tex(maxU, minV).endVertex();
        BufferBuilder.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex(minU, minV).endVertex();
        tessellator.draw();
    }

	@SideOnly(Side.CLIENT)
	public static void drawTexturedModalRectBatched(BufferBuilder BufferBuilder, int x, int y, double zLevel, double minU, double minV, double maxU, double maxV, int widthIn, int heightIn)
    {
        BufferBuilder.pos((double)(x + 0), (double)(y + heightIn), (double)zLevel).tex(minU, maxV).endVertex();
        BufferBuilder.pos((double)(x + widthIn), (double)(y + heightIn), (double)zLevel).tex(maxU, maxV).endVertex();
        BufferBuilder.pos((double)(x + widthIn), (double)(y + 0), (double)zLevel).tex(maxU, minV).endVertex();
        BufferBuilder.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex(minU, minV).endVertex();
    }

	@SideOnly(Side.CLIENT)
	public static void drawColorRectBatched(BufferBuilder BufferBuilder, double x, double y, double zLevel, double widthIn, double heightIn,
			float r1, float g1, float b1, float a1,
			float r2, float g2, float b2, float a2,
			float r3, float g3, float b3, float a3,
			float r4, float g4, float b4, float a4)
    {
        BufferBuilder.pos((double)(x + 0), (double)(y + heightIn), (double)zLevel).color(r1, g1, b1, a1).endVertex();
        BufferBuilder.pos((double)(x + widthIn), (double)(y + heightIn), (double)zLevel).color(r2, g2, b2, a2).endVertex();
        BufferBuilder.pos((double)(x + widthIn), (double)(y + 0), (double)zLevel).color(r3, g3, b3, a3).endVertex();
        BufferBuilder.pos((double)(x + 0), (double)(y + 0), (double)zLevel).color(r4, g4, b4, a4).endVertex();
    }
	
	@SideOnly(Side.CLIENT)
	public static void drawQuadGuiExt(BufferBuilder BufferBuilder, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int minU, int minV, int maxU, int maxV, int texW, int texH, float r, float g, float b, float a){
		float mU = (float)minU / (float)texW;
		float mV = (float)minV / (float)texH;
		float xU = (float)maxU / (float)texW;
		float xV = (float)maxV / (float)texH;
        BufferBuilder.pos(x1 + 0.0F, y1 + 0.0F, 0).tex(mU,xV).color(r, g, b, a).endVertex();
        BufferBuilder.pos(x2 + 0.0F, y2 + 0.0F, 0).tex(xU, xV).color(r, g, b, a).endVertex();
        BufferBuilder.pos(x3 + 0.0F, y3 + 0.0F, 0).tex(xU, mV).color(r, g, b, a).endVertex();
        BufferBuilder.pos(x4 + 0.0F, y4 + 0.0F, 0).tex(mU, mV).color(r, g, b, a).endVertex();
    }
	
	public static void drawTextRGBA(FontRenderer font, String s, int x, int y, int r, int g, int b, int a){
		font.drawString(s, x, y, (a << 24) + (r << 16) + (g << 8) + (b));
	}
	
	public static void renderChunkUniforms(RenderChunk c){
		if (ShaderUtil.currentProgram == ShaderUtil.lightProgram){
			BlockPos pos = c.getPosition();
			int chunkX = GL20.glGetUniformLocation(ShaderUtil.lightProgram, "chunkX");
			int chunkY = GL20.glGetUniformLocation(ShaderUtil.lightProgram, "chunkY");
			int chunkZ = GL20.glGetUniformLocation(ShaderUtil.lightProgram, "chunkZ");
			GL20.glUniform1i(chunkX, pos.getX());
			GL20.glUniform1i(chunkY, pos.getY());
			GL20.glUniform1i(chunkZ, pos.getZ());
		}
	}

	/**
	 * Renders a fluid block, call from TESR. x/y/z is the rendering offset.
	 *
	 * @param fluid Fluid to render
	 * @param pos   BlockPos where the Block is rendered. Used for brightness.
	 * @param w     Width. 1 = full X-Width
	 * @param h     Height. 1 = full Y-Height
	 * @param d     Depth. 1 = full Z-Depth
	 */
	public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double w, double h, double d) {
		double wd = (1d - w) / 2d;
		double hd = (1d - h) / 2d;
		double dd = (1d - d) / 2d;

		renderFluidCuboid(fluid, pos, wd, hd, dd, 1d - wd, 1d - hd, 1d - dd);
	}

	public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x1, double y1, double z1, double x2, double y2, double z2) {
		int color = fluid.getFluid().getColor(fluid);
		renderFluidCuboid(fluid, pos, x1, y1, z1, x2, y2, z2, color);
	}

	/**
	 * Renders block with offset x/y/z from x1/y1/z1 to x2/y2/z2 inside the block local coordinates, so from 0-1
	 */
	public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//RenderUtil.setColorRGBA(color);
		int brightness = Minecraft.getMinecraft().world.getCombinedLight(pos, fluid.getFluid().getLuminosity());

		TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
		TextureAtlasSprite flowing = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());

		// x/y/z2 - x/y/z1 is because we need the width/height/depth
		putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, brightness, false);
		putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, brightness, true);
		putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, brightness, true);
		putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, brightness, true);
		putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, brightness, true);
		putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.UP, color, brightness, false);

		tessellator.draw();
	}

	public static void putTexturedQuad(BufferBuilder renderer, TextureAtlasSprite sprite, double x, double y, double z, double w, double h, double d, EnumFacing face,
									   int color, int brightness, boolean flowing) {
		int l1 = brightness >> 0x10 & 0xFFFF;
		int l2 = brightness & 0xFFFF;

		int a = color >> 24 & 0xFF;
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;

		putTexturedQuad(renderer, sprite, x, y, z, w, h, d, face, r, g, b, a, l1, l2, flowing);
	}

	// x and x+w has to be within [0,1], same for y/h and z/d
	public static void putTexturedQuad(BufferBuilder renderer, TextureAtlasSprite sprite, double x, double y, double z, double w, double h, double d, EnumFacing face,
									   int r, int g, int b, int a, int light1, int light2, boolean flowing) {
		// safety
		if (sprite == null) {
			return;
		}
		double minU;
		double maxU;
		double minV;
		double maxV;

		double size = 16f;
		if (flowing) {
			size = 8f;
		}

		double x1 = x;
		double x2 = x + w;
		double y1 = y;
		double y2 = y + h;
		double z1 = z;
		double z2 = z + d;

		double xt1 = x1 % 1d;
		double xt2 = xt1 + w;
		while (xt2 > 1f) xt2 -= 1f;
		double yt1 = y1 % 1d;
		double yt2 = yt1 + h;
		while (yt2 > 1f) yt2 -= 1f;
		double zt1 = z1 % 1d;
		double zt2 = zt1 + d;
		while (zt2 > 1f) zt2 -= 1f;

		// flowing stuff should start from the bottom, not from the start
		if (flowing) {
			double tmp = 1d - yt1;
			yt1 = 1d - yt2;
			yt2 = tmp;
		}

		switch (face) {
			case DOWN:
			case UP:
				minU = sprite.getInterpolatedU(xt1 * size);
				maxU = sprite.getInterpolatedU(xt2 * size);
				minV = sprite.getInterpolatedV(zt1 * size);
				maxV = sprite.getInterpolatedV(zt2 * size);
				break;
			case NORTH:
			case SOUTH:
				minU = sprite.getInterpolatedU(xt2 * size);
				maxU = sprite.getInterpolatedU(xt1 * size);
				minV = sprite.getInterpolatedV(yt1 * size);
				maxV = sprite.getInterpolatedV(yt2 * size);
				break;
			case WEST:
			case EAST:
				minU = sprite.getInterpolatedU(zt2 * size);
				maxU = sprite.getInterpolatedU(zt1 * size);
				minV = sprite.getInterpolatedV(yt1 * size);
				maxV = sprite.getInterpolatedV(yt2 * size);
				break;
			default:
				minU = sprite.getMinU();
				maxU = sprite.getMaxU();
				minV = sprite.getMinV();
				maxV = sprite.getMaxV();
		}

		switch (face) {
			case DOWN:
				renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				break;
			case UP:
				renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				break;
			case NORTH:
				renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				break;
			case SOUTH:
				renderer.pos(x1, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x1, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				break;
			case WEST:
				renderer.pos(x1, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x1, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				break;
			case EAST:
				renderer.pos(x2, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				break;
		}
	}

}
