package teamroots.embers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Embers.MODID)
public class SoundManager {
    @GameRegistry.ObjectHolder("embers:block.alchemy.fail")
    public static SoundEvent ALCHEMY_FAIL;
    @GameRegistry.ObjectHolder("embers:block.alchemy.success")
    public static SoundEvent ALCHEMY_SUCCESS;
    @GameRegistry.ObjectHolder("embers:block.alchemy.loop")
    public static SoundEvent ALCHEMY_LOOP;
    @GameRegistry.ObjectHolder("embers:block.alchemy.start")
    public static SoundEvent ALCHEMY_START;

    @GameRegistry.ObjectHolder("embers:block.pedestal.loop")
    public static SoundEvent PEDESTAL_LOOP;

    @GameRegistry.ObjectHolder("embers:block.beam_cannon.fire")
    public static SoundEvent BEAM_CANNON_FIRE;
    @GameRegistry.ObjectHolder("embers:block.beam_cannon.hit")
    public static SoundEvent BEAM_CANNON_HIT;

    @GameRegistry.ObjectHolder("embers:block.crystal_cell.loop")
    public static SoundEvent CRYSTAL_CELL_LOOP;
    @GameRegistry.ObjectHolder("embers:block.crystal_cell.grow")
    public static SoundEvent CRYSTAL_CELL_GROW;

    @GameRegistry.ObjectHolder("embers:block.generator.loop")
    public static SoundEvent GENERATOR_LOOP;
    @GameRegistry.ObjectHolder("embers:block.activator.plume")
    public static SoundEvent ACTIVATOR;
    @GameRegistry.ObjectHolder("embers:block.boiler.plume")
    public static SoundEvent PRESSURE_REFINERY;
    @GameRegistry.ObjectHolder("embers:block.ignem_reactor.plume")
    public static SoundEvent IGNEM_REACTOR;

    @GameRegistry.ObjectHolder("embers:block.bore.start")
    public static SoundEvent BORE_START;
    @GameRegistry.ObjectHolder("embers:block.bore.stop")
    public static SoundEvent BORE_STOP;
    @GameRegistry.ObjectHolder("embers:block.bore.loop")
    public static SoundEvent BORE_LOOP;
    @GameRegistry.ObjectHolder("embers:block.bore.loop_mine")
    public static SoundEvent BORE_LOOP_MINE;

    @GameRegistry.ObjectHolder("embers:block.catalytic_plug.start")
    public static SoundEvent CATALYTIC_PLUG_START;
    @GameRegistry.ObjectHolder("embers:block.catalytic_plug.stop")
    public static SoundEvent CATALYTIC_PLUG_STOP;
    @GameRegistry.ObjectHolder("embers:block.catalytic_plug.loop")
    public static SoundEvent CATALYTIC_PLUG_LOOP;
    @GameRegistry.ObjectHolder("embers:block.catalytic_plug.loop_ready")
    public static SoundEvent CATALYTIC_PLUG_LOOP_READY;

    @GameRegistry.ObjectHolder("embers:block.stamper.down")
    public static SoundEvent STAMPER_DOWN;
    @GameRegistry.ObjectHolder("embers:block.stamper.up")
    public static SoundEvent STAMPER_UP;

    @GameRegistry.ObjectHolder("embers:block.heat_coil.high_loop")
    public static SoundEvent HEATCOIL_HIGH;
    @GameRegistry.ObjectHolder("embers:block.heat_coil.mid_loop")
    public static SoundEvent HEATCOIL_MID;
    @GameRegistry.ObjectHolder("embers:block.heat_coil.low_loop")
    public static SoundEvent HEATCOIL_LOW;
    @GameRegistry.ObjectHolder("embers:block.heat_coil.cooking_loop")
    public static SoundEvent HEATCOIL_COOK;

    @GameRegistry.ObjectHolder("embers:block.plinth.loop")
    public static SoundEvent PLINTH_LOOP;
    @GameRegistry.ObjectHolder("embers:block.melter.loop")
    public static SoundEvent MELTER_LOOP;
    @GameRegistry.ObjectHolder("embers:block.mixer.loop")
    public static SoundEvent MIXER_LOOP;
    @GameRegistry.ObjectHolder("embers:block.copper_charger.loop")
    public static SoundEvent COPPER_CHARGER_LOOP;
    @GameRegistry.ObjectHolder("embers:block.injector.loop")
    public static SoundEvent INJECTOR_LOOP;

    @GameRegistry.ObjectHolder("embers:block.metal_seed.loop")
    public static SoundEvent METAL_SEED_LOOP;
    @GameRegistry.ObjectHolder("embers:block.metal_seed.ping")
    public static SoundEvent METAL_SEED_PING;

    @GameRegistry.ObjectHolder("embers:block.inferno_forge.fail")
    public static SoundEvent INFERNO_FORGE_FAIL;
    @GameRegistry.ObjectHolder("embers:block.inferno_forge.success")
    public static SoundEvent INFERNO_FORGE_SUCCESS;
    @GameRegistry.ObjectHolder("embers:block.inferno_forge.loop")
    public static SoundEvent INFERNO_FORGE_LOOP;
    @GameRegistry.ObjectHolder("embers:block.inferno_forge.start")
    public static SoundEvent INFERNO_FORGE_START;
    @GameRegistry.ObjectHolder("embers:block.inferno_forge.open")
    public static SoundEvent INFERNO_FORGE_OPEN;
    @GameRegistry.ObjectHolder("embers:block.inferno_forge.close")
    public static SoundEvent INFERNO_FORGE_CLOSE;

    @GameRegistry.ObjectHolder("embers:block.field_chart.loop")
    public static SoundEvent FIELD_CHART_LOOP;

    @GameRegistry.ObjectHolder("embers:block.ember_transfer.emit.small")
    public static SoundEvent EMBER_EMIT;
    @GameRegistry.ObjectHolder("embers:block.ember_transfer.emit.big")
    public static SoundEvent EMBER_EMIT_BIG;
    @GameRegistry.ObjectHolder("embers:block.ember_transfer.receive.small")
    public static SoundEvent EMBER_RECEIVE;
    @GameRegistry.ObjectHolder("embers:block.ember_transfer.receive.big")
    public static SoundEvent EMBER_RECEIVE_BIG;
    @GameRegistry.ObjectHolder("embers:block.ember_transfer.relay")
    public static SoundEvent EMBER_RELAY;

    @GameRegistry.ObjectHolder("embers:block.steam_engine.start_steam")
    public static SoundEvent STEAM_ENGINE_START_STEAM;
    @GameRegistry.ObjectHolder("embers:block.steam_engine.start_burn")
    public static SoundEvent STEAM_ENGINE_START_BURN;
    @GameRegistry.ObjectHolder("embers:block.steam_engine.steam_loop")
    public static SoundEvent STEAM_ENGINE_LOOP_STEAM;
    @GameRegistry.ObjectHolder("embers:block.steam_engine.burn_loop")
    public static SoundEvent STEAM_ENGINE_LOOP_BURN;
    @GameRegistry.ObjectHolder("embers:block.steam_engine.stop")
    public static SoundEvent STEAM_ENGINE_STOP;

    @GameRegistry.ObjectHolder("embers:block.mini_boiler.rupture")
    public static SoundEvent MINI_BOILER_RUPTURE;
    @GameRegistry.ObjectHolder("embers:block.mini_boiler.loop_slow")
    public static SoundEvent MINI_BOILER_LOOP_SLOW;
    @GameRegistry.ObjectHolder("embers:block.mini_boiler.loop_mid")
    public static SoundEvent MINI_BOILER_LOOP_MID;
    @GameRegistry.ObjectHolder("embers:block.mini_boiler.loop_fast")
    public static SoundEvent MINI_BOILER_LOOP_FAST;
    @GameRegistry.ObjectHolder("embers:block.mini_boiler.pressure_loop_low")
    public static SoundEvent MINI_BOILER_PRESSURE_LOW;
    @GameRegistry.ObjectHolder("embers:block.mini_boiler.pressure_loop_mid")
    public static SoundEvent MINI_BOILER_PRESSURE_MID;
    @GameRegistry.ObjectHolder("embers:block.mini_boiler.pressure_loop_high")
    public static SoundEvent MINI_BOILER_PRESSURE_HIGH;

    @GameRegistry.ObjectHolder("embers:block.pump.slow")
    public static SoundEvent PUMP_SLOW;
    @GameRegistry.ObjectHolder("embers:block.pump.mid")
    public static SoundEvent PUMP_MID;
    @GameRegistry.ObjectHolder("embers:block.pump.fast")
    public static SoundEvent PUMP_FAST;

    @GameRegistry.ObjectHolder("embers:block.pipe.connect")
    public static SoundEvent PIPE_CONNECT;
    @GameRegistry.ObjectHolder("embers:block.pipe.disconnect")
    public static SoundEvent PIPE_DISCONNECT;

    @GameRegistry.ObjectHolder("embers:fireball.big.fire")
    public static SoundEvent FIREBALL_BIG;
    @GameRegistry.ObjectHolder("embers:fireball.big.hit")
    public static SoundEvent FIREBALL_BIG_HIT;
    @GameRegistry.ObjectHolder("embers:fireball.small.fire")
    public static SoundEvent FIREBALL;
    @GameRegistry.ObjectHolder("embers:fireball.small.hit")
    public static SoundEvent FIREBALL_HIT;

    @GameRegistry.ObjectHolder("embers:item.blazing_ray.fire")
    public static SoundEvent BLAZING_RAY_FIRE;
    @GameRegistry.ObjectHolder("embers:item.blazing_ray.empty")
    public static SoundEvent BLAZING_RAY_EMPTY;

    @GameRegistry.ObjectHolder("embers:item.cinder_staff.charge")
    public static SoundEvent CINDER_STAFF_CHARGE;
    @GameRegistry.ObjectHolder("embers:item.cinder_staff.fail")
    public static SoundEvent CINDER_STAFF_FAIL;
    @GameRegistry.ObjectHolder("embers:item.cinder_staff.loop")
    public static SoundEvent CINDER_STAFF_LOOP;

    @GameRegistry.ObjectHolder("embers:item.explosion_charm.absorb")
    public static SoundEvent EXPLOSION_CHARM_ABSORB;
    @GameRegistry.ObjectHolder("embers:item.explosion_charm.recharge")
    public static SoundEvent EXPLOSION_CHARM_RECHARGE;

    @GameRegistry.ObjectHolder("embers:item.ash_amulet.burn")
    public static SoundEvent ASHEN_AMULET_BURN;

    @GameRegistry.ObjectHolder("embers:item.heated.level_up")
    public static SoundEvent HEATED_ITEM_LEVELUP;
    @GameRegistry.ObjectHolder("embers:item.resonating_bell.ring")
    public static SoundEvent RESONATING_BELL;
    @GameRegistry.ObjectHolder("embers:item.cinder_jet.boost")
    public static SoundEvent CINDER_JET;
    @GameRegistry.ObjectHolder("embers:item.inflictor_gem.absorb")
    public static SoundEvent INFLICTOR_GEM;

    @GameRegistry.ObjectHolder("embers:item.metallurgic_dust.convert")
    public static SoundEvent METALLURGIC_DUST;
    @GameRegistry.ObjectHolder("embers:item.metallurgic_dust.fail")
    public static SoundEvent METALLURGIC_DUST_FAIL;

    @GameRegistry.ObjectHolder("embers:item.codex.open")
    public static SoundEvent CODEX_OPEN;
    @GameRegistry.ObjectHolder("embers:item.codex.close")
    public static SoundEvent CODEX_CLOSE;
    @GameRegistry.ObjectHolder("embers:item.codex.category.open")
    public static SoundEvent CODEX_CATEGORY_OPEN;
    @GameRegistry.ObjectHolder("embers:item.codex.category.close")
    public static SoundEvent CODEX_CATEGORY_CLOSE;
    @GameRegistry.ObjectHolder("embers:item.codex.category.select")
    public static SoundEvent CODEX_CATEGORY_SELECT;
    @GameRegistry.ObjectHolder("embers:item.codex.category.unselect")
    public static SoundEvent CODEX_CATEGORY_UNSELECT;
    @GameRegistry.ObjectHolder("embers:item.codex.category.switch")
    public static SoundEvent CODEX_CATEGORY_SWITCH;
    @GameRegistry.ObjectHolder("embers:item.codex.page.open")
    public static SoundEvent CODEX_PAGE_OPEN;
    @GameRegistry.ObjectHolder("embers:item.codex.page.close")
    public static SoundEvent CODEX_PAGE_CLOSE;
    @GameRegistry.ObjectHolder("embers:item.codex.page.switch")
    public static SoundEvent CODEX_PAGE_SWITCH;

    @GameRegistry.ObjectHolder("embers:item.bauble.equip")
    public static SoundEvent BAUBLE_EQUIP;
    @GameRegistry.ObjectHolder("embers:item.bauble.unequip")
    public static SoundEvent BAUBLE_UNEQUIP;

    @GameRegistry.ObjectHolder("embers:item.tyrfing.hit")
    public static SoundEvent TYRFING_HIT;
    @GameRegistry.ObjectHolder("embers:item.scale.break")
    public static SoundEvent SHIFTING_SCALES_BREAK;
    @GameRegistry.ObjectHolder("embers:item.windup.spring")
    public static SoundEvent WINDING_GEARS_SPRING;

    @GameRegistry.ObjectHolder("embers:entity.ancient_golem.step")
    public static SoundEvent ANCIENT_GOLEM_STEP;
    @GameRegistry.ObjectHolder("embers:entity.ancient_golem.hurt")
    public static SoundEvent ANCIENT_GOLEM_HURT;
    @GameRegistry.ObjectHolder("embers:entity.ancient_golem.punch")
    public static SoundEvent ANCIENT_GOLEM_PUNCH;
    @GameRegistry.ObjectHolder("embers:entity.ancient_golem.death")
    public static SoundEvent ANCIENT_GOLEM_DEATH;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(registerSound("block.alchemy.fail"));
        event.getRegistry().register(registerSound("block.alchemy.success"));
        event.getRegistry().register(registerSound("block.alchemy.loop"));
        event.getRegistry().register(registerSound("block.alchemy.start"));

        event.getRegistry().register(registerSound("block.pedestal.loop"));

        event.getRegistry().register(registerSound("block.beam_cannon.fire"));
        event.getRegistry().register(registerSound("block.beam_cannon.hit"));

        event.getRegistry().register(registerSound("block.generator.loop"));
        event.getRegistry().register(registerSound("block.activator.plume"));
        event.getRegistry().register(registerSound("block.boiler.plume"));
        event.getRegistry().register(registerSound("block.ignem_reactor.plume"));

        event.getRegistry().register(registerSound("block.crystal_cell.loop"));
        event.getRegistry().register(registerSound("block.crystal_cell.grow"));

        event.getRegistry().register(registerSound("block.bore.start"));
        event.getRegistry().register(registerSound("block.bore.stop"));
        event.getRegistry().register(registerSound("block.bore.loop"));
        event.getRegistry().register(registerSound("block.bore.loop_mine"));

        event.getRegistry().register(registerSound("block.catalytic_plug.start"));
        event.getRegistry().register(registerSound("block.catalytic_plug.stop"));
        event.getRegistry().register(registerSound("block.catalytic_plug.loop"));
        event.getRegistry().register(registerSound("block.catalytic_plug.loop_ready"));

        event.getRegistry().register(registerSound("block.heat_coil.high_loop"));
        event.getRegistry().register(registerSound("block.heat_coil.mid_loop"));
        event.getRegistry().register(registerSound("block.heat_coil.low_loop"));
        event.getRegistry().register(registerSound("block.heat_coil.cooking_loop"));

        event.getRegistry().register(registerSound("block.stamper.down"));
        event.getRegistry().register(registerSound("block.stamper.up"));

        event.getRegistry().register(registerSound("block.melter.loop"));
        event.getRegistry().register(registerSound("block.mixer.loop"));
        event.getRegistry().register(registerSound("block.plinth.loop"));
        event.getRegistry().register(registerSound("block.copper_charger.loop"));
        event.getRegistry().register(registerSound("block.injector.loop"));

        event.getRegistry().register(registerSound("block.metal_seed.loop"));
        event.getRegistry().register(registerSound("block.metal_seed.ping"));

        event.getRegistry().register(registerSound("block.inferno_forge.fail"));
        event.getRegistry().register(registerSound("block.inferno_forge.success"));
        event.getRegistry().register(registerSound("block.inferno_forge.loop"));
        event.getRegistry().register(registerSound("block.inferno_forge.start"));
        event.getRegistry().register(registerSound("block.inferno_forge.open"));
        event.getRegistry().register(registerSound("block.inferno_forge.close"));

        event.getRegistry().register(registerSound("block.field_chart.loop"));

        event.getRegistry().register(registerSound("block.ember_transfer.emit.small"));
        event.getRegistry().register(registerSound("block.ember_transfer.emit.big"));
        event.getRegistry().register(registerSound("block.ember_transfer.receive.small"));
        event.getRegistry().register(registerSound("block.ember_transfer.receive.big"));
        event.getRegistry().register(registerSound("block.ember_transfer.relay"));

        event.getRegistry().register(registerSound("block.steam_engine.start_steam"));
        event.getRegistry().register(registerSound("block.steam_engine.start_burn"));
        event.getRegistry().register(registerSound("block.steam_engine.steam_loop"));
        event.getRegistry().register(registerSound("block.steam_engine.burn_loop"));
        event.getRegistry().register(registerSound("block.steam_engine.stop"));

        event.getRegistry().register(registerSound("block.mini_boiler.rupture"));
        event.getRegistry().register(registerSound("block.mini_boiler.loop_slow"));
        event.getRegistry().register(registerSound("block.mini_boiler.loop_mid"));
        event.getRegistry().register(registerSound("block.mini_boiler.loop_fast"));
        event.getRegistry().register(registerSound("block.mini_boiler.pressure_loop_low"));
        event.getRegistry().register(registerSound("block.mini_boiler.pressure_loop_mid"));
        event.getRegistry().register(registerSound("block.mini_boiler.pressure_loop_high"));

        event.getRegistry().register(registerSound("block.pump.slow"));
        event.getRegistry().register(registerSound("block.pump.mid"));
        event.getRegistry().register(registerSound("block.pump.fast"));

        event.getRegistry().register(registerSound("block.pipe.connect"));
        event.getRegistry().register(registerSound("block.pipe.disconnect"));

        event.getRegistry().register(registerSound("fireball.small.fire"));
        event.getRegistry().register(registerSound("fireball.small.hit"));
        event.getRegistry().register(registerSound("fireball.big.fire"));
        event.getRegistry().register(registerSound("fireball.big.hit"));

        event.getRegistry().register(registerSound("item.blazing_ray.fire"));
        event.getRegistry().register(registerSound("item.blazing_ray.empty"));

        event.getRegistry().register(registerSound("item.cinder_staff.charge"));
        event.getRegistry().register(registerSound("item.cinder_staff.fail"));
        event.getRegistry().register(registerSound("item.cinder_staff.loop"));

        event.getRegistry().register(registerSound("item.heated.level_up"));
        event.getRegistry().register(registerSound("item.cinder_jet.boost"));
        event.getRegistry().register(registerSound("item.resonating_bell.ring"));
        event.getRegistry().register(registerSound("item.inflictor_gem.absorb"));

        event.getRegistry().register(registerSound("item.metallurgic_dust.convert"));
        event.getRegistry().register(registerSound("item.metallurgic_dust.fail"));

        event.getRegistry().register(registerSound("item.explosion_charm.absorb"));
        event.getRegistry().register(registerSound("item.explosion_charm.recharge"));
        event.getRegistry().register(registerSound("item.ash_amulet.burn"));

        event.getRegistry().register(registerSound("item.codex.open"));
        event.getRegistry().register(registerSound("item.codex.close"));
        event.getRegistry().register(registerSound("item.codex.category.open"));
        event.getRegistry().register(registerSound("item.codex.category.close"));
        event.getRegistry().register(registerSound("item.codex.category.select"));
        event.getRegistry().register(registerSound("item.codex.category.unselect"));
        event.getRegistry().register(registerSound("item.codex.category.switch"));
        event.getRegistry().register(registerSound("item.codex.page.open"));
        event.getRegistry().register(registerSound("item.codex.page.close"));
        event.getRegistry().register(registerSound("item.codex.page.switch"));

        event.getRegistry().register(registerSound("item.bauble.equip"));
        event.getRegistry().register(registerSound("item.bauble.unequip"));

        event.getRegistry().register(registerSound("item.tyrfing.hit"));
        event.getRegistry().register(registerSound("item.scale.break"));
        event.getRegistry().register(registerSound("item.windup.spring"));

        event.getRegistry().register(registerSound("entity.ancient_golem.death"));
        event.getRegistry().register(registerSound("entity.ancient_golem.hurt"));
        event.getRegistry().register(registerSound("entity.ancient_golem.punch"));
        event.getRegistry().register(registerSound("entity.ancient_golem.step"));
    }

    public static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation(Embers.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
