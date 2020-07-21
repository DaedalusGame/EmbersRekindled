package teamroots.embers.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;
import teamroots.embers.ConfigManager;
import teamroots.embers.Embers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
        // NO-OP
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new GuiConfig(parentScreen);
    }


    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }


    public static class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig {

        public GuiConfig(GuiScreen parentScreen) {
            super(parentScreen, getAllElements(), Embers.MODID, false, false, GuiConfig.getAbridgedConfigPath(ConfigManager.config.toString()));
        }

        public static List<IConfigElement> getAllElements() {
            List<IConfigElement> list = new ArrayList<>();

            Set<String> categories = ConfigManager.config.getCategoryNames();
            list.addAll(categories.stream().filter(s -> !s.contains(".")).map(s -> new DummyConfigElement.DummyCategoryElement(s, s, new ConfigElement(ConfigManager.config.getCategory(s)).getChildElements())).collect(Collectors.toList()));

            return list;
        }
    }

}
