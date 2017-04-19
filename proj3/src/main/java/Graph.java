import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    private int V;
    private int E;
    private Map<Long, LinkedList<Long>> adj;
    private Map<Long, Node> nodes = new LinkedHashMap<>();
    private Map<Long, Edge> edges = new LinkedHashMap<>();
    private Node lastNode;
    private Edge lastEdge;
    private Set<Long> actualNodes;

    public Graph(GraphDB g) {
        this.V = 0;
        this.E = 0;
        this.adj = new HashMap<>();
        this.actualNodes = new HashSet<>();
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(String v, String w) {
        Long v1 = Long.parseLong(v);
        Long w1 = Long.parseLong(w);
        addEdge(v, w);
    }


    public void connect(Long v, Long w) {

        boolean isFullV = !adj.get(v).isEmpty();
        boolean isFullW = !adj.get(w).isEmpty();
        if (!isFullV || !isFullW) {
            if (!isFullV && isFullW) {
                actualNodes.add(v);
                V++;
            } else if (isFullV && !isFullW) {
                actualNodes.add(w);
                V++;
            } else {
                actualNodes.add(w);
                actualNodes.add(v);
                V++;
                V++;
            }
        }
        adj.get(v).add(w);
        adj.get(w).add(v);
        E++;
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return actualNodes;
    }


    public Iterable<Long> adj(Long v) {
        LinkedList<Long> lst = adj.get(v);
        return lst;
    }

    public long specialClosest(double lon, double lat) {
        Long closestNodeName = 0L;
        double minDistance = 0;
        int count = 0;
        for (long v: actualNodes) {
            double nodeDistance = distance(lon, lat, nodes.get(v));
            if (count == 0 || nodeDistance < minDistance) {
                minDistance = nodeDistance;
                closestNodeName = v;
            }
            count++;
        }
        return closestNodeName;
    }


    public long closest(double lon, double lat) {
        Long closestNodeName = 0L;
        double minDistance = 0;
        int count = 0;
        for (Node n: nodes.values()) {
            double nodeDistance = distance(lon, lat, n);
            if (count == 0 || nodeDistance < minDistance) {
                minDistance = nodeDistance;
                closestNodeName = n.name;
            }
            count++;
        }
        return closestNodeName;
    }

    public double distance(double lon, double lat, Node n) {
        double diff1 = lon - n.lon;
        double diff2 = lat - n.lat;
        double squareSum = diff1 * diff1 + diff2 * diff2;
        return Math.sqrt(squareSum);
    }

    double lon(long v) {
        return getNodeLon(v);
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return getNodeLat(v);
    }

    double distance(long v, long w) {
        double diff1 = lon(v) - lon(w);
        double diff2 = lat(v) - lat(w);
        double squareSum = diff1 * diff1 + diff2 * diff2;
        return Math.sqrt(squareSum);
    }


    public void addNode(Node n) {
        LinkedList<Long> lst =  new LinkedList<>();
        adj.put(n.name, lst);
        this.nodes.put(n.name, n);
    }




    public void addEdge(Edge e) {
        this.edges.put(e.name, e);
    }


    public void addNodeToEdge(String nodeId, String edgeID) {
        Edge e = edges.get(edgeID);
        e.addNodeToEdge(nodeId);
    }

    public void addNodeToLastEdge(String nodeID) {
        Edge last = getLastEdge();
        last.addNodeToEdge(nodeID);
    }

    public Edge getEdge(String edgeId) {
        return edges.get(edgeId);
    }

    public Node getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    public double getNodeLat(Long v) {
        return nodes.get(v).lat;
    }

    public double getNodeLon(Long v) {
        return nodes.get(v).lon;
    }

    public void updateLastNode(Node n) {
        lastNode = n;
    }

    public void updateLastEdge(Edge e) {
        lastEdge = e;
    }

    public Node getLastNode() {
        return lastNode;
    }

    public Edge getLastEdge() {
        return lastEdge;
    }

    public void validateWay(String edgeId) {
        getEdge(edgeId).validateThisWay();
    }

    public void validateLastWay() {
        getLastEdge().validateThisWay();
    }

    public void addLocationToNode(String nodeId, String name) {
        getNode(nodeId).addLocation(name);
    }

    public void addLocationToNode(Node n, String name) {
        n.addLocation(name);
    }

    public void addLocationToLastNode(String name) {
        getLastNode().addLocation(name);
    }

    public int numOfNodes() {
        return nodes.size();
    }

    public void connectAll() {
        for (Edge e: edges.values()) {
            if (e.isValid) {
                LinkedList<Long> lst = e.getNodes();
                for (int i = 0; i < lst.size() - 1; i++) {
                    connect(lst.get(i), lst.get(i + 1));
                }
            }
        }
    }

    public void connectLastWay() {
        if (lastEdge.isValid) {
            LinkedList<Long> lst = lastEdge.getNodes();
            for (int i = 0; i < lst.size() - 1; i++) {
                connect(lst.get(i), lst.get(i + 1));
            }
        }
    }


    public void setHeuristic(long nodeID, long goalID) {
        nodes.get(nodeID).setHeuristic(distance(nodeID, goalID));
    }

    public double getScore(long nodeID) {
        Node n = nodes.get(nodeID);
        return n.getDistance() + n.getHeuristic();
    }

    public void updateDistance(long previous, long current) {
        double currentDis = distance(previous, current);
        double preDis = nodes.get(previous).getDistance();
        nodes.get(current).setDistance(currentDis + preDis);
    }

    public void setLastWayName(String s) {
        getLastEdge().setWayName(s);
    }

    public void setMaxSpeed(String s) {
        getLastEdge().setMaxSpeed(s);
    }

    public boolean isLastValid() {
        return getLastEdge().isValid;
    }

    public double getHypoScore(long current, long nextNodeID, long goalID) {
        double h = distance(nextNodeID, goalID);
        double preCost = nodes.get(current).getDistance();
        double dis = distance(current, nextNodeID);
        return h + preCost + dis;
    }

    public void setParent(long current, long next) {
        Node nextNode = nodes.get(next);
        nextNode.setParent(current);
    }

    public long getParent(long nodeID) {
        Node current = nodes.get(nodeID);
        return current.parent;
    }


    static class Node {
        Long name;
        String id;
        double lat;
        double lon;
        String location;
        double totalDis;
        double heuristic;
        long parent;


        Node(String id, String lat, String lon) {
            this.id = id;
            this.lat = Double.parseDouble(lat);
            this.lon = Double.parseDouble(lon);
            this.name = Long.parseLong(id);
        }

        public void addLocation(String place) {
            location = place;
        }

        public double getDistance() {
            return totalDis;
        }

        public void setDistance(double dis) {
            this.totalDis = dis;
        }

        public void setHeuristic(double h) {
            this.heuristic = h;
        }

        public double getHeuristic() {
            return heuristic;
        }

        public void setParent(long parentID) {
            this.parent = parentID;
        }

        public long getParentID() {
            return parent;
        }
    }


    static class Edge {
        Long name;
        String id;
        String wayName;
        LinkedList<Long> nodesInEdge;
        boolean isValid = false;
        int maxSpeed;

        Edge(String id) {
            this.id = id;
            name = Long.parseLong(id);
            nodesInEdge = new LinkedList<>();
        }

        public LinkedList<Long> getNodes() {
            return nodesInEdge;
        }

        public void addNodeToEdge(String nodeID) {
            Long nodeName = Long.parseLong(nodeID);
            nodesInEdge.addLast(nodeName);
        }

        public void validateThisWay() {
            isValid = true;
        }

        public void setMaxSpeed(String s) {
            int speed = Integer.parseInt(s);
            maxSpeed = speed;
        }

        public void setWayName(String s) {
            wayName = s;
        }


    }
}
