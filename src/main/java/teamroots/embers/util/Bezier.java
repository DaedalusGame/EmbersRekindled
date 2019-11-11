package teamroots.embers.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

public class Bezier implements DoubleFunction<Vec3d> {
    static class BezierSegment {
        double p0, p1, p2, p3;

        public BezierSegment(double p0, double p1, double p2, double p3) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        public double apply(double value) {
            double ivalue = 1 - value;
            return ivalue * ivalue * ivalue * p0 +
                    3 * ivalue * ivalue * value * p1 +
                    3 * ivalue * value * value * p2 +
                    value * value * value * p3;
        }
    }

    public List<Vec3d> controlPoints;
    public List<BezierSegment> segmentsX;
    public List<BezierSegment> segmentsY;
    public List<BezierSegment> segmentsZ;

    public Bezier(List<Vec3d> controlPoints) {
        this.controlPoints = controlPoints;
        segmentsX = compute(controlPoints.stream().mapToDouble(p -> p.x).toArray());
        segmentsY = compute(controlPoints.stream().mapToDouble(p -> p.y).toArray());
        segmentsZ = compute(controlPoints.stream().mapToDouble(p -> p.z).toArray());
    }

    public Vec3d apply(double value) {
        value = MathHelper.clamp(value,0,1);
        double d = value * (controlPoints.size() - 1);

        int index = (int) d;
        double lerp = d % 1;
        if(value >= 1)
        {
            index = controlPoints.size() - 2;
            lerp = 1;
        }
        Vec3d point = new Vec3d(
                segmentsX.get(index).apply(lerp),
                segmentsY.get(index).apply(lerp),
                segmentsZ.get(index).apply(lerp)
        );
        return point;
    }

    public List<BezierSegment> compute(double[] k) {
        double[] p1 = new double[k.length];
        double[] p2 = new double[k.length];

        double[] a = new double[k.length];
        double[] b = new double[k.length];
        double[] c = new double[k.length];
        double[] r = new double[k.length];

        int n = k.length - 1;

        /*left most segment*/
        a[0] = 0;
        b[0] = 2;
        c[0] = 1;
        r[0] = k[0] + 2 * k[1];

        /*internal segments*/
        for (int i = 1; i < n - 1; i++) {
            a[i] = 1;
            b[i] = 4;
            c[i] = 1;
            r[i] = 4 * k[i] + 2 * k[i + 1];
        }

        /*right segment*/
        a[n - 1] = 2;
        b[n - 1] = 7;
        c[n - 1] = 0;
        r[n - 1] = 8 * k[n - 1] + k[n];

        /*solves Ax=b with the Thomas algorithm (from Wikipedia)*/
        for (int i = 1; i < n; i++) {
            double m = a[i] / b[i - 1];
            b[i] = b[i] - m * c[i - 1];
            r[i] = r[i] - m * r[i - 1];
        }

        p1[n - 1] = r[n - 1] / b[n - 1];

        for (int i = n - 2; i >= 0; --i)
            p1[i] = (r[i] - c[i] * p1[i + 1]) / b[i];

        for (int i = 0; i < n - 1; i++)
            p2[i] = 2 * k[i + 1] - p1[i + 1];

        p2[n - 1] = 0.5 * (k[n] + p1[n - 1]);

        List<BezierSegment> segments = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            segments.add(new BezierSegment(k[i], p1[i], p2[i], k[i + 1]));
        }

        return segments;
    }
}
