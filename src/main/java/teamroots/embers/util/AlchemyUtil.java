package teamroots.embers.util;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.block.BlockAlchemyPedestal;
import teamroots.embers.tileentity.TileEntityAlchemyPedestal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlchemyUtil {
    static HashMap<String,AspectInfo> ASPECT_REGISTRY = new HashMap<>();

    public static void registerAspect(String aspect, Ingredient item)
    {
        ASPECT_REGISTRY.put(aspect,new AspectInfo(aspect,item));
    }

    public static String getAspect(ItemStack aspect)
    {
        for (AspectInfo info : ASPECT_REGISTRY.values()) {
            if(info.matches(aspect))
                return info.id;
        }

        return null;
    }

    public static List<ItemStack> getAspectStacks(String aspect) {
        AspectInfo info = ASPECT_REGISTRY.get(aspect);
        return info != null ? Lists.newArrayList(info.getStacks()) : new ArrayList<>();
    }

    public static List<TileEntityAlchemyPedestal> getNearbyPedestals(World world, BlockPos pos){
        ArrayList<TileEntityAlchemyPedestal> pedestals = new ArrayList<>();
        BlockPos.MutableBlockPos pedestalPos = new BlockPos.MutableBlockPos(pos);
        for (int i = -3; i < 4; i ++){
            for (int j = -3; j < 4; j ++){
                pedestalPos.setPos(pos.getX()+i,pos.getY(),pos.getZ()+j);
                IBlockState state = world.getBlockState(pedestalPos);
                if(state.getBlock() instanceof BlockAlchemyPedestal)
                {
                    if(!state.getValue(BlockAlchemyPedestal.isTop))
                        pedestalPos.move(EnumFacing.UP);
                    TileEntity tile = world.getTileEntity(pedestalPos);
                    if (tile instanceof TileEntityAlchemyPedestal){
                        pedestals.add((TileEntityAlchemyPedestal)tile);
                    }
                }

            }
        }
        return pedestals;
    }

    public static class AspectInfo
    {
        public String id;
        public Ingredient matcher;

        public AspectInfo(String id, Ingredient matcher) {
            this.id = id;
            this.matcher = matcher;
        }

        public boolean matches(ItemStack stack)
        {
            return matcher.apply(stack);
        }

        public ItemStack[] getStacks()
        {
            return matcher.getMatchingStacks();
        }
    }
}

