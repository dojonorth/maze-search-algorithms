package bbc.north.dojo.maze.generator;

import org.junit.Test;

public class MazeGeneratorTest {

    @Test
    public void canGenerateMaze() throws Exception {
        MazeGenerator maze = new MazeGenerator(30, 30);
        maze.display();
    }
}
