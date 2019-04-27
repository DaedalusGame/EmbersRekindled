package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import teamroots.embers.Embers;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

import javax.annotation.Nullable;
import java.awt.*;

public class TileEntityItemRequisitionRenderer extends TileEntitySpecialRenderer<TileEntityItemRequisition> {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/item_pipe_tex.png");
	public int lightx = 0, lighty = 0;
	public StructBox up = new StructBox(0.375,0.625,0.375,0.625,1.0,0.625,new StructUV[]{new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16)});
	public StructBox down = new StructBox(0.375,0.375,0.375,0.625,0,0.625,new StructUV[]{new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16)});
	public StructBox north = new StructBox(0.375,0.375,0.375,0.625,0.625,0,new StructUV[]{new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16)});
	public StructBox south = new StructBox(0.375,0.375,0.625,0.625,0.625,1.0,new StructUV[]{new StructUV(6,12,0,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16)});
	public StructBox west = new StructBox(0.375,0.375,0.375,0,0.625,0.625,new StructUV[]{new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16)});
	public StructBox east = new StructBox(0.625,0.375,0.375,1.0,0.625,0.625,new StructUV[]{new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16)});
	public StructBox upEnd = new StructBox(0.3125,0.75,0.3125,0.6875,1.0,0.6875,new StructUV[]{new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16)});
	public StructBox downEnd = new StructBox(0.3125,0.25,0.3125,0.6875,0,0.6875,new StructUV[]{new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16)});
	public StructBox northEnd = new StructBox(0.3125,0.3125,0.25,0.6875,0.6875,0,new StructUV[]{new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16)});
	public StructBox southEnd = new StructBox(0.3125,0.3125,0.75,0.6875,0.6875,1.0,new StructUV[]{new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16)});
	public StructBox westEnd = new StructBox(0.25,0.3125,0.3125,0,0.6875,0.6875,new StructUV[]{new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16)});
	public StructBox eastEnd = new StructBox(0.75,0.3125,0.3125,1.0,0.6875,0.6875,new StructUV[]{new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16)});
	public TileEntityItemRequisitionRenderer(){
		super();
	}

	@Override
	public void render(TileEntityItemRequisition tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
			if (shouldRenderPipe(tile,EnumFacing.UP)){
				RenderUtil.addBox(buffer, up.x1+x, up.y1+y, up.z1+z, up.x2+x, up.y2+y, up.z2+z, up.textures, new int[]{1,1,1,1,1,1});
			}
			if (shouldRenderPipe(tile,EnumFacing.DOWN)){
				RenderUtil.addBox(buffer, down.x1+x, down.y1+y, down.z1+z, down.x2+x, down.y2+y, down.z2+z, down.textures, new int[]{-1,-1,1,1,1,1});
			}
			if (shouldRenderPipe(tile,EnumFacing.NORTH)){
				RenderUtil.addBox(buffer, north.x1+x, north.y1+y, north.z1+z, north.x2+x, north.y2+y, north.z2+z, north.textures, new int[]{1,1,1,1,1,1});
			}
			if (shouldRenderPipe(tile,EnumFacing.SOUTH)){
				RenderUtil.addBox(buffer, south.x1+x, south.y1+y, south.z1+z, south.x2+x, south.y2+y, south.z2+z, south.textures, new int[]{1,1,-1,-1,1,1});
			}
			if (shouldRenderPipe(tile,EnumFacing.WEST)){
				RenderUtil.addBox(buffer, west.x1+x, west.y1+y, west.z1+z, west.x2+x, west.y2+y, west.z2+z, west.textures, new int[]{1,1,1,1,1,1});
			}
			if (shouldRenderPipe(tile,EnumFacing.EAST)){
				RenderUtil.addBox(buffer, east.x1+x, east.y1+y, east.z1+z, east.x2+x, east.y2+y, east.z2+z, east.textures, new int[]{1,1,1,1,-1,-1});
			}
			if (shouldRenderLip(tile,EnumFacing.UP)){
				RenderUtil.addBox(buffer, upEnd.x1+x, upEnd.y1+y, upEnd.z1+z, upEnd.x2+x, upEnd.y2+y, upEnd.z2+z, upEnd.textures, new int[]{1,1,1,1,1,1});
			}
			if (shouldRenderLip(tile,EnumFacing.DOWN)){
				RenderUtil.addBox(buffer, downEnd.x1+x, downEnd.y1+y, downEnd.z1+z, downEnd.x2+x, downEnd.y2+y, downEnd.z2+z, downEnd.textures, new int[]{-1,-1,1,1,1,1});
			}
			if (shouldRenderLip(tile,EnumFacing.NORTH)){
				RenderUtil.addBox(buffer, northEnd.x1+x, northEnd.y1+y, northEnd.z1+z, northEnd.x2+x, northEnd.y2+y, northEnd.z2+z, northEnd.textures, new int[]{1,1,1,1,1,1});
			}
			if (shouldRenderLip(tile,EnumFacing.SOUTH)){
				RenderUtil.addBox(buffer, southEnd.x1+x, southEnd.y1+y, southEnd.z1+z, southEnd.x2+x, southEnd.y2+y, southEnd.z2+z, southEnd.textures, new int[]{1,1,-1,-1,1,1});
			}
			if (shouldRenderLip(tile,EnumFacing.WEST)){
				RenderUtil.addBox(buffer, westEnd.x1+x, westEnd.y1+y, westEnd.z1+z, westEnd.x2+x, westEnd.y2+y, westEnd.z2+z, westEnd.textures, new int[]{1,1,1,1,1,1});
			}
			if (shouldRenderLip(tile,EnumFacing.EAST)){
				RenderUtil.addBox(buffer, eastEnd.x1+x, eastEnd.y1+y, eastEnd.z1+z, eastEnd.x2+x, eastEnd.y2+y, eastEnd.z2+z, eastEnd.textures, new int[]{1,1,1,1,-1,-1});
			}
            tess.draw();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GL11.glPushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableCull();
			GL11.glTranslated(x + 0.5, y + 1.0, z + 0.5);
			GL11.glScaled(0.25, 0.25, 0.25);
			GL11.glRotated(tile.angle + ((tile.turnRate)) * partialTicks, 0, 1.0, 0);
			ItemStack filterItem = tile.filterItem;
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(filterItem, tile.getWorld(), null);
			//GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(0.1F, 0.0F, 0.0F, 0.1f);
			//Minecraft.getMinecraft().getRenderItem().renderItem(filterItem, model);
			renderItem(filterItem,model);
			GL11.glPopMatrix();
		}
	}

	public void renderItem(ItemStack stack, IBakedModel model)
	{
		float r = 1f;
		float g = 1f;
		float b = 1f;
		float a = 0.5f;
		if (!stack.isEmpty())
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);

			if (model.isBuiltInRenderer())
			{
				GlStateManager.color(r, g, b, a);
				GlStateManager.enableRescaleNormal();
				stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
			}
			else
			{
				Color color = new Color(r, g, b, a);
				Minecraft.getMinecraft().getRenderItem().renderModel(model, color.getRGB(), stack);
			}

			GlStateManager.popMatrix();
		}
	}

	private boolean shouldRenderLip(TileEntityItemRequisition pipe, EnumFacing facing) {
		EnumPipeConnection connection = pipe.getInternalConnection(facing);
		return connection == EnumPipeConnection.BLOCK || connection == EnumPipeConnection.LEVER;
	}

	private boolean shouldRenderPipe(TileEntityItemRequisition pipe, EnumFacing facing) {
		EnumPipeConnection connection = pipe.getInternalConnection(facing);
		return connection == EnumPipeConnection.PIPE || shouldRenderLip(pipe,facing);
	}
}
