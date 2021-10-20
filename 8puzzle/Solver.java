/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 19, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Stack<Board> solution;
    private boolean isSolveable;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // corner case
        if (initial == null) throw new IllegalArgumentException("Null constructor agrument!");

        isSolveable = false;
        MinPQ<Node> pqBoard = new MinPQ<>(); // PQ for solve
        MinPQ<Node> pqTwin = new MinPQ<>(); // PQ for twin solve
        Node init = new Node(initial.manhattan(), 0, initial);
        pqBoard.insert(init);
        // create twin for alternate PQ
        Board twin = initial.twin();
        pqTwin.insert(new Node(twin.manhattan(), 0, twin));
        Node newNode, newNode2;

        // while each queue has not found a goal
        while (!pqBoard.min().getBoard().isGoal() && !pqTwin.min().getBoard().isGoal()) {
            // GET NEIGHBOURS AND STORE IN PQBOARD
            Node temp = pqBoard.delMin(); // store min node in temp variable
            for (Board b : temp.getBoard().neighbors()) {
                // calculate manhattan priority and store new nodes in PQ
                if (temp.getPrev() != null &&
                        b.equals(temp.getPrev().getBoard())) continue; // critical optimization
                newNode = new Node(b.manhattan(), temp.getMoves() + 1, b);
                newNode.setPrev(temp); // keep track of last node
                pqBoard.insert(newNode);
            }

            // GET NEIGHBOURS AND STORE IN PQTWIN
            // store min in temp variable
            Node temp2 = pqTwin.delMin(); // store min in temp node
            for (Board b : temp2.getBoard().neighbors()) {
                // calculate manhattan priority and store new nodes in PQ
                if (temp2.getPrev() != null &&
                        b.equals(temp2.prev.getBoard())) continue; // critical optimization
                newNode2 = new Node(b.manhattan(), temp2.getMoves() + 1, b);
                newNode2.setPrev(temp2); // keep track of last node
                pqTwin.insert(newNode2);
                if (newNode2.getBoard().isGoal()) break; // don't go through other neighbours
            }
        }

        // check which is solved
        if (pqBoard.min().getBoard().isGoal()) isSolveable = true;
        else if (pqTwin.min().getBoard().isGoal()) isSolveable = false;

        // if the board has been solved iterate through and store in a stack
        if (isSolveable) {
            solution = new Stack<>();
            Node temp3 = pqBoard.min();
            while (temp3 != null) {
                solution.push(temp3.getBoard());
                temp3 = temp3.getPrev();
            }
            moves = solution.size() - 1;
        }
    }

    // private Node class
    private class Node implements Comparable<Node> {
        private final int key;
        private final int man;
        private final int moves;
        private final Board board;
        private Node prev;

        public Node(int man, int moves, Board b) {
            this.key = man + moves; // sum of manhattan dist and moves made
            this.board = b; // the new board
            this.man = man;
            this.moves = moves;
            this.prev = null;
        }

        public void setPrev(Node node) {
            this.prev = node;
        }

        public Node getPrev() {
            return this.prev;
        }

        public int getMan() {
            return this.man;
        }

        public int getMoves() {
            return this.moves;
        }

        public Board getBoard() {
            return this.board;
        }

        public int compareTo(Node o1) {
            if (this.key == o1.key) return Integer.compare(this.man, o1.man);
            return Integer.compare(this.key, o1.key);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolveable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolveable) return null;
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
