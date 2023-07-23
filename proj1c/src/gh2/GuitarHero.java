package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

import java.io.InputStream;

public class GuitarHero {
    private static final String keyboard="q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    private static double frequency(int i) {
        return 440 * Math.pow(2, (i - 24.0)/12);
    }

    private void strum(GuitarString str) {
        str.pluck();
        for (int i = 0; i < 10000; i++) {
            StdAudio.play(str.sample());
            str.tic();
        }
    }

    public static void main(String[] args) {
        char input = '?';
        GuitarHero hero = new GuitarHero();
        while (input != 'l') {
            if (StdDraw.hasNextKeyTyped()) {
                input = StdDraw.nextKeyTyped();
                hero.strum(new GuitarString(frequency(keyboard.indexOf(input))));
            }
        }
    }
}
