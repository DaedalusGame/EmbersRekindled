package teamroots.embers.itemmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import teamroots.embers.SoundManager;
import teamroots.embers.api.EmbersAPI;
import teamroots.embers.api.itemmod.ItemModUtil;
import teamroots.embers.api.itemmod.ModifierBase;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageResonatingBellFX;
import teamroots.embers.util.EmberInventoryUtil;

public class ModifierResonatingBell extends ModifierBase {

    public ModifierResonatingBell() {
        super(EnumType.TOOL, "resonating_bell", 5.0, true);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static HashMap<UUID, Float> cooldownTicksServer = new HashMap<>();

    public static void setCooldown(UUID uuid, float ticks) {
        cooldownTicksServer.put(uuid, ticks);
    }

    public static boolean hasCooldown(UUID uuid) {
        return cooldownTicksServer.getOrDefault(uuid, 0.0f) > 0;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (UUID uuid : cooldownTicksServer.keySet()) {
                Float ticks = cooldownTicksServer.get(uuid) - 1;
                cooldownTicksServer.put(uuid, ticks);
            }
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.RightClickBlock event) {
        ItemStack heldStack = event.getItemStack();
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        BlockPos pos = event.getPos();
        if (ItemModUtil.hasHeat(heldStack)) {
            int level = ItemModUtil.getModifierLevel(heldStack, EmbersAPI.RESONATING_BELL);
            UUID uuid = player.getUniqueID();
            if (!world.isRemote && level > 0 && EmberInventoryUtil.getEmberTotal(player) >= EmbersAPI.RESONATING_BELL.cost && !ModifierResonatingBell.hasCooldown(uuid)) {
                ModifierResonatingBell.setCooldown(uuid, 80);
                IBlockState state = world.getBlockState(pos);
                int count = 0;
                List<BlockPos> positions = new ArrayList<>();
                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos);
                int baseX = pos.getX();
                int baseY = pos.getY();
                int baseZ = pos.getZ();
                int blockLimit = 200 * level;
                for (int i = -(1 + 3 * level); i < (2 + 3 * level); i++) {
                    for (int j = -(1 + 3 * level); j < (2 + 3 * level); j++) {
                        for (int k = -(1 + 3 * level); k < (2 + 3 * level); k++) {
                            mutablePos.setPos(baseX + i, baseY + j, baseZ + k);
                            if (world.getBlockState(mutablePos) == state) {
                                positions.add(mutablePos.toImmutable());
                                count++;
                                if (count > blockLimit)
                                    break;
                            }
                        }
                    }
                }
                if (count <= blockLimit) {
                    for (BlockPos p : positions) {
                        PacketHandler.INSTANCE.sendToAll(new MessageResonatingBellFX(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5)); //TODO: Guess who gets to optimize this. The girl reading this.
                    }
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundManager.RESONATING_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
                EmberInventoryUtil.removeEmber(player, EmbersAPI.RESONATING_BELL.cost);
            }
        }
    }
}
