package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        mainMenu();

        StringBuilder inputString = new StringBuilder();
        long seed = 0;

        //main menu options
        boolean load = false;
        String loadString = "";
        while (seed == 0) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                switch (c) {
                    case 'Q' -> {
                        System.exit(0);
                    }
                    case 'N' -> {
                        seed = seedInput();
                        inputString.append('N').append(seed).append('S');
                    }
                    case 'L' -> {
                        load = true;
                        loadString = new In("save/save.txt").readLine();
                        seed = -1;
                    }
                }
            }
        }

        // world creation
        int x, y;
        TETile[][] world;
        TETile[][] sight = new TETile[WIDTH][HEIGHT];
        if (!load) {
            worldGenerator wg = new worldGenerator(WIDTH, HEIGHT, seed);
            world = wg.getWorld();
            int[] position = wg.getPosition();
            x = position[0];
            y = position[1];
        } else {
            String[] info = loadString.split(" ");
            inputString = new StringBuilder(info[0].substring(0, info[0].length() - 2));
            world = interactWithInputString(info[0]);
            x = Integer.parseInt(info[1]);
            y = Integer.parseInt(info[2]);
        }

        // limited sight
        sight(sight, world, x, y);
        boolean sightKey = true;

        int mx = 0, my = 0, _mx, _my;
        TETile temp = Tileset.FLOOR;

        ter.initialize(WIDTH, HEIGHT + 2);
        ter.renderFrame(sight);

        boolean hasKey = false, unlockDoor = false;

        while (!unlockDoor) {

            //HUD
            _mx = (int) StdDraw.mouseX();
            _my = (int) StdDraw.mouseY();
            if (mx != _mx || my != _my) {
                mx = _mx;
                my = _my;
                if ((sightKey && Math.abs(mx - x) + Math.abs(my - y) < 6) || (!sightKey && my < HEIGHT)) {
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.filledRectangle(1, HEIGHT - 1, 5, 3);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.textLeft(1.5, HEIGHT + 0.5, world[mx][my].description());
                    StdDraw.show();
                }
            }

            // the key to unlock lockedDoor
            if (temp.equals(Tileset.KEY)) {
                temp = Tileset.FLOOR;
                hasKey = true;
            }
            if (hasKey) {
                StdDraw.setPenColor(Color.YELLOW);
                StdDraw.text(WIDTH - 5, HEIGHT + 0.5, "You've got the key");
                StdDraw.show();
            }

            // interaction
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                switch (c) {
                    case 'W' -> {
                        if (canGet(world, x, y + 1)) {
                            world[x][y] = temp;
                            y += 1;
                            temp = world[x][y];
                            world[x][y] = Tileset.AVATAR;
                            inputString.append('W');
                        } else if (hasKey && world[x][y + 1].equals(Tileset.LOCKED_DOOR)) {
                            world[x][y + 1] = Tileset.UNLOCKED_DOOR;
                            unlockDoor = true;
                        }
                    }
                    case 'S' -> {
                        if (canGet(world, x, y - 1)) {
                            world[x][y] = temp;
                            y -= 1;
                            temp = world[x][y];
                            world[x][y] = Tileset.AVATAR;
                            inputString.append('S');
                        } else if (hasKey && world[x][y - 1].equals(Tileset.LOCKED_DOOR)) {
                            world[x][y - 1] = Tileset.UNLOCKED_DOOR;
                            unlockDoor = true;
                        }
                    }
                    case 'A' -> {
                        if (canGet(world, x - 1, y)) {
                            world[x][y] = temp;
                            x -= 1;
                            temp = world[x][y];
                            world[x][y] = Tileset.AVATAR;
                            inputString.append('A');
                        } else if (hasKey && world[x - 1][y].equals(Tileset.LOCKED_DOOR)) {
                            world[x - 1][y] = Tileset.UNLOCKED_DOOR;
                            unlockDoor = true;
                        }
                    }
                    case 'D' -> {
                        if (canGet(world, x + 1, y)) {
                            world[x][y] = temp;
                            x += 1;
                            temp = world[x][y];
                            world[x][y] = Tileset.AVATAR;
                            inputString.append('D');
                        } else if (hasKey && world[x + 1][y].equals(Tileset.LOCKED_DOOR)) {
                            world[x + 1][y] = Tileset.UNLOCKED_DOOR;
                            unlockDoor = true;
                        }
                    }
                    case 'L' -> {
                        sightKey = !sightKey;
                    }
                    case ':' -> {
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                c = Character.toUpperCase(StdDraw.nextKeyTyped());
                                if (c == 'Q') {
                                    inputString.append(":Q");
                                    new Out("save/save.txt").print(inputString + " " + x + " " + y);
                                    System.exit(0);
                                } else break;
                            }
                        }
                    }
                }
                sight(sight, world, x, y);
                if (sightKey) {
                    ter.renderFrame(sight);
                } else {
                    ter.renderFrame(world);
                }
            }
        }

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 46));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "YOU WIN!");
        StdDraw.show();
    }

    // judge whether a tile can be reach by avatar
    private boolean canGet(TETile[][] world, int x, int y) {
        TETile position = world[x][y];
        return position.equals(Tileset.FLOOR) || position.equals(Tileset.GRASS)
                || position.equals(Tileset.KEY) || position.equals(Tileset.WATER);
    }

    // create the world with limited sight
    private void sight(TETile[][] sight, TETile[][] world, int x, int y) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (Math.abs(i - x) + Math.abs(j - y) < 6) {
                    sight[i][j] = world[i][j];
                } else {
                    sight[i][j] = Tileset.NOTHING;
                }
            }
        }

    }

    private void mainMenu() {
        StdDraw.filledSquare(0, 0, 1);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.70, "The Game");
        StdDraw.text(0.5, 0.60, "New Game (N)");
        StdDraw.text(0.5, 0.55, "Load Game (L)");
        StdDraw.text(0.5, 0.50, "Quit (Q)");
    }

    // read the seed
    private long seedInput() {
        StringBuilder seed = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(0.5, 0.6, "Enter seed: finish(S)");

                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (c == 'S') {
                    break;
                } else if (Character.isDigit(c)) {
                    seed.append(c);
                    StdDraw.text(0.5, 0.55, seed.toString());
                }
            }
        }
        return Long.parseLong(seed.toString());
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        if (input.charAt(0) == 'L') { input = new In("save/save.txt").readString() + input; }
        String[] info = input.split("S", 2);

        long seed = Long.parseLong(info[0].substring(1));
        String instructions = "";

        boolean save = true;
        if (info[1].length() > 2) {
            instructions = info[1].split(":Q")[0];
            if (instructions.equals(info[1])) {
                save = false;
            }
        }

        worldGenerator wg = new worldGenerator(WIDTH, HEIGHT, seed);

        TETile[][] finalWorldFrame = wg.getWorld();
        int[] position = wg.getPosition();
        int x = position[0], y = position[1];
        TETile temp = Tileset.FLOOR;

        boolean hasKey = false, unlockDoor = false;

        for (char c : instructions.toCharArray()) {
            if (temp.equals(Tileset.KEY)) {
                temp = Tileset.FLOOR;
                hasKey = true;
            }

            switch (c) {
                case 'W' -> {
                    if (canGet(finalWorldFrame, x, y + 1)) {
                        finalWorldFrame[x][y] = temp;
                        y += 1;
                        temp = finalWorldFrame[x][y];
                        finalWorldFrame[x][y] = Tileset.AVATAR;
                    } else if (hasKey && finalWorldFrame[x][y + 1].equals(Tileset.LOCKED_DOOR)) {
                        finalWorldFrame[x][y + 1] = Tileset.UNLOCKED_DOOR;
                        unlockDoor = true;
                    }
                }
                case 'S' -> {
                    if (canGet(finalWorldFrame, x, y - 1)) {
                        finalWorldFrame[x][y] = temp;
                        y -= 1;
                        temp = finalWorldFrame[x][y];
                        finalWorldFrame[x][y] = Tileset.AVATAR;
                    } else if (hasKey && finalWorldFrame[x][y - 1].equals(Tileset.LOCKED_DOOR)) {
                        finalWorldFrame[x][y - 1] = Tileset.UNLOCKED_DOOR;
                        unlockDoor = true;
                    }
                }
                case 'A' -> {
                    if (canGet(finalWorldFrame, x - 1, y)) {
                        finalWorldFrame[x][y] = temp;
                        x -= 1;
                        temp = finalWorldFrame[x][y];
                        finalWorldFrame[x][y] = Tileset.AVATAR;
                    } else if (hasKey && finalWorldFrame[x - 1][y].equals(Tileset.LOCKED_DOOR)) {
                        finalWorldFrame[x][x - 1] = Tileset.UNLOCKED_DOOR;
                        unlockDoor = true;
                    }
                }
                case 'D' -> {
                    if (canGet(finalWorldFrame, x + 1, y)) {
                        finalWorldFrame[x][y] = temp;
                        x += 1;
                        temp = finalWorldFrame[x][y];
                        finalWorldFrame[x][y] = Tileset.AVATAR;
                    } else if (hasKey && finalWorldFrame[x + 1][y].equals(Tileset.LOCKED_DOOR)) {
                        finalWorldFrame[x + 1][y] = Tileset.UNLOCKED_DOOR;
                        unlockDoor = true;
                    }
                }

            }

            if (unlockDoor) {
                ter.initialize(WIDTH, HEIGHT + 2);
                ter.renderFrame(finalWorldFrame);
                StdDraw.clear(Color.black);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "YOU WIN THE GAME!");
                StdDraw.show();
            }

        }

        if (save) {
            new Out("save/save.txt").print(input + " " + x + " " + y);
        } else {
            ter.initialize(WIDTH, HEIGHT + 2);
            ter.renderFrame(finalWorldFrame);
        }
        return finalWorldFrame;
    }
}