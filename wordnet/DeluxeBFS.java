import java.util.LinkedList;
import edu.princeton.cs.algs4.Digraph;

public class DeluxeBFS {
	private int[] distTo;
	private boolean[] marked;

	public DeluxeBFS(Digraph graph, int s) {
		Digraph g = new Digraph(graph);
		distTo = new int[g.V()];
		marked = new boolean[g.V()];

		bfs(g, s);
	}

	public DeluxeBFS(Digraph graph, Iterable<Integer> s) {
		Digraph g = new Digraph(graph);
		distTo = new int[g.V()];
		marked = new boolean[g.V()];

		bfs(g, s);
	}

	public void bfs(Digraph g, int s) {
		LinkedList<Integer> queue = new LinkedList<>();
		marked[s] = true;

		queue.addLast(s);

		while (!queue.isEmpty()) {
			int temp = queue.removeFirst();
			
			for (Integer i : g.adj(temp)) {
				if (!marked[i]) {
					distTo[i] = distTo[temp] + 1;
					marked[i] = true;
					queue.addLast(i);
				}
			}
		}
	}

	public void bfs(Digraph g, Iterable<Integer> s) {
		for (Integer i : s) marked[i] = true;

		LinkedList<Integer> queue = new LinkedList<>();
		for (Integer i : s) queue.addLast(i);

		while (!queue.isEmpty()) {
			int temp = queue.removeFirst();
			for (Integer i : g.adj(temp)) {
				if (!marked[i]) {
					distTo[i] = distTo[temp] + 1;
					marked[i] = true;
					queue.addLast(i);
				}
			}
		}
	}

	public boolean hasPathTo(int v) {
		return marked[v];
	}

	public int distance(int v) {
		if (!hasPathTo(v)) return -1;
		return distTo[v];
	}
}
