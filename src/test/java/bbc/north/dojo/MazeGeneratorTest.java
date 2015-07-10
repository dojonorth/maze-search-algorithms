package bbc.north.dojo;

import org.junit.Test;

public class MazeGeneratorTest {

    @Test
    public void canGenerateMaze() throws Exception {
        MazeGenerator maze = new MazeGenerator(10, 10);
        maze.display();
    }
}
