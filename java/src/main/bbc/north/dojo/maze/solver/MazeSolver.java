package bbc.north.dojo.maze.solver;

import bbc.north.dojo.maze.generator.DefaultMazeGenerator;

public class MazeSolver {

    private int[][] history;

    /**
     * Returns a path for the solution through the maze
     * @return
     */
    public DefaultMazeGenerator.DIR[] solve() {
        return null;
    }

    public int[][] history() {

        // 1  represents 0000001 - traversed
        // 2  represents 0000010 - traversed 1 option
        // 4  represents 0000100 - traversed 2 options
        return history;
    }
}
