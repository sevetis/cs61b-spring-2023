import deque.MaxArrayDeque;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static com.google.common.truth.Truth.assertThat;

public class MaxArrayDequeTest {

    @Test
    public void test() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new comparators.normalComparator<>());
        mad.addLast(123);
        mad.addLast(12);
        mad.addLast(2133);
        mad.addLast(2109);
        assertThat(mad.max()).isEqualTo(2133);

        mad.addLast(2048);
        assertThat(mad.max(new comparators.maxEvenComparator<>())).isEqualTo(2048);

    }
}

class comparators {
    public static class normalComparator<T> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            if (o1 instanceof Number n1 && o2 instanceof Number n2) {
                if (n1.doubleValue() - n2.doubleValue() == 0) {
                    return 0;
                }
                return (int)(n1.doubleValue() - n2.doubleValue());
            }
            throw new IllegalStateException();
        }
    }

    public static class maxEvenComparator<T> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            if (o1 instanceof Integer n1 && o2 instanceof Integer n2) {
                if (n1%2 == 0) {
                    if (n2%2 == 0) {
                        return n1 - n2;
                    }
                    return 1;
                }
                return -1;
            }
            throw new IllegalStateException();
        }
    }
}
