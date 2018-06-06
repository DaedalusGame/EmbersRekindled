package teamroots.embers.util.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class MachineSound extends PositionedSound implements ITickableSound {
    protected TileEntity boundTile;
    protected boolean donePlaying;
    protected int id;

    public MachineSound(TileEntity tile, int id, SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, boolean repeatIn, float xIn, float yIn, float zIn) {
        super(soundIn, categoryIn);
        this.boundTile = tile;
        this.id = id;
        this.volume = volumeIn;
        this.pitch = pitchIn;
        this.xPosF = xIn;
        this.yPosF = yIn;
        this.zPosF = zIn;
        this.repeat = repeatIn;
        this.attenuationType = AttenuationType.LINEAR;
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    @Override
    public void update() {
        if(boundTile == null || boundTile.isInvalid())
            donePlaying = true;
        else if(boundTile instanceof ISoundController) {
            ISoundController controller = (ISoundController) boundTile;
            if(!controller.shouldPlaySound(id))
                donePlaying = true;
            volume = controller.getCurrentVolume(id,volume);
            pitch = controller.getCurrentPitch(id,pitch);
        }
    }
}