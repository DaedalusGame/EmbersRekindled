package teamroots.embers.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.embers.Embers;

@Config(modid = Embers.MODID, name = Embers.CFG_FOLDER + "main")
@Config.LangKey("cfg.embers.main")
@Mod.EventBusSubscriber(modid = Embers.MODID)
public class ConfigMain {

    @Config.Name("Client")
    @Config.LangKey("cfg.embers.main.client")
    @Config.Comment("Rendering stuff")
    public static final ClientCategory CLIENT_CATEGORY = new ClientCategory();

    @Config.Name("Compat")
    @Config.LangKey("cfg.embers.main.compat")
    @Config.Comment("Integration with other mods")
    public static final CompatCategory COMPAT_CATEGORY = new CompatCategory();

    @Config.Name("Everybody is considered an Enemy")
    @Config.Comment("If true, Embers homing projectiles will go for neutral players.")
    public static boolean pvpEverybodyIsEnemy = false;
    @Config.RequiresMcRestart
    @Config.Name("Should categories be locked by default?")
    @Config.Comment("Codex category is shut. Progression is open.")
    public static boolean codexCategoryIsProgress = true;
    @Config.RequiresMcRestart
    @Config.Name("Should entries be hidden by default?")
    @Config.Comment("Codex entry is shut and hidden. Progression is open and shown.")
    public static boolean codexEntryIsProgress = true;

    public static class ClientCategory {
        @Config.Name("Enable Particle Collision")
        @Config.Comment({
                "Whether or not particles should collide with blocks.",
                "Disabling this might significantly improve performance.",
                "[default: true]"
        })
        public boolean enableParticleCollisions = true;

        @Config.Name("Enable Particles")
        @Config.Comment({
                "Whether or not particles are enabled.",
                "Disabling this will change the gameplay experience but significantly improve performance.",
                "[default: true]"
        })
        public boolean enableParticles = true;
    }

    public static class CompatCategory {
        @Config.Name("Enable JEI Cheats")
        @Config.Comment("If true, JEI will show the exact amount of Ash needed in an Alchemy recipe.")
        public boolean enableJeiCheat = false;

        @Config.RequiresMcRestart
        @Config.Name("Enable Baubles Integration")
        @Config.Comment("If true, Embers will register items, blocks and recipes providing Baubles integration.")
        public boolean enableBaublesIntegration = true;

        @Config.RequiresMcRestart
        @Config.Name("Force Baubles Integration")
        @Config.Comment({
                "If true, Embers will register items, blocks and recipes providing Baubles integration",
                "Even if Baubles isn't installed"
        })
        public boolean forceBaublesIntegration = false;

        @Config.RequiresMcRestart
        @Config.Name("Enable Mystical Mechanics Integration")
        @Config.Comment("If true, Embers will register items, blocks and recipes providing Mystical Mechanics integration.")
        public boolean enableMysticalMechanicsIntegration = true;

        @Config.RequiresMcRestart
        @Config.Name("Force Mystical Mechanics Integration")
        @Config.Comment({
                "If true, Embers will register items, blocks and recipes providing Mystical Mechanics integration",
                "Even if Mystical Mechanics isn't installed"
        })
        public boolean forceMysticalMechanicsIntegration = false;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Embers.MODID)) {
            ConfigManager.sync(Embers.MODID, Config.Type.INSTANCE);
        }
    }
}

