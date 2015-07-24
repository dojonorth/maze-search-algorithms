package bbc.north.dojo.maze.generator;

import java.util.Arrays;
import java.util.Collections;

public class RecursiveBackTrackMazeGenerator extends DefaultMazeGenerator {

    public RecursiveBackTrackMazeGenerator(int x, int y) {
        super(x, y);
    }

    @Override
    public int[][] generateMaze(int cx, int cy) {
        DIR[] dirs = DIR.values();
        Collections.shuffle(Arrays.asList(dirs));
        for (DIR dir : dirs) {
            int nx = cx + dir.dx;
            int ny = cy + dir.dy;
            if (between(nx, cx) && between(ny, cy)
                    && (getMaze()[nx][ny] == 0)) {
                getMaze()[cx][cy] |= dir.bit;
                getMaze()[nx][ny] |= dir.opposite.bit;
                generateMaze(nx, ny);
            }
        }
        return new int[0][];
    }

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }


    public int[][] getMaze() {
        return maze;
    }
}
