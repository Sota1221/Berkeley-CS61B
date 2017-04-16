
public class QuadTree {
    private Node root;

    public class Node {
        private int val;
        private Node child1, child2, child3, child4;
        private int depth;
        private String name;
        private double ULLAT;
        private double ULLON;
        private double LRLAT;
        private double LRLON;


        Node(int val, int depth, String nameFragment,
             double[] location) {
            this.val = val;
            this.depth = depth;
            if (val == 0) {
                this.name = nameFragment + "root.png";
            } else {
                this.name = nameFragment + this.val + ".png";
            }
            this.ULLAT = location[0];
            this.ULLON = location[1];
            this.LRLAT = location[2];
            this.LRLON = location[3];
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

        public String getName() {
            return name;
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
    public QuadTree(int initialValue, int maxHeight, String nameFragment, double[] location) {
        root = new Node(initialValue, 0, nameFragment, location);
        generateQuadTree(root, maxHeight, nameFragment, location);
    }

    // creates 4 children unless currentNode's depth is equal to maxHeight
    public Node generateQuadTree(Node currentNode, int maxHeight,
                                 String nameFragment, double[] location) {
        if (currentNode.depth >= maxHeight) {
            return currentNode;
        }
        double[] child1Location = setLocation(1, location);
        double[] child2Location = setLocation(2, location);
        double[] child3Location = setLocation(3, location);
        double[] child4Location = setLocation(4, location);
        currentNode.child1 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 1,
                        depth(currentNode) + 1, nameFragment, child1Location),
                maxHeight, nameFragment, child1Location);
        currentNode.child2 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 2,
                        depth(currentNode) + 1, nameFragment, child2Location),
                maxHeight, nameFragment, child2Location);
        currentNode.child3 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 3,
                        depth(currentNode) + 1, nameFragment, child3Location),
                maxHeight, nameFragment, child3Location);
        currentNode.child4 = generateQuadTree(
                new Node(getVal(currentNode) * 10 + 4,
                        depth(currentNode) + 1, nameFragment, child4Location),
                maxHeight, nameFragment, child4Location);
        return currentNode;
    }

    /*     this.ULLAT = location[0];
           this.ULLON = location[1];
           this.LRLAT = location[2];
           this.LRLON = location[3];
           */
    public double[] setLocation(int childNumber, double[] parentLocation) {
        double[] childLocation = new double[4];
        if (childNumber == 1) {
            childLocation[0] = parentLocation[0];  // ULLAT
            childLocation[1] = parentLocation[1];  // ULLON
            childLocation[2] = (parentLocation[0] + parentLocation[2]) / 2; // LRLAT
            childLocation[3] = (parentLocation[1] + parentLocation[3]) / 2; // LRLON
        } else if (childNumber == 2) {
            childLocation[0] = parentLocation[0];
            childLocation[1] = (parentLocation[1] + parentLocation[3]) / 2;
            childLocation[2] = (parentLocation[0] + parentLocation[2]) / 2;
            childLocation[3] = parentLocation[3];
        } else if (childNumber == 3) {
            childLocation[0] = (parentLocation[0] + parentLocation[2]) / 2;
            childLocation[1] = parentLocation[1];
            childLocation[2] = parentLocation[2];
            childLocation[3] = (parentLocation[1] + parentLocation[3]) / 2;
        } else {
            childLocation[0] = (parentLocation[0] + parentLocation[2]) / 2;
            childLocation[1] = (parentLocation[1] + parentLocation[3]) / 2;
            childLocation[2] = parentLocation[2];
            childLocation[3] = parentLocation[3];
        }
        return childLocation;
    }


    public Node getRoot() {
        return root;
    }

    public int getVal() {
        return root.val;
    }

    public int getVal(Node x) {
        return x.val;
    }

    public int depth() {
        return depth(root);
    }

    public int depth(Node x) {
        if (x == null) {
            return 0;
        }
        return x.depth;
    }

    public String name(Node x) {
        return x.name;
    }

    public boolean isLeaf(Node x) {
        if (x.child1 == null) {
            return true;
        }
        return false;
    }


}
