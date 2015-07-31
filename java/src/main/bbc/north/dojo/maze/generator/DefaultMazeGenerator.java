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
    public int[][] generateMaze(int cx, int cy) throws MazeGenerationFailureException {
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
                maze[cx][cy] |= dir.state;
                maze[nx][ny] |= dir.opposite.state;
                recursiveBacktrack(nx, ny);
            }
        }
        return maze;
    }

    protected static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    public enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);

        final int state;
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

        private DIR(int state, int dx, int dy) {
            this.state = state;
            this.dx = dx;
            this.dy = dy;
        }

        public static DIR[] toArray(int availableDirections) {
            if (availableDirections == 1) {
                return new DIR[] { DIR.N };
            } else if (availableDirections == 2) {
                return new DIR[] { DIR.S };
            } else if (availableDirections == 3) {
                return new DIR[] { DIR.N, DIR.S };
            } else if (availableDirections == 4) {
                return new DIR[] { DIR.E };
            } else if (availableDirections == 5) {
                return new DIR[] { DIR.E, DIR.N };
            } else if (availableDirections == 6) {
                return new DIR[] { DIR.E, DIR.S };
            } else if (availableDirections == 7) {
                return new DIR[] { DIR.N, DIR.S, DIR.E };
            } else if (availableDirections == 8) {
                return new DIR[] { DIR.W };
            } else if (availableDirections == 9) {
                return new DIR[] { DIR.W, DIR.N };
            } else if (availableDirections == 10) {
                return new DIR[] { DIR.W, DIR.S };
            } else if (availableDirections == 11) {
                return new DIR[] { DIR.W, DIR.S, DIR.N };
            } else if (availableDirections == 12) {
                return new DIR[] { DIR.W, DIR.E };
            } else if (availableDirections == 13) {
                return new DIR[] { DIR.N, DIR.E, DIR.W };
            } else if (availableDirections == 14) {
                return new DIR[] { DIR.S, DIR.E, DIR.W };
            }
            return new DIR[0];  //To change body of created methods use File | Settings | File Templates.
        }
    };

    public enum TRAVERSAL {
        TRAVERSED(1), AVAILABLE_1(2), AVAILABLE_2(4), AVAILABLE_3(8), DEAD_END(16), EXIT(32);

        final int state;

        private TRAVERSAL(int bit) {
            this.state = bit;
        }
    };
}
