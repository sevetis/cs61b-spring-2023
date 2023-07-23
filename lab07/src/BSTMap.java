import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class BST {
        K key;
        V val;
        BST left;
        BST right;
        int size;
        BST() {}
        BST(K k, V v) {
            size = 0;
            key = k;
            val = v;
        }
    }
    int size;
    BST root;
    BSTMap() { size = 0;}

    @Override
    public void put(K key, V value) {
        root = insertBST(root, new BST(key, value));
    }

    private BST insertBST(BST root, BST insert) {
        if (root == null) {
            size++;
            return insert;
        } else if (root.key.compareTo(insert.key) > 0) {
            root.left = insertBST(root.left, insert);
        } else if (root.key.compareTo(insert.key) < 0) {
            root.right = insertBST(root.right, insert);
        } else if (root.key.compareTo(insert.key) == 0) {
            root.val = insert.val;
        }
        return root;
    }

    @Override
    public V get(K key) {
        BST search = searchBST(key);
        if (search != null) {
            return search.val;
        }
        return null;
    }

    // Recursive Way
    private BST searchBST(BST root, K key) {
        if (root == null) {
            return null;
        } else if (root.key.compareTo(key) > 0) {
            return searchBST(root.left, key);
        } else if (root.key.compareTo(key) < 0) {
            return searchBST(root.right, key);
        }
        return root;
    }

    private BST searchBST(K k) {
        BST p = root;
        while (p != null) {
            if (p.key.compareTo(k) > 0) {
                p = p.left;
            } else if (p.key.compareTo(k) < 0) {
                p = p.right;
            } else {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return searchBST(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public V remove(K key) {
        BST rm = searchBST(key);
        if (rm != null) {
            removeBST(rm);
            return rm.val;
        }
        return null;
    }

    // BULLSHIT!
    private void removeBST(BST rm) {
        BST pre = searchPreBST(root, rm);
        BST leftReplace = searchLeftNeighbor(rm);
        if (pre != null) {   // rm is not root
            size--;
            if (leftReplace == null) {
                if (pre.left == rm) {
                    pre.left = rm.right;
                } else {
                    pre.right = rm.right;
                }
            } else {
                BST preReplace = searchPreBST(root, leftReplace);
                preReplace.right.left = rm.left;
                preReplace.right.right = rm.right;
                rm.left = null;
                rm.right = null;
                if (pre.left == rm) {
                    pre.left = preReplace.right;
                } else {
                    pre.right = preReplace.right;
                }
                preReplace.right = null;
            }
        } else if (leftReplace == null) { // no left branch
            size--;
            root = root.right;
            rm.right = null;
        } else {
            BST preReplace = searchPreBST(root, leftReplace);
            if (preReplace == root) {         // left branch is a leaf
                root.left.right = root.right;
                root = root.left;
            } else {
                preReplace.right.left = rm.left;
                preReplace.right.right = rm.right;
                root = preReplace;
                preReplace.right = null;
            }
            rm.right = null;
            rm.left = null;
            size--;
        }
    }

    private BST searchPreBST(BST root, BST target) {
        if (root != null && target != root) {
            if (root.left == target || root.right == target) {
                return root;
            } else if (root.key.compareTo(target.key) > 0) {
                return searchPreBST(root.left, target);
            } else {
                return searchPreBST(root.right, target);
            }
        }
        return null;
    }

    private BST searchLeftNeighbor(BST rm) {
        if (rm == null) {
            throw new IllegalStateException();
        } else if (rm.left == null) {
            return null;
        } else {
            BST p = rm.left;
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> res = new TreeSet<>();
        setCreator(res, root);
        return res;
    }

    private void setCreator(Set<K> res, BST root) {
        if (root == null) {
            return;
        }
        setCreator(res, root.left);
        res.add(root.key);
        setCreator(res, root.right);
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator(root);
    }

    private class BSTMapIterator implements Iterator<K> {
        Iterator<K> i;
        Set<K> keys;
        BSTMapIterator(BST root) {
            keys = keySet();
            i = keys.iterator();
        }

        @Override
        public boolean hasNext() {
            return i.hasNext();
        }

        @Override
        public K next() {
            if (i.hasNext()) {
                return i.next();
            }
            throw new IllegalStateException();
        }
    }
}
