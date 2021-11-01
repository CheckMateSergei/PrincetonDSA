public class Outcast {
	private final WordNet wordnet;
	// constructor takes a wordnet object
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;	
	}

	// given an array of wordnet nouns, return an outcast
	public String outcast(String[] nouns) {
		int[] dist = new int[nouns.length];

		for (int i = 0; i < nouns.length; i++) {
			for (int j = 0; j < nouns.length; j++) {
				dist[i] += wordnet.distance(nouns[i], nouns[j]);
			}
		}
		int max = 0;
		int index = 0;
		for (int i = 0; i < dist.length; i++) {
			if (max < dist[i]) {
				max = dist[i];
				index = i;
			}
		}
		return nouns[index];
	}

	// see test client below
	public static void main(String[] args) {

		System.out.println("pmd warning shh");

	}
}
