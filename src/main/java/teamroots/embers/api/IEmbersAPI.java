package teamroots.embers.api;

import net.minecraft.item.Item;
import teamroots.embers.api.itemmod.ModifierBase;

public interface IEmbersAPI {
    void registerModifier(ModifierBase modifier, Item item);
}
