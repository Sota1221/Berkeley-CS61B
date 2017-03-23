package hw3.puzzle;

import edu.princeton.cs.algs4.Queue;


public class Board implements WorldState {
    int size;
    int[][] board;

    //Constructs a board from an N-by-N array of tiles where
    //tiles[i][j] = tile at row i, column j
    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    //Returns value of tile at row i, column j (or 0 if blank)
    //The tileAt() method should throw a java.lang.IndexOutOfBoundsException
    // unless both i and j are between 0 and N − 1.
    public int tileAt(int i, int j) {
        if (i < 0 || size() - 1 < i || i < 0 || size() - 1 < i) {
            throw new IndexOutOfBoundsException("ERROR");
        }
        return board[i][j];
    }

    //Returns the board size N
    public int size() {
        return size;
    }

    //Returns the neighbors of the current board
    // cited from: http://joshh.ug/neighbors.html
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }



    //Hamming estimate described below
    public int hamming() {
        int total = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    if (board[i][j] != i * size + j + 1) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    //Manhattan estimate described below
    public int manhattan() {
        int total = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    int row = (board[i][j] - 1) / size;
                    int col = (board[i][j] - 1) % size;
                    total += Math.abs(i - row) + Math.abs(j - col);
                }
            }
        }
        return total;
    }

    //Estimated distance to goal. This method should
    //simply return the results of manhattan() when submitted to
    //Gradescope.
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    //Returns true if is this board the goal board
    public boolean isGoal() {
        return hamming() == 0;
    }

    public int hashCode() {
        return ((Object) this).hashCode();
    }

    //Returns true if this board's tile values are the same
    //position as y's
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }
        Board board2 = (Board) y;
        if (size() != board2.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tileAt(i, j) != board2.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
