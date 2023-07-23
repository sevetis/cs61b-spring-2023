package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void put(K key, V value) {
        if (1.0 * size / capcity > loadFactor) {
            resize();
        }
        int i = Math.floorMod(key.hashCode(), capcity);
        if (buckets[i] == null) {
            buckets[i] = createBucket();
        }
        for (Node n: buckets[i]) {
            if (n.key.equals(key)) {
                n.value = value;
                return;
            }
        }
        size++;
        buckets[i].add(new Node(key, value));
    }

    private void resize() {
        capcity *= 2;
        Collection<Node>[] newBuckets = createTable(capcity);
        for (var bucket: buckets) {
            if (bucket != null) {
                for (Node node: bucket) {
                    int i = Math.floorMod(node.key.hashCode(), capcity);
                    if (newBuckets[i] == null) newBuckets[i] = createBucket();
                    newBuckets[i].add(node);
                }
            }
        }
        buckets = newBuckets;
    }

    @Override
    public V get(K key) {
        int i = Math.floorMod(key.hashCode(), capcity);
        if (buckets[i] != null) {
            for (Node n: buckets[i]) {
                if (n.key.equals(key)) {
                    return n.value;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int i = Math.floorMod(key.hashCode(), capcity);
        if (buckets[i] != null) {
            for (Node n: buckets[i]) {
                if (n.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        capcity = 16;
        buckets = createTable(capcity);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    double loadFactor;
    private int size;
    private int capcity;

    /** Constructors */
    public MyHashMap() {
        size = 0;
        loadFactor = 0.75;
        capcity = 16;
        buckets = createTable(capcity);
    }

    public MyHashMap(int initialCapacity) {
        size = 0;
        loadFactor = 0.75;
        capcity = initialCapacity;
        buckets = createTable(capcity);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        size = 0;
        this.loadFactor= loadFactor;
        capcity = initialCapacity;
        buckets = createTable(capcity);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
