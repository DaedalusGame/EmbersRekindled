package teamroots.embers.api.filter;

public enum EnumFilterSetting {
    STRICT,
    FUZZY;

    public EnumFilterSetting rotate(int i) {
        return get(ordinal()+i);
    }

    public static EnumFilterSetting get(int i)
    {
        EnumFilterSetting[] settings = values();
        if(i < 0)
            i = (i % settings.length) + settings.length;
        return settings[i % settings.length];
    }
}
