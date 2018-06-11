package teamroots.embers.api;

import net.minecraft.item.Item;
import teamroots.embers.api.itemmod.ModifierBase;

public class EmbersAPI {
    public static IEmbersAPI IMPL;

    public static ModifierBase CORE;
    public static ModifierBase SUPERHEATER;
    public static ModifierBase JET_AUGMENT;
    public static ModifierBase CASTER_ORB;
    public static ModifierBase RESONATING_BELL;
    public static ModifierBase BLASTING_CORE;
    public static ModifierBase FLAME_BARRIER;
    public static ModifierBase ELDRITCH_INSIGNIA;
    public static ModifierBase INTELLIGENT_APPARATUS;

    public static void registerModifier(ModifierBase modifier, Item item) {
        IMPL.registerModifier(modifier,item);
    }
}
