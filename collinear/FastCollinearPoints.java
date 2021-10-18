/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 17, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class FastCollinearPoints {

    private final LinkedList<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Point array reference null\n");

        if (points.length == 1)
            if (points[0] == null) throw new IllegalArgumentException("Null point in array\n");

        // check if any value in the array is null or if two points are equal
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == null || points[j] == null)
                    throw new IllegalArgumentException("Null point in array\n");
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Two points are equal");
            }
        }

        // store the line segments
        segments = getSegments(points);
    }

    private LinkedList<LineSegment> getSegments(Point[] points) {
        // initialize linked list
        LinkedList<LineSegment> lineSegments = new LinkedList<>();

        // make a copy of points and sort it by natural order
        Point[] copy = Arrays.copyOf(points, points.length);
        Arrays.sort(copy);
        // make a copy which will be ordered by slopeOrder
        Point[] slopeCopy = Arrays.copyOf(points, points.length);

        // for each point
        for (Point p : copy) {
            // sort by slopeOrder
            Comparator<Point> slopeOrder = p.slopeOrder();
            // sort by natural order then slope order
            Arrays.sort(slopeCopy);
            Arrays.sort(slopeCopy, slopeOrder);

            // store temporary slope for comparison
            double slope = p.slopeTo(slopeCopy[0]);
            // used to skip duplicates
            double skipSlope = Double.NEGATIVE_INFINITY;

            // loop through slopeOrdered array and count matching slopes
            int count = 1;
            for (int i = 1; i < points.length; i++) {
                // to avoid duplicates: if the origin point > point in slopeOrdered array
                // continue as this will add a duplicate, store the skipped slope and skip
                // other points with same slope as they will be duplicates
                if (skipSlope != Double.NEGATIVE_INFINITY && slope == skipSlope) {
                    skipSlope = p.slopeTo(slopeCopy[i]);
                    count = 0; // must reset count to zero as next iteration will increase to 1
                    if (i != points.length - 1) slope = p.slopeTo(slopeCopy[i + 1]);
                    continue; // if slope has been skipped skip it again
                }
                if (p.compareTo(slopeCopy[i]) > 0) {
                    if (count >= 3) {
                        lineSegments.addLast(new LineSegment(p, slopeCopy[i - 1]));
                    }
                    skipSlope = p.slopeTo(slopeCopy[i]);
                    count = 0; // must reset count to zero as next iteration will increase to 1
                    if (i != points.length - 1) slope = p.slopeTo(slopeCopy[i + 1]);
                    continue; // if p is greater than point[i] skip
                }
                // if we have reached the end of points
                if (i == points.length - 1) {
                    // if count >= 3
                    if (count >= 2 && slope == p.slopeTo(slopeCopy[i])) {
                        // adds segment to last element
                        lineSegments.addLast(new LineSegment(p, slopeCopy[i]));
                    }
                    else if (count >= 3) {
                        // adds segment to second last element
                        lineSegments.addLast(new LineSegment(p, slopeCopy[i - 1]));
                    }
                }
                // if the slopes match
                else if (slope == p.slopeTo(slopeCopy[i])) {
                    count++; // increment the count
                }
                // else the slopes don't match
                else {
                    // if we have counted 3 or more matching slopes in a row
                    if (count >= 3) {
                        lineSegments.addLast(new LineSegment(p, slopeCopy[i - 1]));
                    }
                    slope = p.slopeTo(slopeCopy[i]); // update slope
                    count = 1; // reset count
                }
            }
        }
        return lineSegments;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        /*

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        */

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        // StdDraw.show();

    }
}
