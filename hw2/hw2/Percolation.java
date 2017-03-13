package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private int size;
    private WeightedQuickUnionUF set;
    private int numOpen;

    // create N-by-N grid, with all sites initially blocked.
    // O(N^2)
    // java.lang.IllegalArgumentException if N ≤ 0
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("ERROR: invalid size");
        }
        this.size = N;
        this.grid = new boolean[N][N];
        // set(N*N) is a checker above the grid. Calls it topChecker
        // set(N*N+1) is a checker blow the grid Calls it bottomChecker
        this.set = new WeightedQuickUnionUF(N * N + 2);
        this.numOpen = 0;
    }


    // open the site (row, col) if it is not open already
    // throw java.lang.IndexOutOfBoundsException if you need
    public void open(int row, int col) {
        if ((row < 0 || this.size - 1 < row) || (col < 0 || this.size - 1 < col)) {
            throw new IndexOutOfBoundsException("ERROR: invalid index");
        }
        if (grid[row][col]) {
            return;
        }
        this.grid[row][col] = true;
        this.numOpen++;

        int current = toSetIndex(row, col);
        int[] indicesToBeChecked;
        if (col == 0) {
            if (row == 0) {
                //  unions for    __ shape (type 1)
                //               |
                set.union(toSetIndex(row, col), this.size * this.size);
                indicesToBeChecked = new int[]{toSetIndex(row + 1, col), toSetIndex(row, col + 1)};
            } else if (row == this.size - 1) {
                // unions for |__   shape (type 2)
                set.union(toSetIndex(row, col), this.size * this.size + 1);
                indicesToBeChecked = new int[]{toSetIndex(row - 1, col), toSetIndex(row, col + 1)};
            } else {
                // unions for |__ shape (type 3)
                //            |
                indicesToBeChecked = new int[]{toSetIndex(row - 1, col), toSetIndex(row, col + 1),
                        toSetIndex(row + 1, col)};
            }
        } else if (col == this.size - 1) {
            if (row == 0) {
                // unions for __  shape (type 4)
                //              |
                set.union(toSetIndex(row, col), this.size * this.size);
                indicesToBeChecked = new int[]{toSetIndex(row, col - 1), toSetIndex(row + 1, col)};
            } else if (row == this.size - 1) {
                // unions for __| shape (type 5)
                set.union(toSetIndex(row, col), this.size * this.size + 1);
                indicesToBeChecked = new int[]{toSetIndex(row - 1, col), toSetIndex(row, col - 1)};
            } else {
                // unions for __| shape (type 6)
                //              |
                indicesToBeChecked = new int[]{toSetIndex(row - 1, col), toSetIndex(row, col - 1),
                        toSetIndex(row + 1, col)};
            }
        } else {
            if (row == 0) {
                // unions for  ___ shape (type 7)
                //              |
                set.union(toSetIndex(row, col), this.size * this.size);
                indicesToBeChecked = new int[]{toSetIndex(row, col - 1), toSetIndex(row, col + 1),
                                               toSetIndex(row + 1, col)};
            } else if (row == this.size - 1) {
                // unions for _|_ shape (type 8)
                set.union(toSetIndex(row, col), this.size * this.size + 1);
                indicesToBeChecked = new int[]{toSetIndex(row, col - 1), toSetIndex(row - 1, col),
                                               toSetIndex(row, col + 1)};
            } else {
                // unions for __|__ shape (type 9)
                //              |
                indicesToBeChecked = new int[]{toSetIndex(row - 1, col), toSetIndex(row, col - 1),
                        toSetIndex(row, col + 1), toSetIndex(row + 1, col)};
            }
        }
        unionSpecifiedSties(indicesToBeChecked, current);
    }


    private void unionSpecifiedSties(int[] indices, int current) {
        for (int i = 0; i < indices.length; i++) {
            if (isOpen(indices[i])) {
                set.union(indices[i], current);
            }
        }
    }


    private int toSetIndex(int row, int col) {
        return row * this.size + col;
    }

    private int[] to2DIndex(int setIndex) {
        int[] result = new int[2];
        result[0] = setIndex / this.size;
        result[1] = setIndex % this.size;
        return result;
    }


    private boolean isOpen(int setIndex) {
        int[] array = to2DIndex(setIndex);
        return isOpen(array[0], array[1]);
    }


    // is the site (row, col) open?
    // throw java.lang.IndexOutOfBoundsException if you need
    public boolean isOpen(int row, int col) {
        if ((row < 0 || this.size - 1 < row) || (col < 0 || this.size - 1 < col)) {
            throw new IndexOutOfBoundsException("ERROR: invalid index");
        }
        return this.grid[row][col];
    }


    // is the site (row, col) full?
    // throw java.lang.IndexOutOfBoundsException if you need
    public boolean isFull(int row, int col) {
        if ((row < 0 || this.size - 1 < row) || (col < 0 || this.size - 1 < col)) {
            throw new IndexOutOfBoundsException("ERROR: invalid index");
        }
        return set.connected(this.size * this.size, toSetIndex(row, col));
    }


    // number of open sites
    // O(1)
    public int numberOfOpenSites() {
        return numOpen;
    }


    // does the system percolate?
    public boolean percolates() {
        return set.connected(this.size * this.size, this.size * this.size + 1);
    }


/*    // unit testing (not required)
    public static void main(String[] args) {

    }

*/
}
