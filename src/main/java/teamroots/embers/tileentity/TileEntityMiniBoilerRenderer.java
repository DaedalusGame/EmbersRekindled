package teamroots.embers.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.lwjgl.opengl.GL11;
import teamroots.embers.Embers;
import teamroots.embers.util.EnumPipeConnection;
import teamroots.embers.util.PipeRenderUtil;

public class TileEntityMiniBoilerRenderer extends TileEntitySpecialRenderer<TileEntityMiniBoiler> {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/pipe_tex.png");

	public TileEntityMiniBoilerRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityMiniBoiler tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
			for (EnumFacing facing : EnumFacing.HORIZONTALS) {
				if(shouldRenderPipe(tile, facing))
					PipeRenderUtil.addPipe(buffer, x, y, z, facing);
				if(shouldRenderLip(tile, facing))
					PipeRenderUtil.addPipeLip(buffer, x, y, z, facing);
			}
            tess.draw();
			GlStateManager.enableCull();
        }
	}

	private EnumPipeConnection getPipeConnection(World world, BlockPos pos, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos.offset(facing));
		if (tile instanceof IFluidPipeConnectable) {
			return ((IFluidPipeConnectable) tile).getConnection(facing.getOpposite());
		} else if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite())) {
			return EnumPipeConnection.BLOCK;
		}
		return EnumPipeConnection.NONE;
	}

	private boolean shouldRenderLip(TileEntityMiniBoiler pipe, EnumFacing facing) {
		EnumPipeConnection connection = getPipeConnection(pipe.getWorld(), pipe.getPos(), facing);
		return connection == EnumPipeConnection.BLOCK || connection == EnumPipeConnection.LEVER;
	}

	private boolean shouldRenderPipe(TileEntityMiniBoiler pipe, EnumFacing facing) {
		EnumPipeConnection connection = getPipeConnection(pipe.getWorld(), pipe.getPos(), facing);
		return connection == EnumPipeConnection.PIPE || shouldRenderLip(pipe,facing);
	}
}
