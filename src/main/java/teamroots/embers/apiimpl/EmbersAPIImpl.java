package teamroots.embers.apiimpl;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.IEmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.api.misc.ICoefficientFuel;
import teamroots.embers.api.misc.IFuel;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.api.misc.IMetalCoefficient;
import teamroots.embers.api.upgrades.UpgradeUtil;
import teamroots.embers.tileentity.TileEntityCatalyzer;
import teamroots.embers.tileentity.TileEntityCombustor;
import teamroots.embers.util.*;

import java.util.ArrayList;

public class EmbersAPIImpl implements IEmbersAPI {
    //TODO: Move to more suitable spot? Directly into the API package?
    public static ArrayList<IFuel> emberFuels = new ArrayList<>();
    public static ArrayList<ICoefficientFuel> catalysisFuels = new ArrayList<>();
    public static ArrayList<ICoefficientFuel> combustionFuels = new ArrayList<>();
    public static ArrayList<IMetalCoefficient> metalCoefficients = new ArrayList<>();
    public static ArrayList<ILiquidFuel> boilerLiquids = new ArrayList<>();
    public static ArrayList<ILiquidFuel> steamEngineFuels = new ArrayList<>();

    public static void init() {
        EmbersAPI.IMPL = new EmbersAPIImpl();
        ItemModUtil.IMPL = new ItemModUtilImpl();
        UpgradeUtil.IMPL = new UpgradeUtilImpl();
    }

    @Override
    public void registerModifier(Item item, ModifierBase modifier) {
        teamroots.embers.util.ItemModUtil.registerModifier(item, modifier);
    }

    @Override
    public void registerAlchemyAspect(Ingredient ingredient, String aspect) {
        AlchemyUtil.registerAspect(aspect,ingredient);
    }

    @Override
    public void registerEmberFuel(Ingredient ingredient, double ember) {
        registerEmberFuel(new IFuel() { //TODO: move to actual class in apiimpl
            @Override
            public boolean matches(ItemStack stack) {
                return ingredient.apply(stack);
            }

            @Override
            public double getFuelValue(ItemStack stack) {
                return ember;
            }
        });
    }

    @Override
    public void registerEmberFuel(IFuel fuel) {
        emberFuels.add(fuel);
    }

    @Override
    public double getEmberValue(ItemStack stack) {
        for(IFuel fuel : emberFuels)
            if(fuel.matches(stack))
                return fuel.getFuelValue(stack);
        return 0;
    }

    @Override
    public void registerCatalysisFuel(Ingredient ingredient, double coefficient) {
        registerCatalysisFuel(new ICoefficientFuel() {
            @Override
            public boolean matches(ItemStack stack) {
                return ingredient.apply(stack);
            }

            @Override
            public double getCoefficient(ItemStack stack) {
                return coefficient;
            }

            @Override
            public int getDuration(ItemStack stack) {
                return TileEntityCatalyzer.PROCESS_TIME;
            }
        });
    }

    @Override
    public void registerCatalysisFuel(ICoefficientFuel fuel) {
        catalysisFuels.add(fuel);
    }

    @Override
    public ICoefficientFuel getCatalysisFuel(ItemStack stack) {
        for(ICoefficientFuel fuel : catalysisFuels)
            if(fuel.matches(stack))
                return fuel;
        return null;
    }

    @Override
    public void registerCombustionFuel(Ingredient ingredient, double coefficient) {
        registerCombustionFuel(new ICoefficientFuel() {
            @Override
            public boolean matches(ItemStack stack) {
                return ingredient.apply(stack);
            }

            @Override
            public double getCoefficient(ItemStack stack) {
                return coefficient;
            }

            @Override
            public int getDuration(ItemStack stack) {
                return TileEntityCombustor.PROCESS_TIME;
            }
        });
    }

    @Override
    public void registerCombustionFuel(ICoefficientFuel fuel) {
        combustionFuels.add(fuel);
    }

    @Override
    public ICoefficientFuel getCombustionFuel(ItemStack stack) {
        for(ICoefficientFuel fuel : combustionFuels)
            if(fuel.matches(stack))
                return fuel;
        return null;
    }

    @Override
    public void registerMetalCoefficient(String oredict, double coefficient) {
        registerMetalCoefficient(new IMetalCoefficient() {
            @Override
            public boolean matches(IBlockState state) {
                return ItemUtil.matchesOreDict(Misc.getStackFromState(state), oredict);
            }

            @Override
            public double getCoefficient(IBlockState state) {
                return coefficient;
            }
        });
    }

    @Override
    public void registerMetalCoefficient(IMetalCoefficient coefficient) {
        metalCoefficients.add(coefficient);
    }

    @Override
    public double getMetalCoefficient(IBlockState state) {
        for(IMetalCoefficient coefficient : metalCoefficients)
            if(coefficient.matches(state))
                return coefficient.getCoefficient(state);
        return 0;
    }

    @Override
    public void registerBoilerFluid(Fluid fluid, Fluid gas, double multiplier) {
        registerBoilerFluid(new ILiquidFuel() {
            @Override
            public boolean matches(FluidStack stack) {
                return stack != null && FluidUtil.areFluidsEqual(stack.getFluid(),fluid);
            }

            @Override
            public FluidStack getRemainder(FluidStack stack) {
                return new FluidStack(gas, (int) (stack.amount * multiplier));
            }

            @Override
            public double getPower(FluidStack stack) {
                return 0;
            }
        });
    }

    @Override
    public void registerBoilerFluid(ILiquidFuel fuel) {
        boilerLiquids.add(fuel);
    }

    @Override
    public ILiquidFuel getBoilerFluid(FluidStack fluidstack) {
        for(ILiquidFuel fuel : boilerLiquids)
            if(fuel.matches(fluidstack))
                return fuel;
        return null;
    }

    @Override
    public void registerSteamEngineFuel(Fluid fluid, double power) {
        registerSteamEngineFuel(new ILiquidFuel() {
            @Override
            public boolean matches(FluidStack stack) {
                return stack != null && FluidUtil.areFluidsEqual(stack.getFluid(),fluid);
            }

            @Override
            public FluidStack getRemainder(FluidStack stack) {
                return null;
            }

            @Override
            public double getPower(FluidStack stack) {
                return power * stack.amount;
            }
        });
    }

    @Override
    public void registerSteamEngineFuel(ILiquidFuel fuel) {
        steamEngineFuels.add(fuel);
    }

    @Override
    public ILiquidFuel getSteamEngineFuel(FluidStack fluidstack) {
        for(ILiquidFuel fuel : steamEngineFuels)
            if(fuel.matches(fluidstack))
                return fuel;
        return null;
    }

    @Override
    public double getEmberTotal(EntityPlayer player) {
        return EmberInventoryUtil.getEmberTotal(player);
    }

    @Override
    public double getEmberCapacityTotal(EntityPlayer player) {
        return EmberInventoryUtil.getEmberCapacityTotal(player);
    }

    @Override
    public void removeEmber(EntityPlayer player, double amount) {
        EmberInventoryUtil.removeEmber(player, amount);
    }
}
