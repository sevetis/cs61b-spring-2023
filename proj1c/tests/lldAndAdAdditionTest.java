import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class lldAndAdAdditionTest {
    @Test
    public void iteratorTest() {
        Deque<Integer> lld = new LinkedListDeque<>();
        Deque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            lld.addLast(i);
            ad.addLast(i);
        }
        int count = 0;
        for (int i: lld) {
            assertThat(i).isEqualTo(count++);
        }
        count = 0;
        for (int i: ad) {
            assertThat(i).isEqualTo(count++);
        }
    }

    public void addLastTestBasicWithoutToList() {
        Deque<String> lld1 = new LinkedListDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1).containsExactly("front", "middle", "back");
    }

    @Test
    public void testEqualDeques() {
        Deque<String> lld1 = new LinkedListDeque<>();
        Deque<String> lld2 = new LinkedListDeque<>();

        assertThat(lld1.equals(lld2)).isTrue();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        assertThat(lld1).isEqualTo(lld2);

        Deque<Integer> ad1 = new ArrayDeque<>();
        Deque<Integer> ad2 = new ArrayDeque<>();

        assertThat(ad1).isEqualTo(ad2);
        for (int i = 0; i < 10; i++) {
            ad1.addLast(i);
            ad2.addLast(i);
        }
        assertThat(ad1).isEqualTo(ad2);
        assertThat(ad1.equals(lld1)).isFalse();
    }
}
