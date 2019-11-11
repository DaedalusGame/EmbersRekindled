package teamroots.embers.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.DoubleFunction;

public class Spline {
    static class Point {
        public double index;
        public Vec3d point;
        public double distance;

        public Point(double index, Vec3d point) {
            this.index = index;
            this.point = point;
        }
    }

    DoubleFunction<Vec3d> function;
    ArrayList<Point> points = new ArrayList<>();
    HashMap<Double,Integer> pointMap = new HashMap<>();
    double pointInterval;
    double totalArcLength;

    public Spline(DoubleFunction<Vec3d> function) {
        this.function = function;
    }

    public double getTotalArcLength() {
        return totalArcLength;
    }

    public Vec3d getPoint(double arcLength) {
        return function.apply(getIndex(arcLength));
    }

    public double getIndex(double arcLength) {
        arcLength = MathHelper.clamp(arcLength,0, this.totalArcLength);
        double index = calculateIndex(arcLength);
        int closeIndex = pointMap.get(index);
        double dist = points.get(closeIndex).distance;
        if(arcLength == dist)
            return points.get(closeIndex).index;
        else if(arcLength < dist)
            return getIndexLeft(closeIndex,arcLength);
        else
            return getIndexRight(closeIndex,arcLength);
    }

    private double getIndexLeft(int rightIndex, double arcLength) {
        int leftIndex = rightIndex-1;
        double dist = points.get(leftIndex).distance;
        if(arcLength == dist)
            return points.get(leftIndex).index;
        else if(arcLength < dist)
            return getIndexLeft(leftIndex,arcLength);
        else
            return interpolateIndex(leftIndex,rightIndex,arcLength);
    }

    private double getIndexRight(int leftIndex, double arcLength) {
        int rightIndex = leftIndex+1;
        double dist = points.get(rightIndex).distance;
        if(arcLength == dist)
            return points.get(rightIndex).index;
        else if(arcLength < dist)
            return interpolateIndex(leftIndex,rightIndex,arcLength);
        else
            return getIndexRight(rightIndex,arcLength);
    }

    private double interpolateIndex(int leftIndex, int rightIndex, double arcLength) {
        Point leftPoint = points.get(leftIndex);
        Point rightPoint = points.get(rightIndex);

        double midpoint = (arcLength - leftPoint.distance) / (rightPoint.distance - leftPoint.distance);

        return MathHelper.clampedLerp(leftPoint.index,rightPoint.index,midpoint);
    }

    public double calculateIndex(double arcLength) {
        return Math.floor(arcLength / pointInterval) * pointInterval;
    }

    public void cachePoints(int minSegments, double maxDist, double cacheInterval) {
        pointInterval = cacheInterval;

        for(int i = 0; i < minSegments; i++) {
            double index = (double)i / (minSegments - 1);
            points.add(new Point(index, function.apply(index)));
        }

        for(int i = 0; i < points.size()-1;) {
            int e = i+1;

            Point pi = points.get(i);
            Point pe = points.get(e);

            double midpoint = (pi.index + pe.index) / 2;
            if(midpoint != pi.index && midpoint != pe.index && pi.point.squareDistanceTo(pe.point) > maxDist*maxDist) {
                points.add(e, new Point(midpoint,function.apply(midpoint)));
            } else {
                i++;
            }
        }

        double totalDistance = 0;
        for(int i = 0; i < points.size(); i++) {
            Point pi = points.get(i);
            if(i < points.size()-1) {
                int e = i + 1;
                Point pe = points.get(e);

                double distance = pi.point.distanceTo(pe.point);
                totalDistance += distance;
                pe.distance = totalDistance;
            }
            pointMap.put(calculateIndex(pi.distance),i);
        }
        totalArcLength = totalDistance;
        for(int i = 0; i * pointInterval < totalArcLength; i++) {
            double index = i * pointInterval;
            double lastIndex = (i-1) * pointInterval;
            pointMap.put(index,pointMap.getOrDefault(index,pointMap.get(lastIndex)));
        }
    }
}
