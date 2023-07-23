import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {
    static class Node<T> {
        public Node() {}
        public Node(T v) { val = v; }
        T val;
        Node<T> next;
        Node<T> prev;
    }
    Node<T> sentinel;
    int size;
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node<>();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    @Override
    public void addFirst(T x) {
        size++;
        Node<T> n = new Node<>(x);
        sentinel.next.prev = n;
        n.next = sentinel.next;
        n.prev = sentinel;
        sentinel.next = n;
    }

    @Override
    public void addLast(T x) {
        size++;
        Node<T> n = new Node<>(x);
        sentinel.prev.next = n;
        n.prev = sentinel.prev;
        n.next = sentinel;
        sentinel.prev = n;
    }

    @Override
    public List<T> toList() {
        Node<T> p = sentinel.next;
        List<T> res = new ArrayList<>();
        while (p != sentinel) {
            res.add(p.val);
            p = p.next;
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        size--;
        T res = sentinel.next.val;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev.next = null;
        sentinel.next.prev.prev = null;
        sentinel.next.prev = sentinel;
        return res;
    }

    @Override
    public T removeLast() {
        size--;
        T res = sentinel.prev.val;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next.next = null;
        sentinel.prev.next.prev = null;
        sentinel.prev.next = sentinel;
        return res;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) { return null; }
        Node<T> p =sentinel.next;
        while (index-- > 0) {
            p = p.next;
        }
        return p.val;
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index >= size) { return null; }
        return getRHelper(sentinel.next, index);
    }
    private T getRHelper(Node<T> n, int index) {
        if (index == 0) {
            return n.val;
        }
        return getRHelper(n.next, index-1);
    }
}
