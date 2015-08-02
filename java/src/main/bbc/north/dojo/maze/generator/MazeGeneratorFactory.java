package bbc.north.dojo.maze.generator;

import bbc.north.dojo.maze.Maze;

public class MazeGeneratorFactory {

    public static MazeGenerator createInstance(int x, int y, String type) {
        MazeGenerator mazeGenerator = new DefaultMazeGenerator(x, y);
        if (Maze.BRAID_MAZE_TYPE.equals(type)) {
            mazeGenerator = new BraidMazeGenerator(x, y);
        }
        return mazeGenerator;
    }
}
