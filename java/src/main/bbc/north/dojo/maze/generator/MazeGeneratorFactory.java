package bbc.north.dojo.maze.generator;

public class MazeGeneratorFactory {

    public static DefaultMazeGenerator createInstance(int x, int y, String type) {
        if (type.equals("recursive-backtracker")) {
            return new DefaultMazeGenerator(x, y);
        }
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
