package teamroots.embers.config;

import net.minecraftforge.common.config.Config;
import teamroots.embers.Embers;

@Config(modid = Embers.MODID, category = "mob", name = Embers.CFG_FOLDER + "mob")
@Config.LangKey("cfg.embers.mob")
public class ConfigMob {

    @Config.Name("Ember Golem")
    @Config.LangKey("cfg.embers.mob.ember_golem")
    @Config.Comment("Options about the Ember Golem")
    public static final EmberGolemCategory EMBER_GOLEM = new EmberGolemCategory();

    public static class EmberGolemCategory {
        @Config.RequiresMcRestart
        @Config.Name("Spawn Weight")
        @Config.Comment("Spawning weight of the Ancient Golem mob. Higher values make golems spawn more frequently.")
        public int spawnWeight = 25;

        @Config.RequiresMcRestart
        @Config.Name("Knockback Resistance")
        @Config.Comment("How much knockback resistance Ancient Golems have.")
        public double knockbackResistance = 1.0;

        @Config.RequiresMcRestart
        @Config.Name("Follow Range")
        @Config.Comment("How much follow range Ancient Golems have.")
        public double followRange = 32.0;

        @Config.RequiresMcRestart
        @Config.Name("Movement Speed")
        @Config.Comment("How much movement speed Ancient Golems have.")
        public double movementSpeed = 0.5;

        @Config.RequiresMcRestart
        @Config.Name("Attack Damage")
        @Config.Comment("How much attack damage Ancient Golems have.")
        public double attackDamage = 6.0;

        @Config.RequiresMcRestart
        @Config.Name("Max Health")
        @Config.Comment("How much health Golems have.")
        public double maxHealth = 40.0;
    }
}
