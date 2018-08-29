package teamroots.embers.util;

public enum EnumPipeConnection {
    NONE(0),
    PIPE(1),
    BLOCK(2),
    LEVER(3),
    FORCENONE(4),
    NEIGHBORNONE(5);

    int index;

    EnumPipeConnection(int index) {
        this.index = index;
    }

    public static EnumPipeConnection[] VALUES = new EnumPipeConnection[]{NONE,PIPE,BLOCK,LEVER,FORCENONE,NEIGHBORNONE};

    public static EnumPipeConnection fromIndex(int index) {
        return VALUES[index];
    }

    public int getIndex() {
        return index;
    }
}
