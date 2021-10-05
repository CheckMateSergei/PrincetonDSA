import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 4, 2021
 **************************************************************************** */
public class RandomWord {
    public static void main(String[] args) {

        // First word is always declared initial champion
        String champ = StdIn.readString();

        // Initialize variable to track the probability of i'th word being champ
        double i = 2;

        // While there are words left in stdin do
        while (!StdIn.isEmpty()) {
            // If bernoulli returns true (prob: 1/i)
            if (StdRandom.bernoulli(1 / i)) {
                // Champion is now the i'th word
                champ = StdIn.readString();
            }
            else {
                StdIn.readString();
            }
            // Decrease probability of i'th word being champ
            i++;
        }

        // Print the champion
        System.out.printf("%s\n", champ);
    }
}
