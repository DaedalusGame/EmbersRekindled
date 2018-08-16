package teamroots.embers.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mysticalmechanics.api.IGearBehavior;
import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.IEmberItem;
import teamroots.embers.item.IInventoryEmberCell;
import teamroots.embers.item.ItemBase;
import teamroots.embers.item.ItemEmberBulb;
import teamroots.embers.item.bauble.ItemEmberAmulet;
import teamroots.embers.item.bauble.ItemEmberBelt;
import teamroots.embers.item.bauble.ItemEmberRing;

import javax.annotation.Nullable;

public class MysticalMechanicsIntegration {
    public static Item gear_dawnstone;

    public static ResourceLocation getRL(String s){
        return new ResourceLocation(Embers.MODID,s);
    }

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new ShapedOreRecipe(getRL("gear_dawnstone"),new ItemStack(gear_dawnstone,1),true,new Object[]{
                " N ",
                "NCN",
                " N ",
                'C', "nuggetDawnstone",
                'N', "ingotDawnstone"}).setRegistryName(getRL("gear_dawnstone")));
    }

    public static void registerAll() //éw parté déux
    {
        RegistryManager.items.add(gear_dawnstone = new ItemBase("gear_dawnstone",true));
    }

    public static void init()
    {
        OreDictionary.registerOre("gearDawnstone",gear_dawnstone);
        MysticalMechanicsAPI.IMPL.registerGear(new ResourceLocation(Embers.MODID, "gear_dawnstone"), new OreIngredient("gearDawnstone"), new IGearBehavior() {
            @Override
            public double transformPower(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear, double power) {
                return power;
            }

            @Override
            public void visualUpdate(TileEntity tile, @Nullable EnumFacing facing, ItemStack gear) {
                //NOOP
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void registerClientSide()
    {

    }
}
