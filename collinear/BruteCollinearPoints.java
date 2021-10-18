/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 17, 2021
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {

    private final LinkedList<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Point array reference null\n");

        if (points.length == 1) {
            if (points[0] == null) throw new IllegalArgumentException("Null point in array\n");
        }

        // check if any value in the array is null or if two points are equal
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == null || points[j] == null)
                    throw new IllegalArgumentException("Null point in array\n");
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Two points are equal");
            }
        }


        // make copy of points array
        Point[] sortedPoints = Arrays.copyOf(points, points.length);
        // sort points by natural order
        Arrays.sort(sortedPoints);
        // initialize segments list
        segments = new LinkedList<>();

        // iterate through all combinations of 4 points
        for (int i = 0; i < sortedPoints.length; i++) {
            for (int j = i + 1; j < sortedPoints.length; j++) {
                if (sortedPoints[i].compareTo(sortedPoints[j]) > 0) continue; // avoid duplication
                for (int k = j + 1; k < sortedPoints.length; k++) {
                    if (sortedPoints[i].compareTo(sortedPoints[k]) > 0)
                        continue; // avoid duplication
                    if (sortedPoints[i].slopeTo(sortedPoints[j]) != sortedPoints[j]
                            .slopeTo(sortedPoints[k]))
                        continue; // if first two points not collinear
                    for (int h = k + 1; h < sortedPoints.length; h++) {
                        if (sortedPoints[i].compareTo(sortedPoints[h]) > 0)
                            continue; // avoid duplication
                        // else first three collinear so check third and fourth
                        if (sortedPoints[k].slopeTo(sortedPoints[h]) == sortedPoints[j]
                                .slopeTo(sortedPoints[k])) {
                            segments.addLast(new LineSegment(sortedPoints[i], sortedPoints[h]));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        if (segments == null) return 0;
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    public static void main(String[] args) {
        Point[] p = new Point[3];
        p[0] = new Point(8, 9);
        p[1] = null;
        p[2] = null;

        BruteCollinearPoints collinear = new BruteCollinearPoints(p);

        for (LineSegment ls : collinear.segments) {
            System.out.println(ls.toString());
        }

    }
}
