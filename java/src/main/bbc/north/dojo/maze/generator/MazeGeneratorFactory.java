package bbc.north.dojo.maze.generator;

public class MazeGeneratorFactory {

    public static DefaultMazeGenerator createInstance(int x, int y, String type) {
        return new DefaultMazeGenerator(x, y);
    }
}
