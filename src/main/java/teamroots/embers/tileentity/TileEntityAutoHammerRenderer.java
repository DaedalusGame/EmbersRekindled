package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.block.BlockAutoHammer;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityAutoHammerRenderer extends TileEntitySpecialRenderer {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/auto_hammer.png");
	public int lightx = 0, lighty = 0;
	public StructBox hammerShaft = new StructBox(-0.0625,0,-0.0625,0.0625,0.625,0.0625,new StructUV[]{new StructUV(1,1,3,3,16,16),new StructUV(2,2,4,4,16,16),new StructUV(0,4,2,14,16,16),new StructUV(0,4,2,14,16,16),new StructUV(0,4,2,14,16,16),new StructUV(0,4,2,14,16,16)});
	public StructBox hammerHead = new StructBox(-0.125,0.625,-0.1875,0.125,0.875,0.1875,new StructUV[]{new StructUV(4,0,10,4,16,16),new StructUV(4,0,10,4,16,16),new StructUV(0,0,4,4,16,16),new StructUV(0,0,4,4,16,16),new StructUV(4,0,10,4,16,16),new StructUV(4,0,10,4,16,16)});
	
	public TileEntityAutoHammerRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float partialTicks, int destroyStage){
		if (t instanceof TileEntityAutoHammer){
			float progress = ((TileEntityAutoHammer)t).progress;
	            
			GlStateManager.pushMatrix();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            Tessellator tess = Tessellator.getInstance();
            VertexBuffer buffer = tess.getBuffer();
            GlStateManager.translate(x, y, z);
            EnumFacing facing = t.getWorld().getBlockState(t.getPos()).getValue(BlockAutoHammer.facing);
            float angle = 0;
            if (facing == EnumFacing.SOUTH){
            	angle = 180;
            }
            if (facing == EnumFacing.EAST){
            	angle = 270;
            }
            if (facing == EnumFacing.WEST){
            	angle = 90;
            }
            float hammerAngle = -45.0f;
            if (progress != -1){
            	hammerAngle = -45.0f - 90.0f*(1.0f-((progress-partialTicks)/5.0f)+1.0f);
            	if (hammerAngle < -135.0f){
            		hammerAngle = -135.0f - (hammerAngle + 135.0f);
            	}
            }
            GlStateManager.translate(0.5f, 0.5f, 0.5f);
            GlStateManager.rotate(angle, 0, 1, 0);
            GlStateManager.translate(0, 0, -0.25f);
            GlStateManager.rotate(hammerAngle, 1, 0, 0);
           
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, hammerShaft.x1, hammerShaft.y1, hammerShaft.z1, hammerShaft.x2, hammerShaft.y2, hammerShaft.z2, hammerShaft.textures, new int[]{1,1,1,1,1,1});
            RenderUtil.addBox(buffer, hammerHead.x1, hammerHead.y1, hammerHead.z1, hammerHead.x2, hammerHead.y2, hammerHead.z2, hammerHead.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
		}
	}
}
