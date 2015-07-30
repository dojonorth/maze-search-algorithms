package bbc.north.dojo.maze.generator;

import java.util.Arrays;
import java.util.Collections;

public class DefaultMazeGenerator implements MazeGenerator {

    protected final int x;
    protected final int y;
    protected int[][] maze;
    protected int[][] traversal;

    public DefaultMazeGenerator(int x, int y) {
        this.x = x;
        this.y = y;
        maze = new int[this.x][this.y];
        traversal = new int[this.x][this.y];
    }

    @Override
    public int[][] generateMaze(int cx, int cy) {
        return recursiveBacktrack(cx, cy);
    }

    protected int[][] recursiveBacktrack(int cx, int cy) {
        DIR[] dirs = DIR.values();
        Collections.shuffle(Arrays.asList(dirs));
        for (DIR dir : dirs) {
            int nx = cx + dir.dx;
            int ny = cy + dir.dy;
            if (between(nx, x) && between(ny, y)
                    && (maze[nx][ny] == 0)) {
                maze[cx][cy] |= dir.bit;
                maze[nx][ny] |= dir.opposite.bit;
                generateMaze(nx, ny);
            }
        }
        return maze;
    }

    protected static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    public enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);

        final int bit;
        final int dx;
        final int dy;
        DIR opposite;
        // use the static initializer to resolve forward references
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        private DIR(int bit, int dx, int dy) {
            this.bit = bit;
            this.dx = dx;
            this.dy = dy;
        }

    };

    public enum TRAVERSAL {
        T(1), T2(2), T1(4), DEAD_END(8);

        final int bit;

        private TRAVERSAL(int bit) {
            this.bit = bit;
        }
    };
}
