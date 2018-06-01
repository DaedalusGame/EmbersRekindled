package teamroots.embers.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockAxle;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityAxleRenderer extends TileEntitySpecialRenderer<TileEntityAxle> {
	public ResourceLocation ironTexture = new ResourceLocation(Embers.MODID + ":textures/blocks/axle_iron.png");
	public ResourceLocation dawnstoneTexture = new ResourceLocation(Embers.MODID + ":textures/blocks/axle_dawnstone.png");
	public int lightx = 0, lighty = 0;
	
	StructUV[] textures = new StructUV[]{
			new StructUV(7,0,9,2,16,16),
			new StructUV(7,0,9,2,16,16),
			new StructUV(7,0,9,16,16,16),
			new StructUV(7,0,9,16,16,16),
			new StructUV(7,0,9,16,16,16),
			new StructUV(7,0,9,16,16,16)
	};
	
	public TileEntityAxleRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityAxle tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			if (state.getBlock() instanceof BlockAxle){
		        EnumFacing face = state.getValue(BlockAxle.facing);
		        ResourceLocation texture = null;
		        if (state.getBlock() == RegistryManager.axle_iron){
		        	texture = ironTexture;
		        }
	            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
	            GlStateManager.disableCull();
	            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	            Tessellator tess = Tessellator.getInstance();
	            BufferBuilder buffer = tess.getBuffer();
	            
	            GlStateManager.pushMatrix();
	            GlStateManager.translate(x+0.5, y+0.5, z+0.5);
				if (face == EnumFacing.UP){
					GL11.glRotated(180, 1, 0, 0);
				}
				
				if (face == EnumFacing.NORTH){
					GL11.glRotated(90, 1, 0, 0);
				}
				
				if (face == EnumFacing.WEST){
					GL11.glRotated(90, 0, 1, 0);
					GL11.glRotated(90, 1, 0, 0);
				}
				
				if (face == EnumFacing.SOUTH){
					GL11.glRotated(180, 0, 1, 0);
					GL11.glRotated(90, 1, 0, 0);
				}
				
				if (face == EnumFacing.EAST){
					GL11.glRotated(270, 0, 1, 0);
					GL11.glRotated(90, 1, 0, 0);
				}
	            GlStateManager.rotate(((float)(EventManager.ticks%360)+partialTicks)*(float) tile.capability.getPower(null), 0, 1, 0);
	            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
	            RenderUtil.addBox(buffer, -0.0625, -0.5, -0.0625, 0.0625, 0.5, 0.0625, textures, new int[]{1,1,1,1,1,1});
	            tess.draw();
	            GlStateManager.popMatrix();
			}
            
        }
	}
}
