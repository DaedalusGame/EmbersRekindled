package teamroots.embers.tileentity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelFluid.FluidLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.Embers;
import teamroots.embers.block.BlockStamper;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.util.FluidTextureUtil;
import teamroots.embers.util.RenderUtil;
import teamroots.embers.util.StructBox;
import teamroots.embers.util.StructUV;

public class TileEntityStamperRenderer extends TileEntitySpecialRenderer {
	public ResourceLocation texture = new ResourceLocation(Embers.MODID + ":textures/blocks/stampTop.png");
	public ResourceLocation stampBar = new ResourceLocation(Embers.MODID + ":textures/items/stampBar.png");
	public ResourceLocation stampFlat = new ResourceLocation(Embers.MODID + ":textures/items/stampFlat.png");
	public ResourceLocation stampPlate = new ResourceLocation(Embers.MODID + ":textures/items/stampPlate.png");
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public StructBox stampX = new StructBox(0,0.25,0.25,1.0,0.75,0.75,new StructUV[]{new StructUV(0,0,8,16,32,32),new StructUV(0,0,8,16,32,32),new StructUV(16,0,32,8,32,32),new StructUV(16,0,32,8,32,32),new StructUV(8,0,16,8,32,32),new StructUV(8,0,16,8,32,32)});
	public StructBox stampY = new StructBox(0.25,0,0.25,0.75,1.0,0.75,new StructUV[]{new StructUV(8,0,16,8,32,32),new StructUV(8,0,16,8,32,32),new StructUV(0,0,8,16,32,32),new StructUV(0,0,8,16,32,32),new StructUV(0,0,8,16,32,32),new StructUV(0,0,8,16,32,32)});
	public StructBox stampZ = new StructBox(0.25,0.25,0,0.75,0.75,1.0,new StructUV[]{new StructUV(16,0,32,8,32,32),new StructUV(16,0,32,8,32,32),new StructUV(8,0,16,8,32,32),new StructUV(8,0,16,8,32,32),new StructUV(16,0,32,8,32,32),new StructUV(16,0,32,8,32,32)});
	public StructBox stampBox = new StructBox(-0.1875,-0.1875,-0.1875,0.1875,0.1875,0.1875, new StructUV[]{new StructUV(3,3,13,13,16,16),new StructUV(3,3,13,13,16,16),new StructUV(3,3,13,13,16,16),new StructUV(3,3,13,13,16,16),new StructUV(3,3,13,13,16,16),new StructUV(3,3,13,13,16,16)});
	public TileEntityStamperRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage){
		if (tile instanceof TileEntityStamper){
			TileEntityStamper stamp = (TileEntityStamper)tile;
			IBlockState state = tile.getWorld().getBlockState(tile.getPos());
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            GlStateManager.disableCull();
            Tessellator tess = Tessellator.getInstance();
            VertexBuffer buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            float offX = 0;
            float offY = 0;
            float offZ = 0;
            if (stamp.powered){
            	if (state.getValue(BlockStamper.facing) == EnumFacing.EAST){
            		offX = 1;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.WEST){
            		offX = -1;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.NORTH){
            		offZ = -1;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.SOUTH){
            		offZ = 1;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.UP){
            		offY = 1;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.DOWN){
            		offY = -1;
            	}
			}
            float magnitude = 0;
            if (!stamp.powered){
            	if (stamp.prevPowered){
            		magnitude = 1.0f-partialTicks;
            	}
            }
            else if (stamp.powered){
            	if (!stamp.prevPowered){
            		magnitude = partialTicks;
            	}
            	else {
            		magnitude = 1;
            	}
            }
            offX *= magnitude;
            offY *= magnitude;
            offZ *= magnitude;
			if (state.getValue(BlockStamper.facing) == EnumFacing.EAST || state.getValue(BlockStamper.facing) == EnumFacing.WEST){
				RenderUtil.addBox(buffer, stampX.x1+x-0.0001+offX, stampX.y1+y, stampX.z1+z, stampX.x2+x+0.0001+offX, stampX.y2+y, stampX.z2+z, stampX.textures, new int[]{1,1,1,1,1,1});
			}
			if (state.getValue(BlockStamper.facing) == EnumFacing.UP || state.getValue(BlockStamper.facing) == EnumFacing.DOWN){
				RenderUtil.addBox(buffer, stampY.x1+x, stampY.y1-0.0001+y+offY, stampY.z1+z, stampY.x2+x, stampY.y2+y+0.0001+offY, stampY.z2+z, stampY.textures, new int[]{1,1,1,1,1,1});
			}
			if (state.getValue(BlockStamper.facing) == EnumFacing.NORTH || state.getValue(BlockStamper.facing) == EnumFacing.SOUTH){
				RenderUtil.addBox(buffer, stampZ.x1+x, stampZ.y1+y, stampZ.z1-0.0001+z+offZ, stampZ.x2+x, stampZ.y2+y, stampZ.z2+0.0001+z+offZ, stampZ.textures, new int[]{1,1,1,1,1,1});
			}
			
			tess.draw();
			EnumStampType type = EnumStampType.getType(((TileEntityStamper) tile).stamp.getStackInSlot(0));
			if (type != EnumStampType.TYPE_NULL){
				if (type == EnumStampType.TYPE_BAR){
					Minecraft.getMinecraft().renderEngine.bindTexture(stampBar);
				}
				if (type == EnumStampType.TYPE_PLATE){
					Minecraft.getMinecraft().renderEngine.bindTexture(stampPlate);
				}
				if (type == EnumStampType.TYPE_FLAT){
					Minecraft.getMinecraft().renderEngine.bindTexture(stampFlat);
				}
	            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
	            float addX = 0;
	            float addY = 0;
	            float addZ = 0;
            	if (state.getValue(BlockStamper.facing) == EnumFacing.EAST){
            		addX = 0.35f;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.WEST){
            		addX = -0.35f;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.NORTH){
            		addZ = -0.35f;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.SOUTH){
            		addZ = 0.35f;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.UP){
            		addY = 0.35f;
            	}
            	if (state.getValue(BlockStamper.facing) == EnumFacing.DOWN){
            		addY = -0.35f;
            	}
				RenderUtil.addBox(buffer, stampBox.x1+0.5+x-0.0001+offX+addX, stampBox.y1+0.5+y+offY+addY, stampBox.z1+0.5+z+offZ+addZ, stampBox.x2+0.5+x+0.0001+offX+addX, stampBox.y2+0.5+y+offY+addY, stampBox.z2+0.5+z+offZ+addZ, stampBox.textures, new int[]{1,1,1,1,1,1});
				
	            tess.draw();
	            GlStateManager.enableCull();
			}
		}
	}
}
