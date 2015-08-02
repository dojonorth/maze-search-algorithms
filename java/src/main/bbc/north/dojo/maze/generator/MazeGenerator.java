package bbc.north.dojo.maze.generator;

public interface MazeGenerator {

    int[][] generateMaze(int cx, int cy) throws Throwable;
}
