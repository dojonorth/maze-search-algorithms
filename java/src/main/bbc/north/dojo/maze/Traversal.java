package bbc.north.dojo.maze;

import bbc.north.dojo.maze.generator.DefaultMazeGenerator;

public class Traversal {
    public final int state;
    private final int cx;
    private final int cy;

    public final DefaultMazeGenerator.DIR previous;
    final boolean firstTraversal;

    public Traversal(int state, DefaultMazeGenerator.DIR previous, int cx, int cy) {
        this.state = state;
        this.cx = cx;
        this.cy = cy;
        this.previous = previous;
        this.firstTraversal = false;
    }

    public Traversal(int state, boolean firstTraversal, int cx, int cy) {
        this.state = state;
        this.previous = null;
        this.cx = cx;
        this.cy = cy;
        this.firstTraversal = firstTraversal;
    }

    public int toX() {
        return cx;
    }

    public int toY() {
        return cy;
    }
}
