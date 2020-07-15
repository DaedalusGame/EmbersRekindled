package teamroots.embers.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.misc.IMetalCoefficient;
import teamroots.embers.util.Misc;

import java.awt.*;
import java.util.function.Predicate;

@ZenRegister
@ZenClass(EmberGeneration.CLASS) //I'm mad that i gave this a terrible name. should've been ember utils.
public class EmberGeneration {
    public static final String NAME = "EmberGeneration";
    public static final String CLASS = "mods.embers.EmberGeneration";

    @ZenMethod
    public static void addEmberFuel(IIngredient item, double ember) {
        CraftTweakerAPI.apply(new AddEmberFuel(item, ember));
    }

    @ZenMethod
    public static void removeEmberFuel(IItemStack item) {
        CraftTweakerAPI.apply(new RemoveEmberFuel(item));
    }

    @ZenMethod
    public static void addCatalysisFuel(IIngredient item, double coefficient) {
        CraftTweakerAPI.apply(new AddCatalysisFuel(item, coefficient));
    }

    @ZenMethod
    public static void removeCatalysisFuel(IItemStack item) {
        CraftTweakerAPI.apply(new RemoveCatalysisFuel(item));
    }

    @ZenMethod
    public static void addCombustionFuel(IIngredient item, double coefficient) {
        CraftTweakerAPI.apply(new AddCombustionFuel(item, coefficient));
    }

    @ZenMethod
    public static void removeCombustionFuel(IItemStack item) {
        CraftTweakerAPI.apply(new RemoveCombustionFuel(item));
    }

    @ZenMethod
    public static void addMetalCoefficient(IIngredient block, double coefficient) {
        CraftTweakerAPI.apply(new AddMetalCoefficient(block.toString(), (state) -> block.matches(CraftTweakerMC.getIItemStack(Misc.getStackFromState(state))), coefficient));
    }

    @ZenMethod
    public static void addBoilerFluid(ILiquidStack liquid, ILiquidStack gas, double multiplier, int[] color) {
        CraftTweakerAPI.apply(new AddBoilerFluid(liquid, gas, multiplier, CTUtil.parseColor(color)));
    }

    @ZenMethod
    public static void removeBoilerFluid(ILiquidStack liquid) {
        CraftTweakerAPI.apply(new RemoveBoilerFluid(liquid));
    }

    @ZenMethod
    public static void addSteamEngineFuel(ILiquidStack liquid, double multiplier, int time, int[] color) {
        CraftTweakerAPI.apply(new AddSteamEngineFuel(liquid, multiplier, time, CTUtil.parseColor(color)));
    }

    @ZenMethod
    public static void removeSteamEngineFuel(ILiquidStack liquid) {
        CraftTweakerAPI.apply(new RemoveSteamEngineFuel(liquid));
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

    public static class RemoveEmberFuel implements IAction
    {
        IItemStack item;

        public RemoveEmberFuel(IItemStack item) {
            this.item = item;
        }

        @Override
        public void apply() {
            EmbersAPI.unregisterEmberFuel(EmbersAPI.getEmberFuel(CraftTweakerMC.getItemStack(item)));
        }

        @Override
        public String describe() {
            return "Removing ember fuel "+item.toString();
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

    public static class RemoveCatalysisFuel implements IAction
    {
        IItemStack item;

        public RemoveCatalysisFuel(IItemStack item) {
            this.item = item;
        }

        @Override
        public void apply() {
            EmbersAPI.unregisterCatalysisFuel(EmbersAPI.getCatalysisFuel(CraftTweakerMC.getItemStack(item)));
        }

        @Override
        public String describe() {
            return "Removing catalysis fuel "+item.toString();
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

    public static class RemoveCombustionFuel implements IAction
    {
        IItemStack item;

        public RemoveCombustionFuel(IItemStack item) {
            this.item = item;
        }

        @Override
        public void apply() {
            EmbersAPI.unregisterCombustionFuel(EmbersAPI.getCombustionFuel(CraftTweakerMC.getItemStack(item)));
        }

        @Override
        public String describe() {
            return "Removing combustion fuel "+item.toString();
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

    public static class AddBoilerFluid implements IAction
    {
        double multiplier;
        ILiquidStack liquid;
        ILiquidStack gas;
        Color color;

        public AddBoilerFluid(ILiquidStack liquid, ILiquidStack gas, double multiplier, Color color) {
            this.liquid = liquid;
            this.gas = gas;
            this.multiplier = multiplier;
            this.color = color;
        }

        @Override
        public void apply() {
            EmbersAPI.registerBoilerFluid(CraftTweakerMC.getLiquidStack(liquid).getFluid(),CraftTweakerMC.getLiquidStack(gas).getFluid(),multiplier,color);
        }

        @Override
        public String describe() {
            return "Adding boiler fluid "+liquid.toString()+" -> "+gas.toString()+": "+multiplier;
        }
    }

    public static class RemoveBoilerFluid implements IAction
    {
        ILiquidStack liquid;

        public RemoveBoilerFluid(ILiquidStack liquid) {
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            EmbersAPI.unregisterBoilerFluid(EmbersAPI.getBoilerFluid(CraftTweakerMC.getLiquidStack(liquid)));
        }

        @Override
        public String describe() {
            return "Removing boiler fuel "+liquid.toString();
        }
    }

    public static class AddSteamEngineFuel implements IAction
    {
        double power;
        int time;
        ILiquidStack liquid;
        Color color;

        public AddSteamEngineFuel(ILiquidStack liquid, double power, int time, Color color) {
            this.liquid = liquid;
            this.power = power;
            this.time = time;
            this.color = color;
        }

        @Override
        public void apply() {
            EmbersAPI.registerSteamEngineFuel(CraftTweakerMC.getLiquidStack(liquid).getFluid(),power,time,color);
        }

        @Override
        public String describe() {
            return "Adding steam engine fuel "+liquid.toString()+" -> "+power+" for "+time+"ticks";
        }
    }

    public static class RemoveSteamEngineFuel implements IAction
    {
        ILiquidStack liquid;

        public RemoveSteamEngineFuel(ILiquidStack liquid) {
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            EmbersAPI.unregisterSteamEngineFuel(EmbersAPI.getSteamEngineFuel(CraftTweakerMC.getLiquidStack(liquid)));
        }

        @Override
        public String describe() {
            return "Removing steam engine fuel "+liquid.toString();
        }
    }
}
