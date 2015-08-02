package bbc.north.dojo.maze.generator;

import bbc.north.dojo.maze.Maze;
import org.junit.Test;

public class MazeTest {

    @Test
    public void canGenerateMaze() throws Throwable {
        Maze maze = new Maze(30, "recursive-backtracker");
        maze.display();
    }
}
