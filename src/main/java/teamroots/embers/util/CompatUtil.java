package teamroots.embers.util;

import net.minecraftforge.fml.common.Loader;
import teamroots.embers.config.ConfigMain;

public class CompatUtil {
    public static boolean isBaublesIntegrationEnabled() {
        return ConfigMain.COMPAT_CATEGORY.enableBaublesIntegration && Loader.isModLoaded("baubles") || ConfigMain.COMPAT_CATEGORY.forceBaublesIntegration;
    }

    public static boolean isMysticalMechanicsIntegrationEnabled() {
        return ConfigMain.COMPAT_CATEGORY.enableMysticalMechanicsIntegration && Loader.isModLoaded("mysticalmechanics") || ConfigMain.COMPAT_CATEGORY.forceMysticalMechanicsIntegration;
    }
}
