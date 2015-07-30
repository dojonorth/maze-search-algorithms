package bbc.north.dojo.maze.generator;

import java.util.Arrays;
import java.util.Collections;

public class BraidMazeGenerator extends DefaultMazeGenerator {

    public BraidMazeGenerator(int x, int y) {
        super(x, y);
    }

    @Override
    public int[][] generateMaze(int cx, int cy) {
        maze = recursiveBacktrack(cx, cy);
        traversal = populateTraversalGraph(maze, traversal);

        int current = maze[x - 1] [y - 1];
        int entrance = current;
        DIR next;
        if (hasOneAvailableRoute(current)) {
            if (current == 14) { // only available is north
                next = DIR.N;
            } else if (current == 7) { // only available is west
                next = DIR.W;
            }
        } else if (hasTwoAvailableRoutes(current)) {
            DIR[] availableDirs = new DIR[]{ DIR.N, DIR.W };
            next = selectRandomDirection(availableDirs);
            // reduce the number of directions
            traversal[x - 1][y - 1] -= DIR.;
        }

        // 1.Start at entrance (get the entrance cell)
        // 2.Check all directions for walls
        // 3.If there is one option take it
        // 4.Else pick an untraversed option
        // 5.Reduce the number of options by 1
        // 6.Store a reference to this cell in the 'toVisit' queue
        // 7.Remove from the 'toVisit' queue
        // 8.Are we at a dead end? i.e. there no options?
        // 9a.Yes - Remove a random wall but not a side wall
        // 9b.Mark current cell as traversed - remove cell from 'toVisit' queue
        // 10.Is toVisit queue empty?
        // 10a.Yes END
        // 10b.No goto next cell in the toVisit queue
        // 10c.Continue from 2.
        // 9c.No get Continue to 2
        return null;
    }

    private DIR selectRandomDirection(DIR[] available) {
        Collections.shuffle(Arrays.asList(available));
        return available[0];
    }

    private boolean hasTwoAvailableRoutes(int cell) {
        return cell == TRAVERSAL.T2.bit;
    }

    private boolean hasOneAvailableRoute(int cell) {
        return cell == TRAVERSAL.T1.bit;
    }

    private int[][] populateTraversalGraph(int[][] maze, int[][] history) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                // if maze
                // 1 (N)
                // 2 (S)
                // 3 (N & S)
                // 5 (E & N)
                // 6 (E & S)
                // 7 (N & S & E)
                // 8 (W)
                // 9 (W & N)
                // 10 (W & S)
                // 11 (W & S & N)
                // 12 (W & E)
                // 13 (N & E & W)
                // 14 (S & E & W)

                if (maze[i][j] == 1 || maze[i][j] == 2 || maze[i][j] == 8) {
                    history[i][j] = TRAVERSAL.T2.bit;
                } else if (maze[i][j] == 3 || maze[i][j] == 5 || maze[i][j] == 6 ||
                           maze[i][j] == 9 || maze[i][j] == 10 || maze[i][j] == 12) {
                    history[i][j] = TRAVERSAL.T1.bit;
                } else if (maze[i][j] == 7 || maze[i][j] == 11 || maze[i][j] == 13 || maze[i][j] == 14) {
                    history[i][j] = TRAVERSAL.DEAD_END.bit;
                }
            }
        }
        return history;
    }
}
