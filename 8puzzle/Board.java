/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 19, 2021
 **************************************************************************** */

import java.util.LinkedList;

public class Board {

    private final int size;
    private int[][] tiles;
    private int i0, j0; // index of the blank square if known

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = copy(tiles);
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = this.dimension();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) continue; // don't count the empty square
                if (tiles[i][j] != this.dimension() * i + j + 1) count++; // tile out of position
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dist = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] == size * i + j + 1) continue;
                // gets the proper y coordinate for element
                int y = (tiles[i][j] - 1) % size;
                // gets the proper x coordinate for element
                int x = (int) Math.abs(Math.floor((double) (tiles[i][j] - y - 1) / size));
                dist += Math.abs(x - i) + Math.abs(y - j);
            }
        }
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // initialize goal board
        int s = this.dimension();
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                // check every value to see if it is the goal
                if (i == s - 1 && j == s - 1) {
                    if (this.tiles[i][j] != 0) return false;
                    if (this.tiles[i][j] == 0) return true;
                }
                if (this.tiles[i][j] != s * i + j + 1) return false;
            }
        }
        // it is the goal board!
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        // check class of parameter
        if (y.getClass() != this.getClass()) return false;

        // cast Object to Board
        Board that = (Board) y;
        // check if sizes match
        if (this.dimension() != that.dimension()) return false;
        // check every instance and return false if not matching
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        // everything matches return true
        return true;
    }

    // swap two tiles
    private void swap(int i, int j, int x, int y) {
        int temp = this.tiles[i][j];
        this.tiles[i][j] = this.tiles[x][y];
        this.tiles[x][y] = temp;
    }

    // copy contents of a board to an array and return
    private int[][] copy(int[][] tilesToCopy) {
        int[][] copy = new int[tilesToCopy.length][tilesToCopy.length];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy.length; j++) {
                copy[i][j] = tilesToCopy[i][j];
            }
        }
        return copy;
    }

    // returns neighbours for board with blank square in a corner
    private LinkedList<Board> cornerNeighbours() {
        LinkedList<Board> it = new LinkedList<>();

        // if top left corner
        if (this.isTopLeftCorner(this.i0, this.j0)) {
            // swap right tile
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            // swap bottom tile
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
        }
        // if top right corner
        else if (this.isTopRightCorner(this.i0, this.j0)) {
            // swap left tile
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
            // swap bottom tile
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
        }
        else if (this.isBottomLeftCorner(this.i0, this.j0)) {
            // swap top tile
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            // swap right rile
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
        }
        else if (this.isBottomRightCorner(this.i0, this.j0)) {
            // swap top tile
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            // swap left tile
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
        }
        return it;
    }

    // returns neighbours for board with blank square on an edge
    private LinkedList<Board> midNeighbours() {
        LinkedList<Board> it = new LinkedList<>();

        // swap all four adjacent tiles
        // swap top tile
        this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
        it.addLast(new Board(copy(this.tiles)));
        // swap back
        this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
        // swap left tile
        this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
        it.addLast(new Board(copy(this.tiles)));
        // swap back
        this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
        // swap right tile
        this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
        it.addLast(new Board(copy(this.tiles)));
        // swap back
        this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
        // swap bottom tile
        this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
        it.addLast(new Board(copy(this.tiles)));
        // swap back
        this.swap(this.i0, this.j0, this.i0 + 1, this.j0);

        return it;
    }

    // returns neighbours for board with blank square on an edge
    private LinkedList<Board> edgeNeighbours() {
        LinkedList<Board> it = new LinkedList<>();

        // if top edge
        if (this.isTopEdge(this.i0, this.j0)) {
            // swap right tile
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            // swap bottom tile
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
            // swap left tile
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
        }
        // if left edge
        else if (this.isLeftEdge(this.i0, this.j0)) {
            // swap right tile
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            // swap bottom tile
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
            // swap top tile
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
        }
        // if bottom edge
        else if (this.isBottomEdge(this.i0, this.j0)) {
            // swap top tile
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            // swap right tile
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 + 1);
            // swap left tile
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
        }
        // if right edge
        else if (this.isRightEdge(this.i0, this.j0)) {
            // swap top tile
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 - 1, this.j0);
            // swap left tile
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0, this.j0 - 1);
            // swap bottom tile
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
            it.addLast(new Board(copy(this.tiles)));
            // swap back
            this.swap(this.i0, this.j0, this.i0 + 1, this.j0);
        }
        return it;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        // get the number of neighbours
        int num = this.numNeighbours();
        // return the list of neighbouring boards
        LinkedList<Board> it = new LinkedList<>();

        // depending on how many neighbours exist
        if (num == 2) it = this.cornerNeighbours();
        if (num == 3) it = this.edgeNeighbours();
        if (num == 4) it = this.midNeighbours();

        return it;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // board to return
        Board twin = new Board(copy(this.tiles));
        int s = this.dimension();

        // find an out of place tile
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                // tile out of place
                if (twin.tiles[i][j] != s * i + j + 1) {
                    // if not at right edge and current and next tile not 0
                    if (j < s - 1 && twin.tiles[i][j] != 0 &&
                            twin.tiles[i][j + 1] != 0) {
                        // exchange tiles
                        twin.swap(i, j, i, j + 1);
                        return twin;
                    }
                    // if at right edge and current and previous tile not 0
                    else if (j == s - 1 && twin.tiles[i][j - 1] != 0 &&
                            twin.tiles[i][j] != 0) {
                        twin.swap(i, j, i, j - 1);
                        return twin;
                    }
                }
            }
        }
        // if we have reached here without an out of place tile it is goal
        twin.swap(0, 0, 0, 1); // swap 1 and 2
        return twin;
    }

    // return the number of neighbours (depends on where 0 is)
    private int numNeighbours() {
        int i = 0, j = 0;
        int s = this.dimension();
        // find where 0 is on the board
        for (int x = 0; x < s; x++) {
            for (int y = 0; y < s; y++) {
                if (this.tiles[x][y] == 0) {
                    i = x;
                    j = y;
                }
            }
        }
        // set the 0 indices so we know for later
        this.i0 = i;
        this.j0 = j;
        // if corner then return 2
        if (this.isCorner(i, j)) return 2;
        // if on an edge then return 3
        if (this.isEdge(i, j)) return 3;
        // else the tile is somewhere in th middle and theres 4 neighbours
        return 4;
    }

    // returns true if index is in a corner
    private boolean isCorner(int x, int y) {
        // if in a corner return true
        if (this.isTopRightCorner(x, y) || this.isTopLeftCorner(x, y) ||
                this.isBottomRightCorner(x, y) || this.isBottomLeftCorner(x, y)) return true;
        return false;
    }

    // returns true if index is in top right corner
    private boolean isTopRightCorner(int x, int y) {
        int s = this.dimension();
        // if in a corner return true
        if (x == 0 && y == s - 1) return true;
        return false;
    }

    // returns true if index is in top left corner
    private boolean isTopLeftCorner(int x, int y) {
        // if in a corner return true
        if (x == 0 && y == 0) return true;
        return false;
    }

    // returns true if index is in bottom right corner
    private boolean isBottomRightCorner(int x, int y) {
        int s = this.dimension();
        // if in a corner return true
        if (x == s - 1 && y == s - 1) return true;
        return false;
    }

    // returns true if index is bottom left corner
    private boolean isBottomLeftCorner(int x, int y) {
        int s = this.dimension();
        // if in a corner return true
        if (x == s - 1 && y == 0) return true;
        return false;
    }

    // returns true if on any edge
    private boolean isEdge(int i, int j) {
        // if on an edge return true
        if (this.isTopEdge(i, j) || this.isBottomEdge(i, j) || this.isRightEdge(i, j) ||
                this.isLeftEdge(i, j)) return true;
        return false;
    }

    // returns true if on the top edge
    private boolean isTopEdge(int i, int j) {
        int s = this.dimension();
        // if on an edge return true
        if (i == 0 && j < s - 1) return true;
        return false;
    }

    // returns true if on the bottom edge
    private boolean isBottomEdge(int i, int j) {
        int s = this.dimension();
        // if on an edge return true
        if (i == s - 1 && j < s - 1) return true;
        return false;
    }

    // returns true if on the right edge
    private boolean isRightEdge(int i, int j) {
        int s = this.dimension();
        // if on an edge return true
        if (j == s - 1 && i < s - 1) return true;
        return false;
    }

    // returns true if on the left edge
    private boolean isLeftEdge(int i, int j) {
        int s = this.dimension();
        // if on an edge return true
        if (j == 0 && i < s - 1) return true;
        return false;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        int[][] board = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };

        int[][] board2 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };

        int[][] board3 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 0, 8 } };

        int[][] board4 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };

        Board b = new Board(board2);
        System.out.printf("The size of the board is: %d\n", b.dimension());
        System.out.println("String rep of board: ");
        System.out.print(b.toString());

        System.out.println("Hamming Distance:");
        System.out.println(Integer.toString(b.hamming()));
        System.out.println("Manhattan Distance:");
        System.out.println(Integer.toString(b.manhattan()));
        System.out.println();

        System.out.println("Board a: ");
        Board a = new Board(board);
        System.out.println(a.toString());
        System.out.println("Neighbours of a: ");

        for (Board value : a.neighbors()) {
            System.out.println(value.toString());
        }

        Board d = a;
        Board e = new Board(board4);

        System.out.println("Does d = b?");
        System.out.printf("%b\n\n", d.equals(b));
        System.out.println("Does d = a?");
        System.out.printf("%b\n\n", d.equals(a));
        System.out.println("Does b = e?");
        System.out.printf("%b\n\n", b.equals(e));

        System.out.println("Twin of board a: ");
        System.out.println(a.twin().toString());

        Board c = new Board(board3);
        System.out.println("Board c:");
        System.out.println(c.toString());
        System.out.printf("c is goal? %b\n\n", c.isGoal());
        c.swap(2, 1, 2, 2);
        System.out.println(c.toString());
        System.out.printf("c is goal now? %b\n", c.isGoal());
        System.out.printf("Manhattan distance of c is %d\n", c.manhattan());
    }
}
