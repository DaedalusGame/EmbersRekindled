package teamroots.embers.util.sound;

public interface ISoundController {
    void playSound(int id);

    void stopSound(int id);

    boolean isSoundPlaying(int id);

    int[] getSoundIDs();

    default void handleSound() {
        for (int id : getSoundIDs()) {
            if(shouldPlaySound(id) && !isSoundPlaying(id))
                playSound(id);
            if(!shouldPlaySound(id) && isSoundPlaying(id))
                stopSound(id);
        }
    }

    default boolean shouldPlaySound(int id) {
        return false;
    }

    default float getCurrentVolume(int id, float volume) {
        return volume;
    }

    default float getCurrentPitch(int id, float pitch) {
        return pitch;
    }
}
