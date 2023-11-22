package teamroots.embers.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Loader;
import teamroots.embers.Embers;

import java.util.Arrays;

@Config(modid = Embers.MODID, category = "material", name = Embers.CFG_FOLDER + "material")
@Config.LangKey("cfg.embers.material")
public class ConfigMaterial {
    public enum RegistrationModes {
        /**
         * FORCE - Register the material independent of other mods
         * OFF - Don't register the material at all
         * SMART - Don't register the material if it already exists
         */
        FORCE, OFF, SMART
    }

    @Config.RequiresMcRestart
    @Config.Name("*SMART* Mods")
    @Config.Comment("A list of all mods, that will be used in the SMART registration check.")
    public static String[] smartMods = new String[]{
            "thermalfoundation"
    };

    @Config.Name("Copper")
    @Config.LangKey("cfg.embers.material.copper")
    @Config.Comment("Options for Copper")
    public static final Material COPPER = new Material(RegistrationModes.FORCE);

    @Config.Name("Silver")
    @Config.LangKey("cfg.embers.material.silver")
    @Config.Comment("Options for Silver")
    public static final Material SILVER = new Material(RegistrationModes.FORCE);

    @Config.Name("Lead")
    @Config.LangKey("cfg.embers.material.lead")
    @Config.Comment("Options for Lead")
    public static final Material LEAD = new Material(RegistrationModes.FORCE);

    @Config.Name("Aluminum")
    @Config.LangKey("cfg.embers.material.aluminum")
    @Config.Comment("Options for Aluminum")
    public static final Material ALUMINUM = new Material(RegistrationModes.SMART);

    @Config.Name("Bronze")
    @Config.LangKey("cfg.embers.material.bronze")
    @Config.Comment("Options for Bronze")
    public static final Material BRONZE = new Material(RegistrationModes.SMART);

    @Config.Name("Electrum")
    @Config.LangKey("cfg.embers.material.electrum")
    @Config.Comment("Options for Electrum")
    public static final Material ELECTRUM = new Material(RegistrationModes.SMART);

    @Config.Name("Nickel")
    @Config.LangKey("cfg.embers.material.nickel")
    @Config.Comment("Options for Nickel")
    public static final Material NICKEL = new Material(RegistrationModes.SMART);

    @Config.Name("Tin")
    @Config.LangKey("cfg.embers.material.tin")
    @Config.Comment("Options for Tin")
    public static final Material TIN = new Material(RegistrationModes.SMART);

    @Config.Name("Iron")
    @Config.LangKey("cfg.embers.material.iron")
    @Config.Comment("Options for Iron")
    public static final Material IRON = new Material(RegistrationModes.SMART);

    @Config.Name("Gold")
    @Config.LangKey("cfg.embers.material.gold")
    @Config.Comment("Options for Gold")
    public static final Material GOLD = new Material(RegistrationModes.SMART);

    @Config.Name("Dawnstone")
    @Config.LangKey("cfg.embers.material.dawnstone")
    @Config.Comment("Options for Dawnstone")
    public static final Material DAWNSTONE = new Material(RegistrationModes.SMART);

    @Config.Name("Mithril")
    @Config.LangKey("cfg.embers.material.mithril")
    @Config.Comment("[WIP] Options for Mithril")
    public static final Material MITHRIL = new Material(RegistrationModes.OFF);

    @Config.Name("Astralite")
    @Config.LangKey("cfg.embers.material.astralite")
    @Config.Comment("[WIP] Options for Astralite")
    public static final Material ASTRALITE = new Material(RegistrationModes.OFF);

    @Config.Name("Umber Steel")
    @Config.LangKey("cfg.embers.material.umber_steel")
    @Config.Comment("[WIP] Options for Umber Steel")
    public static final Material UMBERSTEEL = new Material(RegistrationModes.OFF);

    @Config.Name("Tools")
    @Config.LangKey("cfg.embers.material.tool")
    @Config.Comment({
            "Options about various tool stats.",
            "Each tool material can be disabled.",
            "A tool material won't be registered,",
            "if the corresponding material is missing."
    })
    public static final ToolCategory TOOL = new ToolCategory();

    public static class ToolCategory {

        @Config.Name("Copper Tools")
        @Config.LangKey("cfg.embers.material.tool.copper")
        @Config.Comment("Options for Copper Tools")
        public final Tool COPPER = new Tool(true, 2, 181, 5.4f, 1.5f, 16);

        @Config.Name("Silver Tools")
        @Config.LangKey("cfg.embers.material.tool.silver")
        @Config.Comment("Options for Silver Tools")
        public final Tool SILVER = new Tool(true, 2, 202, 7.6f, 2.0f, 20);

        @Config.Name("Lead Tools")
        @Config.LangKey("cfg.embers.material.tool.lead")
        @Config.Comment("Options for Lead Tools")
        public final Tool LEAD = new Tool(true, 2, 168, 6.0f, 2.0f, 4);

        @Config.Name("Aluminum Tools")
        @Config.LangKey("cfg.embers.material.tool.aluminum")
        @Config.Comment("Options for Aluminum Tools")
        public final Tool ALUMINUM = new Tool(true, 2, 220, 5.2f, 1.5f, 14);

        @Config.Name("Bronze Tools")
        @Config.LangKey("cfg.embers.material.tool.bronze")
        @Config.Comment("Options for Bronze Tools")
        public final Tool BRONZE = new Tool(true, 2, 510, 6.5f, 2.0f, 20);

        @Config.Name("Electrum Tools")
        @Config.LangKey("cfg.embers.material.tool.electrum")
        @Config.Comment("Options for Electrum Tools")
        public final Tool ELECTRUM = new Tool(true, 2, 71, 10.8f, 1.0f, 30);

        @Config.Name("Nickel Tools")
        @Config.LangKey("cfg.embers.material.tool.nickel")
        @Config.Comment("Options for Nickel Tools")
        public final Tool NICKEL = new Tool(true, 2, 331, 6.4f, 2.0f, 18);

        @Config.Name("Tin Tools")
        @Config.LangKey("cfg.embers.material.tool.tin")
        @Config.Comment("Options for Tin Tools")
        public final Tool TIN = new Tool(true, 1, 145, 4.9f, 1.0f, 12);

        @Config.Name("Dawnstone Tools")
        @Config.LangKey("cfg.embers.material.tool.dawnstone")
        @Config.Comment("Options for Dawnstone Tools")
        public final Tool DAWNSTONE = new Tool(true, 2, 512, 7.5f, 0.0f, 24);

        public static class Tool {
            @Config.RequiresMcRestart
            @Config.Name("Should register?")
            @Config.Comment("If the tools for this material should be registered.")
            public boolean register;

            @Config.RequiresMcRestart
            @Config.Name("Harvest Level")
            @Config.Comment("The harvest level of this material.")
            public int harvestLevel;

            @Config.RequiresMcRestart
            @Config.Name("Base Durability")
            @Config.Comment("The base durability of this material.")
            public int durability;

            @Config.RequiresMcRestart
            @Config.Name("Base Efficiency")
            @Config.Comment("The base efficiency of this material.")
            public float efficiency;

            @Config.RequiresMcRestart
            @Config.Name("Base Damage")
            @Config.Comment("The base damage of this material.")
            public float damage;

            @Config.RequiresMcRestart
            @Config.Name("Enchantability")
            @Config.Comment("The enchantability of this material.")
            public int enchantability;

            private Tool(boolean register, int harvestLevel, int durability, float efficiency, float damage, int enchantability) {
                this.register = register;
                this.harvestLevel = harvestLevel;
                this.durability = durability;
                this.efficiency = efficiency;
                this.damage = damage;
                this.enchantability = enchantability;
            }
        }
    }

    public static class Material {
        @Config.RequiresMcRestart
        @Config.Name("Registration Mode")
        @Config.Comment({
                "FORCE - Register the material independent of other mods",
                "OFF - Don't register the material at all",
                "SMART - Don't register the material if it already exists"
        })
        public RegistrationModes registrationMode;

        /**
         * @return if the extra items should be registered.
         * It's used for stuff like the metal seeds
         * as well as the special metals added by the mod.
         */
        public boolean isNotOff() {
            return this.registrationMode != RegistrationModes.OFF;
        }

        /**
         * @return if the base items should be registered.
         * It's used fot stuff like plates, ingots, nuggets, blocks
         */
        public boolean mustLoad() {
            return this.registrationMode == RegistrationModes.FORCE || Arrays.stream(ConfigMaterial.smartMods).noneMatch(Loader::isModLoaded);
        }

        private Material(RegistrationModes registrationMode) {
            this.registrationMode = registrationMode;
        }
    }
}
