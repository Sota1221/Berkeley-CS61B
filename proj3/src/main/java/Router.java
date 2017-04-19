import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                                double destlon, double destlat) {

        LinkedList<Long> result = new LinkedList<>();
        long startNodeID = g.specialClosest(stlon, stlat);
        long goadNodeID = g.specialClosest(destlon, destlat);
        PriorityQueue<Long> graphMinPQ = new PriorityQueue<>(new NodeIDComparator(g));
        g.updateDistance(startNodeID, startNodeID);
        g.setHeuristic(startNodeID, goadNodeID);
        graphMinPQ.add(startNodeID);
        Set<Long> marked = new HashSet<>();
//        Map<Long, Long> pathMap = new HashMap<>();
        long currentNodeID = graphMinPQ.poll();
        while (true) {
            if (currentNodeID == goadNodeID) {
                break;
            }
            marked.add(currentNodeID);
//            PriorityQueue<Long> tempMinPQ = new PriorityQueue<>(new nodeIDComparator(g));
            for (Long v: g.adjacent(currentNodeID)) {
                if (marked.contains(v)) {
                    continue;
                }
                if (graphMinPQ.contains(v)) {
                    if (g.getHypoScore(currentNodeID, v, goadNodeID) < g.getScore(v)) {
                        graphMinPQ.remove(v);
                        g.updateDistance(currentNodeID, v);
                        g.setHeuristic(v, goadNodeID);
                        graphMinPQ.add(v);
                        g.setParent(currentNodeID, v);
                    }
                } else {
                    g.updateDistance(currentNodeID, v);
                    g.setHeuristic(v, goadNodeID);
                    graphMinPQ.add(v);
                    g.setParent(currentNodeID, v);
                }
            }
            if (graphMinPQ.isEmpty()) {
                break;
            }
            long nextCurrent = graphMinPQ.poll();
            currentNodeID = nextCurrent;
        }
        if (graphMinPQ.isEmpty()) {
            return result;
        }
        return createPath(g, goadNodeID, startNodeID, result);
    }


    public static LinkedList<Long> createPath(GraphDB g, long current, long start,
                                       LinkedList<Long> result) {
        result.addLast(current);
        while (current != start) {
            current = g.getParent(current);
            result.addFirst(current);
        }
        return result;
    }

    static class NodeIDComparator implements Comparator<Long> {
        GraphDB g;

        NodeIDComparator(GraphDB g) {
            this.g = g;
        }

        public int compare(Long node1ID, Long node2ID) {
            double score1 = g.getScore(node1ID);
            double score2 = g.getScore(node2ID);
            if (score1 > score2) {
                return 1;
            } else if (score1 == score2) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
