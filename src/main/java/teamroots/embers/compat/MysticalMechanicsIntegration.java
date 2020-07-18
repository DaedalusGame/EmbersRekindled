package teamroots.embers.compat;

import mysticalmechanics.api.IGearBehavior;
import mysticalmechanics.api.IGearData;
import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import mysticalmechanics.handler.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import teamroots.embers.ConfigManager;
import teamroots.embers.Embers;
import teamroots.embers.EventManager;
import teamroots.embers.RegistryManager;
import teamroots.embers.api.capabilities.EmbersCapabilities;
import teamroots.embers.api.power.IEmberCapability;
import teamroots.embers.api.tile.IExtraCapabilityInformation;
import teamroots.embers.block.BlockMechActuator;
import teamroots.embers.block.BlockMechActuatorSingle;
import teamroots.embers.block.BlockSteamEngine;
import teamroots.embers.item.ItemBase;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.research.subtypes.ResearchFakePage;
import teamroots.embers.research.subtypes.ResearchShowItem;
import teamroots.embers.tileentity.TileEntityMechActuator;
import teamroots.embers.tileentity.TileEntityMechActuatorRenderer;
import teamroots.embers.tileentity.TileEntityMechActuatorSingle;
import teamroots.embers.tileentity.TileEntitySteamEngine;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MysticalMechanicsIntegration {
    public static final ResourceLocation IRON_GEAR_BEHAVIOR = new ResourceLocation("mysticalmechanics", "gear_iron");
    public static final ResourceLocation GOLD_GEAR_BEHAVIOR = new ResourceLocation("mysticalmechanics", "gear_gold");
    public static final ResourceLocation GOLD_GEAR_ON_BEHAVIOR = new ResourceLocation("mysticalmechanics", "gear_gold_on");
    public static final ResourceLocation GOLD_GEAR_OFF_BEHAVIOR = new ResourceLocation("mysticalmechanics", "gear_gold_off");
    public static final ResourceLocation DAWNSTONE_GEAR_BEHAVIOR = new ResourceLocation(Embers.MODID, "gear_dawnstone");
    public static final double IRON_GEAR_MAX_POWER = 80;
    public static final double GOLD_GEAR_MAX_POWER = 320;

    public static Item gear_dawnstone;

    public static Block steam_engine;
    public static Block mech_actuator;
    public static Block mech_actuator_single;

    static Random random = new Random();

    public static ResourceLocation getRL(String s){
        return new ResourceLocation(Embers.MODID,s);
    }

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        Block axle_iron = Block.getBlockFromName("mysticalmechanics:axle_iron");
        event.getRegistry().register(new ShapedOreRecipe(getRL("gear_dawnstone"),new ItemStack(gear_dawnstone,1),true,new Object[]{
                " N ",
                "NCN",
                " N ",
                'C', "nuggetDawnstone",
                'N', "ingotDawnstone"}).setRegistryName(getRL("gear_dawnstone")));
        event.getRegistry().register(new ShapedOreRecipe(getRL("mech_actuator_single"),new ItemStack(mech_actuator_single,1),true,new Object[]{
                "SPI",
                'P', "gearIron",
                'S', RegistryManager.mech_accessor,
                'I', axle_iron}).setRegistryName(getRL("mech_actuator_single")));
        event.getRegistry().register(new ShapedOreRecipe(getRL("mech_actuator"),new ItemStack(mech_actuator,1),true,new Object[]{
                " I ",
                "IPI",
                "SI ",
                'P', "gearIron",
                'S', RegistryManager.mech_accessor,
                'I', axle_iron}).setRegistryName(getRL("mech_actuator")));
        event.getRegistry().register(new ShapedOreRecipe(getRL("steam_engine"),new ItemStack(steam_engine,1),true,new Object[]{
                " II",
                "APC",
                "FFC",
                'C', "plateCopper",
                'P', "gearIron",
                'A', axle_iron,
                'I', RegistryManager.pipe,
                'F', "plateIron"}).setRegistryName(getRL("steam_engine")));

        Ingredient stampGear = Ingredient.fromItem(RegistryManager.stamp_gear);
        int gearAmount = ConfigManager.stampGearAmount * RecipeRegistry.INGOT_AMOUNT;
        RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(Ingredient.EMPTY,new FluidStack(RegistryManager.fluid_molten_iron, gearAmount), stampGear, new ItemStack(RegistryHandler.IRON_GEAR,1)));
        RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(Ingredient.EMPTY,new FluidStack(RegistryManager.fluid_molten_gold, gearAmount), stampGear, new ItemStack(RegistryHandler.GOLD_GEAR,1)));
        RecipeRegistry.stampingRecipes.add(new ItemStampingRecipe(Ingredient.EMPTY,new FluidStack(RegistryManager.fluid_molten_dawnstone, gearAmount), stampGear, new ItemStack(MysticalMechanicsIntegration.gear_dawnstone,1)));
    }

    public static void registerAll() //éw parté déux
    {
        RegistryManager.blocks.add(steam_engine = (new BlockSteamEngine(Material.ROCK,"steam_engine",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
        RegistryManager.blocks.add(mech_actuator = (new BlockMechActuator(Material.ROCK,"mech_actuator",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
        RegistryManager.blocks.add(mech_actuator_single = (new BlockMechActuatorSingle(Material.ROCK,"mech_actuator_single",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));

        RegistryManager.items.add(gear_dawnstone = new ItemBase("gear_dawnstone",true));

        GameRegistry.registerTileEntity(TileEntitySteamEngine.class, Embers.MODID+":tile_entity_steam_engine");
        GameRegistry.registerTileEntity(TileEntityMechActuator.class, Embers.MODID+":tile_entity_mech_actuator");
        GameRegistry.registerTileEntity(TileEntityMechActuatorSingle.class, Embers.MODID+":tile_entity_mech_actuator_single");
    }

    public static void init()
    {
        OreDictionary.registerOre("gearDawnstone",gear_dawnstone);

        MysticalMechanicsAPI.IMPL.registerGear(DAWNSTONE_GEAR_BEHAVIOR, new OreIngredient("gearDawnstone"), new IGearBehavior() {
            @Override
            public double transformPower(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear, IGearData data, double power) {
                return power;
            }

            @Override
            public void visualUpdate(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear, IGearData data, double powerIn, double powerOut) {
                int particles = Math.min((int)Math.ceil(powerIn / 40),5);
                if(powerIn >= IRON_GEAR_MAX_POWER)
                    for(int i = 0; i < particles; i++) {
                        float xOff = 0.1f+random.nextFloat()*0.8f;
                        float yOff = 0.1f+random.nextFloat()*0.8f;
                        float zOff = 0.1f+random.nextFloat()*0.8f;
                        switch (facing.getAxis()) {
                            case X:
                                xOff = 0.5f + facing.getFrontOffsetX() / 2.0f; break;
                            case Y:
                                yOff = 0.5f + facing.getFrontOffsetY() / 2.0f; break;
                            case Z:
                                zOff = 0.5f + facing.getFrontOffsetZ() / 2.0f; break;
                        }
                        ParticleUtil.spawnParticleGlow(tile.getWorld(), tile.getPos().getX() + xOff, tile.getPos().getY() + yOff, tile.getPos().getZ() + zOff, 0, 0, 0, 255, 64, 16, 2.0f, 24);
                    }
            }
        });

        replaceBehavior(IRON_GEAR_BEHAVIOR,new OreIngredient("gearIron"),behavior -> wrapPowerLevelBehavior(behavior,IRON_GEAR_MAX_POWER,1));
        replaceBehavior(GOLD_GEAR_BEHAVIOR,new OreIngredient("gearGold"),behavior -> wrapPowerLevelBehavior(behavior,GOLD_GEAR_MAX_POWER,1));
        replaceBehavior(GOLD_GEAR_ON_BEHAVIOR,Ingredient.fromItem(RegistryHandler.GOLD_GEAR_ON),behavior -> wrapPowerLevelBehavior(behavior,GOLD_GEAR_MAX_POWER,1));
        replaceBehavior(GOLD_GEAR_OFF_BEHAVIOR,Ingredient.fromItem(RegistryHandler.GOLD_GEAR_OFF),behavior -> wrapPowerLevelBehavior(behavior,GOLD_GEAR_MAX_POWER,1));
    }

    private static void replaceBehavior(ResourceLocation name, Ingredient ingredient, Function<IGearBehavior, IGearBehavior> transformer)
    {
        IGearBehavior behavior = MysticalMechanicsAPI.IMPL.getGearBehavior(name);
        MysticalMechanicsAPI.IMPL.unregisterGear(name);
        MysticalMechanicsAPI.IMPL.registerGear(name, ingredient,transformer.apply(behavior));
    }

    private static IGearBehavior wrapPowerLevelBehavior(IGearBehavior behavior, double max_power, double slope) {
        return new IGearBehavior() {
            @Override
            public double transformPower(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear, IGearData data, double power) {
                power = behavior.transformPower(tile, facing, gear, data, power);
                return Misc.getDiminishedPower(power,max_power,slope); //Diminishing returns
            }

            @Override
            public void visualUpdate(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear, IGearData data, double powerIn, double powerOut) {
                behavior.visualUpdate(tile,facing,gear,data,powerIn,powerOut);
            }
        };
    }

    @SideOnly(Side.CLIENT)
    public static void registerClientSide()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechActuator.class, new TileEntityMechActuatorRenderer());
    }

    public static void initMysticalMechanicsCategory() {
        ResearchManager.gearbox = new ResearchBase("gearbox", new ItemStack(RegistryHandler.GEARBOX_FRAME), 2, 4);
        ResearchManager.mergebox = new ResearchBase("mergebox", new ItemStack(RegistryHandler.MERGEBOX_FRAME), 1, 6).addAncestor(ResearchManager.gearbox);
        ResearchManager.axle_iron = new ResearchBase("axle_iron", new ItemStack(RegistryHandler.IRON_AXLE), 2, 0).addAncestor(ResearchManager.gearbox);
        ItemStack gearIron = new ItemStack(RegistryHandler.IRON_GEAR);
        ItemStack gearGold = new ItemStack(RegistryHandler.GOLD_GEAR);
        ItemStack gearDawnstone = new ItemStack(gear_dawnstone);
        ItemStack gearGoldOn = new ItemStack(RegistryHandler.GOLD_GEAR_ON);
        ItemStack gearGoldOff = new ItemStack(RegistryHandler.GOLD_GEAR_OFF);
        ResearchManager.gear_iron = new ResearchShowItem("gear_iron", gearIron, 4, 1).addItem(new ResearchShowItem.DisplayItem(gearIron)).addAncestor(ResearchManager.gearbox)
                .addPage(new ResearchShowItem("gear_gold", gearGold,0,0).addItem(new ResearchShowItem.DisplayItem(gearGold)))
                .addPage(new ResearchShowItem("gear_redstone", gearGoldOn,0,0).addItem(new ResearchShowItem.DisplayItem(gearGoldOff,gearGoldOn)))
                .addPage(new ResearchShowItem("gear_dawnstone", gearDawnstone,0,0).addItem(new ResearchShowItem.DisplayItem(gearDawnstone)));
        ResearchManager.actuator = new ResearchBase("actuator", new ItemStack(mech_actuator), 9, 5).addAncestor(ResearchManager.gearbox)
                .addPage(new ResearchShowItem("actuator_bore",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.ember_bore))))
                .addPage(new ResearchShowItem("actuator_pump",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.mechanical_pump))))
                .addPage(new ResearchShowItem("actuator_stamper",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.stamper))))
                .addPage(new ResearchShowItem("actuator_mixer",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.mixer))))
                .addPage(new ResearchShowItem("actuator_auto_hammer",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.auto_hammer))));
        ResearchFakePage mechanical_mini_boiler = new ResearchFakePage(ResearchManager.mini_boiler, 12, 0);
        ResearchManager.steam_engine = new ResearchBase("steam_engine", new ItemStack(steam_engine), 9, 2).addAncestor(ResearchManager.gearbox).addAncestor(mechanical_mini_boiler)
                .addPage(new ResearchBase("steam_engine_overclock",ItemStack.EMPTY,0,0));


        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.gearbox);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.mergebox);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.axle_iron);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.gear_iron);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.actuator);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.steam_engine);

        ResearchManager.subCategoryMechanicalPower.addResearch(mechanical_mini_boiler);
    }

    public static void addCapabilityInformation(List<String> text, TileEntity tileEntity, EnumFacing facing) {
        addCapabilityMechanicalDescription(text,tileEntity,facing);
    }

    public static void addCapabilityMechanicalDescription(List<String> text, TileEntity tile, EnumFacing facing) {
        Capability<IMechCapability> capability = MysticalMechanicsAPI.MECH_CAPABILITY;
        if(tile.hasCapability(capability,facing)) {
            IMechCapability mechCapability = tile.getCapability(capability,facing);
            boolean canInput = mechCapability.isInput(facing);
            boolean canOutput = mechCapability.isOutput(facing);
            IExtraCapabilityInformation.EnumIOType ioType = getEnumIOType(canInput, canOutput);
            if(tile instanceof IExtraCapabilityInformation && ((IExtraCapabilityInformation) tile).hasCapabilityDescription(capability)) {
                ((IExtraCapabilityInformation) tile).addCapabilityDescription(text, capability,facing);
            } else {
                text.add(IExtraCapabilityInformation.formatCapability(ioType, "embers.tooltip.goggles.mechanical", null));
            }
        }
    }

    private static IExtraCapabilityInformation.EnumIOType getEnumIOType(boolean canInput, boolean canOutput) {
        IExtraCapabilityInformation.EnumIOType ioType;
        if(canInput && canOutput)
            ioType = IExtraCapabilityInformation.EnumIOType.BOTH;
        else if(canInput)
            ioType = IExtraCapabilityInformation.EnumIOType.INPUT;
        else if(canOutput)
            ioType = IExtraCapabilityInformation.EnumIOType.OUTPUT;
        else
            ioType = IExtraCapabilityInformation.EnumIOType.NONE;
        return ioType;
    }
}
