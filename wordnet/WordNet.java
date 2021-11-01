import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.TreeSet;
import java.util.ArrayList;

public class WordNet {
	
	private final Digraph graph;
	private final TreeSet<Noun> nouns;
	private final SAP sap;
	private final ArrayList<String> syns;

	// contructor takes the name of two input files
	public WordNet(String synsets, String hypernyms) {
		// corner cases
		if (synsets == null || hypernyms == null) 
			throw new IllegalArgumentException();

		In file1 = new In(synsets);
		String[] fields;
		nouns = new TreeSet<>();
		int lines = 0;
		syns = new ArrayList<>();

		// populate the nouns list
		while (!file1.isEmpty()) {
			lines++;
			fields = file1.readLine().split(",");
			int id = Integer.parseInt(fields[0]);
			// add for ancestor retrieval
			syns.add(fields[1]);
			fields = fields[1].split(" ");
			for (int i = 0; i < fields.length; i++) {
				if (nouns.contains(new Noun(fields[i]))) {
					Noun n = nouns.ceiling(new Noun(fields[i]));
					n.addID(id);
				}
				else {
					Noun n = new Noun(fields[i]);
					n.addID(id);
					nouns.add(n);
				}
			}
		}

		graph = new Digraph(lines);
		In file2 = new In(hypernyms);
		boolean[] root = new boolean[lines]; 

		// add edges to the graph
		while (!file2.isEmpty()) {
			fields = file2.readLine().split(",");
			int x = Integer.parseInt(fields[0]);
			root[x] = true;

			for (int i = 1; i < fields.length; i++) {
				graph.addEdge(x, Integer.parseInt(fields[i]));
			}
		}

		// check if this is a DAG
		DirectedCycle d = new DirectedCycle(graph);
		if (d.hasCycle()) throw new IllegalArgumentException();

		int rootCount = 0;
		// check if it has one root
		for (int i = 0; i < lines; i++) {
			if (!root[i]) rootCount++;
		}
		if (rootCount > 1) throw new IllegalArgumentException();

		sap = new SAP(graph);
	}

	private class Noun implements Comparable<Noun> {
		private final String noun;
		private final ArrayList<Integer> ids;

		public Noun(String noun) {
			this.noun = noun;
			ids = new ArrayList<>();
		}

		public void addID(int id) {
			ids.add(id);
		}

		public int compareTo(Noun that) {
			return this.noun.compareTo(that.noun);
		}
	}
	
	// returns all WordNet nouns
	public Iterable<String> nouns() {
		ArrayList<String> list = new ArrayList<>();
		for (Noun n : nouns) list.add(n.noun);
		return list;
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) throw new IllegalArgumentException();
		Noun n = new Noun(word);
		return nouns.contains(n);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		Noun n1 = nouns.ceiling(new Noun(nounA));
		Noun n2 = nouns.ceiling(new Noun(nounB));
		return sap.length(n1.ids, n2.ids);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		Noun n1 = nouns.ceiling(new Noun(nounA));
		Noun n2 = nouns.ceiling(new Noun(nounB));
		return syns.get(sap.ancestor(n1.ids, n2.ids));
	}

	// do unit testing of this class
	public static void main(String[] args) {

		WordNet wordnet = new WordNet(args[0], args[1]);

		int x = wordnet.distance("a", "b");

		System.out.printf("Distance between %s and %s is %d\n", "a", "b", x);

		System.out.printf("Number of synsets: %d\n", wordnet.nouns.size());


	}
}
