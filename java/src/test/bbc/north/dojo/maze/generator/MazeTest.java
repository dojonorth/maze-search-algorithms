package bbc.north.dojo.maze.generator;

import org.junit.Test;

public class MazeTest {

    @Test
    public void canGenerateMaze() throws Exception {
        Maze maze = new Maze(30, 30, "recursive-backtracker");
        maze.display();
    }
}
