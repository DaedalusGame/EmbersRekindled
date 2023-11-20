package teamroots.embers.config;

import net.minecraftforge.common.config.Config;
import teamroots.embers.Embers;

@Config(modid = Embers.MODID, category = "tool", name = Embers.CFG_FOLDER + "tool")
@Config.LangKey("cfg.embers.tool")
public class ConfigTool {
    @Config.Name("Blazing Ray")
    @Config.LangKey("cfg.embers.tool.blazing_ray")
    @Config.Comment("Options about the Blazing Ray")
    public static final BlazingRayCategory BLAZING_RAY_CATEGORY = new BlazingRayCategory();

    @Config.Name("Cinder Staff")
    @Config.LangKey("cfg.embers.tool.cinder_staff")
    @Config.Comment("Options about the Cinder Staff")
    public static final CinderStaffCategory CINDER_STAFF_CATEGORY = new CinderStaffCategory();

    @Config.Name("Shifting Scales")
    @Config.LangKey("cfg.embers.tool.shifting_scales")
    @Config.Comment("Options about the Shifting Scales")
    public static final ShiftingScalesCategory SHIFTING_SCALES_CATEGORY = new ShiftingScalesCategory();

    public static class BlazingRayCategory {
        @Config.RequiresMcRestart
        @Config.Name("Ember Cost")
        @Config.Comment("Ember used up by each shot.")
        public double cost = 25.0;

        @Config.RequiresMcRestart
        @Config.Name("Charge")
        @Config.Comment("Time in ticks to fully charge.")
        public double charge = 20.0;

        @Config.RequiresMcRestart
        @Config.Name("Cooldown")
        @Config.Comment("Cooldown in ticks between each shot.")
        public int cooldown = 10;

        @Config.RequiresMcRestart
        @Config.Name("Damage")
        @Config.Comment("Damage dealt by one shot.")
        public float damage = 7.0f;

        @Config.RequiresMcRestart
        @Config.Name("Distance")
        @Config.Comment("Maximum shot distance.")
        public float distance = 96.0f;

        @Config.RequiresMcRestart
        @Config.Name("Spread")
        @Config.Comment("Maximum spread.")
        public double spread = 30.0;
    }

    public static class CinderStaffCategory {
        @Config.RequiresMcRestart
        @Config.Name("Ember Cost")
        @Config.Comment("Ember used up by each shot.")
        public double cost = 25.0;

        @Config.RequiresMcRestart
        @Config.Name("Charge")
        @Config.Comment("Time in ticks to fully charge.")
        public double charge = 60.0;

        @Config.RequiresMcRestart
        @Config.Name("Cooldown")
        @Config.Comment("Cooldown in ticks between each shot.")
        public int cooldown = 10;

        @Config.RequiresMcRestart
        @Config.Name("Damage")
        @Config.Comment("Damage dealt by one shot.")
        public float damage = 17.0f;

        @Config.RequiresMcRestart
        @Config.Name("Size")
        @Config.Comment("Size of the projectile.")
        public float size = 17.0f;

        @Config.RequiresMcRestart
        @Config.Name("AOE Size")
        @Config.Comment("Area of Effect on impact.")
        public float aoe = 2.125f;

        @Config.RequiresMcRestart
        @Config.Name("Lifetime")
        @Config.Comment("Lifetime in ticks of projectile.")
        public int lifetime = 160;
    }

    public static class ShiftingScalesCategory {
        @Config.RequiresMcRestart
        @Config.Name("Damage Passes")
        @Config.Comment({
                "Syntax is 'damagetype:rate'.",
                "Determines which damage types are partially unaffected by the shifting scales augment."
        })
        public String[] damagePasses = new String[]{
                "drown:1.0",
                "starve:1.0",
        };

        @Config.RequiresMcRestart
        @Config.Name("Damage Rates")
        @Config.Comment({
                "Syntax is 'damagetype:rate'.",
                "Specifies a separate damage rate for depleting the scales."
        })
        public String[] damageRates = new String[]{};
    }
}
