package teamroots.embers.compat.jei.wrapper;

import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.Embers;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.compat.jei.EmbersJEIPlugin;

import java.util.ArrayList;
import java.util.List;

public class EngineRecipeWrapper extends FluidRecipeWrapper {
    public static final int GEAR_X = 102;
    public static final int GEAR_Y = 6;

    ILiquidFuel fuelHandler;
    FluidStack input;

    public IDrawable gear;

    public EngineRecipeWrapper(ILiquidFuel fuelHandler, FluidStack input) {
        this.fuelHandler = fuelHandler;
        this.input = input;
        this.gear = EmbersJEIPlugin.HELPER.getGuiHelper().createDrawable(new ResourceLocation(Embers.MODID, "textures/gui/jei_boiler.png"), 126, 0, 16, 16);
    }

    protected void drawGear(Minecraft minecraft, int gearX, int gearY) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(gearX, gearY, 0);
        GlStateManager.translate(8, 8, 0);
        long l = System.currentTimeMillis() % 360000;
        double speed = fuelHandler.getPower(input);
        double angle = l / (1000 / 20.0) * speed;
        GlStateManager.rotate((float) angle, 0, 0, 1);
        GlStateManager.translate(-8, -8, 0);
        gear.draw(minecraft, 0, 0);
        GlStateManager.popMatrix();
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (mouseX >= GEAR_X && mouseY >= GEAR_Y && mouseX < GEAR_X + 16 && mouseY < GEAR_Y + 16) {
            return getPowerTooltip();
        }
        return null;
    }

    private List<String> getPowerTooltip() {
        List<String> tooltip = new ArrayList<>();
        addInfo(tooltip);
        return tooltip;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        drawGear(minecraft, GEAR_X, GEAR_Y);
    }

    @Override
    public FluidStack getInput() {
        return input;
    }

    @Override
    public FluidStack getOutput() {
        return null;
    }

    @Override
    public void addInfo(List<String> tooltip) {
        fuelHandler.addInfo(input, tooltip);
    }
}
