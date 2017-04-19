
public class QuadTree {
    private Node root;

    public class Node {
        private int val;
        private Node child1, child2, child3, child4;
        private int depth;
        private double ULLAT;
        private double ULLON;
        private double LRLAT;
        private double LRLON;


        Node(int val, int depth,
             double ullat, double ullon,
             double lrlat, double lrlon) {
            this.val = val;
            this.depth = depth;
            this.ULLAT = ullat;
            this.ULLON = ullon;
            this.LRLAT = lrlat;
            this.LRLON = lrlon;
        }

        public Node getChild(int childNumber) {
            if (childNumber == 1) {
                return this.child1;
            } else if (childNumber == 2) {
                return this.child2;
            } else if (childNumber == 3) {
                return this.child3;
            } else {
                return this.child4;
            }
        }

        public int getVal() {
            return val;
        }

        public double getULLAT() {
            return ULLAT;
        }

        public double getULLON() {
            return ULLON;
        }

        public double getLRLAT() {
            return LRLAT;
        }

        public double getLRLON() {
            return LRLON;
        }

        public int depth() {
            return depth;
        }
    }

    /* create quadTree
        1
        1, 2, 3, 4
        11 12 13 14, 21 22 23 24, ...


     */
    public QuadTree(int initialValue, int maxHeight, double ullat, double ullon,
                    double lrlat, double lrlon) {
        root = new Node(initialValue, 0, ullat, ullon, lrlat, lrlon);
        generateQuadTree(root, maxHeight, ullat, ullon, lrlat, lrlon);
    }

    // creates 4 children unless currentNode's depth is equal to maxHeight
    public Node generateQuadTree(Node currentNode, int maxHeight,
                                 double ullat, double ullon,
                                 double lrlat, double lrlon) {
        if (currentNode.depth >= maxHeight) {
            return currentNode;
        }
        double halfLat = (ullat + lrlat) / 2;
        double halfLon = (ullon + lrlon) / 2;
        currentNode.child1 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 1,
                        depth(currentNode) + 1, ullat, ullon,
                         halfLat, halfLon),
                maxHeight, ullat, ullon, halfLat, halfLon);
        currentNode.child2 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 2,
                        depth(currentNode) + 1, ullat, halfLon,
                        halfLat, lrlon),
                maxHeight, ullat, halfLon, halfLat, lrlon);
        currentNode.child3 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 3,
                        depth(currentNode) + 1, halfLat, ullon, lrlat,
                        halfLon),
                maxHeight, halfLat, ullon, lrlat, halfLon);
        currentNode.child4 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 4,
                        depth(currentNode) + 1, halfLat, halfLon, lrlat, lrlon),
                maxHeight, halfLat, halfLon, lrlat, lrlon);
        return currentNode;
    }



    public Node getRoot() {
        return root;
    }


    public int getVal(Node x) {
        return x.val;
    }

    public int depth(Node x) {
        if (x == null) {
            return 0;
        }
        return x.depth;
    }


}
