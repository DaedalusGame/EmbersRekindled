package teamroots.embers.compat.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.RegistryManager;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

public class ThaumcraftIntegration {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void AspectRegistryEvent(AspectRegistryEvent event)
    {
        registerAspects(event.register);
    }

    //Special thanks to Mangoose for writing all of this.
    public static void registerAspects(AspectEventProxy register) {
        registerOreAspects(register);
        registerItemAspects(register);
        registerBlockAspects(register);

        //No equivalent with new api
        ThaumcraftApi.registerEntityTag("ancient_golem", new AspectList().add(Aspect.MECHANISM, 20).add(Aspect.MOTION, 5).add(Aspect.EARTH, 15).add(Aspect.ALCHEMY, 5));
    }

    private static void registerItemAspects(AspectEventProxy register) {
        register.registerObjectTag(new ItemStack(RegistryManager.brick_caminite),
                new AspectList().add(Aspect.EARTH, 2).add(Aspect.WATER, 1).add(Aspect.FIRE, 1));
        register.registerObjectTag(new ItemStack(RegistryManager.plate_caminite),
                new AspectList().add(Aspect.EARTH, 3).add(Aspect.WATER, 1).add(Aspect.FIRE, 1));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.tinker_hammer),
                new AspectList().add(Aspect.TOOL, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.stamp_bar),
                new AspectList().add(Aspect.EARTH, 6).add(Aspect.WATER, 3).add(Aspect.FIRE, 1).add(Aspect.CRAFT, 5));
        register.registerObjectTag(new ItemStack(RegistryManager.stamp_flat),
                new AspectList().add(Aspect.EARTH, 12).add(Aspect.WATER, 6).add(Aspect.FIRE, 1).add(Aspect.CRAFT, 5));
        register.registerObjectTag(new ItemStack(RegistryManager.stamp_plate),
                new AspectList().add(Aspect.EARTH, 6).add(Aspect.WATER, 3).add(Aspect.FIRE, 1).add(Aspect.CRAFT, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.ember_detector),
                new AspectList().add(Aspect.MECHANISM, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.dust_ember),
                new AspectList().add(Aspect.FIRE, 15).add(Aspect.ENERGY, 15).add(Aspect.ENTROPY, 5));
        register.registerObjectTag(new ItemStack(RegistryManager.shard_ember),
                new AspectList().add(Aspect.FIRE, 3).add(Aspect.ENERGY, 3).add(Aspect.CRYSTAL, 3));
        register.registerObjectTag(new ItemStack(RegistryManager.crystal_ember),
                new AspectList().add(Aspect.FIRE, 18).add(Aspect.ENERGY, 18).add(Aspect.CRYSTAL, 10));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.ember_jar),
                new AspectList().add(Aspect.VOID, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.ember_cartridge),
                new AspectList().add(Aspect.VOID, 10));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.ignition_cannon),
                new AspectList().add(Aspect.AVERSION, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.staff_ember),
                new AspectList().add(Aspect.AVERSION, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.pickaxe_clockwork),
                new AspectList().add(Aspect.MECHANISM, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.grandhammer),
                new AspectList().add(Aspect.AVERSION, 8));
        register.registerObjectTag(new ItemStack(RegistryManager.dust_ash),
                new AspectList().add(Aspect.ENTROPY, 5).add(Aspect.FIRE, 3));
        register.registerObjectTag(new ItemStack(RegistryManager.aspectus_iron),
                new AspectList().add(Aspect.METAL, 15).add(Aspect.ALCHEMY, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.aspectus_copper),
                new AspectList().add(Aspect.METAL, 10).add(Aspect.EXCHANGE, 5).add(Aspect.ALCHEMY, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.aspectus_lead),
                new AspectList().add(Aspect.METAL, 10).add(Aspect.ORDER, 5).add(Aspect.ALCHEMY, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.aspectus_silver),
                new AspectList().add(Aspect.METAL, 10).add(Aspect.DESIRE, 5).add(Aspect.ALCHEMY, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.aspectus_dawnstone),
                new AspectList().add(Aspect.METAL, 10).add(Aspect.MECHANISM, 5).add(Aspect.FIRE, 5).add(Aspect.ALCHEMY, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.alchemic_waste),
                new AspectList().add(Aspect.ENTROPY, 7).add(Aspect.ALCHEMY, 5));
        register.registerObjectTag(new ItemStack(RegistryManager.inflictor_gem),
                new AspectList().add(Aspect.PROTECT, 15).add(Aspect.CRYSTAL, 15).add(Aspect.METAL, 7).add(Aspect.ALCHEMY, 2));
        register.registerObjectTag(new ItemStack(RegistryManager.glimmer_shard),
                new AspectList().add(Aspect.LIGHT, 25).add(Aspect.CRYSTAL, 15).add(Aspect.FIRE, 10).add(Aspect.ENERGY, 10).add(Aspect.ALCHEMY, 5));
        register.registerObjectTag(new ItemStack(RegistryManager.ashen_cloth),
                new AspectList().add(Aspect.ENTROPY, 4).add(Aspect.FIRE, 3).add(Aspect.CRAFT, 5).add(Aspect.BEAST, 7).add(Aspect.ALCHEMY, 1));
        register.registerObjectTag(new ItemStack(RegistryManager.archaic_brick),
                new AspectList().add(Aspect.EARTH, 3).add(Aspect.DARKNESS, 1).add(Aspect.ORDER, 1));
        register.registerObjectTag(new ItemStack(RegistryManager.ancient_motive_core),
                new AspectList().add(Aspect.MIND, 10).add(Aspect.SOUL, 5).add(Aspect.ELDRITCH, 3).add(Aspect.ALCHEMY, 2));
        register.registerObjectTag(new ItemStack(RegistryManager.isolated_materia),
                new AspectList().add(Aspect.ALCHEMY, 15).add(Aspect.EXCHANGE, 10).add(Aspect.CRAFT, 10).add(Aspect.ORDER, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.tyrfing),
                new AspectList().add(Aspect.METAL, 26).add(Aspect.FIRE, 54).add(Aspect.ENERGY, 50).add(Aspect.ORDER, 4).add(Aspect.ALCHEMY, 6));
        register.registerObjectTag(new ItemStack(RegistryManager.adhesive),
                new AspectList().add(Aspect.WATER, 5).add(Aspect.CRAFT, 5).add(Aspect.ALCHEMY, 2));
        register.registerObjectTag(new ItemStack(RegistryManager.ember_cluster),
                new AspectList().add(Aspect.FIRE, 27).add(Aspect.ENERGY, 27).add(Aspect.CRYSTAL, 15).add(Aspect.ALCHEMY, 3));
        register.registerObjectTag(new ItemStack(RegistryManager.wildfire_core),
                new AspectList().add(Aspect.METAL, 20).add(Aspect.FIRE, 27).add(Aspect.ENERGY, 20).add(Aspect.CRYSTAL, 11).add(Aspect.ALCHEMY, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.codex),
                new AspectList().add(Aspect.MIND, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.blasting_core),
                new AspectList().add(Aspect.METAL, 41).add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10).add(Aspect.ENERGY, 7).add(Aspect.ALCHEMY, 4));
        register.registerObjectTag(new ItemStack(RegistryManager.flame_barrier),
                new AspectList().add(Aspect.FIRE, 24).add(Aspect.METAL, 30).add(Aspect.MECHANISM, 11).add(Aspect.AVERSION, 10).add(Aspect.PROTECT, 10).add(Aspect.ALCHEMY, 2));
        register.registerObjectTag(new ItemStack(RegistryManager.eldritch_insignia),
                new AspectList().add(Aspect.DARKNESS, 3).add(Aspect.ELDRITCH, 5).add(Aspect.EARTH, 11).add(Aspect.PROTECT, 15).add(Aspect.SOUL, 15).add(Aspect.ALCHEMY, 4));
        register.registerObjectTag(new ItemStack(RegistryManager.intelligent_apparatus),
                new AspectList().add(Aspect.METAL, 33).add(Aspect.MIND, 10).add(Aspect.EXCHANGE, 15).add(Aspect.ORDER, 4).add(Aspect.DESIRE, 7).add(Aspect.ALCHEMY, 3));
        register.registerObjectTag(new ItemStack(RegistryManager.dust_metallurgic),
                new AspectList().add(Aspect.METAL, 15).add(Aspect.ENTROPY, 5).add(Aspect.ALCHEMY, 5));
    }

    private static void registerOreAspects(AspectEventProxy register) {
        register.registerObjectTag("ingotDawnstone",
                new AspectList().add(Aspect.METAL, 10).add(Aspect.MECHANISM, 5).add(Aspect.FIRE, 5));
        register.registerObjectTag("ingotAluminum",
                new AspectList().add(Aspect.METAL, 10).add(Aspect.AIR, 5));
        register.registerObjectTag("ingotElectrum",
                new AspectList().add(Aspect.METAL, 10).add(Aspect.ENERGY, 5).add(Aspect.DESIRE, 5));
        register.registerObjectTag("ingotNickel",
                new AspectList().add(Aspect.METAL, 10).add(Aspect.CRAFT, 5));
        register.registerObjectTag("oreAluminum",
                new AspectList().add(Aspect.EARTH, 5).add(Aspect.METAL, 10).add(Aspect.AIR, 5));
        register.registerObjectTag("oreNickel",
                new AspectList().add(Aspect.EARTH, 5).add(Aspect.METAL, 10).add(Aspect.CRAFT, 5));
    }

    private static void registerBlockAspects(AspectEventProxy register) {
        register.registerComplexObjectTag(new ItemStack(RegistryManager.block_tank),
                new AspectList().add(Aspect.VOID, 20));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.pipe),
                new AspectList().add(Aspect.MOTION, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.pump),
                new AspectList().add(Aspect.MECHANISM, 5).add(Aspect.EXCHANGE, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.copper_cell),
                new AspectList().add(Aspect.ENERGY, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.item_pipe),
                new AspectList().add(Aspect.MOTION, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.item_pump),
                new AspectList().add(Aspect.MECHANISM, 5).add(Aspect.EXCHANGE, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.stamper),
                new AspectList().add(Aspect.MECHANISM, 7).add(Aspect.MOTION, 7));
        register.registerObjectTag(new ItemStack(RegistryManager.ember_bore),
                new AspectList().add(Aspect.METAL, 117).add(Aspect.MECHANISM, 20).add(Aspect.EARTH, 18).add(Aspect.ORDER, 15).add(Aspect.EXCHANGE, 11).add(Aspect.ENTROPY, 10).add(Aspect.FIRE, 9));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.mech_accessor),
                new AspectList().add(Aspect.MECHANISM, 15).add(Aspect.EXCHANGE, 7));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.mech_core),
                new AspectList().add(Aspect.MECHANISM, 25).add(Aspect.EXCHANGE, 10).add(Aspect.ORDER, 10));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.heat_coil),
                new AspectList().add(Aspect.FIRE, 25));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.item_dropper),
                new AspectList().add(Aspect.MECHANISM, 5).add(Aspect.EXCHANGE, 3));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.block_lantern),
                new AspectList().add(Aspect.LIGHT, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.charger),
                new AspectList().add(Aspect.ENERGY, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.ashen_stone),
                new AspectList().add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 2).add(Aspect.FIRE, 1));
        register.registerObjectTag(new ItemStack(RegistryManager.ashen_brick),
                new AspectList().add(Aspect.EARTH, 3).add(Aspect.ENTROPY, 1));
        register.registerObjectTag(new ItemStack(RegistryManager.ashen_tile),
                new AspectList().add(Aspect.EARTH, 3).add(Aspect.ENTROPY, 1));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.alchemy_pedestal),
                new AspectList().add(Aspect.ALCHEMY, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.alchemy_tablet),
                new AspectList().add(Aspect.ALCHEMY, 25).add(Aspect.CRAFT, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.item_transfer),
                new AspectList().add(Aspect.MECHANISM, 10).add(Aspect.EXCHANGE, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.beam_cannon),
                new AspectList().add(Aspect.AVERSION, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.alchemy_pedestal),
                new AspectList().add(Aspect.ALCHEMY, 15));
        register.registerObjectTag(new ItemStack(RegistryManager.archaic_tile),
                new AspectList().add(Aspect.EARTH, 7).add(Aspect.DARKNESS, 2).add(Aspect.ORDER, 2));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.archaic_light),
                new AspectList().add(Aspect.LIGHT, 10));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.auto_hammer),
                new AspectList().add(Aspect.MECHANISM, 15).add(Aspect.TOOL, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.sealed_planks),
                new AspectList().add(Aspect.PLANT, 3).add(Aspect.ORDER, 1));
        register.registerObjectTag(new ItemStack(RegistryManager.wrapped_sealed_planks),
                new AspectList().add(Aspect.METAL, 3).add(Aspect.PLANT, 3).add(Aspect.ORDER, 1));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.vacuum),
                new AspectList().add(Aspect.VOID, 15).add(Aspect.MECHANISM, 5).add(Aspect.EXCHANGE, 5));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.breaker),
                new AspectList().add(Aspect.ENTROPY, 15).add(Aspect.MECHANISM, 10).add(Aspect.TOOL, 8));
        register.registerObjectTag(new ItemStack(RegistryManager.seed, 1, 0),
                new AspectList().add(Aspect.METAL, 25).add(Aspect.CRYSTAL, 5).add(Aspect.ORDER, 5).add(Aspect.DESIRE, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.seed, 1, 1),
                new AspectList().add(Aspect.METAL, 20).add(Aspect.CRYSTAL, 5).add(Aspect.ORDER, 5).add(Aspect.DESIRE, 20));
        register.registerObjectTag(new ItemStack(RegistryManager.seed, 1, 2),
                new AspectList().add(Aspect.METAL, 20).add(Aspect.CRYSTAL, 5).add(Aspect.ORDER, 5).add(Aspect.DESIRE, 10).add(Aspect.EXCHANGE, 5));
        register.registerObjectTag(new ItemStack(RegistryManager.seed, 1, 3),
                new AspectList().add(Aspect.METAL, 20).add(Aspect.CRYSTAL, 5).add(Aspect.ORDER, 10).add(Aspect.DESIRE, 10));
        register.registerObjectTag(new ItemStack(RegistryManager.seed, 1, 4),
                new AspectList().add(Aspect.METAL, 20).add(Aspect.CRYSTAL, 5).add(Aspect.ORDER, 5).add(Aspect.DESIRE, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.field_chart),
                new AspectList().add(Aspect.SENSES, 15));
        register.registerComplexObjectTag(new ItemStack(RegistryManager.ember_pulser),
                new AspectList().add(Aspect.EXCHANGE, 3));
        register.registerObjectTag(new ItemStack(RegistryManager.caminite_lever),
                new AspectList().add(Aspect.EARTH, 2).add(Aspect.MECHANISM, 3));
        register.registerObjectTag(new ItemStack(RegistryManager.catalytic_plug),
                new AspectList().add(Aspect.ENERGY, 50).add(Aspect.METAL, 15).add(Aspect.MOTION, 7).add(Aspect.EXCHANGE, 5).add(Aspect.CRYSTAL, 5).add(Aspect.ALCHEMY, 4));
    }
}
