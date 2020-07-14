package teamroots.embers.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.Embers;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageEyeButton;
import teamroots.embers.util.IFilterComparator;
import teamroots.embers.util.Vec2i;

import java.io.IOException;
import java.util.List;

public class GuiEye extends GuiContainer {

    private static final ResourceLocation guiLocation = new ResourceLocation(Embers.MODID,"textures/gui/eye_gui.png");

    public GuiEye(EntityPlayer player) {
        super(new ContainerEye(player));
        this.xSize = ContainerEye.WIDTH;
        this.ySize = ContainerEye.HEIGHT;
    }

    private Vec2i getButton(float angle)
    {
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        return ((ContainerEye) inventorySlots).getButtonPosition(xPos, yPos, angle);
    }

    private Vec2i getFinish() {
        return getButton(180);
    }

    private Vec2i getLeft() {
        return getButton(140);
    }

    private Vec2i getRight() {
        return getButton(-140);
    }

    private Vec2i getInvert() {
        return getButton(-40);
    }

    private Vec2i getFlag() {
        return getButton(40);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(isOnButton(mouseX,mouseY,getFinish())) {
            triggerButton(ContainerEye.EnumButton.FINISH);
        } else if(isOnButton(mouseX,mouseY,getLeft())) {
            triggerButton(ContainerEye.EnumButton.LEFT);
        } else if(isOnButton(mouseX,mouseY,getRight())) {
            triggerButton(ContainerEye.EnumButton.RIGHT);
        } else if(isOnButton(mouseX,mouseY,getInvert())) {
            triggerButton(ContainerEye.EnumButton.INVERT);
        } else if(isOnButton(mouseX,mouseY,getFlag())) {
            triggerButton(ContainerEye.EnumButton.FLAG);
        } else {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    private void triggerButton(ContainerEye.EnumButton button) {
        PacketHandler.INSTANCE.sendToServer(new MessageEyeButton(button));
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        Slot slot = getSlotUnderMouse();
        ContainerEye container = (ContainerEye) inventorySlots;
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;

        if(isOnButton(mouseX,mouseY,getFinish())) {
            List<String> tooltip = Lists.newArrayList("Finish");
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
        } else if(isOnButton(mouseX,mouseY,getLeft())) {
            List<String> tooltip = Lists.newArrayList("Previous");
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
        } else if(isOnButton(mouseX,mouseY,getRight())) {
            List<String> tooltip = Lists.newArrayList("Next");
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
        } else if(isOnButton(mouseX,mouseY,getInvert())) {
            List<String> tooltip = Lists.newArrayList("Invert");
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
        } else if(isOnButton(mouseX,mouseY,getFlag())) {
            List<String> tooltip = Lists.newArrayList("Flag");
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
        } else {
            super.renderHoveredToolTip(mouseX, mouseY);
        }
    }

    private boolean isOnButton(int mouseX, int mouseY, Vec2i button)
    {
        return mouseX >= button.x - 16 && mouseX < button.x + 16 && mouseY >= button.y - 16 && mouseY < button.y + 16;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiLocation);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;

        int yInventory = yPos + this.ySize - 85;

        drawTexturedModalRect(xPos, yInventory, 0, 0, this.xSize, 85);
        int textWidth = 212;
        int textHeight = 33;
        int textX = xPos + (this.xSize - textWidth) / 2;
        int textY = yInventory - textHeight;
        drawTexturedModalRect(textX, textY, 0, 86, 212, 33);

        ContainerEye eye = (ContainerEye)inventorySlots;
        IFilterComparator comparator = eye.comparator;
        if(comparator != null) {
            ItemStack left = eye.filterInventory.getStackInSlot(0);
            ItemStack right = eye.filterInventory.getStackInSlot(1);
            GuiCodex.drawCenteredTextGlowing(fontRenderer, comparator.format(left,right,eye.flag,eye.inverted), textX + textWidth / 2, textY + textHeight / 2 - 4);
        }
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(guiLocation);

        drawButton(xPos, yPos, 180, false, 176, 32); //Finish
        drawButton(xPos, yPos, 140, false, 176, 16); //Left
        drawButton(xPos, yPos, -140, false, 176 + 16, 16); //Right
        drawButton(xPos, yPos, 90, true, 0, 0); //Item1
        drawButton(xPos, yPos, -90, true, 0, 0); //Item2
        drawButton(xPos, yPos, -40, false, 176 + 32, 16); //Invert
        drawButton(xPos, yPos, 40, false, 176 + 48, 16); //Flag

        int xCenter = xPos + ContainerEye.WIDTH / 2;
        int yCenter = yPos + 85 - 50;

        GlStateManager.pushMatrix();
        GlStateManager.translate(xCenter,yCenter,0);
        GlStateManager.scale(2,2,0);
        drawTexturedModalRect(-8, -8, 176, 0, 16, 16);
        GlStateManager.popMatrix();
    }

    private void drawButton(int xPos, int yPos, float angle, boolean itemSlot, int u, int v)
    {
        Vec2i button = ((ContainerEye) inventorySlots).getButtonPosition(xPos, yPos, angle);
        GlStateManager.pushMatrix();
        GlStateManager.translate(button.x, button.y, 0);
        GlStateManager.rotate(-angle, 0, 0, 1);

        drawTexturedModalRect(-24, -24, 0, 119, 48, 48);

        GlStateManager.popMatrix();

        if (itemSlot)
            drawTexturedModalRect(button.x - 24, button.y - 24, 48, 119, 48, 48);
        else
            drawTexturedModalRect(button.x - 8, button.y - 8, u, v, 16, 16);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
