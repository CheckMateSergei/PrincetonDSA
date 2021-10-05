/* *****************************************************************************
 *  Name:              Braydon Horcoff
 *  Last modified:     October 4, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// perform Monte Carlo simulation to estimate the percolation threshold
public class PercolationStats {

    // store results of trials in array for later
    private double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // throw exception if n <= 0 or trials <= 0
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        // initialize results with space for all trials
        results = new double[trials];
        // grid object reference
        Percolation percs;

        // perform trials
        for (int i = 0; i < trials; i++) {
            // create a new grid with all sites blocked
            percs = new Percolation(n);

            // while percs has not yet percolated
            while (!percs.percolates()) {
                // choose random row and column
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                // open the randomly chosen site
                percs.open(row, col);
            }
            // store results
            results[i] = (double) percs.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        // compute and store mean and stddev
        double xBar = StdStats.mean(results);
        double s = StdStats.stddev(results);
        // compute and store root(trials)
        double rootT = Math.sqrt(results.length);
        // return low confidence int endpoint
        return xBar - (1.96 * s) / rootT;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        // compute and store mean and stddev
        double xBar = StdStats.mean(results);
        double s = StdStats.stddev(results);
        // compute and store root(trials)
        double rootT = Math.sqrt(results.length);
        // return high confidence int endpoint
        return xBar + (1.96 * s) / rootT;
    }


    public static void main(String[] args) {
        // store n and number of trials from args
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.printf("mean\t\t\t= %f\n", stats.mean());
        System.out.printf("stddev\t\t\t= %f\n", stats.stddev());
        System.out.printf("95%% confidence interval = [%f, %f]\n",
                stats.confidenceLo(), stats.confidenceHi());

    }
}
