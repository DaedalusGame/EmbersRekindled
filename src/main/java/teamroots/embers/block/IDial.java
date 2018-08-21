package teamroots.embers.block;

public interface IDial extends teamroots.embers.api.block.IDial {
    @Override
    default String getDialType() {
        return null;
    }
}
