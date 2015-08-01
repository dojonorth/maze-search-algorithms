package bbc.north.dojo.maze;

import bbc.north.dojo.maze.generator.DefaultMazeGenerator;

public class Traversal {
    public final int current;
    public final DefaultMazeGenerator.DIR previous;
    final boolean firstTraversal;

    public Traversal(int current, DefaultMazeGenerator.DIR previous) {
        this.current = current;
        this.previous = previous;
        this.firstTraversal = false;
    }

    public Traversal(int current, boolean firstTraversal) {
        this.current = current;
        this.previous = null;
        this.firstTraversal = firstTraversal;
    }
}
