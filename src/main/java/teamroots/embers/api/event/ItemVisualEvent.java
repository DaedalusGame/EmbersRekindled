package teamroots.embers.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.awt.*;

public class ItemVisualEvent extends Event {
    private EntityLivingBase entity;
    private EntityEquipmentSlot slot;
    private ItemStack item;
    private Color color;
    private SoundEvent sound;
    private float pitch;
    private float volume;
    private String state;

    public EntityLivingBase getEntity() {
        return entity;
    }

    public EntityEquipmentSlot getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getUseState() {
        return state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public SoundEvent getSound() {
        return sound;
    }

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setNoParticles() {
        this.color = new Color(0,0,0,0);
    }

    public void setNoSound() {
        this.sound = null;
        this.volume = 0;
        this.pitch = 0;
    }

    public boolean hasSound()
    {
        return sound != null;
    }

    public boolean hasParticles()
    {
        return color.getAlpha() > 0;
    }

    public ItemVisualEvent(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack item, Color color, SoundEvent sound, float pitch, float volume, String state) {
        this.entity = entity;
        this.slot = slot;
        this.item = item;
        this.color = color;
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
        this.state = state;
    }
}
