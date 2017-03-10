package lab8;

import java.util.Iterator;
import java.util.Set;


public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private int N;

        Node(K key, V value, int N) {
            this.key = key;
            this.value = value;
            this.N = N;
        }

        private void print(Node x) {
            if (x == null) {
                return;
            }
            print(x.left);
            System.out.print(x.value);
            print(x.right);
        }

        private boolean hasKey(Node x, K givenkey) {
            if (x == null || x.key == null) {
                return false;
            }
            if (x.key.equals(givenkey)) {
                return true;
            }
            return hasKey(x.left, givenkey) || hasKey(x.right, givenkey);
        }
    }


    public void printInOrder() {
        if (this.root.N == 0) {
            return;
        }
        this.root.print(this.root);
    }


    @Override
    public void clear() {
        this.root.key = null;
        this.root.value = null;
        this.root.right = null;
        this.root.left = null;
        this.root.N = 0;
    }


    @Override
    public boolean containsKey(K key) {
        if (this.root == null) {
            return false;
        }
        return this.root.hasKey(this.root, key);
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (x == null || !containsKey(key)) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }

    @Override
    public int size() {
        return size(root);
    }

    public int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.N;
        }
    }


    @Override
    public void put(K key, V value) {
        this.root = put(this.root, key, value);
    }

    public Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, value);
        } else if (cmp > 0) {
            x.right = put(x.right, key, value);
        } else {
            x.value = value;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("in KeySet()");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("in remove(K key)");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("in remove(K key, V value)");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("in iterator()");
    }

}
