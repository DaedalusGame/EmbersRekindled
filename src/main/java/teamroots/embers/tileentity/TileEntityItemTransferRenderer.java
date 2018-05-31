package teamroots.embers.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityItemTransferRenderer extends TileEntitySpecialRenderer {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityItemTransferRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile instanceof TileEntityItemTransfer){
			TileEntityItemTransfer transfer = (TileEntityItemTransfer)tile;
			if (!transfer.filterItem.isEmpty() && Minecraft.getMinecraft().world != null) {
				GlStateManager.pushAttrib();
				GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
				GL11.glPushMatrix();
				EntityItem item = new EntityItem(Minecraft.getMinecraft().world, x, y, z, new ItemStack(transfer.filterItem.getItem(), 1, transfer.filterItem.getMetadata()));
				item.hoverStart = 0;
				item.onGround = false;
				GL11.glTranslated(x + 0.5, y + 0.15, z + 0.5);
				GL11.glScaled(0.75, 0.75, 0.75);
				GL11.glRotated(transfer.angle + ((transfer.turnRate)) * partialTicks, 0, 1.0, 0);
				Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, false);
				GL11.glPopMatrix();
				GlStateManager.popAttrib();
			}
		}
	}
}
