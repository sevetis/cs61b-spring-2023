package gh2;

import deque.Deque;
import deque.LinkedListDeque;
import edu.princeton.cs.algs4.StdAudio;
// TODO: maybe more imports

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR/frequency);
        buffer = new LinkedListDeque<>();
        for (int i = 0; i < capacity; i++) {
            buffer.addLast(.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        while (buffer.get(0) == .0) {
            buffer.removeFirst();
            buffer.addLast(Math.random() - .5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double pop = buffer.removeFirst();
        buffer.addLast(DECAY * (pop + buffer.get(0))/2);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
    public static void main(String[] args) {
        GuitarString a = new GuitarString(440.0);
        a.pluck();
        for (int i = 0; i < 1000; i++) {
            StdAudio.play(a.sample());
            a.tic();
        }
    }
}
