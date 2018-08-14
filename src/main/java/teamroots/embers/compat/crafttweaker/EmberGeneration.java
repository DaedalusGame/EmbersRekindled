package teamroots.embers.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.misc.IMetalCoefficient;
import teamroots.embers.util.Misc;

import java.util.function.Predicate;

@ZenRegister
@ZenClass(EmberGeneration.CLASS)
public class EmberGeneration {
    public static final String NAME = "EmberGeneration";
    public static final String CLASS = "mods.embers.EmberGeneration";

    @ZenMethod
    public static void addEmberFuel(IIngredient item, double ember) {
        CraftTweakerAPI.apply(new AddEmberFuel(item, ember));
    }

    @ZenMethod
    public static void addCatalysisFuel(IIngredient item, double coefficient) {
        CraftTweakerAPI.apply(new AddCatalysisFuel(item, coefficient));
    }

    @ZenMethod
    public static void addCombustionFuel(IIngredient item, double coefficient) {
        CraftTweakerAPI.apply(new AddCombustionFuel(item, coefficient));
    }

    @ZenMethod
    public static void addMetalCoefficient(IIngredient block, double coefficient) {
        CraftTweakerAPI.apply(new AddMetalCoefficient(block.toString(), (state) -> block.matches(CraftTweakerMC.getIItemStack(Misc.getStackFromState(state))), coefficient));
    }

    public static class AddEmberFuel implements IAction
    {
        double ember;
        IIngredient item;

        public AddEmberFuel(IIngredient item, double ember) {
            this.ember = ember;
            this.item = item;
        }

        @Override
        public void apply() {
            EmbersAPI.registerEmberFuel(CTUtil.toIngredient(item),ember);
        }

        @Override
        public String describe() {
            return "Adding ember fuel "+item.toString()+" with value "+ember;
        }
    }

    public static class AddCatalysisFuel implements IAction
    {
        double coefficient;
        IIngredient item;

        public AddCatalysisFuel(IIngredient item, double coefficient) {
            this.item = item;
            this.coefficient = coefficient;
        }

        @Override
        public void apply() {
            EmbersAPI.registerCatalysisFuel(CTUtil.toIngredient(item),coefficient);
        }

        @Override
        public String describe() {
            return "Adding catalysis fuel "+item.toString()+" with coefficient "+coefficient;
        }
    }

    public static class AddCombustionFuel implements IAction
    {
        double coefficient;
        IIngredient item;

        public AddCombustionFuel(IIngredient item, double coefficient) {
            this.item = item;
            this.coefficient = coefficient;
        }

        @Override
        public void apply() {
            EmbersAPI.registerCombustionFuel(CTUtil.toIngredient(item),coefficient);
        }

        @Override
        public String describe() {
            return "Adding combustion fuel "+item.toString()+" with coefficient "+coefficient;
        }
    }

    public static class AddMetalCoefficient implements IAction
    {
        String matchDesc;
        Predicate<IBlockState> match;
        double coefficient;

        public AddMetalCoefficient(String matchDesc, Predicate<IBlockState> match, double coefficient) {
            this.matchDesc = matchDesc;
            this.match = match;
            this.coefficient = coefficient;
        }

        @Override
        public void apply() {
            EmbersAPI.registerMetalCoefficient(new IMetalCoefficient() {
                @Override
                public boolean matches(IBlockState state) {
                    return match.test(state);
                }

                @Override
                public double getCoefficient(IBlockState state) {
                    return coefficient;
                }
            });
        }

        @Override
        public String describe() {
            return "Adding metal coefficient "+coefficient+" for "+matchDesc;
        }
    }
}
