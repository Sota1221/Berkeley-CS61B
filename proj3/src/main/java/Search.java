import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Search {
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    private Map<String, Double> target;
    private LinkedList<Double> sortedKeys;
    private double targetULLAT;
    private double targetULLON;
    private double targetLRLAT;
    private double targetLRLON;
    private double targetWidth;
    private double targetHeight;
    private double targetLonDPP;
    private Map<Double, LinkedList<Integer>> classifiedNodes;
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
    private String imgRoot;

    Search(Map<String, Double> target, String nameFragment) {
        this.imgRoot = nameFragment;
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
        seekValidNode(0, ROOT_ULLAT, ROOT_ULLON, ROOT_LRLAT, ROOT_LRLON, 0, DEPTH_OF_ROOT);
        if (!querySuccess) {
            setValidMap();
        } else {
            renderGrid =
                    new String[classifiedNodes.size()][numOfPics / classifiedNodes.size()];
            setValidPics();
            setValidMap();
        }

    }

    // target has
    // lrlon, ullon, w, h, ullat, lrlat
    public void seekValidNode(int currentVal, double parentULLAT, double parentULLON,
                              double parentLRLAT, double parentLRLON, int selfType, int depth) {
        double ullat = parentULLAT;
        double ullon = parentULLON;
        double lrlat = parentLRLAT;
        double lrlon = parentLRLON;
        if (selfType == 1) {
            lrlat = (ullat + lrlat) / 2;
            lrlon = (ullon + lrlon) / 2;
        } else if (selfType == 2) {
            ullon = (ullon + lrlon) / 2;
            lrlat = (ullat + lrlat) / 2;
        } else if (selfType == 3) {
            ullat = (ullat + lrlat) / 2;
            lrlon = (ullon + lrlon) / 2;
        } else if (selfType == 4) {
            ullat = (ullat + lrlat) / 2;
            ullon = (ullon + lrlon) / 2;
        }

        if (lrlat > targetULLAT || targetLRLON < ullon
                || lrlon < targetULLON || targetLRLAT > ullat) {
            if (depth == DEPTH_OF_ROOT) {
                querySuccess = false;
            }
            return;
        }
        double lonDPP = (lrlon - ullon) / WIDTH_FOR_EACH_IMAGE;
        if (lonDPP > targetLonDPP && depth < MAX_HEIGHT) {
            seekValidNode(currentVal * 10 + 1, ullat, ullon, lrlat, lrlon, 1, depth + 1);
            seekValidNode(currentVal * 10 + 2, ullat, ullon, lrlat, lrlon, 2, depth + 1);
            seekValidNode(currentVal * 10 + 3, ullat, ullon, lrlat, lrlon, 3, depth + 1);
            seekValidNode(currentVal * 10 + 4, ullat, ullon, lrlat, lrlon, 4, depth + 1);
            return;
        }
        if (!classifiedNodes.containsKey(ullat)) {
            if (classifiedNodes.isEmpty()) {
                rasterUlLat = ullat;
                rasterUlLon = ullon;
            }
            LinkedList<Integer> lst = new LinkedList<>();
            lst.add(currentVal);
            sortedKeys.addLast(ullat);
            classifiedNodes.put(ullat, lst);
            rasterLrLat = lrlat;
            rasterLrLon = lrlon;
            rasterDepth = depth;
            numOfPics++;
        } else {
            classifiedNodes.get(ullat).addLast(currentVal);
            rasterLrLat = lrlat;
            rasterLrLon = lrlon;
            rasterDepth = depth;
            numOfPics++;
        }
    }


    public void setValidPics() {
        int rowIndex = 0;
        for (Double key: sortedKeys) {
            LinkedList<Integer> lst = classifiedNodes.get(key);
            for (int i = 0; i < lst.size(); i++) {
                renderGrid[rowIndex][i] = imgRoot + lst.get(i) + ".png";
            }
            rowIndex++;
        }
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
