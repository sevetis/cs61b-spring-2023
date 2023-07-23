package ngordnet.ngrams;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class Graph<V> extends HashMap<V, Set<V>> {

    public void addNode(V v) {
        put(v, null);
    }

    public void addEdge(V v, V c) {
        Set<V> child = get(v);
        if (child == null) {
            child = new HashSet<>();
        }
        child.add(c);
        put(v, child);
    }

}

