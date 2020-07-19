package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import teamroots.embers.Embers;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.PipeRenderUtil;

public class TileEntityEmberPipeRenderer extends TileEntitySpecialRenderer<TileEntityEmberPipe> {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/ember_pipe_tex.png");

	public TileEntityEmberPipeRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityEmberPipe tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
			for (EnumFacing facing : EnumFacing.VALUES) {
				if(shouldRenderPipe(tile, facing))
					PipeRenderUtil.addPipe(buffer, x, y, z, facing);
				if(shouldRenderLip(tile, facing))
					PipeRenderUtil.addPipeLip(buffer, x, y, z, facing);
			}
            tess.draw();
			GlStateManager.enableCull();
        }
	}

	private boolean shouldRenderLip(TileEntityEmberPipe pipe, EnumFacing facing) {
		EnumPipeConnection connection = pipe.getInternalConnection(facing);
		return connection == EnumPipeConnection.BLOCK || connection == EnumPipeConnection.LEVER;
	}

	private boolean shouldRenderPipe(TileEntityEmberPipe pipe, EnumFacing facing) {
		EnumPipeConnection connection = pipe.getInternalConnection(facing);
		return connection == EnumPipeConnection.PIPE || shouldRenderLip(pipe,facing);
	}
}
