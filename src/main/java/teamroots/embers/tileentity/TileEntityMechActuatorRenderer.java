package teamroots.embers.tileentity;

import mysticalmechanics.api.GearHelper;
import mysticalmechanics.api.GearHelperTile;
import mysticalmechanics.api.MysticalMechanicsAPI;
import mysticalmechanics.block.BlockGearbox;
import mysticalmechanics.tileentity.TileEntityGearbox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import teamroots.embers.block.BlockMechActuator;

public class TileEntityMechActuatorRenderer extends TileEntitySpecialRenderer<TileEntityMechActuator> {
    public TileEntityMechActuatorRenderer(){
        super();
    }

    @Override
    public void render(TileEntityMechActuator tile, double x, double y, double z, float partialTicks, int destroyStage, float tileAlpha){
        if (tile != null){
            IBlockState state = tile.getWorld().getBlockState(tile.getPos());
            if (state.getBlock() instanceof BlockMechActuator){
                EntityPlayer player = Minecraft.getMinecraft().player;
                ItemStack gearHologram = player.getHeldItemMainhand();

                for (int i = 0; i < 6; i ++){
                    EnumFacing face = EnumFacing.getFront(i);
                    GearHelperTile gear = tile.gears[i];

                    boolean sideHit = MysticalMechanicsAPI.IMPL.isGearHit(tile, face);
                    boolean renderHologram = MysticalMechanicsAPI.IMPL.shouldRenderHologram(gearHologram, !gear.isEmpty(), sideHit, tile.canAttachGear(face, gearHologram));

                    if (!gear.isEmpty() || renderHologram){
                        GlStateManager.disableCull();
                        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x+0.5, y+0.5, z+0.5);

                        switch (face) {

                            case DOWN:
                                GlStateManager.rotate(-90, 1, 0, 0);
                                break;
                            case UP:
                                GlStateManager.rotate(90, 1, 0, 0);
                                break;
                            case NORTH:
                                break;
                            case SOUTH:
                                GlStateManager.rotate(180, 0, 1, 0);
                                break;
                            case WEST:
                                GlStateManager.rotate(90, 0, 1, 0);
                                break;
                            case EAST:
                                GlStateManager.rotate(270, 0, 1, 0);
                                break;
                        }

                        MysticalMechanicsAPI.IMPL.renderGear(gear.getGear(), gearHologram, renderHologram, partialTicks, -0.375, 0.875, (float) gear.getPartialAngle(partialTicks));

                        GlStateManager.popMatrix();
                    }
                }
            }

        }
    }
}