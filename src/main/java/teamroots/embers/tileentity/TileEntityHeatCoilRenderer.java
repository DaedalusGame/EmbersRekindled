package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityHeatCoilRenderer extends TileEntitySpecialRenderer {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/furnace_coil.png");
	public int lightx = 0, lighty = 0;
	public StructBox coil = new StructBox(-1,0,-1,1,0.25,1,new StructUV[]{new StructUV(0,0,32,32,64,64),new StructUV(0,0,32,32,64,64),new StructUV(0,32,32,36,64,64),new StructUV(0,32,32,36,64,64),new StructUV(0,32,32,36,64,64),new StructUV(0,32,32,36,64,64)});
	
	public TileEntityHeatCoilRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntity t, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (t instanceof TileEntityHeatCoil){
	            
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, x+0.5+coil.x1, y+1.0+coil.y1, z+0.5+coil.z1, x+0.5+coil.x2, y+1.0+coil.y2, z+0.5+coil.z2, coil.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            
        }
	}
}
