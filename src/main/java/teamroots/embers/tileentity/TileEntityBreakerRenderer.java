package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockAutoHammer;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityBreakerRenderer extends TileEntitySpecialRenderer {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/breaker_blade.png");
	public int lightx = 0, lighty = 0;
	public StructBox blade = new StructBox(-0.375,0.5f,-0.375,0.375,0.625,0.375,new StructUV[]{new StructUV(0,0,12,12,16,16),new StructUV(0,0,12,12,16,16),new StructUV(0,10,12,12,16,16),new StructUV(0,10,12,12,16,16),new StructUV(0,10,12,12,16,16),new StructUV(0,10,12,12,16,16)});
	
	public TileEntityBreakerRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float partialTicks, int destroyStage){
		if (t instanceof TileEntityBreaker && t.getWorld().getBlockState(t.getPos()).getBlock() == RegistryManager.breaker){
			float ticks = ((TileEntityBreaker)t).ticksExisted;
			    
			GlStateManager.pushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            Tessellator tess = Tessellator.getInstance();
            VertexBuffer buffer = tess.getBuffer();
            GlStateManager.translate(x, y, z);
            EnumFacing facing = t.getWorld().getBlockState(t.getPos()).getValue(BlockAutoHammer.facing);
            float angle = 0;
            float pitch = -90;
            if (facing == EnumFacing.SOUTH){
            	angle = 180;
            }
            if (facing == EnumFacing.EAST){
            	angle = 270;
            }
            if (facing == EnumFacing.WEST){
            	angle = 90;
            }
            if (facing == EnumFacing.UP){
            	pitch = 0;
            }
            if (facing == EnumFacing.DOWN){
            	pitch = 180;
            }
            /*float hammerAngle = -45.0f;
            if (progress != -1){
            	hammerAngle = -45.0f - 90.0f*(1.0f-((progress-partialTicks)/5.0f)+1.0f);
            	if (hammerAngle < -135.0f){
            		hammerAngle = -135.0f - (hammerAngle + 135.0f);
            	}
            }*/
            GlStateManager.translate(0.5f, 0.5f, 0.5f);
            GlStateManager.rotate(angle, 0, 1, 0);
            GlStateManager.rotate(pitch, 1, 0, 0);
            GlStateManager.rotate(9.0f*(ticks+partialTicks), 0, 1, 0);
           
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            RenderUtil.addBox(buffer, blade.x1, blade.y1, blade.z1, blade.x2, blade.y2, blade.z2, blade.textures, new int[]{1,1,1,1,1,1});
            tess.draw();
            
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
		}
	}
}
