package teamroots.embers.compat;

import mysticalmechanics.api.IGearBehavior;
import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import mysticalmechanics.handler.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.block.BlockMechActuator;
import teamroots.embers.block.BlockSteamEngine;
import teamroots.embers.item.ItemBase;
import teamroots.embers.particle.ParticleUtil;
import teamroots.embers.research.ResearchBase;
import teamroots.embers.research.ResearchManager;
import teamroots.embers.research.subtypes.ResearchFakePage;
import teamroots.embers.research.subtypes.ResearchShowItem;
import teamroots.embers.tileentity.TileEntityMechActuator;
import teamroots.embers.tileentity.TileEntityMechActuatorRenderer;
import teamroots.embers.tileentity.TileEntitySteamEngine;
import teamroots.embers.util.Misc;

import javax.annotation.Nullable;
import java.util.Random;

public class MysticalMechanicsIntegration {
    public static final ResourceLocation IRON_GEAR_BEHAVIOR = new ResourceLocation("mysticalmechanics", "gear_iron");
    public static final ResourceLocation DAWNSTONE_GEAR_BEHAVIOR = new ResourceLocation(Embers.MODID, "gear_dawnstone");
    public static final int IRON_GEAR_MAX_POWER = 80;

    public static Item gear_dawnstone;

    public static Block steam_engine;
    public static Block mech_actuator;

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
        event.getRegistry().register(new ShapedOreRecipe(getRL("mech_actuator"),new ItemStack(mech_actuator,1),true,new Object[]{
                "SPI",
                'P', "gearIron",
                'S', RegistryManager.mech_accessor,
                'I', axle_iron}).setRegistryName(getRL("mech_actuator")));
        event.getRegistry().register(new ShapedOreRecipe(getRL("steam_engine"),new ItemStack(steam_engine,1),true,new Object[]{
                " II",
                "APC",
                "FFC",
                'C', "plateCopper", //This recipe crashes on load
                'P', "gearIron",    //And basically you're fucking stupid
                'A', axle_iron,
                'I', RegistryManager.pipe,
                'F', "plateIron"}).setRegistryName(getRL("steam_engine")));
    }

    public static void registerAll() //éw parté déux
    {
        RegistryManager.blocks.add(steam_engine = (new BlockSteamEngine(Material.ROCK,"steam_engine",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));
        RegistryManager.blocks.add(mech_actuator = (new BlockMechActuator(Material.ROCK,"mech_actuator",true)).setIsFullCube(false).setIsOpaqueCube(false).setHarvestProperties("pickaxe", 0).setHardness(1.0f));

        RegistryManager.items.add(gear_dawnstone = new ItemBase("gear_dawnstone",true));

        GameRegistry.registerTileEntity(TileEntitySteamEngine.class, Embers.MODID+":tile_entity_steam_engine");
        GameRegistry.registerTileEntity(TileEntityMechActuator.class, Embers.MODID+":tile_entity_mech_actuator");
    }

    public static void init()
    {
        OreDictionary.registerOre("gearDawnstone",gear_dawnstone);

        IGearBehavior ironGear = MysticalMechanicsAPI.IMPL.getGearBehavior(IRON_GEAR_BEHAVIOR);
        MysticalMechanicsAPI.IMPL.unregisterGear(IRON_GEAR_BEHAVIOR);

        MysticalMechanicsAPI.IMPL.registerGear(DAWNSTONE_GEAR_BEHAVIOR, new OreIngredient("gearDawnstone"), new IGearBehavior() {
            @Override
            public double transformPower(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear, double power) {
                return power;
            }

            @Override
            public void visualUpdate(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear) {
                if(facing != null && tile.getWorld().isRemote && tile.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY,facing)) {
                    IMechCapability capability = tile.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY,facing);
                    double power = capability.getPower(facing);
                    int particles = Math.min((int)Math.ceil(power / 40),5);
                    if(power >= IRON_GEAR_MAX_POWER)
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
            }
        });
        MysticalMechanicsAPI.IMPL.registerGear(IRON_GEAR_BEHAVIOR, new OreIngredient("gearIron"), new IGearBehavior() {
            @Override
            public double transformPower(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear, double power) {
                power = ironGear.transformPower(tile, facing, gear, power);
                return Misc.getDiminishedPower(power,IRON_GEAR_MAX_POWER,1); //Diminishing returns
            }

            @Override
            public void visualUpdate(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear) {
                ironGear.visualUpdate(tile,facing,gear);
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void registerClientSide()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMechActuator.class, new TileEntityMechActuatorRenderer());
    }

    public static void initMysticalMechanicsCategory() {
        ResearchManager.gearbox = new ResearchBase("gearbox", new ItemStack(RegistryHandler.GEARBOX_FRAME), 2, 4);
        ResearchManager.axle_iron = new ResearchBase("axle_iron", new ItemStack(RegistryHandler.IRON_AXLE), 2, 0).addAncestor(ResearchManager.gearbox);
        ResearchManager.gear_iron = new ResearchShowItem("gear_iron", new ItemStack(RegistryHandler.IRON_GEAR), 4, 1).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryHandler.IRON_GEAR))).addAncestor(ResearchManager.gearbox)
                .addPage(new ResearchShowItem("gear_dawnstone",new ItemStack(gear_dawnstone),0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(gear_dawnstone))));
        ResearchManager.actuator = new ResearchBase("actuator", new ItemStack(mech_actuator), 9, 5).addAncestor(ResearchManager.gearbox)
                .addPage(new ResearchShowItem("actuator_bore",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.ember_bore))))
                .addPage(new ResearchShowItem("actuator_pump",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.mechanical_pump))))
                .addPage(new ResearchShowItem("actuator_stamper",ItemStack.EMPTY,0,0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.stamper))));
        ResearchFakePage mechanical_mini_boiler = new ResearchFakePage(ResearchManager.mini_boiler, 12, 0);
        ResearchManager.steam_engine = new ResearchBase("steam_engine", new ItemStack(steam_engine), 9, 2).addAncestor(ResearchManager.gearbox).addAncestor(mechanical_mini_boiler)
                .addPage(new ResearchBase("steam_engine_overclock",ItemStack.EMPTY,0,0));


        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.gearbox);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.axle_iron);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.gear_iron);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.actuator);
        ResearchManager.subCategoryMechanicalPower.addResearch(ResearchManager.steam_engine);

        ResearchManager.subCategoryMechanicalPower.addResearch(mechanical_mini_boiler);
    }
}
