package teamroots.embers.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Quaternion;

public class TurretHelper {
    Quaternion angleStart = new Quaternion(0, 1, 0, 0);
    Quaternion angleEnd = new Quaternion(0, 1, 0, 0);
    float slide;
    float slideLast;

    Vec3d front;
    Vec3d up;

    public TurretHelper(Vec3d front, Vec3d up) {
        this.front = front;
        this.up = up;
    }

    public Quaternion getCurrentAngle() {
        return Misc.slerp(angleStart, angleEnd, slide);
    }

    public Quaternion getCurrentAngle(float partialTicks) {
        return Misc.slerp(angleStart, angleEnd, (float) MathHelper.clampedLerp(slideLast, slide, partialTicks));
    }

    public static Vec3d getForward(Quaternion angle) {
        float x = 2 * (angle.x*angle.z + angle.w*angle.y);
        float y = 2 * (angle.y*angle.z - angle.w*angle.x);
        float z = 1 - 2 * (angle.x*angle.x + angle.y*angle.y);
        return new Vec3d(x,y,z).normalize();
    }

    public static Vec3d getUp(Quaternion angle) {
        float x = 2 * (angle.x*angle.y - angle.w*angle.z);
        float y = 1 - 2 * (angle.x*angle.x + angle.z*angle.z);
        float z = 2 * (angle.y*angle.z + angle.w*angle.x);
        return new Vec3d(x, y, z);
    }

    public static Vec3d getSide(Quaternion angle) {
        float x = 1 - 2 * (angle.y*angle.y + angle.z*angle.z);
        float y = 2 * (angle.x*angle.y + angle.w*angle.z);
        float z = 2 * (angle.x*angle.z - angle.w*angle.y);
        return new Vec3d(x, y, z);
    }

    public void rotateTowards(Quaternion angle) {
        angleStart = getCurrentAngle();
        angleEnd = angle;
        slide = 0;
    }

    public void rotateTowards(Vec3d forward) {
        forward = forward.normalize();
        Vec3d rotAxis = front.crossProduct(forward).normalize();
        if(rotAxis.lengthSquared() == 0)
            rotAxis = up;
        double dot = front.dotProduct(forward);
        double ang = Math.acos(dot);

        double s = Math.sin(ang/2.0);
        Vec3d u = rotAxis.normalize();
        rotateTowards(new Quaternion((float)(u.x * s), (float)(u.y * s), (float)(u.z * s), (float)Math.cos(ang / 2.0)));
    }

    public void update(float speed) {
        slideLast = slide;
        slide = Math.min(slide + speed, 1);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("angleStart", writeQuat(angleStart));
        compound.setTag("angleEnd", writeQuat(angleEnd));
        compound.setFloat("slide", slide);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        angleStart = readQuat(compound.getCompoundTag("angleStart"));
        angleEnd = readQuat(compound.getCompoundTag("angleEnd"));
        slide = compound.getFloat("slide");
    }

    public static NBTTagCompound writeQuat(Quaternion quaternion) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("x", quaternion.x);
        compound.setFloat("y", quaternion.y);
        compound.setFloat("z", quaternion.z);
        compound.setFloat("w", quaternion.w);
        return compound;
    }

    public static Quaternion readQuat(NBTTagCompound compound) {
        return new Quaternion(compound.getFloat("x"), compound.getFloat("y"), compound.getFloat("z"), compound.getFloat("w"));
    }
}
