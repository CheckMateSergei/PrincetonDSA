/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 4, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid;
    private WeightedQuickUnionUF ufgrid;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // if n <= 0 throw exception
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        // create n x n grid and a UF object with n^2 + 2 entries
        // used several times
        int nSquared = n * n;
        // initialize with two extra nodes used as virtual sites
        ufgrid = new WeightedQuickUnionUF(nSquared + 2);
        // maybe intialize the counter for each object and youll get the right results eh?
        openSites = 0;

        // connect the two virtual sites to the top and bottom rows
        // let the 0th and (n^2 + 1)th nodes be virtual
        for (int i = 1; i < n + 1; i++) {
            ufgrid.union(0, i);
        }
        for (int i = (nSquared + 1 - n); i < nSquared + 1; i++) {
            ufgrid.union(nSquared + 1, i);
        }
        // initialize the grid
        grid = new int[n][n];

        // initialize the grid with all entries blocked
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                grid[i][j] = 0;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // check if in bounds throw exception if not
        if (row <= 0 || col <= 0 || row > grid.length || col > grid.length) {
            throw new IllegalArgumentException();
        }

        // to keep track of corresponding entry in ufgrid
        int track = (row - 1) * grid.length + col;

        // check if the entry is open and open it if not
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = 1;
            openSites++;
        }
        // connect to site ABOVE if in bounds and open
        if (row - 1 > 0 && isOpen(row - 1, col)) {
            int track2 = (row - 2) * grid.length + col;
            // union corresponding entries in ufgrid
            ufgrid.union(track, track2);
        }
        // connect to site BELOW if in bounds and open
        if (row < grid.length && isOpen(row + 1, col)) {
            int track2 = row * grid.length + col;
            // union corresponding entries in ufgrid
            ufgrid.union(track, track2);
        }
        // connect to site to RIGHT if in bounds and open
        if (col + 1 <= grid.length && isOpen(row, col + 1)) {
            int track2 = (row - 1) * grid.length + col + 1;
            // union corresponding entries in ufgrid
            ufgrid.union(track, track2);
        }
        // connect to site to LEFT if in bounds and open
        if (col - 1 > 0 && isOpen(row, col - 1)) {
            int track2 = (row - 1) * grid.length + col - 1;
            // union corresponding entries in ufgrid
            ufgrid.union(track, track2);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // check if in bounds throw exception if not
        if (row <= 0 || col <= 0 || row > grid.length || col > grid.length) {
            throw new IllegalArgumentException();
        }

        // track current site in ufgrid
        int track = (row - 1) * grid.length + col;
        // check if the site is connected to the top virtual site
        return ufgrid.find(0) == ufgrid.find(track);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // check if any elements on the bottom row are full
        // linear time O(n)
        for (int i = 1; i < grid.length + 1; i++) {
            if (isFull(grid.length, i)) {
                return true;
            }
        }

        return false;
    }
}
