package teamroots.embers.util.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class ItemUseSound extends MovingSound {
    EntityLivingBase linkedEntity;
    Item itemType;

    public ItemUseSound(EntityLivingBase linkedEntity, Item itemType, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, float volume, float pitch) {
        super(soundIn, categoryIn);
        this.linkedEntity = linkedEntity;
        this.itemType = itemType;
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = repeat;
    }

    @Override
    public void update() {
        if(linkedEntity == null) {
            donePlaying = true;
            return;
        }
        ItemStack heldItem = linkedEntity.getHeldItem(linkedEntity.getActiveHand());
        if(linkedEntity.isDead || !linkedEntity.isHandActive() || heldItem.getItem() != itemType)
            donePlaying = true;
        xPosF = (float) linkedEntity.posX;
        yPosF = (float) linkedEntity.posY;
        zPosF = (float) linkedEntity.posZ;
    }
}
