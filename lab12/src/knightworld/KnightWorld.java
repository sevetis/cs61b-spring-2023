package knightworld;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

/**
 * Draws a world consisting of knight-move holes.
 */
public class KnightWorld {

    private TETile[][] tiles;
    // TODO: Add additional instance variables here
    private int WIDTH;

    private int HEIGHT;

    private int HOLESIZE;

    public KnightWorld(int width, int height, int holeSize) {
        // TODO: Fill in this constructor and class, adding helper methods and/or classes as necessary to draw the
        //  specified pattern of the given hole size for a window of size width x height. If you're stuck on how to
        //  begin, look at the provided demo code!
        WIDTH = width;
        HEIGHT = height;
        HOLESIZE = holeSize;

        tiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.LOCKED_DOOR;
            }
        }

        int unitSize = 5 * HOLESIZE;
        for (int i = 0; i < WIDTH; i+= unitSize) {
            for (int j = 0; j < HEIGHT; j+= unitSize) {
                handleUnit(i, j);
            }
        }
    }

    private void handleUnit(int x, int y) {
        dig(x, y);
        dig(x + 2 * HOLESIZE, y + HOLESIZE);
        dig(x + 4 * HOLESIZE, y + 2 * HOLESIZE);
        dig(x + HOLESIZE, y + 3 * HOLESIZE);
        dig(x + 3 * HOLESIZE, y + 4 * HOLESIZE);
    }

    private void dig(int x, int y) {
        for (int width = 0; width < HOLESIZE; width++) {
            for (int height = 0; height < HOLESIZE; height++) {
                if (x+width < WIDTH && y+height < HEIGHT) {
                    tiles[x+width][y+height] = Tileset.NOTHING;
                }
            }
        }
    }

    /** Returns the tiles associated with this KnightWorld. */
    public TETile[][] getTiles() {
        return tiles;
    }

    public static void main(String[] args) {
        // Change these parameters as necessary
        int width = 50;
        int height = 30;
        int holeSize = 2;

        KnightWorld knightWorld = new KnightWorld(width, height, holeSize);

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        ter.renderFrame(knightWorld.getTiles());

    }
}
