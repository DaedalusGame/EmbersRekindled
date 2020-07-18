package teamroots.embers.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.api.misc.ICoefficientFuel;
import teamroots.embers.api.misc.IFuel;
import teamroots.embers.api.misc.ILiquidFuel;
import teamroots.embers.api.misc.IMetalCoefficient;

import java.awt.*;

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
    public static ModifierBase DIFFRACTION;
    public static ModifierBase FOCAL_LENS;
    public static ModifierBase TINKER_LENS;
    public static ModifierBase ANTI_TINKER_LENS;
    public static ModifierBase SHIFTING_SCALES;
    public static ModifierBase WINDING_GEARS;
    public static ModifierBase CORE_STONE;

    public static void registerModifier(Item item, ModifierBase modifier) {
        IMPL.registerModifier(item, modifier);
    }

    public static void registerAlchemyAspect(Ingredient ingredient, String aspect) {
        IMPL.registerAlchemyAspect(ingredient,aspect);
    }

    public static void registerEmberFuel(Ingredient ingredient, double ember){
        IMPL.registerEmberFuel(ingredient, ember);
    }

    public static void registerEmberFuel(IFuel fuel){
        IMPL.registerEmberFuel(fuel);
    }

    public static void unregisterEmberFuel(IFuel fuel){
        IMPL.unregisterEmberFuel(fuel);
    }

    public static IFuel getEmberFuel(ItemStack stack) {
        return IMPL.getEmberFuel(stack);
    }

    public static double getEmberValue(ItemStack stack) {
        return IMPL.getEmberValue(stack);
    }

    public static void registerCatalysisFuel(Ingredient ingredient, double coefficient){
        IMPL.registerCatalysisFuel(ingredient, coefficient);
    }

    public static void registerCatalysisFuel(ICoefficientFuel fuel){
        IMPL.registerCatalysisFuel(fuel);
    }

    public static void unregisterCatalysisFuel(ICoefficientFuel fuel){
        IMPL.unregisterCatalysisFuel(fuel);
    }

    public static ICoefficientFuel getCatalysisFuel(ItemStack stack) {
        return IMPL.getCatalysisFuel(stack);
    }

    public static void registerCombustionFuel(Ingredient ingredient, double coefficient){
        IMPL.registerCombustionFuel(ingredient, coefficient);
    }

    public static void registerCombustionFuel(ICoefficientFuel fuel){
        IMPL.registerCombustionFuel(fuel);
    }

    public static void unregisterCombustionFuel(ICoefficientFuel fuel){
        IMPL.unregisterCombustionFuel(fuel);
    }

    public static ICoefficientFuel getCombustionFuel(ItemStack stack) {
        return IMPL.getCombustionFuel(stack);
    }

    public static void registerMetalCoefficient(String oredict, double coefficient){
        IMPL.registerMetalCoefficient(oredict, coefficient);
    }

    public static void registerMetalCoefficient(IMetalCoefficient coefficient){
        IMPL.registerMetalCoefficient(coefficient);
    }

    public static void unregisterMetalCoefficient(IMetalCoefficient coefficient){
        IMPL.unregisterMetalCoefficient(coefficient);
    }

    public static IMetalCoefficient getMetalCoefficient(IBlockState state) {
        return IMPL.getMetalCoefficient(state);
    }

    public static void registerBoilerFluid(Fluid fluid, Fluid gas, double multiplier, Color color) {
        IMPL.registerBoilerFluid(fluid, gas, multiplier, color);
    }

    public static void registerBoilerFluid(ILiquidFuel fuel) {
        IMPL.registerBoilerFluid(fuel);
    }

    public static void unregisterBoilerFluid(ILiquidFuel fuel) {
        IMPL.unregisterBoilerFluid(fuel);
    }

    public static ILiquidFuel getBoilerFluid(FluidStack fluidstack) {
        return IMPL.getBoilerFluid(fluidstack);
    }

    public static void registerSteamEngineFuel(Fluid fluid, double power, int time, Color color) {
        IMPL.registerSteamEngineFuel(fluid, power, time, color);
    }

    public static void registerSteamEngineFuel(ILiquidFuel fuel) {
        IMPL.registerSteamEngineFuel(fuel);
    }

    public static void unregisterSteamEngineFuel(ILiquidFuel fuel) {
        IMPL.unregisterSteamEngineFuel(fuel);
    }

    public static ILiquidFuel getSteamEngineFuel(FluidStack fluidstack) {
        return IMPL.getSteamEngineFuel(fluidstack);
    }

    public static double getEmberTotal(EntityPlayer player){
        return IMPL.getEmberTotal(player);
    }

    public static double getEmberCapacityTotal(EntityPlayer player){
        return IMPL.getEmberCapacityTotal(player);
    }

    public static void removeEmber(EntityPlayer player, double amount){
        IMPL.removeEmber(player, amount);
    }

    public static double getScales(EntityLivingBase entity) {
        return IMPL.getScales(entity);
    }

    public static void setScales(EntityLivingBase entity, double scales) {
        IMPL.setScales(entity,scales);
    }
}
