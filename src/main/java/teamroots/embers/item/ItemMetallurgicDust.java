package teamroots.embers.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.embers.util.OreTransmutationUtil;

public class ItemMetallurgicDust extends ItemBase {
    public ItemMetallurgicDust(String name, boolean addToTab) {
        super(name, addToTab);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        } else {
            ItemStack itemstack = player.getHeldItem(hand);

            if (!player.canPlayerEdit(pos, facing, itemstack)) {
                return EnumActionResult.FAIL;
            } else {
                boolean success = OreTransmutationUtil.transmuteOres(worldIn,pos);

                if(success) {
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }
}
