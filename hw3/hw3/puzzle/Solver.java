package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.LinkedList;


public class Solver {

    SearchNode current;
    LinkedList<WorldState> path;

    /*Constructor which solves the puzzle, computing
    everything necessary for moves() and solution() to
    not have to solve the problem again. Solves the
    puzzle using the A* algorithm. Assumes a solution exists. */
    public Solver(WorldState initial) {
        current = new SearchNode(initial, null);
        MinPQ<SearchNode> mapHeap = new MinPQ<>(current);
        mapHeap.insert(current);

        while (!current.currentWS.isGoal()) {
            for (WorldState neighbor: current.currentWS.neighbors()) {
                if (current.previousSN != null) {
                    if (neighbor.equals(current.previousSN.currentWS)) {
                        continue;
                    }
                }
                mapHeap.insert(new SearchNode(neighbor, current));
            }
            current = mapHeap.delMin();
        }
    }


    public LinkedList<WorldState> path() {
        SearchNode pointer = current;
        path = new LinkedList<>();
        int i = 0;
        while (pointer != null) {
            path.addFirst(pointer.currentWS);
            pointer = pointer.previousSN;
        }
        return path;
    }

    /*Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState. */
    public int moves() {
        return current.numOfMove;
    }




    /* Returns a sequence of WorldStates from the initial WorldState
                 to the solution.*/
    public Iterable<WorldState> solution() {
        path = path();
        return path;
    }
}