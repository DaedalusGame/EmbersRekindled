package teamroots.embers.util;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import teamroots.embers.api.alchemy.AspectList.AspectRangeList;
import teamroots.embers.config.ConfigMain;

import java.util.ArrayList;

public class AspectRenderUtil {
    private final IGuiHelper helper;
    private final int aspectbarsX;
    private final int aspectbarsY;
    private final int aspectbars;
    private int u;
    private int v;
    private int width;
    private int height;
    private final int spacing = 4;
    private final ResourceLocation resourceLocation;

    public AspectRenderUtil(IGuiHelper helper, int aspectbars, int aspectbarsX, int aspectbarsY, int u, int v, int width, int height, ResourceLocation resourceLocation) {
        this.helper = helper;
        this.aspectbars = aspectbars;
        this.aspectbarsX = aspectbarsX;
        this.aspectbarsY = aspectbarsY;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.resourceLocation = resourceLocation;
    }

    public void addAspectStacks(IHasAspects hasAspects, IGuiItemStackGroup stacks, int id) {
        //Aspects
        ArrayList<String> aspects = new ArrayList<>(hasAspects.getAspects().getMaxAspects().getAspects());
        for(int i = 0; i < Math.min(aspects.size(),aspectbars); i++)
        {
            String aspect = aspects.get(i);
            stacks.init(id+i, false, -4, aspectbarsY +height/2-8+(height+spacing)*i);
            stacks.set(id+i, AlchemyUtil.getAspectStacks(aspect));
        }
    }

    public void drawAspectBars(Minecraft minecraft, IHasAspects hasAspects) {
        AspectRangeList aspectRange = hasAspects.getAspects();
        ArrayList<String> aspects = new ArrayList<>(aspectRange.getMaxAspects().getAspects());
        for(int i = 0; i < Math.min(aspects.size(),aspectbars); i++) {
            String aspect = aspects.get(i);
            drawAspectBar(minecraft, aspectRange, aspectbarsX, aspectbarsY +(height+spacing)*i, aspect);
        }
    }

    public void drawAspectBar(Minecraft minecraft, AspectRangeList aspectRange, int x, int y, String aspect) {
        int max = aspectRange.getMax(aspect);
        u = 128;
        v = 0;
        width = 54;
        height = 7;
        IDrawable background = helper.createDrawable(resourceLocation, u, v + height*2, width, height);
        background.draw(minecraft,x,y);
        if (max > 0){
            int min = aspectRange.getMin(aspect);
            int aspectTotal = aspectRange.getMaxAspects().getTotal();
            int exact = aspectRange.getExact(aspect, Minecraft.getMinecraft().world);
            String cheatsheet = ConfigMain.COMPAT_CATEGORY.enableJeiCheat ? String.format(" §e(%d)§r", exact) : "";
            IDrawable ashBar = helper.createDrawable(resourceLocation, u, v, ((width * min)/aspectTotal), height);
            IDrawable ashPartialBar = helper.createDrawable(resourceLocation, u, v + height, ((width * max)/aspectTotal), height);
            ashPartialBar.draw(minecraft, x, y);
            ashBar.draw(minecraft, x, y);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(min+"-"+max+cheatsheet, x + width + 3, y-1, 0xFFFFFF);
        }
    }
}

