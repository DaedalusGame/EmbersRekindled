package teamroots.embers.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.api.misc.ICoefficientFuel;
import teamroots.embers.api.misc.IFuel;
import teamroots.embers.api.misc.IMetalCoefficient;

public interface IEmbersAPI {
    void registerModifier(Item item, ModifierBase modifier);

    void registerAlchemyAspect(Ingredient ingredient, String aspect);

    void registerEmberFuel(Ingredient ingredient, double ember);

    void registerEmberFuel(IFuel fuel);

    double getEmberValue(ItemStack stack);

    void registerCatalysisFuel(Ingredient ingredient, double coefficient);

    void registerCatalysisFuel(ICoefficientFuel fuel);

    ICoefficientFuel getCatalysisFuel(ItemStack stack);

    void registerCombustionFuel(Ingredient ingredient, double coefficient);

    void registerCombustionFuel(ICoefficientFuel fuel);

    ICoefficientFuel getCombustionFuel(ItemStack stack);

    void registerMetalCoefficient(String oredict, double coefficient);

    void registerMetalCoefficient(IMetalCoefficient coefficient);

    double getMetalCoefficient(IBlockState state);

    double getEmberTotal(EntityPlayer player);

    double getEmberCapacityTotal(EntityPlayer player);

    void removeEmber(EntityPlayer player, double amount);
}
