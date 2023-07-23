import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// TODO: Add any other necessary imports.


public class Percolation {
    // TODO: Add any necessary instance variables.
    WeightedQuickUnionUF grid;
    WeightedQuickUnionUF antiBackWash;
    boolean[] status;
    int Width;
    int top;
    int bottom;
    int openSize;

    public Percolation(int N) {
        // TODO: Fill in this constructor.
        Width = N;
        openSize = 0;
        top = N * N;
        bottom = top + 1;
        grid = new WeightedQuickUnionUF(2 + N * N);
        antiBackWash = new WeightedQuickUnionUF(1 + N * N);
        status = new boolean[N * N];
        for (int i = 0; i < Width; i++) {
            grid.union(top, i);
            antiBackWash.union(top, i);
            grid.union(bottom, translateXY(Width - 1, i));
        }
    }

    private int translateXY(int row, int col) {
        return row * Width + col;
    }

    private int[] neighbor(int row, int col) {
        int[] neighbors = {-1, -1, -1, -1};
        if (row == 0) {
            neighbors[0] = translateXY(row, col);
        } else if (row - 1 >= 0) {
            neighbors[0] = translateXY(row - 1, col);
        }
        if (col - 1 >= 0) {
            neighbors[1] = translateXY(row, col - 1);
        }
        if (col + 1 < Width) {
            neighbors[2] = translateXY(row, col + 1);
        }
        if (row + 1 < Width) {
            neighbors[3] = translateXY(row + 1, col);
        }
        return neighbors;
    }

    public void open(int row, int col) {
        // TODO: Fill in this method.
        if (status[translateXY(row, col)]) { return;}
        openSize++;
        status[translateXY(row, col)] = true;
        for (int id: neighbor(row, col)) {
             if (id != -1 && status[id]) {
                 grid.union(id, translateXY(row, col));
                 antiBackWash.union(id, translateXY(row, col));
                 if (antiBackWash.connected(id, top)) {
                     grid.union(top, translateXY(row, col));
                     antiBackWash.union(top, translateXY(row, col));
                 }
             }
        }
    }

    public boolean isOpen(int row, int col) {
        // TODO: Fill in this method.
        return status[translateXY(row, col)];
    }

    public boolean isFull(int row, int col) {
        // TODO: Fill in this method.
        return isOpen(row, col) && antiBackWash.connected(top, translateXY(row, col));
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return openSize;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return grid.connected(top, bottom);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.
}
