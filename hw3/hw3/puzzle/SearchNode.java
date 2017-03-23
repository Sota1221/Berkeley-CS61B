package hw3.puzzle;

import java.util.Comparator;

public class SearchNode<T> implements Comparator<T>{
    WorldState currentWS;
    int numOfMove;
    SearchNode previousSN;

    public SearchNode(WorldState currentNode, SearchNode previous) {
        currentWS = currentNode;
        previousSN = previous;
        if (previousSN == null) {
            numOfMove = 0;
        } else {
            numOfMove = previous.numOfMove + 1;
        }
    }

    /** Provides an estimate of the number of moves to reach the goal.
     * Must be less than or equal to the correct distance. */
    public int estimatedDistanceToGoal() {
        return currentWS.estimatedDistanceToGoal();
    }



    public int compare(T object1, T object2) {
        SearchNode sn1 = (SearchNode) object1;
        SearchNode sn2 = (SearchNode) object2;
        int val1 = sn1.numOfMove + sn1.estimatedDistanceToGoal();
        int val2 = sn2.numOfMove + sn2.estimatedDistanceToGoal();
        return val1 - val2;
    }

    public boolean equals(T object1, T object2) {
        SearchNode sn1 = (SearchNode) object1;
        SearchNode sn2 = (SearchNode) object2;
        int val1 = sn1.numOfMove + sn1.estimatedDistanceToGoal();
        int val2 = sn2.numOfMove + sn2.estimatedDistanceToGoal();
        return val1 == val2;
    }
}
