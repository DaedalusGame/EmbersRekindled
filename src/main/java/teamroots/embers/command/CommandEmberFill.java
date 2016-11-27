package teamroots.embers.command;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import teamroots.embers.Embers;
import teamroots.embers.world.EmberWorldData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author WireSegal
 *         Created at 6:21 PM on 11/26/16.
 */
public class CommandEmberFill extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "ember-fill";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return Embers.MODID + ".commands.fill.usage";
    }

    @Nonnull
    private String getCommandResult(@Nonnull String action) {
        return Embers.MODID + ".commands.fill." + action;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length > 2) {
            BlockPos pos = sender.getPosition();
            int x = (int) parseDouble(pos.getX(), args[0], -3000000, 3000000, false);
            int z = (int) parseDouble(pos.getZ(), args[1], -3000000, 3000000, false);

            int chunkX = x / 16;
            int chunkZ = z / 16;

            EmberWorldData data = EmberWorldData.get(sender.getEntityWorld());
            String key = "" + chunkX + " " + chunkZ;

            if ("set".equals(args[2])) {
                if (args.length > 3) {
                    double level;

                    if ("empty".equals(args[3]))
                        level = 0;
                    else if ("filled".equals(args[3]))
                        level = 10240000;
                    else
                        level = parseDouble(args[3], 0);

                    if (data.emberData.containsKey(key)) {
                        data.emberData.put(key, level);

                        notifyCommandListener(sender, this, getCommandResult("set"), level, chunkX, chunkZ, x, z);

                        return;
                    } else
                        throw new NumberInvalidException(getCommandResult("failed"), chunkX, chunkZ, x, z);
                } else
                    throw new WrongUsageException(getCommandUsage(sender) + ".set");
            } else if ("add".equals(args[2])) {
                if (args.length > 3) {
                    double level = parseDouble(args[1]);

                    if (data.emberData.containsKey(key)) {
                        double previous = data.emberData.get(key);
                        double newLevel = Math.max(0, previous + level);

                        data.emberData.put(key, newLevel);

                        notifyCommandListener(sender, this, getCommandResult("add"), level, newLevel, chunkX, chunkZ, x, z);

                        return;
                    } else
                        throw new NumberInvalidException(getCommandResult("failed"), chunkX, chunkZ, x, z);
                } else
                    throw new WrongUsageException(getCommandUsage(sender) + ".add");
            } else if ("query".equals(args[2])) {
                if (data.emberData.containsKey(key)) {
                    double level = data.emberData.get(key);

                    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, (int) level);
                    notifyCommandListener(sender, this, getCommandResult("query"), level, chunkX, chunkZ, x, z);

                    return;
                } else
                    throw new NumberInvalidException(getCommandResult("failed"), chunkX, chunkZ, x, z);
            }
        }

        throw new WrongUsageException(getCommandUsage(sender));
    }

    @Nonnull
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1)
            return getTabCompletionCoordinate(args, 0, sender.getPosition());
        else if (args.length == 2)
            return getTabCompletionCoordinate(args, -1, sender.getPosition());
        else if (args.length == 3)
            return getListOfStringsMatchingLastWord(args, "set", "add", "query");
        return Collections.emptyList();
    }
}
