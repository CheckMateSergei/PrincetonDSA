/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 21, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> twoDTree;

    // construct an empty set of points
    public PointSET() {
        this.twoDTree = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return twoDTree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return twoDTree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Tried to add null point.");
        this.twoDTree.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Tried to query a null point.");
        return twoDTree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.twoDTree) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Input rectangle is null.");
        // store in list
        LinkedList<Point2D> inside = new LinkedList<>();

        for (Point2D p : twoDTree) {
            if (rect.contains(p)) inside.addLast(p); // if inside rectangle add
        }

        return inside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Parameter given is null.");
        if (this.twoDTree.isEmpty()) return null;

        double min = Double.POSITIVE_INFINITY;
        Point2D point = null;
        for (Point2D q : this.twoDTree) {
            if (q.equals(p)) return q; // I guess they will give us equal points
            double dist = p.distanceSquaredTo(q);
            if (min > dist) {
                min = dist;
                point = q;
            }
        }

        return point;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        System.out.println("Sorry pmd warnings ;)");

    }
}
