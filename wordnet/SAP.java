import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
	private Digraph g;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph graph) {
		if (graph == null) throw new IllegalArgumentException();
		g = new Digraph(graph);
	}

	// length of the shortest ancestral path between v and w; -1 if no such path exists
	public int length(int v, int w) {
		if (v < 0 || w < 0) throw new IllegalArgumentException();
		if (v > g.V() - 1 || w > g.V() - 1) throw new IllegalArgumentException();
		if (v == w) return 0;

		// do bfs on both vertices
		DeluxeBFS bfsV = new DeluxeBFS(g, v);
		DeluxeBFS bfsW = new DeluxeBFS(g, w);
		int minDist = Integer.MAX_VALUE, current = Integer.MAX_VALUE, count = 0;

		for (int x = 0; x < g.V(); x++) {
			if (bfsV.hasPathTo(x) && bfsW.hasPathTo(x)) {
				count++;
				current = bfsV.distance(x) + bfsW.distance(x);
			}
			if (current < minDist) minDist = current;
		}

		// if count is zero no common ancestor
		if (count == 0) return -1;
		return minDist;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path exists
	public int ancestor(int v, int w) {
		if (v < 0 || w < 0) throw new IllegalArgumentException();
		if (v > g.V() - 1 || w > g.V() - 1) throw new IllegalArgumentException();
		if (v == w) return v;
			
		// do bfs on both vertices
		DeluxeBFS bfsV = new DeluxeBFS(g, v);
		DeluxeBFS bfsW = new DeluxeBFS(g, w);
		int minDist = Integer.MAX_VALUE, current = Integer.MAX_VALUE;
		int champion = -1;

		for (int x = 0; x < g.V(); x++) {
			if (bfsV.hasPathTo(x) && bfsW.hasPathTo(x)) {
				current = bfsV.distance(x) + bfsW.distance(x);
			}
			if (current < minDist) {
				minDist = current;
				champion = x;
			}
		}

		// returns -1 if no champion disocvered
		return champion;
	}

	// length of the shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path exists
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) throw new IllegalArgumentException();
		for (Integer i : v) {
			if (i == null) throw new IllegalArgumentException();
			if (i < 0) throw new IllegalArgumentException();
			if (i > g.V() - 1) throw new IllegalArgumentException();
		}
		for (Integer i : w) {
			if (i == null) throw new IllegalArgumentException();
			if (i < 0) throw new IllegalArgumentException();
			if (i > g.V() - 1) throw new IllegalArgumentException();
		}

		// do bfs on both vertices
		DeluxeBFS bfsV = new DeluxeBFS(g, v);
		DeluxeBFS bfsW = new DeluxeBFS(g, w);
		int minDist = Integer.MAX_VALUE, current = Integer.MAX_VALUE, count = 0;

		for (int x = 0; x < g.V(); x++) {
			if (bfsV.hasPathTo(x) && bfsW.hasPathTo(x)) {
				count++;
				current = bfsV.distance(x) + bfsW.distance(x);
			}
			if (current < minDist) minDist = current;
		}

		// if count is zero no common ancestor
		if (count == 0) return -1;
		return minDist;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path exists
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) throw new IllegalArgumentException();
		for (Integer i : v) {
			if (i == null) throw new IllegalArgumentException();
			if (i < 0) throw new IllegalArgumentException();
			if (i > g.V() - 1) throw new IllegalArgumentException();
		}
		for (Integer i : w) {
			if (i == null) throw new IllegalArgumentException();
			if (i < 0) throw new IllegalArgumentException();
			if (i > g.V() - 1) throw new IllegalArgumentException();
		}

		// do bfs on both vertices
		DeluxeBFS bfsV = new DeluxeBFS(g, v);
		DeluxeBFS bfsW = new DeluxeBFS(g, w);
		int minDist = Integer.MAX_VALUE, current = Integer.MAX_VALUE;
		int champion = -1;

		for (int x = 0; x < g.V(); x++) {
			if (bfsV.hasPathTo(x) && bfsW.hasPathTo(x)) {
				current = bfsV.distance(x) + bfsW.distance(x);
			}
			if (current < minDist) {
				minDist = current;
				champion = x;
			}
		}

		// returns -1 if no champion disocvered
		return champion;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);    
		Digraph G = new Digraph(in);
	    	SAP sap = new SAP(G);
	    	while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
        		int length   = sap.length(v, w);
		        int ancestor = sap.ancestor(v, w);
        		StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    		}
	}
}
