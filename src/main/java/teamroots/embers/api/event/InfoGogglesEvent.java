package teamroots.embers.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class InfoGogglesEvent extends Event {
    EntityPlayer player;
    boolean shouldDisplay;

    public InfoGogglesEvent(EntityPlayer player, boolean shouldDisplay) {
        this.player = player;
        this.shouldDisplay = shouldDisplay;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public boolean shouldDisplay() {
        return shouldDisplay;
    }

    public void setShouldDisplay(boolean shouldDisplay) {
        this.shouldDisplay = shouldDisplay;
    }
}
