package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import teamroots.embers.Embers;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructUV;

public class TileEntityPumpRenderer extends TileEntitySpecialRenderer<TileEntityPumpBottom> {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/pump_piston.png");
	public int lightx = 0, lighty = 0;
	
	StructUV[] large_box = new StructUV[]{
			new StructUV(6,10,12,16,16,16),
			new StructUV(6,10,12,16,16,16),
			new StructUV(0,10,6,14,16,16),
			new StructUV(0,10,6,14,16,16),
			new StructUV(0,10,6,14,16,16),
			new StructUV(0,10,6,14,16,16)
	};
	StructUV[] small_box = new StructUV[]{
			new StructUV(10,4,14,8,16,16),
			new StructUV(10,4,14,8,16,16),
			new StructUV(10,0,14,4,16,16),
			new StructUV(10,0,14,4,16,16),
			new StructUV(10,0,14,4,16,16),
			new StructUV(10,0,14,4,16,16)
	};
	StructUV[] top_plate = new StructUV[]{
			new StructUV(0,0,10,10,16,16),
			new StructUV(0,0,10,10,16,16),
			new StructUV(0,8,10,10,16,16),
			new StructUV(0,8,10,10,16,16),
			new StructUV(0,8,10,10,16,16),
			new StructUV(0,8,10,10,16,16)
	};
	
	public TileEntityPumpRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityPumpBottom t, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (t != null){
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            
            double power = t.capability.getEmber() >= TileEntityPumpBottom.EMBER_COST ? 1 : 0;
            double amountUp = Math.abs(Math.sin((Math.PI * ((double)(t.progress) + power*partialTicks))/400.0));
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+0.5, y+1, z+0.5);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, -0.1875, 0.4375+amountUp*0.25, -0.1875, 0.1875, 0.6875+amountUp*0.25, 0.1875, large_box, new int[]{1,1,1,1,1,1});
            RenderUtil.addBox(buffer, -0.125, 0.4375+amountUp*0.5, -0.125, 0.125, 0.6875+amountUp*0.5, 0.125, small_box, new int[]{1,1,1,1,1,1});
            RenderUtil.addBox(buffer, -0.3125, 0.6875+amountUp*0.5, -0.3125, 0.3125, 0.8125+amountUp*0.5, 0.3125, top_plate, new int[]{1,1,1,1,1,1});
            tess.draw();
            GlStateManager.popMatrix();
        }
	}
}
