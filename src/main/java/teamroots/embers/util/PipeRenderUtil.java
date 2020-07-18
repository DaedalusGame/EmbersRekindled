package teamroots.embers.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;

//TODO: maybe switch this over to models
public class PipeRenderUtil {
    public static StructBox up = new StructBox(0.375,0.625,0.375,0.625,1.0,0.625,new StructUV[]{new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16)});
    public static StructBox down = new StructBox(0.375,0.375,0.375,0.625,0,0.625,new StructUV[]{new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16)});
    public static StructBox north = new StructBox(0.375,0.375,0.375,0.625,0.625,0,new StructUV[]{new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16)});
    public static StructBox south = new StructBox(0.375,0.375,0.625,0.625,0.625,1.0,new StructUV[]{new StructUV(6,12,0,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16)});
    public static StructBox west = new StructBox(0.375,0.375,0.375,0,0.625,0.625,new StructUV[]{new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16)});
    public static StructBox east = new StructBox(0.625,0.375,0.375,1.0,0.625,0.625,new StructUV[]{new StructUV(12,12,16,6,16,16),new StructUV(12,12,16,6,16,16),new StructUV(0,12,6,16,16,16),new StructUV(0,12,6,16,16,16),new StructUV(12,12,16,16,16,16),new StructUV(12,12,16,16,16,16)});
    public static StructBox upEnd = new StructBox(0.3125,0.75,0.3125,0.6875,1.0,0.6875,new StructUV[]{new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16)});
    public static StructBox downEnd = new StructBox(0.3125,0.25,0.3125,0.6875,0,0.6875,new StructUV[]{new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16)});
    public static StructBox northEnd = new StructBox(0.3125,0.3125,0.25,0.6875,0.6875,0,new StructUV[]{new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16)});
    public static StructBox southEnd = new StructBox(0.3125,0.3125,0.75,0.6875,0.6875,1.0,new StructUV[]{new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16)});
    public static StructBox westEnd = new StructBox(0.25,0.3125,0.3125,0,0.6875,0.6875,new StructUV[]{new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16)});
    public static StructBox eastEnd = new StructBox(0.75,0.3125,0.3125,1.0,0.6875,0.6875,new StructUV[]{new StructUV(0,6,6,10,16,16),new StructUV(0,6,6,10,16,16),new StructUV(6,6,10,0,16,16),new StructUV(6,6,10,0,16,16),new StructUV(0,0,6,6,16,16),new StructUV(0,0,6,6,16,16)});

    private static StructBox getPipe(EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return down;
            case UP:
                return up;
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case WEST:
                return west;
            case EAST:
                return east;
            default:
                throw new RuntimeException();
        }
    }

    private static StructBox getPipeEnd(EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return downEnd;
            case UP:
                return upEnd;
            case NORTH:
                return northEnd;
            case SOUTH:
                return southEnd;
            case WEST:
                return westEnd;
            case EAST:
                return eastEnd;
            default:
                throw new RuntimeException();
        }
    }

    private static int[] getInversions(EnumFacing facing) {
        switch (facing) {
            case DOWN:
                return new int[]{-1,-1,1,1,1,1};
            case UP:
                return new int[]{1,1,1,1,1,1};
            case NORTH:
                return new int[]{1,1,1,1,1,1};
            case SOUTH:
                return new int[]{1,1,-1,-1,1,1};
            case WEST:
                return new int[]{1,1,1,1,1,1};
            case EAST:
                return new int[]{1,1,1,1,-1,-1};
            default:
                throw new RuntimeException();
        }
    }

    public static void addPipePart(BufferBuilder buffer, StructBox box, double x, double y, double z, EnumFacing facing) {
        int[] inversions = getInversions(facing);
        RenderUtil.addBox(buffer, x + box.x1, y + box.y1, z + box.z1, x + box.x2, y + box.y2, z + box.z2, box.textures, inversions);
    }

    public static void addPipe(BufferBuilder buffer, double x, double y, double z, EnumFacing facing) {
        addPipePart(buffer, getPipe(facing), x, y, z, facing);
    }

    public static void addPipeLip(BufferBuilder buffer, double x, double y, double z, EnumFacing facing) {
        addPipePart(buffer, getPipeEnd(facing), x, y, z, facing);
    }

    public static void addPipeEnd(BufferBuilder buffer, double x, double y, double z, EnumFacing facing) {
        addPipePart(buffer, getPipe(facing), x, y, z, facing);
        addPipePart(buffer, getPipeEnd(facing), x, y, z, facing);
    }


}
