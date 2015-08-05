package bbc.north.dojo.maze;

import bbc.north.dojo.maze.generator.DefaultMazeGenerator;

public class Traversal {
    public final int state;
    private final int cx;
    private final int cy;

    public final DefaultMazeGenerator.DIR previous;

    public Traversal(int state, DefaultMazeGenerator.DIR previous, int cx, int cy) {
        this.state = state;
        this.cx = cx;
        this.cy = cy;
        this.previous = previous;
    }

    public Traversal(int state, int cx, int cy) {
        this.state = state;
        this.previous = null;
        this.cx = cx;
        this.cy = cy;
    }

    public int toX() {
        return cx;
    }

    public int toY() {
        return cy;
    }
}
