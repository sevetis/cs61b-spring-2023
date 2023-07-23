import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    /** front and back
        A Deque:
            Front             Back
            [1, 2, 3, ..., n-1, n]

        Underlying Array:
            [n, n-1, ...garbage... 3, 2, 1]
             back ---->           <---- front

                a loop:
                    ... 4 , 3, 2, 1, 0]  [0, 1, 2, 3, 4 ...
                        index of front    index of back

                real index:
                                the end of array       the beginning of array
                ... length-3, length-2, length-1]     [0, 1, 2, 3, 4 ...
                         status  |     left         |   status   |   right
                Front: |   >=0   |  length-front-1  |    <0      |   -front-1
                Back:  |   <0    |  length+back     |    >=0     |    back

     **/
public class ArrayDeque<T> implements Deque<T> {
    T[] Array;

    int front;
    int back;
    private int fIndex(boolean status) {return status?Array.length-front-1:-front-1;}
    private int bIndex(boolean status) {return status?back:Array.length+back;}
    public ArrayDeque() {
        Array = (T[]) new Object[8];
        front = 0;
        back = 0;
    }
    @Override
    public void addFirst(T x) {
        if (front == size() || size() == Array.length - 1)
            resize(true);
        Array[fIndex(front>=0)] = x;
        front++;
    }

    @Override
    public void addLast(T x) {
        if (back == size() || size() == Array.length - 1)
            resize(true);
        Array[bIndex(back>=0)] = x;
        back++;
    }

    @Override
    public List<T> toList() {
        List<T> res = new ArrayList<>();
        if (front >= 0 && back >= 0) {
            res.addAll(Arrays.asList(Array).subList(fIndex(true)+1, Array.length));
            res.addAll(Arrays.asList(Array).subList(0, bIndex(true)));
        } else if (back < 0) {
            res.addAll(Arrays.asList(Array).subList(fIndex(true)+1, bIndex(false)));
        } else {
            res.addAll(Arrays.asList(Array).subList(fIndex(false)+1, bIndex(true)));
        }
        return res;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return front + back;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) return null;
        T res = front==0?Array[0]:Array[fIndex(front>=0)+1];
        front--;
        if (size() < Array.length>>1 && Array.length > 16)
            resize(false);
        return res;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) return null;
        T res = Array[bIndex(back>=0)-1];
        back--;
        if (size() < Array.length>>1 && Array.length > 16)
            resize(false);
        return res;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) { return null;}
        if (index < front) {
            return Array[fIndex(true)+1+index];
        } else if (front >= 0) {
            return Array[index-front];
        }
        return Array[fIndex(false)+1+index];
    }
    private void resize(boolean status) {
        int newLen = status?Array.length<<1:Array.length>>1;
        T[] newArray = (T[]) new Object[newLen];
        if (front >= 0 && back >= 0) {
            for (int i = fIndex(true), j = 0; i < Array.length; i++, j++)
                newArray[j] = Array[i];
            System.arraycopy(Array, 0, newArray, front, back);
        } else if (front >= 0) {
            for (int i = fIndex(true), j = 0; i > bIndex(false); i--, j++)
                newArray[j] = Array[Array.length-1-i];
        } else {
            System.arraycopy(Array, fIndex(false) + 1, newArray, 0, bIndex(true) - (fIndex(false) + 1));
        }
        back = size();
        front = 0;
        Array = newArray;
    }
}
