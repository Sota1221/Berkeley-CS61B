package hw3.puzzle;

import hw3.puzzle.SearchNode;
import hw3.puzzle.WorldState;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Solver {
SearchNode current;

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
                mapHeap.insert(new SearchNode(neighbor, current));
            }
            current = mapHeap.delMin();
        }
    }



    /*Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState. */
    public int moves(){
        return current.numOfMove;
    }

    /* Returns a sequence of WorldStates from the initial WorldState
                 to the solution.*/
    public Iterable<WorldState> solution() {
        LinkedList<WorldState> path = new LinkedList<>();
        SearchNode pointer = current;
        for (int i = 0; i < moves() + 1; i++) {
            path.add(pointer.currentWS);
            pointer = pointer.previousSN;
        }
        Collections.reverse(path);
        return path;
    }
}
