
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;

//import java.io.InterruptedIOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
//import java.io.InterruptedIOException;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {


    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    Trie prefixTre = new Trie();
    private Map<Long, LinkedList<Long>> adj;
    private Map<Integer, Set<Long>> actualNodes;
    private Map<Long, Node> nodes = new LinkedHashMap<>();
    private Node lastNode;
    private Edge lastEdge;
    Map<String, Set<Node>> locationMap = new HashMap<>();
    private Set<Long> realNodes = new HashSet<>();
    public static final double HALF_LON = -122.255859;


    static class Node {
        Long name;
        Double lat;
        Double lon;
        String location;
        double totalDis;
        double heuristic;
        long parent;


        Node(String id, String lat, String lon) {
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
    }




    public GraphDB(String dbPath) {
        try {
            this.adj = new HashMap<>();
            this.actualNodes = new HashMap<Integer, Set<Long>>();
            actualNodes.put(1, new HashSet<Long>());
            actualNodes.put(-1, new HashSet<Long>());
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TO do:Your code here.
        return;
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return realNodes;
    }

    public Map<Long, Node> getNodes() {
        return nodes;
    }

    //TO DO
    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        if (adj.get(v) == null) {
            return new LinkedList<Long>();
        }
        return adj.get(v);
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        double diff1 = lon(v) - lon(w);
        double diff2 = lat(v) - lat(w);
        double squareSum = diff1 * diff1 + diff2 * diff2;
        return Math.sqrt(squareSum);
    }

    public double distance(double lon, double lat, Node n) {
        double diff1 = lon - n.lon;
        double diff2 = lat - n.lat;
        double squareSum = diff1 * diff1 + diff2 * diff2;
        return Math.sqrt(squareSum);
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
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

    long specialClosest(double lon, double lat) {
        Long closestNodeName = 0L;
        double minDistance = 0;
        int count = 0;
        if (lon < HALF_LON) {
            for (long v: actualNodes.get(1)) {
                double nodeDistance = distance(lon, lat, nodes.get(v));
                if (count == 0 || nodeDistance < minDistance) {
                    minDistance = nodeDistance;
                    closestNodeName = v;
                }
                count++;
            }
        } else {
            for (long v: actualNodes.get(-1)) {
                double nodeDistance = distance(lon, lat, nodes.get(v));
                if (count == 0 || nodeDistance < minDistance) {
                    minDistance = nodeDistance;
                    closestNodeName = v;
                }
                count++;
            }
        }
        return closestNodeName;
    }

    public double getNodeLat(Long v) {
        return nodes.get(v).lat;
    }

    public double getNodeLon(Long v) {
        return nodes.get(v).lon;
    }


    /** Longitude of vertex v. */
    double lon(long v) {
        return getNodeLon(v);
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return getNodeLat(v);
    }

    public void addNode(Node n) {
        this.nodes.put(n.name, n);
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

    public void addNodeToLastEdge(String nodeID) {
        Edge last = getLastEdge();
        last.addNodeToEdge(nodeID);
    }
    public void validateLastWay() {
        getLastEdge().validateThisWay();
    }

    public void addLocationToLastNode(String name) {
        prefixTre.root.add(name);
        getLastNode().addLocation(name);
        if (!locationMap.containsKey(name)) {
            Set<Node> s = new HashSet<>();
            s.add(lastNode);
            locationMap.put(name, s);
        } else {
            locationMap.get(name).add(lastNode);
        }
    }


    public void connect(Long v, Long w) {
        boolean isFullV = adj.get(v) != null;
        boolean isFullW = adj.get(w) != null;
        if (!isFullV && isFullW) {
            adj.get(w).add(v);
            LinkedList<Long> lst = new LinkedList<>();
            lst.add(w);
            adj.put(v, lst);
            realNodes.add(v);
            if (nodes.get(v).lon < HALF_LON) {
                actualNodes.get(1).add(v);
            } else {
                actualNodes.get(-1).add(v);
            }
            return;
        } else if (isFullV && !isFullW) {
            adj.get(v).add(w);
            LinkedList<Long> lst = new LinkedList<>();
            lst.add(v);
            adj.put(w, lst);
            realNodes.add(w);
            if (nodes.get(w).lon < HALF_LON) {
                actualNodes.get(1).add(w);
            } else {
                actualNodes.get(-1).add(w);
            }
            return;
        } else if (!isFullV && !isFullW) {
            LinkedList<Long> lst = new LinkedList<>();
            lst.add(w);
            LinkedList<Long> lst2 = new LinkedList<>();
            lst2.add(v);
            adj.put(v, lst);
            adj.put(w, lst2);
            realNodes.add(v);
            realNodes.add(w);
            if (nodes.get(v).lon < HALF_LON) {
                actualNodes.get(1).add(v);
            } else {
                actualNodes.get(-1).add(v);
            }
            if (nodes.get(w).lon < HALF_LON) {
                actualNodes.get(1).add(w);
            } else {
                actualNodes.get(-1).add(w);
            }
            return;
        } else {
            adj.get(v).add(w);
            adj.get(w).add(v);
            return;
        }
    }


    public void connectLastWay() {
        if (lastEdge.isValid) {
            LinkedList<Long> lst = lastEdge.nodesInEdge;
            for (int i = 0; i < lst.size() - 1; i++) {
                connect(lst.get(i), lst.get(i + 1));
            }
        }
    }

    public void setMaxSpeed(String s) {
        getLastEdge().setMaxSpeed(s);
    }

    public void setParent(long current, long next) {
        Node nextNode = nodes.get(next);
        nextNode.setParent(current);
    }


    public long getParent(long nodeID) {
        Node current = nodes.get(nodeID);
        return current.parent;
    }


    public boolean isLastValid() {
        return getLastEdge().isValid;
    }

    public void setHeuristic(long nodeID, long goalID) {
        nodes.get(nodeID).setHeuristic(distance(nodeID, goalID));
    }

    public double getScore(long nodeID) {
        Node n = nodes.get(nodeID);
        return n.totalDis + n.heuristic;
    }

    public double getHypoScore(long current, long nextNodeID, long goalID) {
        double h = distance(nextNodeID, goalID);
        double preCost = nodes.get(current).totalDis;
        double dis = distance(current, nextNodeID);
        return h + preCost + dis;
    }

    public void updateDistance(long previous, long current) {
        double currentDis = distance(previous, current);
        double preDis = nodes.get(previous).totalDis;
        nodes.get(current).setDistance(currentDis + preDis);
    }

}
