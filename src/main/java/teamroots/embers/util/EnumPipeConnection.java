package teamroots.embers.util;

public enum EnumPipeConnection {
    NONE(0,false),
    PIPE(1,true),
    BLOCK(2,true),
    LEVER(3,false),
    FORCENONE(4,false),
    NEIGHBORNONE(5,false);

    int index;
    boolean canTransfer;

    EnumPipeConnection(int index, boolean canTransfer) {
        this.index = index;
        this.canTransfer = canTransfer;
    }

    public static EnumPipeConnection[] VALUES = new EnumPipeConnection[]{NONE,PIPE,BLOCK,LEVER,FORCENONE,NEIGHBORNONE};

    public static EnumPipeConnection fromIndex(int index) {
        return VALUES[index];
    }

    public int getIndex() {
        return index;
    }

    public boolean canTransfer() {
        return canTransfer;
    }
}
