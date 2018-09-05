package teamroots.embers.util;

import com.google.common.collect.Maps;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class DecimalFormats implements IResourceManagerReloadListener {
    private static final Map<String,DecimalFormat> decimalFormats = Maps.newHashMap();

    public static DecimalFormat getDecimalFormat(String key) {
        DecimalFormat format = decimalFormats.get(key);
        if(format == null) {
            if(I18n.hasKey(key))
                format = new DecimalFormat(I18n.format(key));
            else
                format = new DecimalFormat("0.#######");
            decimalFormats.put(key,format);
        }
        return format;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        decimalFormats.clear();
    }
}
