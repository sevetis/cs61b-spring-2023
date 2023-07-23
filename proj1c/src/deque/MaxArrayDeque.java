package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> c;
    public MaxArrayDeque(Comparator<T> c) {
        this.c = c;
    }

    public T max() {
        T res = get(0);
        for (T v: this) {
            if (c.compare(res, v) < 0) {
                res = v;
            }
        }
        return res;
    }

    public T max(Comparator<T> c) {
        T res = get(0);
        for (T v: this) {
            if (c.compare(res, v) < 0) {
                res = v;
            }
        }
        return res;
    }
}
