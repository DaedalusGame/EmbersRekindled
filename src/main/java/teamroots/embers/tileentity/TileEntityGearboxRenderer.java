package teamroots.embers.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockGearbox;

public class TileEntityGearboxRenderer extends TileEntitySpecialRenderer<TileEntityGearbox> {
	public TileEntityGearboxRenderer(){
		super();
	}
	
	@Override
	public void render(TileEntityGearbox tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
		if (tile != null){
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			if (state.getBlock() instanceof BlockGearbox){
				for (int i = 0; i < 6; i ++){
					if (!tile.gears[i].isEmpty()){
						EnumFacing face = EnumFacing.getFront(i);
				        
			            GlStateManager.disableCull();
			            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

						double power = tile.capability.getPower(face);
			            GlStateManager.pushMatrix();
			            GlStateManager.translate(x+0.5, y+0.5, z+0.5);
			            if (face == EnumFacing.DOWN){
							GlStateManager.rotate(-90, 1, 0, 0);
						}
						
						if (face == EnumFacing.UP){
							GlStateManager.rotate(90, 1, 0, 0);
						}
						
						if (face == EnumFacing.NORTH){
							
						}
						
						if (face == EnumFacing.WEST){
							GlStateManager.rotate(90, 0, 1, 0);
						}
						
						if (face == EnumFacing.SOUTH){
							GlStateManager.rotate(180, 0, 1, 0);
						}
						
						if (face == EnumFacing.EAST){
							GlStateManager.rotate(270, 0, 1, 0);
						}
						GlStateManager.translate(0, 0, -0.375);
						GlStateManager.scale(0.875, 0.875, 0.875);
						GlStateManager.rotate(((float)(EventManager.ticks%360)+partialTicks)*(float)power, 0, 0, 1);
						Minecraft.getMinecraft().getRenderItem().renderItem(tile.gears[i], TransformType.FIXED);
			            GlStateManager.popMatrix();
					}
				}
			}
            
        }
	}
}
