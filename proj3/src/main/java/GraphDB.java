
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
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

    private Graph myGraph;

    public GraphDB(String dbPath) {
        try {
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
        return myGraph.vertices();
    }

    //TO DO
    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        return myGraph.adj(v);
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        return myGraph.distance(v, w);
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        return myGraph.closest(lon, lat);
    }

    long specialClosest(double lon, double lat) {
        return myGraph.specialClosest(lon, lat);
    }


    /** Longitude of vertex v. */
    double lon(long v) {
        return myGraph.getNodeLon(v);
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return myGraph.getNodeLat(v);
    }

    public void createGraph() {
        myGraph = new Graph(this);
    }

    public void  addNode(Graph.Node n) {
        myGraph.addNode(n);
    }

    public void addEdge(Graph.Edge e) {
        myGraph.addEdge(e);
    }

    public void updateLastNode(Graph.Node n) {
        myGraph.updateLastNode(n);
    }

    public void updateLastEdge(Graph.Edge e) {
        myGraph.updateLastEdge(e);
    }

    public Graph.Node getLastNode() {
        return myGraph.getLastNode();
    }

    public Graph.Edge getLastEdge() {
        return myGraph.getLastEdge();
    }

    public void addNodeToLastEdge(String nodeId) {
        myGraph.addNodeToLastEdge(nodeId);
    }

    public void validateLastWay() {
        myGraph.validateLastWay();
    }

    public void addLocationToLastNode(String location) {
        myGraph.addLocationToLastNode(location);
    }

    public void connectAllGraph() {
        myGraph.connectAll();
    }

    public void connectLastWay() {
        myGraph.connectLastWay();
    }

    public void setMaxSpeed(String s) {
        myGraph.setMaxSpeed(s);
    }

    public void setParent(long current, long next) {
        myGraph.setParent(current, next);
    }

    public long getParent(long nodeID) {
        return myGraph.getParent(nodeID);
    }

    public boolean isLastValid() {
        return myGraph.isLastValid();
    }

    public void setLastWayName(String s) {
        myGraph.setLastWayName(s);
    }

    public void setHeuristic(long nodeID, long goalID) {
        myGraph.setHeuristic(nodeID, goalID);
    }

    public double getScore(long nodeID) {
        return myGraph.getScore(nodeID);
    }

    public double getHypoScore(long current, long nodeID, long goal) {
        return myGraph.getHypoScore(current, nodeID, goal);
    }

    public void updateDistance(long previous, long current) {
        myGraph.updateDistance(previous, current);
    }
}
