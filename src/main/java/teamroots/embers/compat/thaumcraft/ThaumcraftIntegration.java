package teamroots.embers.compat.thaumcraft;

import net.minecraft.item.ItemStack;
import teamroots.embers.RegistryManager;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ThaumcraftIntegration {
    public static void init() {
        ThaumcraftApi.registerObjectTag(new ItemStack(RegistryManager.ingot_aluminum),new AspectList().add(Aspect.METAL,10).add(Aspect.AIR,5));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegistryManager.ingot_electrum),new AspectList().add(Aspect.METAL,10).add(Aspect.ENERGY,5).add(Aspect.DESIRE,5));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegistryManager.ingot_nickel),new AspectList().add(Aspect.METAL,10).add(Aspect.CRAFT,5));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegistryManager.ingot_dawnstone),new AspectList().add(Aspect.METAL,10));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegistryManager.ore_aluminum),new AspectList().add(Aspect.METAL,10).add(Aspect.AIR,5).add(Aspect.EARTH,5));
        ThaumcraftApi.registerObjectTag(new ItemStack(RegistryManager.ore_nickel),new AspectList().add(Aspect.METAL,10).add(Aspect.CRAFT,5).add(Aspect.EARTH,5));
    }
}
