/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 12, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {
        // create randomized queue
        RandomizedQueue<String> strQueue = new RandomizedQueue<>();
        // read k from std input
        int k = Integer.parseInt(args[0]);

        // input the strings
        while (!StdIn.isEmpty()) {
            strQueue.enqueue(StdIn.readString());
        }

        // strQueue.debug();

        // randomly remove n - k elements
        int m = strQueue.size() - k;
        for (int i = 0; i < m; i++) {
            strQueue.dequeue();
        }

        // iterate and print a permutation of the input
        for (String s : strQueue) System.out.printf("%s\n", s);
    }
}
