package teamroots.embers.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.research.ResearchCategory;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.util.EmberGenUtil;
import teamroots.embers.util.Misc;
import teamroots.embers.util.NoiseGenUtil;
import teamroots.embers.util.RenderUtil;

public class GuiCodex extends GuiScreen {
	public double mouseX = 0;
	public double mouseY = 0; 
	public double smoothMouseX = 0;
	public double smoothMouseY = 0; 
	public int selectedIndex = -1;
	public int selectedPageIndex = -1;
	public int categoryIndex = -1;
	public int researchPage = -1;
	
	public float ticks = 1.0f;
	
	public boolean showLeftArrow = false, showRightArrow = false;
	
	public int tooltipX = 0, tooltipY = 0;
	ItemStack tooltipStack = null;
	public boolean renderTooltip = false;
	public int framesExisted = 0;
	public float[] raise = null;
	public float[] raiseTargets = null;
	public String[] sentences = null;
	
	public GuiCodex(){
	}
	
	public void markTooltipForRender(ItemStack stack, int x, int y){
		renderTooltip = true;
		tooltipX = x;
		tooltipY = y;
		tooltipStack = stack;
	}
	
	public void doRenderTooltip(){
		if (renderTooltip){
			this.renderToolTip(tooltipStack, tooltipX, tooltipY);
			renderTooltip = false;
		}
	}
	
	public void renderItemStackAt(ItemStack stack, int x, int y, int mouseX, int mouseY){
		if (!stack.isEmpty()){
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemIntoGUI(stack, x, y);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, x, y, stack.getCount() != 1 ? Integer.toString(stack.getCount()) : "");
			if (mouseX >= x && mouseY >= y && mouseX < x+16 && mouseY < y+16){
				this.markTooltipForRender(stack, mouseX, mouseY);
			}
			RenderHelper.enableStandardItemLighting();
		}
		GlStateManager.disableLighting();
	}
	
	public void renderItemStackMinusTooltipAt(ItemStack stack, int x, int y, int mouseX, int mouseY){
		if (!stack.isEmpty()){
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemIntoGUI(stack, x, y);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, x, y, stack.getCount() != 1 ? Integer.toString(stack.getCount()) : "");
			RenderHelper.enableStandardItemLighting();
		}
		GlStateManager.disableLighting();
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException{
		if (keyCode == Keyboard.KEY_ESCAPE){
			if (categoryIndex != -1){
				if (researchPage != -1){
					researchPage = -1;
					return;
				}
				categoryIndex = -1;
				return;
			}
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton){
		if (selectedIndex != -1 && this.categoryIndex == -1){
			this.categoryIndex = selectedIndex;
		}
		if (selectedPageIndex != -1 && this.researchPage == -1){
			this.researchPage = selectedPageIndex;
		}
	}
	
	public static void drawText(FontRenderer font, String s, int x, int y, int color){
		RenderUtil.drawTextRGBA(font, s, x-1, y, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x+1, y, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x, y-1, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x, y+1, 0, 0, 0, 64);
		font.drawString(s, x, y, color);
	}
	
	public static void drawTextLessShadow(FontRenderer font, String s, int x, int y, int color){
		RenderUtil.drawTextRGBA(font, s, x-1, y, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x+1, y, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x, y-1, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x, y+1, 0, 0, 0, 64);
		font.drawString(s, x, y, color);
	}
	
	public static void drawTextGlowing(FontRenderer font, String s, int x, int y){
		float sine = 0.5f*((float)Math.sin(Math.toRadians(4.0f*((float)EventManager.ticks + Minecraft.getMinecraft().getRenderPartialTicks())))+1.0f);
		RenderUtil.drawTextRGBA(font, s, x-1, y, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x+1, y, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x, y-1, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x, y+1, 0, 0, 0, 64);
		RenderUtil.drawTextRGBA(font, s, x-2, y, 0, 0, 0, 40);
		RenderUtil.drawTextRGBA(font, s, x+2, y, 0, 0, 0, 40);
		RenderUtil.drawTextRGBA(font, s, x, y-2, 0, 0, 0, 40);
		RenderUtil.drawTextRGBA(font, s, x, y+2, 0, 0, 0, 40);
		RenderUtil.drawTextRGBA(font, s, x-1, y+1, 0, 0, 0, 40);
		RenderUtil.drawTextRGBA(font, s, x+1, y-1, 0, 0, 0, 40);
		RenderUtil.drawTextRGBA(font, s, x-1, y-1, 0, 0, 0, 40);
		RenderUtil.drawTextRGBA(font, s, x+1, y+1, 0, 0, 0, 40);
		font.drawString(s, x, y, Misc.intColor(255, 64+(int)(64*sine), 16));
	}
	
	public static void drawTextGlowingAura(FontRenderer font, String s, int x, int y){
		float sine = 0.5f*((float)Math.sin(Math.toRadians(4.0f*((float)EventManager.ticks + Minecraft.getMinecraft().getRenderPartialTicks())))+1.0f);
		RenderUtil.drawTextRGBA(font, s, x-1, y, 255, 64+(int)(64*sine), 16, 40);
		RenderUtil.drawTextRGBA(font, s, x+1, y, 255, 64+(int)(64*sine), 16, 40);
		RenderUtil.drawTextRGBA(font, s, x, y-1, 255, 64+(int)(64*sine), 16, 40);
		RenderUtil.drawTextRGBA(font, s, x, y+1, 255, 64+(int)(64*sine), 16, 40);
		RenderUtil.drawTextRGBA(font, s, x-2, y, 255, 64+(int)(64*sine), 16, 20);
		RenderUtil.drawTextRGBA(font, s, x+2, y, 255, 64+(int)(64*sine), 16, 20);
		RenderUtil.drawTextRGBA(font, s, x, y-2, 255, 64+(int)(64*sine), 16, 20);
		RenderUtil.drawTextRGBA(font, s, x, y+2, 255, 64+(int)(64*sine), 16, 20);
		RenderUtil.drawTextRGBA(font, s, x-1, y+1, 255, 64+(int)(64*sine), 16, 20);
		RenderUtil.drawTextRGBA(font, s, x+1, y-1, 255, 64+(int)(64*sine), 16, 20);
		RenderUtil.drawTextRGBA(font, s, x-1, y-1, 255, 64+(int)(64*sine), 16, 20);
		RenderUtil.drawTextRGBA(font, s, x+1, y+1, 255, 64+(int)(64*sine), 16, 20);
		font.drawString(s, x, y, Misc.intColor(255, 64+(int)(64*sine), 16));
	}
	
	public static void drawTextGlowingAuraTransparent(FontRenderer font, String s, int x, int y, int r, int g, int b, int a){
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		font.drawString(s, x, y, Misc.intColor(r,g,b) + (a << 24));
		RenderUtil.drawTextRGBA(font, s, x-1, y, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+1, y, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y-1, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y+1, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x-2, y, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+2, y, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y-2, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y+2, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x-1, y+1, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+1, y-1, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x-1, y-1, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+1, y+1, r,g,b, (20*a)/255);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}
	
	public static void drawTextGlowingAuraTransparentIntColor(FontRenderer font, String s, int x, int y, int color, int a){
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		font.drawString(s, x, y, color + (a << 24));
		font.drawString(s, x-1, y, color + (((40*a)/255) << 24));
		font.drawString(s, x+1, y, color + (((40*a)/255) << 24));
		font.drawString(s, x, y-1, color + (((40*a)/255) << 24));
		font.drawString(s, x, y+1, color + (((40*a)/255) << 24));
		font.drawString(s, x-2, y, color + (((20*a)/255) << 24));
		font.drawString(s, x+2, y, color + (((20*a)/255) << 24));
		font.drawString(s, x, y-2, color + (((20*a)/255) << 24));
		font.drawString(s, x, y+2, color + (((20*a)/255) << 24));
		font.drawString(s, x-1, y+1, color + (((20*a)/255) << 24));
		font.drawString(s, x+1, y-1, color + (((20*a)/255) << 24));
		font.drawString(s, x-1, y-1, color + (((20*a)/255) << 24));
		font.drawString(s, x+1, y+1, color + (((20*a)/255) << 24));
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}
	
	public static void drawTextGlowingAuraTransparent(FontRenderer font, String s, int x, int y, int a){
		float sine = 0.5f*((float)Math.sin(Math.toRadians(4.0f*((float)EventManager.ticks + Minecraft.getMinecraft().getRenderPartialTicks())))+1.0f);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		int r = 255;
		int g = 64+(int)(64*sine);
		int b = 16;
		font.drawString(s, x, y, Misc.intColor(r,g,b) + (a << 24));
		RenderUtil.drawTextRGBA(font, s, x-1, y, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+1, y, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y-1, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y+1, r,g,b, (40*a)/255);
		RenderUtil.drawTextRGBA(font, s, x-2, y, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+2, y, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y-2, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x, y+2, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x-1, y+1, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+1, y-1, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x-1, y-1, r,g,b, (20*a)/255);
		RenderUtil.drawTextRGBA(font, s, x+1, y+1, r,g,b, (20*a)/255);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
	}
	
	public static void drawCenteredText(FontRenderer font, String s, int x, int y, int color){
		drawText(font, s,x-font.getStringWidth(s)/2,y, color);
	}
	
	public static void drawCenteredTextGlowing(FontRenderer font, String s, int x, int y){
		drawTextGlowing(font, s,x-font.getStringWidth(s)/2,y);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		boolean doUpdateSynced = ticks > partialTicks;
		ticks = partialTicks;
		int numResearches = ResearchManager.researches.size();
		if (this.raise == null){
			this.raise = new float[numResearches];
			for (int i = 0; i < raise.length; i ++){
				raise[i] = 0f;
			}
		}
		if (this.raiseTargets == null){
			this.raiseTargets = new float[numResearches];
			for (int i = 0; i < raiseTargets.length; i ++){
				raiseTargets[i] = 0f;
			}
		}
		String categoryString = "null";
		
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
		float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
		GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
		
		this.drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		int basePosX = (int)((float)width/2.0f)-96;
		int basePosY = (int)((float)height/2.0f)-128;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		selectedIndex = -1;
		this.selectedPageIndex = -1;
		if (this.categoryIndex == -1){
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("embers:textures/gui/codex_index.png"));
			GlStateManager.color(1, 1, 1, 1);
			
			this.drawTexturedModalRect(basePosX, basePosY, 0, 0, 192, 256);
			
			GlStateManager.enableBlend();
			GlStateManager.disableLighting();
			GlStateManager.enableAlpha();
			
			/*for (int i = 0; i < sentences.length; i ++){
				this.drawCenteredTextGlowing(Minecraft.getMinecraft().standardGalacticFontRenderer,sentences[i],basePosX+96, basePosY+22+i*12);
			}*/
			GlStateManager.color(1, 1, 1, 1);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("embers:textures/gui/codex_parts.png"));
			this.drawTexturedModalRect(basePosX-16, basePosY-16, 0, 0, 48, 48);
			this.drawTexturedModalRect(basePosX+160, basePosY-16, 48, 0, 48, 48);
			this.drawTexturedModalRect(basePosX+160, basePosY+224, 96, 0, 48, 48);
			this.drawTexturedModalRect(basePosX-16, basePosY+224, 144, 0, 48, 48);
			this.drawTexturedModalRect(basePosX+72, basePosY-16, 0, 48, 48, 48);
			this.drawTexturedModalRect(basePosX+72, basePosY+224, 0, 48, 48, 48);
			this.drawTexturedModalRect(basePosX-16, basePosY+64, 0, 48, 48, 48);
			this.drawTexturedModalRect(basePosX+160, basePosY+64, 0, 48, 48, 48);

			for (float i = 0; i < numResearches; i ++){
				float mouseDir = (float)Math.toDegrees(Math.atan2(mouseY-(basePosY+88), mouseX-(basePosX+96)))+90f;
				float distSq = (mouseX - (basePosX+96))*(mouseX - (basePosX+96)) + (mouseY - (basePosY+96))*(mouseY - (basePosY+96));
				float angle = i * (360.0f/(float) numResearches);
				boolean selected = false;
				float diff = Math.min(Math.min(Math.abs(mouseDir - angle),Math.abs((mouseDir-360f) - angle)), (Math.abs(mouseDir+360f) - angle));
				ResearchCategory category = ResearchManager.researches.get((int) i);
				Minecraft.getMinecraft().getTextureManager().bindTexture(category.getIndexTexture());
				if (diff < 180.0f/(float) numResearches && distSq < 16000){
					selected = true;
					selectedIndex = (int)i;
					categoryString = category.name;
					if (raise[(int)i] < 1.0f && doUpdateSynced){
						raise[(int)i] = raiseTargets[(int)i];
						raiseTargets[(int)i] = raiseTargets[(int)i] * 0.5f + 0.5f;
					}
				}
				else {
					if (/*raise[(int)i] > 0.0f && */doUpdateSynced){
						raise[(int)i] = raiseTargets[(int)i];
						raiseTargets[(int)i] = raiseTargets[(int)i] * 0.5f;
					}
				}
				float instRaise = raise[(int)i] * (1.0f-partialTicks) + raiseTargets[(int)i] * (partialTicks);
				GlStateManager.pushMatrix();
				GlStateManager.translate(basePosX+96, basePosY+88, 0);
				GlStateManager.rotate(angle, 0, 0, 1);
				this.drawTexturedModalRect(-16, -88-12f*instRaise, 192, 112, 32, 64);
				this.drawTexturedModalRect(-6, -80-12f*instRaise, (int) category.getIconU()+(selected ? 16 : 0), (int) category.getIconV(), 12, 12);
				GlStateManager.popMatrix();
			}
			
			this.drawTexturedModalRect(basePosX+64, basePosY+56, 192, 176, 64, 64);
			
			drawCenteredTextGlowing(this.fontRenderer, I18n.format("embers.research."+categoryString), basePosX+96, basePosY+207);
		}
		else {
			if (this.researchPage == -1){
				
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				GlStateManager.enableAlpha();
				basePosX = (int)((float)width/2.0f)-192;
				basePosY = (int)((float)height/2.0f)-136;
				int basePosY2 = Math.min(height-33, basePosY+272);
				GlStateManager.color(1, 1, 1, 1);
				
				ResearchCategory category = ResearchManager.researches.get(categoryIndex);
				Minecraft.getMinecraft().getTextureManager().bindTexture(category.getBackgroundTexture());
				
				RenderUtil.drawTexturedModalRect(basePosX, basePosY, zLevel, 0f/256f, 0f/256f, 192f/256f, 272f/512f, 384, 272);
				for (int i = 0; i < category.researches.size(); i ++){
					ResearchBase r = category.researches.get(i);
					if (mouseX >= basePosX+r.x-24 && mouseY >= basePosY+r.y-24 && mouseX <= basePosX+r.x+24 && mouseY <= basePosY+r.y+24){
						this.selectedPageIndex = i;
						if (r.selectedAmount < 1.0f && doUpdateSynced){
							r.selectedAmount = r.selectionTarget;
							r.selectionTarget = r.selectionTarget*(1.0f-partialTicks) + (r.selectionTarget * 0.8f + 0.2f) *partialTicks;
						}
					}
					else if (r.selectedAmount > 0.0f && doUpdateSynced){
						r.selectedAmount = r.selectionTarget;
						r.selectionTarget = r.selectionTarget*(1.0f-partialTicks) + (r.selectionTarget * 0.9f) *partialTicks;
					}
					if (r.selectedAmount > 0.1f){
						Tessellator tess = Tessellator.getInstance();
						BufferBuilder b = tess.getBuffer();
						float x = r.x;
						float y = r.y;
						GlStateManager.color(1, 1, 1, 1);
						GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
						GlStateManager.shadeModel(GL11.GL_SMOOTH);
						GlStateManager.disableTexture2D();
						float amt = r.selectedAmount;
						for (float j = 0; j < 8; j ++){
							float coeff = (j+1.0f) /8.0f;
							b.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
							RenderUtil.renderHighlightCircle(b,basePosX+x,basePosY+y,(25.0f+20.0f*coeff*coeff)* amt);
							tess.draw();
						}
						GlStateManager.shadeModel(GL11.GL_FLAT);
						GlStateManager.enableTexture2D();
						GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
					}
					if (r.ancestors.size() > 0){
						for (int l = 0; l < r.ancestors.size(); l ++){
							Tessellator tess = Tessellator.getInstance();
							BufferBuilder b = tess.getBuffer();
							float x1 = r.x;
							float y1 = r.y;
							float x2 = r.ancestors.get(l).x;
							float y2 = r.ancestors.get(l).y;
							float dx = Math.abs(x1-x2);
							float dy = Math.abs(y1-y2);
							GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
							GlStateManager.shadeModel(GL11.GL_SMOOTH);
							GlStateManager.disableTexture2D();
							for (float j = 0; j < 8; j ++){
								float coeff = (float)Math.pow((j+1.0f)/8.0f,1.5f);
								b.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
								RenderUtil.renderWavyEmberLine(b, basePosX+x1, basePosY+y1, basePosX+x2, basePosY+y2, 4.0f*coeff);
								tess.draw();
							}
							GlStateManager.shadeModel(GL11.GL_FLAT);
							GlStateManager.enableTexture2D();
							GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
						}
					}
				}
				for (int i = 0; i < category.researches.size(); i ++){
					ResearchBase r = category.researches.get(i);
					Minecraft.getMinecraft().getTextureManager().bindTexture(r.getIconBackground());
					double u = r.getIconBackgroundU();
					double v = r.getIconBackgroundV();
					RenderUtil.drawTexturedModalRect(basePosX+r.x-24, basePosY+r.y-24, zLevel, u, v, u + 24f/256f, v + 24f/256f, 48, 48);
					this.renderItemStackMinusTooltipAt(r.icon, basePosX+r.x-8, basePosY+r.y-8, mouseX, mouseY);
				}
				Minecraft.getMinecraft().getTextureManager().bindTexture(category.getBackgroundTexture());
				RenderUtil.drawTexturedModalRect(basePosX, basePosY2, zLevel, 0f/256f, 272f/512f, 192f/256f,305f/512f, 384, 33);
				for (int i = 0; i < category.researches.size(); i ++){
					if (i == this.selectedPageIndex){
						drawCenteredTextGlowing(this.fontRenderer, I18n.format("embers.research."+category.name+"."+category.researches.get(i).name), basePosX+192, basePosY2+13);
						GlStateManager.color(1f, 1f, 1f, 1f);
					}
				}
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				GlStateManager.enableAlpha();
			}
			else {
				ResearchCategory c = ResearchManager.researches.get(this.categoryIndex);
				ResearchBase r = c.researches.get(this.researchPage);

				Minecraft.getMinecraft().getTextureManager().bindTexture(r.getBackground());
				GlStateManager.color(1, 1, 1, 1);
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				GlStateManager.enableAlpha();
				this.drawTexturedModalRect(basePosX, basePosY, 0, 0, 192, 256);

				drawCenteredTextGlowing(this.fontRenderer, I18n.format("embers.research."+c.name+"."+r.name+".title"), basePosX+96, basePosY+19);
				GlStateManager.color(1f, 1f, 1f, 1f);
				List<String> strings = r.getLines(I18n.format("embers.research."+c.name+"."+r.name+".desc"), 152);
				for (int i = 0; i < strings.size(); i ++){
					drawTextGlowing(this.fontRenderer, strings.get(i), basePosX+20, basePosY+43+i*12);
				}
			}
		}
		
		GlStateManager.disableAlpha();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1f, 1f, 1f, 1f);
		
		doRenderTooltip();
		GlStateManager.color(1f, 1f, 1f, 1f);
		
	    GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.disableAlpha();
		GlStateManager.alphaFunc(func, ref);
	}
	
	public void renderAura(float x, float y){
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder b = tess.getBuffer();
		b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		for (float i = -80; i < 80; i += 10f){
			RenderUtil.drawQuadGuiExt(b, x-10, y-10, x+10, y-10, x+10, y+10, x-10, y+10, 0, 0, 1, 1, 1, 1, 0.25f, 0.25f, 0.25f, 255f);
		}
		tess.draw();
	}
	
	public float getVert(float i, float f1, float f2){
		float coeff = Math.abs(i) + EventManager.tickCounter + Minecraft.getMinecraft().getRenderPartialTicks();
		float vert = Math.abs(10.0f*((1.0f-(Math.abs(i/80.0f))) * (float)(Math.sin(coeff*f1) + 0.4f*Math.sin(coeff*f2))));
		return vert;
	}
	
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
		for (ResearchCategory category : ResearchManager.researches){
			for (ResearchBase base : category.researches){
				base.selectedAmount = 0.0f;
			}
		}
	}
}
