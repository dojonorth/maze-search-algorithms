package bbc.north.dojo.maze.generator;

public interface MazeGenerator {

    public int[][] generateMaze(int cx, int cy) throws Throwable;
}
