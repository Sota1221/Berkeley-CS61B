package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private double[] records;
    private int T;


    // perform T independent experiments on an N-by-N grid
    // throw a java.lang.IllegalArgumentException if either N ≤ 0 or T ≤ 0.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("invalid input");
        }
        this.T = T;
        this.records = new double[this.T];
        for (int i = 0; i < this.T; i++) {
            Percolation test = new Percolation(N);
            while (!test.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                test.open(row, col);
            }
            this.records[i] = (double) test.numberOfOpenSites() / (N * N);
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.records);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.records);
    }


    // low  endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.pow(this.T, 0.5);
    }


    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.pow(this.T, 0.5);
    }
}

