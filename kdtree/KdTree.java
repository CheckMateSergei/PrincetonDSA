/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 21, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class KdTree {

    private LinkedList<Point2D> inRange;
    private Point2D champion;
    private double dist;

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        this.root = null; // initialize empty tree
        this.size = 0;
    }

    private void put(Point2D p) {
        this.root = put(this.root, p, 1);
        this.root.r = new RectHV(0, 0, 1, 1);
    }

    // used to insert into our KdTree
    private Node put(Node node, Point2D p, int cmps) {
        // if last node to call was null, insert as child
        if (node == null) return new Node(p);
        int cmp;
        // keep track of compares to determine which coordinate to compare with
        if (cmps % 2 == 0) { // on an even level compare with y
            // compare y values
            cmp = Double.compare(node.getY(), p.y());
            cmps++; // increment cmps
            if (cmp <= 0) // if current node has smaller y recurse right
                node.right = put(node.right, p, cmps);
            if (cmp > 0) // if current node has bigger y recurse left
                node.left = put(node.left, p, cmps);

            // calculate the axis aligned rectangle
            // if the node just added is the right child
            if (node.right != null && node.right.isEqual(p))
                // if new child has greater y value
                node.right.r = new RectHV(node.r.xmin(), node.getY(), node.r.xmax(), node.r.ymax());
            // if new child has smaller y value
            if (node.left != null && node.left.isEqual(p))
                node.left.r = new RectHV(node.r.xmin(), node.r.ymin(), node.r.xmax(), node.getY());
        }
        else { // on an odd level compare with x
            // compare x values
            cmp = Double.compare(node.getX(), p.x());
            cmps++; // increment cmps
            if (cmp <= 0) // if current node has smaller x then recurse down right subtree
                node.right = put(node.right, p, cmps);
            if (cmp > 0) // if current node has bigger x then recurse down left subtree
                node.left = put(node.left, p, cmps); // note this doesn't recurse back up

            // calculate the axis aligned rectangle
            // if the node just added is the right child
            if (node.right != null && node.right.isEqual(p))
                // if new child has greater x value
                node.right.r = new RectHV(node.getX(), node.r.ymin(), node.r.xmax(), node.r.ymax());
            // if new child has smaller y value
            if (node.left != null && node.left.isEqual(p))
                node.left.r = new RectHV(node.r.xmin(), node.r.ymin(), node.getX(), node.r.ymax());
        }
        return node; // will only return the root to itself
    }

    // return the point within the tree, return null if not found
    private Point2D get(Point2D p) {
        Node node = root;
        int cmps = 1;
        int cmp;

        // continue as long as null link not hit
        while (node != null) {
            // check if we have found the node
            if (node.isEqual(p)) return node.p;
            // check which level
            if (cmps % 2 == 0) { // on even level
                // compare y values
                cmp = Double.compare(node.getY(), p.y());
                cmps++; // increment cmps
                if (cmp <= 0) // if current node has smaller y
                    node = node.right; // continue down right subtree
                if (cmp > 0) // if current node has bigger y
                    node = node.left; // continue down left subtree
            }
            else { // on odd level
                // compare x values
                cmp = Double.compare(node.getX(), p.x());
                cmps++; // increment cmps
                if (cmp <= 0) // if current node has smaller x
                    node = node.right; // continue down right subtree
                if (cmp > 0) // if current node has bigger y
                    node = node.left; // continue down left subtree
            }
        }
        return null; // if not found return null
    }

    private static class Node {
        private final Point2D p;
        private RectHV r;
        private Node left;
        private Node right;

        public Node(Point2D p) {
            this.p = p;
            this.left = null;
            this.right = null;
        }

        public double getX() {
            return p.x();
        }

        public double getY() {
            return p.y();
        }

        public boolean isEqual(Point2D point) {
            if (this.getX() == point.x() && this.getY() == point.y())
                return true;
            return false;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Tried to insert null point.");

        // check if p is in the set already
        if (get(p) != null) return; // do not add double points

        // call put with point p
        put(p); // calls the first put function then recurse through tree
        size++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Tried to query null point.");

        // check if get returns null
        if (get(p) == null) return false;
        return true; // return true if not
    }

    // draw all points to standard draw in level order
    public void draw() {
        if (root == null) throw new NoSuchElementException("Tree is empty.");

        Queue<Node> q = new Queue<>();
        q.enqueue(root);
        // keep track of level
        int level = 1;

        // draws the points and respective line segments in level order
        while (!q.isEmpty()) {
            Node temp = q.dequeue();

            // if we are on an odd level
            if (level % 2 != 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                // print vertical line
                StdDraw.line(temp.getX(), temp.r.ymin(), temp.getX(), temp.r.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                // print horizontal line
                StdDraw.line(temp.r.xmin(), temp.getY(), temp.r.xmax(), temp.getY());
            }
            // enqueue the children of temp if they exist, they will be the next drawn
            if (temp.left != null) q.enqueue(temp.left);
            if (temp.right != null) q.enqueue(temp.right);
            level++; // increment the level

            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            temp.p.draw();
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Input rectangle is null.");
        // store the points in an iterable
        inRange = new LinkedList<>();

        if (root == null) return inRange;

        if (rect.contains(root.p)) {
            inRange.addLast(root.p);
            range(rect, root.left);
            range(rect, root.right);
            return inRange;
        }
        if (root.left != null && rect.intersects(root.left.r)) {
            range(rect, root.left);
        }
        if (root.right != null && rect.intersects(root.right.r)) {
            range(rect, root.right);
        }
        return inRange;
    }

    private void range(RectHV rect, Node node) {
        if (node == null) return;

        if (rect.contains(node.p)) {
            inRange.addLast(node.p);
            range(rect, node.left);
            range(rect, node.right);
            return;
        }
        if (node.left != null && rect.intersects(node.left.r)) {
            range(rect, node.left);
        }
        if (node.right != null && rect.intersects(node.right.r)) {
            range(rect, node.right);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Input parameter is null.");

        if (root == null) return null;

        // could I optimize this by starting with the side the point is on?
        dist = root.p.distanceSquaredTo(p);
        champion = root.p;

        // System.out.println("Visited root.");

        if (root.right != null && root.left != null) {
            double d1, d2;
            d1 = root.left.r.distanceSquaredTo(p);
            d2 = root.right.r.distanceSquaredTo(p);
            // check which subtree to go down first
            if (d1 < d2) { // go down left first
                nearest(p, root.left);

                // check if necessary to check right subtree of root
                if (champion.distanceSquaredTo(p) < root.right.r.distanceSquaredTo(p))
                    return champion;

                // System.out.println("About to traverse right of root");

                // traverse right subtree if not
                nearest(p, root.right);
                return champion;
            }
            else {
                // go down right subtree first
                nearest(p, root.right);

                // check if necessary to check left subtree of root
                if (champion.distanceSquaredTo(p) < root.left.r.distanceSquaredTo(p))
                    return champion;

                // traverse left subtree if not
                nearest(p, root.left);
                return champion;
            }
        }

        // recurse down left subtree
        if (root.left != null && dist >= root.left.r.distanceSquaredTo(p)) nearest(p, root.left);

        // System.out.println("In original after recursing left. Champion is " + champion.toString());

        // recurse down right subtree
        if (root.right != null && dist >= root.right.r.distanceSquaredTo(p)) nearest(p, root.right);

        // System.out.println("In original after recursing right. Champion is " + champion.toString());

        return champion;
    }

    private void nearest(Point2D p, Node node) {
        double d = p.distanceSquaredTo(node.p);
        if (d < dist) {
            champion = node.p;
            dist = d;
        } // update champion

        // System.out.println("Visited " + node.p.toString());

        if (node.right != null && node.left != null) {
            double d1, d2;
            d1 = node.left.r.distanceSquaredTo(p);
            d2 = node.right.r.distanceSquaredTo(p);
            // check which subtree to go down first
            if (d1 < d2) { // go down left first
                nearest(p, node.left);

                // check if necessary to check right subtree of root
                if (champion.distanceSquaredTo(p) < node.right.r.distanceSquaredTo(p))
                    return;

                // System.out.println("About to traverse right of " + node.p.toString());

                // traverse right subtree if not
                nearest(p, node.right);
                return;
            }
            else {
                // go down right subtree first
                nearest(p, node.right);

                // check if necessary to check left subtree of root
                if (champion.distanceSquaredTo(p) < node.left.r.distanceSquaredTo(p))
                    return;

                // traverse left subtree if not
                nearest(p, node.left);
                return;
            }
        }


        // System.out.println("Champion before recursing left " + champion.toString());

        // recurse down left subtree
        if (node.left != null && dist >= node.left.r.distanceSquaredTo(p)) nearest(p, node.left);

        // System.out.println("Champion before recursing right " + champion.toString());


        if (node.right != null && dist >= node.right.r.distanceSquaredTo(p)) nearest(p, node.right);

        // System.out.println("Champion after recursing right " + champion.toString());

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree kd = new KdTree();

        RectHV r = new RectHV(0.05, 0.05, 0.9, 0.8);


        kd.insert(new Point2D(0.7, 0.2));
        kd.insert(new Point2D(0.5, 0.4));
        kd.insert(new Point2D(0.2, 0.3));
        kd.insert(new Point2D(0.4, 0.7));
        kd.insert(new Point2D(0.9, 0.6));


        /*
        kd.insert(new Point2D(0.372, 0.497));
        kd.insert(new Point2D(0.564, 0.413));
        kd.insert(new Point2D(0.226, 0.577));
        kd.insert(new Point2D(0.144, 0.179));
        kd.insert(new Point2D(0.083, 0.51));
        kd.insert(new Point2D(0.32, 0.708));
        kd.insert(new Point2D(0.417, 0.362));
        kd.insert(new Point2D(0.862, 0.825));
        kd.insert(new Point2D(0.785, 0.725));
        kd.insert(new Point2D(0.499, 0.208));

        kd.insert(new Point2D(0.375, 1.0));
        kd.insert(new Point2D(0.375, 0.0));
        kd.insert(new Point2D(0.375, 0.125));
        kd.insert(new Point2D(0.375, 0.25));
        kd.insert(new Point2D(1.0, 0.375));
         */


        System.out.printf("Tree contains (0.1, 0.1)? %b\n", kd.contains(new Point2D(0.1, 0.1)));

        System.out.printf("Tree contains (0.6, 0.9)? %b\n", kd.contains(new Point2D(0.6, 0.9)));

        System.out.print("The rectangle conatins:\n");
        for (Point2D p : kd.range(r)) {
            System.out.println(p.toString());
        }

        System.out.printf("Nearest point to (0.42, 0.82) is: %s\n",
                          kd.nearest(new Point2D(0.23, 0.91)));

        r.draw();

        kd.draw();

    }
}
