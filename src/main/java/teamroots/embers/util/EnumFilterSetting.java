package teamroots.embers.util;

public enum EnumFilterSetting {
    STRICT,
    FUZZY;

    public EnumFilterSetting rotate(int i) {
        EnumFilterSetting[] settings = values();
        return settings[(ordinal()+i) % settings.length];
    }
}
