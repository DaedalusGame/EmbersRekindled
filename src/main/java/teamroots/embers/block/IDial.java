package teamroots.embers.block;

@Deprecated
public interface IDial extends teamroots.embers.api.block.IDial {
    @Override
    default String getDialType() {
        return null;
    }
}
