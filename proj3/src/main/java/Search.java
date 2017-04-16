import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Search {
    private QuadTree imageTree;
    private QuadTree.Node currentNode;
    private Map<String, Double> target;
    private LinkedList<Double> sortedKeys;
    private double targetULLAT;
    private double targetULLON;
    private double targetLRLAT;
    private double targetLRLON;
    private double targetWidth;
    private double targetHeight;
    private double targetLonDPP;
    private Map<Double, LinkedList<QuadTree.Node>> classifiedNodes;
    private String[][] renderGrid;
    private int numOfPics;
    private double rasterUlLon;
    private double rasterUlLat;
    private double rasterLrLon;
    private double rasterLrLat;
    private int rasterDepth;
    private boolean querySuccess;
    public static final int WIDTH_FOR_EACH_IMAGE =  256;
    public static final int DEPTH_OF_ROOT = 0;
    public static final int MAX_HEIGHT = 7;
    private Map<String, Object> result;

    Search(QuadTree imageTree, Map<String, Double> target) {
        this.imageTree = imageTree;
        this.currentNode = imageTree.getRoot();
        this.target = target;
        this.targetULLAT = target.get("ullat");
        this.targetULLON = target.get("ullon");
        this.targetLRLAT = target.get("lrlat");
        this.targetLRLON = target.get("lrlon");
        this.targetWidth = target.get("w");
        this.targetHeight = target.get("h");
        this.targetLonDPP = (targetLRLON - targetULLON) / targetWidth;
        this.querySuccess = true;
        classifiedNodes = new HashMap<>();
        sortedKeys = new LinkedList<>();
        seekValidNode(currentNode, DEPTH_OF_ROOT);
        if (!querySuccess) {
            setValidMap();
        } else {
            renderGrid =
                    new String[classifiedNodes.size()][numOfPics / classifiedNodes.size()];
            setValidPics();
            setResultInfo();
            setValidMap();
        }
    }

    // target has
    // lrlon, ullon, w, h, ullat, lrlat
    public void seekValidNode(QuadTree.Node current, int depth) {
        double ullat = current.getULLAT();
        double ullon = current.getULLON();
        double lrlat = current.getLRLAT();
        double lrlon = current.getLRLON();
        double lonDPP = (lrlon - ullon) / WIDTH_FOR_EACH_IMAGE;
        if (lrlat > targetULLAT || targetLRLON < ullon
                || lrlon < targetULLON || targetLRLAT > ullat) {
            if (depth == DEPTH_OF_ROOT) {
                querySuccess = false;
            }
            return;
        }
        if (lonDPP > targetLonDPP && depth < MAX_HEIGHT) {
            seekValidNode(current.getChild(1), depth + 1);
            seekValidNode(current.getChild(2), depth + 1);
            seekValidNode(current.getChild(3), depth + 1);
            seekValidNode(current.getChild(4), depth + 1);
            return;
        }
        if (!classifiedNodes.containsKey(current.getULLAT())) {
            LinkedList<QuadTree.Node> lst = new LinkedList<>();
            lst.add(current);
            putToSortedKeys(current.getULLAT());
            classifiedNodes.put(current.getULLAT(), lst);
            numOfPics++;
        } else {
            classifiedNodes.get(current.getULLAT()).addLast(current);
            numOfPics++;
        }
    }

    public void putToSortedKeys(Double key) {
        if (sortedKeys.isEmpty()) {
            sortedKeys.add(key);
            return;
        }
        for (int i = 0; i < sortedKeys.size(); i++) {
            if (key > sortedKeys.get(i)) {
                sortedKeys.add(i, key);
                return;
            }
        }
        sortedKeys.addLast(key);
    }

    public void setValidPics() {
        int rowIndex = 0;
        for (Double key: sortedKeys) {
            LinkedList<QuadTree.Node> lst = classifiedNodes.get(key);
            for (int i = 0; i < lst.size(); i++) {
                renderGrid[rowIndex][i] = ((QuadTree.Node) lst.get(i)).getName();
            }
            rowIndex++;
        }
    }

    public void setResultInfo() {
        Double firstKey = sortedKeys.getFirst();
        Double lastKey = sortedKeys.getLast();
        QuadTree.Node ul = classifiedNodes.get(firstKey).getFirst();
        QuadTree.Node lr = classifiedNodes.get(lastKey).getLast();
        rasterUlLon = ul.getULLON();
        rasterUlLat = ul.getULLAT();
        rasterLrLon = lr.getLRLON();
        rasterLrLat = lr.getLRLAT();
        rasterDepth = ul.depth();
    }

    /*
    private String[][] render_grid;
    private double raster_ul_lon;
    private double raster_ul_lat;
    private double raster_lr_lon;
    private double raster_lr_lat;
    private int raster_depth;
    private boolean query_success;
     */
    public void setValidMap() {
        result = new HashMap<>();
        if (!querySuccess) {
            result.put("render_grid", null);
            result.put("raster_ul_lon", null);
            result.put("raster_ul_lat", null);
            result.put("raster_lr_lon", null);
            result.put("raster_lr_lat", null);
            result.put("raster_depth", null);
            result.put("query_success", querySuccess);
            return;
        }
        result.put("render_grid", renderGrid);
        result.put("raster_ul_lon", rasterUlLon);
        result.put("raster_ul_lat", rasterUlLat);
        result.put("raster_lr_lon", rasterLrLon);
        result.put("raster_lr_lat", rasterLrLat);
        result.put("depth", rasterDepth);
        result.put("query_success", querySuccess);
    }

    public Map<String, Object> getValidMap() {
        return result;
    }

}
