package lab9;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MyHashMap<K, V> implements Map61B<K, V> {

    private int tableSize;
    private int actualTableSize;
    private double loadFactor = 5.0;
    private HashEntry<K, V>[] table;
    private Set<K> keySet;


    private class HashEntry<K, V> {
        private K key;
        private V value;
        private HashEntry<K, V> next;

        HashEntry(K key, V value) {
            this(key, value, null);
        }

        HashEntry(K key, V value, HashEntry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public void add(HashEntry<K, V> entry) {
            HashEntry<K, V> pointer = this;
            while (pointer.next != null) {
                pointer = pointer.next;
            }
            pointer.next = entry;
        }
    }



    public MyHashMap() {
        this(8, 3);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 3);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        if (initialSize < 1 || loadFactor <= 0) {
            throw new IllegalArgumentException();
        }
        this.tableSize = initialSize;

        this.table = new HashEntry[this.tableSize];
        for (int i = 0; i < this.tableSize; i++) {
            this.table[i] = new HashEntry<K, V>(null, null);
        }
        this.actualTableSize = 0;
        this.loadFactor = loadFactor;
        this.keySet = new HashSet<K>();
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % this.tableSize;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.table = new HashEntry[this.tableSize];
        for (int i = 0; i < this.tableSize; i++) {
            this.table[i] = new HashEntry<K, V>(null, null);
        }
        this.keySet = new HashSet<K>();
        this.actualTableSize = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return this.keySet.contains(key);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (!(this.keySet.contains(key))) {
            return null;
        }
        int hash = hash(key);
        HashEntry<K, V> entry = this.table[hash];
        HashEntry<K, V> pointer = entry.next;
        while (pointer != null) {
            if (pointer.getKey().equals(key)) {
                return pointer.getValue();
            }
            pointer = pointer.next;
        }
        return null;
    }



    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.actualTableSize;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        HashEntry<K, V> newEntry = new HashEntry<K, V>(key, value);
        int hash = hash(key);
        HashEntry<K, V> pointer = this.table[hash];
        if (this.keySet.contains(key)) {
            if (get(key).equals(value)) {
                return;
            }
            while (pointer.key == null || !(pointer.key.equals(key))) {
                pointer = pointer.next;
            }
            pointer.value = value;
            return;
        }
        this.actualTableSize++;
        pointer.add(newEntry);
        this.keySet.add(key);
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return this.keySet;
    }

    public Iterator iterator() {
        return this.keySet.iterator();
    }



    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("ERROR");
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("ERROR");
    }
}
