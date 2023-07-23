import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {
    public void addFirstTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    @Test
    public void removeTestAndSizeAndIsEmptyAndGetTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.isEmpty()).isTrue();
        assertThat(lld1.size()).isEqualTo(0);

        for (int i = 0; i < 10; i++) {
            lld1.addLast(i);
            assertThat(lld1.get(i)).isEqualTo(i);
            assertThat(lld1.size()).isEqualTo(i+1);
        }
        assertThat(lld1.isEmpty()).isFalse();

        assertThat(lld1.removeFirst()).isEqualTo(0);
        assertThat(lld1.size()).isEqualTo(9);

        for (int i = 0; i < lld1.size(); i++) {
            assertThat(lld1.get(i)).isEqualTo(i+1);
        }

    }

    @Test
    public void IntegrateTest() {
        Deque<Integer> lld = new ArrayDeque<>();
        List<Integer> t = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            t.add(i-2);
            lld.addLast(i-2);
        }
        assertThat(lld.toList()).isEqualTo(t);

        for (int i = 0; i < 10; i++) lld.removeFirst();
        assertThat(lld.removeLast()).isNull();

        for (int i = 0; i < 10; i++) lld.addLast(i*2);
        assertThat(lld.size()).isEqualTo(10);

        assertThat(lld.toList()).containsExactly(0,2,4,6,8,10,12,14,16,18).inOrder();
    }
}
