package teamroots.embers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = Embers.MODID)
public class SoundManager {
    @GameRegistry.ObjectHolder("embers:block.embers.alchemy.fail")
    public static SoundEvent ALCHEMY_FAIL;
    @GameRegistry.ObjectHolder("embers:block.embers.alchemy.success")
    public static SoundEvent ALCHEMY_SUCCESS;
    @GameRegistry.ObjectHolder("embers:block.embers.alchemy.loop")
    public static SoundEvent ALCHEMY_LOOP;
    @GameRegistry.ObjectHolder("embers:block.embers.alchemy.start")
    public static SoundEvent ALCHEMY_START;
    @GameRegistry.ObjectHolder("embers:block.embers.pedestal.loop")
    public static SoundEvent PEDESTAL_LOOP;
    @GameRegistry.ObjectHolder("embers:block.embers.beam_cannon.fire")
    public static SoundEvent BEAM_CANNON_FIRE;
    @GameRegistry.ObjectHolder("embers:block.embers.beam_cannon.hit")
    public static SoundEvent BEAM_CANNON_HIT;
    @GameRegistry.ObjectHolder("embers:block.embers.crystalcell")
    public static SoundEvent CRYSTAL_CELL;
    @GameRegistry.ObjectHolder("embers:block.embers.activator")
    public static SoundEvent ACTIVATOR;
    @GameRegistry.ObjectHolder("embers:block.embers.bore.start")
    public static SoundEvent BORE_START;
    @GameRegistry.ObjectHolder("embers:block.embers.bore.stop")
    public static SoundEvent BORE_STOP;
    @GameRegistry.ObjectHolder("embers:block.embers.bore.loop")
    public static SoundEvent BORE_LOOP;
    @GameRegistry.ObjectHolder("embers:block.embers.bore.loop_mine")
    public static SoundEvent BORE_LOOP_MINE;
    @GameRegistry.ObjectHolder("embers:block.embers.stamper.down")
    public static SoundEvent STAMPER_DOWN;
    @GameRegistry.ObjectHolder("embers:block.embers.stamper.up")
    public static SoundEvent STAMPER_UP;
    @GameRegistry.ObjectHolder("embers:block.embers.melter.loop")
    public static SoundEvent MELTER_LOOP;
    @GameRegistry.ObjectHolder("embers:block.embers.mixer.loop")
    public static SoundEvent MIXER_LOOP;
    @GameRegistry.ObjectHolder("embers:block.embers.still.loop")
    public static SoundEvent STILL_LOOP;
    @GameRegistry.ObjectHolder("embers:block.embers.still.slow")
    public static SoundEvent STILL_SLOW;
    @GameRegistry.ObjectHolder("embers:block.embers.still.fast")
    public static SoundEvent STILL_FAST;
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

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(registerSound("block.embers.alchemy.fail"));
        event.getRegistry().register(registerSound("block.embers.alchemy.success"));
        event.getRegistry().register(registerSound("block.embers.alchemy.loop"));
        event.getRegistry().register(registerSound("block.embers.alchemy.start"));
        event.getRegistry().register(registerSound("block.embers.pedestal.loop"));
        event.getRegistry().register(registerSound("block.embers.beam_cannon.fire"));
        event.getRegistry().register(registerSound("block.embers.beam_cannon.hit"));
        event.getRegistry().register(registerSound("block.embers.crystalcell"));
        event.getRegistry().register(registerSound("block.embers.activator"));
        event.getRegistry().register(registerSound("block.embers.bore.start"));
        event.getRegistry().register(registerSound("block.embers.bore.stop"));
        event.getRegistry().register(registerSound("block.embers.bore.loop"));
        event.getRegistry().register(registerSound("block.embers.bore.loop_mine"));
        event.getRegistry().register(registerSound("block.embers.stamper.down"));
        event.getRegistry().register(registerSound("block.embers.stamper.up"));
        event.getRegistry().register(registerSound("block.embers.melter.loop"));
        event.getRegistry().register(registerSound("block.embers.mixer.loop"));
        event.getRegistry().register(registerSound("block.embers.plinth.loop"));
        event.getRegistry().register(registerSound("block.embers.still.loop"));
        event.getRegistry().register(registerSound("block.embers.still.slow"));
        event.getRegistry().register(registerSound("block.embers.still.fast"));
        event.getRegistry().register(registerSound("fireball.small.fire"));
        event.getRegistry().register(registerSound("fireball.small.hit"));
        event.getRegistry().register(registerSound("fireball.big.fire"));
        event.getRegistry().register(registerSound("fireball.big.hit"));
        event.getRegistry().register(registerSound("item.blazing_ray.fire"));
        event.getRegistry().register(registerSound("item.blazing_ray.empty"));
    }

    public static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation(Embers.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
