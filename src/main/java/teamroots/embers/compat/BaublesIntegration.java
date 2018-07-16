package teamroots.embers.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import teamroots.embers.Embers;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.IEmberItem;
import teamroots.embers.item.IInventoryEmberCell;
import teamroots.embers.item.ItemEmberBulb;
import teamroots.embers.item.bauble.ItemEmberAmulet;
import teamroots.embers.item.bauble.ItemEmberBelt;
import teamroots.embers.item.bauble.ItemEmberRing;

public class BaublesIntegration {
    public static Item ember_amulet, ember_belt, ember_ring, mantle_bulb;

    public static ResourceLocation getRL(String s){
        return new ResourceLocation(Embers.MODID,s);
    }

    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new ShapedOreRecipe(getRL("ember_ring"),new ItemStack(ember_ring,1),true,new Object[]{
                "CN ",
                "N N",
                " N ",
                'C', RegistryManager.ember_cluster,
                'N', "nuggetDawnstone"}).setMirrored(true).setRegistryName(getRL("ember_ring")));
        event.getRegistry().register(new ShapedOreRecipe(getRL("ember_amulet"),new ItemStack(ember_amulet,1),true,new Object[]{
                " L ",
                "L L",
                "NCN",
                'C', RegistryManager.ember_cluster,
                'N', "nuggetDawnstone",
                'L', Items.LEATHER}).setRegistryName(getRL("ember_amulet")));
        event.getRegistry().register(new ShapedOreRecipe(getRL("ember_belt"),new ItemStack(ember_belt,1),true,new Object[]{
                "LIL",
                "L L",
                "PCP",
                'C', RegistryManager.ember_cluster,
                'I', "ingotDawnstone",
                'P', "plateDawnstone",
                'L', Items.LEATHER}).setRegistryName(getRL("ember_belt")));
        event.getRegistry().register(new ShapedOreRecipe(getRL("ember_bulb"),new ItemStack(mantle_bulb,1),true,new Object[]{
                " CI",
                "GSG",
                " G ",
                'I', "ingotLead",
                'S', RegistryManager.ember_cluster,
                'C', "plateDawnstone",
                'G', "blockGlass"}).setMirrored(true).setRegistryName(getRL("ember_bulb")));
    }

    public static void registerAll() //éw
    {
        RegistryManager.items.add(ember_ring = new ItemEmberRing("ember_ring",true));
        RegistryManager.items.add(ember_belt = new ItemEmberBelt("ember_belt",true));
        RegistryManager.items.add(ember_amulet = new ItemEmberAmulet("ember_amulet",true));
        RegistryManager.items.add(mantle_bulb = new ItemEmberBulb());
    }

    public static double getEmberCapacityTotal(EntityPlayer player){
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        double amount = 0;
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack bauble = handler.getStackInSlot(i);
            if (!bauble.isEmpty() && bauble.getItem() instanceof IInventoryEmberCell && bauble.getItem() instanceof IEmberItem) {
                amount += ((IEmberItem) bauble.getItem()).getEmberCapacity(bauble);
            }
        }
        return amount;
    }

    public static double getEmberTotal(EntityPlayer player){
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        double amount = 0;
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack bauble = handler.getStackInSlot(i);
            if (!bauble.isEmpty() && bauble.getItem() instanceof IInventoryEmberCell && bauble.getItem() instanceof IEmberItem) {
                amount += ((IEmberItem) bauble.getItem()).getEmber(bauble);
            }
        }
        return amount;
    }

    public static double removeEmber(EntityPlayer player, double amount){
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack bauble = handler.getStackInSlot(i);
            if (!bauble.isEmpty() && bauble.getItem() instanceof IInventoryEmberCell && bauble.getItem() instanceof IEmberItem) {
                amount -= ((IEmberItem) bauble.getItem()).removeAmount(bauble, amount, true);
            }
        }
        return amount;
    }
}
