package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class worldGenerator {
    private final int width;
    private final int height;
    private final Random random;
    private final TETile[][] world;

    // coordination of avatar
    private final int[] position;

    public worldGenerator(int w, int h, long seed) {
        width = w;
        height = h;
        random = new Random(seed);
        world = new TETile[w][h];
        position = worldGenerate();
    }

    public TETile[][] getWorld() {
        return world;
    }

    public int[] getPosition() {
        return position;
    }

    private int[] worldGenerate() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        //rooms generation
        int num = RandomUtils.uniform(random, 12, 24);
        int[][] rooms = new int[num][2];
        for (int i = 0; i < num; i++) {
            int x = random.nextInt(width - 13) + 1;
            int y = random.nextInt(height - 13) + 1;

            int width = RandomUtils.uniform(random, 4, 11);
            int height = RandomUtils.uniform(random, 4, 11);

            roomGenerator(x, y, width, height);

            rooms[i][0] = (x * 2 + width) / 2;
            rooms[i][1] = (y * 2 + height) / 2;
        }

        //hallways generation
        for (int i = 1; i < num; i++) {
            int x1 = rooms[i - 1][0], y1 = rooms[i - 1][1];
            int x2 = rooms[i][0], y2 = rooms[i][1];

            int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
            int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
            int verticalLen = maxY - minY, horizontalLen = maxX - minX;
            int delta = (x1 - x2) * (y1 - y2);

            if (RandomUtils.bernoulli(random)) {
                hallWayGenerator(minX, minY, verticalLen, true);
                if (delta > 0) {
                    hallWayGenerator(minX, maxY, horizontalLen, false);
                } else {
                    hallWayGenerator(minX, minY, horizontalLen, false);
                }
            } else {
                hallWayGenerator(minX, minY, horizontalLen, false);
                if (delta > 0) {
                    hallWayGenerator(maxX, minY, verticalLen, true);
                } else {
                    hallWayGenerator(minX, minY, verticalLen, true);
                }
            }

            // increase the diversity of world's tiles
            if (RandomUtils.bernoulli(random, 0.6)) {
                grow(x1, y1);
            } else {
                water(x1, y1);
            }
        }

        // generate a key
        while (true) {
            int i = rooms[0][0] - RandomUtils.uniform(random, -4, 5), j = rooms[0][1] - RandomUtils.uniform(random, -4, 5);
            if (world[i][j].equals(Tileset.FLOOR)) {
                world[i][j] = Tileset.KEY;
                break;
            }
        }
        // generate a lockedDoor
        while (true) {
            int i = RandomUtils.uniform(random, 2, width - 2), j = RandomUtils.uniform(random, 2, height - 2);
            if (world[i][j].equals(Tileset.WALL) &&
                    ((world[i + 1][j].equals(Tileset.NOTHING) || world[i - 1][j].equals(Tileset.NOTHING)) ||
                    (world[i][j + 1].equals(Tileset.NOTHING) || world[i][j - 1].equals(Tileset.NOTHING)))) {
                world[i][j] = Tileset.LOCKED_DOOR;
                break;
            }
        }

        world[rooms[num - 1][0]][rooms[num - 1][1]] = Tileset.AVATAR;
        return rooms[num - 1];
    }

    private void roomGenerator(int x, int y, int width, int height) {
        for (int row = x + 1; row < x + width; row++) {
            for (int col = y + 1; col < y + height; col++) {
                world[row][col] = Tileset.FLOOR;
            }
        }
        drawHorizontalEdge(x, y, width);
        drawHorizontalEdge(x, y + height, width);
        drawVerticalEdge(x, y, height);
        drawVerticalEdge(x + width, y, height);
    }

    private void hallWayGenerator(int x, int y, int len, boolean isVertical) {
        if (isVertical) {
            for (int i = 0; i <= len; i++) {
                world[x + 1][y + i] = Tileset.FLOOR;
            }
            drawVerticalEdge(x, y, len);
            drawVerticalEdge(x + 2, y, len);
            toWall(x + 1, y);
            toWall(x + 1, y + len);
        } else {
            for (int i = 0; i < len; i++) {
                world[x + i][y + 1] = Tileset.FLOOR;
            }
            drawHorizontalEdge(x, y, len);
            drawHorizontalEdge(x ,y + 2, len);
            toWall(x, y + 1);
            toWall(x + len, y + 1);
        }
    }

    //draw a horizontal line
    private void drawHorizontalEdge(int x, int y, int len) {
        for (int row = 0; row <= len; row++) {
            if (world[x + row][y].equals(Tileset.NOTHING)) {
                world[x + row][y] = Tileset.WALL;
            }
            toFloor(x + row, y);
        }
    }

    // draw a vertical line
    private void drawVerticalEdge(int x, int y, int len) {
        for (int col = 0; col <= len; col++) {
            if (world[x][y + col].equals(Tileset.NOTHING)) {
                world[x][y + col] = Tileset.WALL;
            }
            toFloor(x, y + col);
        }
    }

    // transform a tile to a floor tile if it can be floor
    private void toFloor(int x, int y) {
        boolean p = world[x + 1][y].equals(Tileset.FLOOR), q = world[x - 1][y].equals(Tileset.FLOOR),
                r = world[x][y - 1].equals(Tileset.FLOOR), s = world[x][y + 1].equals(Tileset.FLOOR);
        if ((p & q || r & s)) {
            world[x][y] = Tileset.FLOOR;
        }
    }

    // transform a tile to a wall tile if it should be wall
    private void toWall(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (world[i][j].equals(Tileset.NOTHING)) {
                    world[x][y] = Tileset.WALL;
                    return;
                }
            }
        }
    }

    // distribute some grass tile
    private void grow(int x, int y) {
        if (0 < x && x < width && 0 < y && y < height && world[x][y].equals(Tileset.FLOOR)) {
            if (RandomUtils.bernoulli(random, 0.6)) {
                world[x][y] = Tileset.GRASS;
            } else if (RandomUtils.bernoulli(random, 0.7)) {
                world[x][y] = Tileset.FLOOR;
            } else {
                world[x][y] = Tileset.TREE;
            }
        }
        if (RandomUtils.bernoulli(random, 0.9)) {
            int _x = RandomUtils.uniform(random, -1, 2), _y = RandomUtils.uniform(random, -1, 2);
            grow(x + _x, y + _y);
        }
    }

    // distribute some water tile
    private void water(int x, int y) {
        if (0 < x && x < width && 0 < y && y < height && world[x][y].equals(Tileset.FLOOR)) {
            if (RandomUtils.bernoulli(random, 0.9)) {
                world[x][y] = Tileset.WATER;
            }
        }
        if (RandomUtils.bernoulli(random, 0.95)) {
            int _x = RandomUtils.uniform(random, -1, 2), _y = RandomUtils.uniform(random, -1, 2);
            water(x + _x, y + _y);
        }
    }
}