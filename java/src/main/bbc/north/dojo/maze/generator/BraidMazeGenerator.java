package bbc.north.dojo.maze.generator;

import bbc.north.dojo.maze.Intersection;

import java.util.*;

public class BraidMazeGenerator extends DefaultMazeGenerator {

    DIR availableDirection;
    Map<String, Intersection> toVisit = new LinkedHashMap<>();

    public BraidMazeGenerator(int x, int y) {
        super(x, y);
    }

    @Override
    public int[][] generateMaze(int cx, int cy) throws MazeGenerationFailureException {
        maze = recursiveBacktrack(cx, cy);
        // 1. Create traversal graph
        traversal = populateTraversalGraph(maze, traversal);

        // 2.Start at entrance (get the entrance cell)
        int entranceX = x - 1,
            entranceY = y - 1;

        int current = maze[entranceX][entranceY];
        int entrance = current;
        traverse(current, cx, cy);
        return null;
    }

    private void traverse(int current, int cx, int cy) throws MazeGenerationFailureException {
        // Recursive follow Loop probably starts here
        // 3.If there is one option take it
        int traversalState = traversal[cx][cy];
        if (hasOneAvailableRoute(traversalState)) {
            if (current == 14) { // only available is north
                availableDirection = DIR.N;
            } else if (current == 7) { // only available is west
                availableDirection = DIR.W;
            } else if (current == 11) { // only available is east
                availableDirection = DIR.E;
            } else if (current == 13) { // only available is south
                availableDirection = DIR.S;
            }
            // 7.Remove from the 'toVisit' queue
            String toVisitKey = toVisitKey(cx, cy);
            if (toVisit.containsKey(toVisitKey)) {
                toVisit.remove(toVisitKey);
            }
        } else if (hasMoreThanOneAvailableRoute(traversalState)) {
            // 4.Else pick an untraversed option
            int blockedDirections = listOfBlockedDirections(cx, cy);
            // available directions is inverse of the
            int availableDirections = ~blockedDirections;

            DIR availableDirection = selectRandomDirection(DIR.toArray(availableDirections));
            // 5.Reduce the number of options by 1
            // check if available direction has been traversed or not
            if (traversalState == TRAVERSAL.TRAVERSED.state) {
                // get the next
                if (toVisit.size() != 0) {
                    Intersection nextIntersection = toVisit.get(toVisit.size() - 1); // get the last intersection (always work from the back -- more efficient)
                    int nx = nextIntersection.x;
                    int ny = nextIntersection.y;
                    current = maze[nx][ny];
                    traverse(current, nx, ny);
                }
            } else {
                if (traversalState == TRAVERSAL.AVAILABLE_3.state) {
                    traversal[cx][cy] = TRAVERSAL.AVAILABLE_2.state;
                } else if (traversalState == TRAVERSAL.AVAILABLE_2.state) {
                    traversal[cx][cy] = TRAVERSAL.AVAILABLE_1.state;
                } else if (traversalState == TRAVERSAL.AVAILABLE_1.state) {
                    traversal[cx][cy] = TRAVERSAL.TRAVERSED.state;
                }
                // 6.Store a reference to this cell in the 'toVisit' queue
                toVisit.put(toVisitKey(cx, cy), new Intersection(cx, cy));
            }
        } else if (isDeadEnd(current) && !traversed(traversalState)) {
            // 8.Are we at a dead end? i.e. there no options?
            // 9a.Yes - Remove a random wall but not a side wall
            // if cy == 0 -- south wall
            // if cx == 0 -- west wall
            // if cy + 1 % maze[0].length == 0 -- east wall
            // if cx + 1 % maze.length == 0 -- north wall

            removeRandomAvailableWall(cx, cy);
            // mark as traversed
            traversal[cx][cy] = TRAVERSAL.TRAVERSED.state;

            if (toVisit.size() != 0) {
                Intersection nextIntersection = toVisit.get(toVisit.size() - 1); // get the last intersection (always work from the back -- more efficient)
                int nx = nextIntersection.x;
                int ny = nextIntersection.y;
                current = maze[nx][ny];
                traverse(current, nx, ny);
            } else {
                // we're finished - reached last dead end
            }
        }
    }

    private boolean traversed(int traversalState) {
        return traversalState == TRAVERSAL.TRAVERSED.state;
    }

    private String toVisitKey(int cx, int cy) {
        return String.valueOf(cx) + "," + String.valueOf(cy);
    }

    private int[][] populateTraversalGraph(int[][] maze, int[][] history) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                // if maze
                // 1 (N)   0001
                // 2 (S)   0010
                // 3 (N & S) 0011
                // 4 (E)
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

                if (isExit(i, j)) {
                    history[i][j] = TRAVERSAL.EXIT.state;
                } else if (maze[i][j] == 1 || maze[i][j] == 2 || maze[i][j] == 8) {
                    history[i][j] = TRAVERSAL.AVAILABLE_2.state;
                } else if (maze[i][j] == 3 || maze[i][j] == 5 || maze[i][j] == 6 ||
                        maze[i][j] == 9 || maze[i][j] == 10 || maze[i][j] == 12) {
                    history[i][j] = TRAVERSAL.AVAILABLE_1.state;
                } else if (maze[i][j] == 7 || maze[i][j] == 11 || maze[i][j] == 13 || maze[i][j] == 14) {
                    history[i][j] = TRAVERSAL.DEAD_END.state;
                }
            }
        }
        return history;
    }

    private void removeRandomAvailableWall(int cx, int cy) throws MazeGenerationFailureException {
        DIR[] availableWallsToRemove = listAvailableWalls(cx, cy);
        Collections.shuffle(Arrays.asList(availableWallsToRemove));
        DIR wallToRemove = availableWallsToRemove[0];
        maze[cx][cy] -= wallToRemove.state;
    }

    private DIR[] listAvailableWalls(int cx, int cy) throws MazeGenerationFailureException {
        DIR[] availableWallsToRemove;
        if (isAdjacentToSouthWall(cy)) {
            if (isAdjacentToEastWall(cy)) {
                availableWallsToRemove = new DIR[] { DIR.N, DIR.W };
            } else if (isAdjacentToWestWall(cx)) {
                availableWallsToRemove = new DIR[] { DIR.N, DIR.E };
            } else {
                availableWallsToRemove = new DIR[] { DIR.N, DIR.E, DIR.W };
            }
        } else if (isAdjacentToNorthWall(cx)) {
            if (isAdjacentToEastWall(cy)) {
                availableWallsToRemove = new DIR[] { DIR.W, DIR.S };
            } else if (isAdjacentToWestWall(cy)) {
                availableWallsToRemove = new DIR[] { DIR.E, DIR.S };
            } else {
                availableWallsToRemove = new DIR[] { DIR.N, DIR.W, DIR.S };
            }
        } else if (isAdjacentToEastWall(cx)) {
            availableWallsToRemove = new DIR[] { DIR.N, DIR.W, DIR.S };
        } else if (isAdjacentToWestWall(cx)) {
            availableWallsToRemove = new DIR[] { DIR.N, DIR.E, DIR.S };
        } else {
            throw new MazeGenerationFailureException("Failed to generate maze - failed to detect adjacent walls");
        }
        return availableWallsToRemove;
    }

    /**
     * List all available directions but excluding walls (not edge walls)
     * @param cx
     * @param cy
     * @return
     * @throws MazeGenerationFailureException
     */
    private int listOfBlockedDirections(int cx, int cy) throws MazeGenerationFailureException {
        int directions;
        if (isAdjacentToSouthWall(cy)) {
            if (isAdjacentToEastWall(cy)) {
                directions = 9;
            } else if (isAdjacentToWestWall(cx)) {
                directions = 5;
            } else {
                directions = 13;
            }
        } else if (isAdjacentToNorthWall(cx)) {
            if (isAdjacentToEastWall(cy)) {
                directions = 8;
            } else if (isAdjacentToWestWall(cy)) {
                directions = 6;
            } else {
                directions = 14;
            }
        } else if (isAdjacentToEastWall(cx)) {
            directions = 11;
        } else if (isAdjacentToWestWall(cx)) {
            directions = 7;
        } else { // can traverse in any direction
            directions = 15;
        }
        return directions;
    }

    private DIR selectRandomDirection(DIR[] available) {
        Collections.shuffle(Arrays.asList(available));
        return available[0];
    }



    private boolean hasOneAvailableRoute(int traversalState) {
        return traversalState == TRAVERSAL.AVAILABLE_1.state;
    }

    private boolean hasMoreThanOneAvailableRoute(int traversalState) {
        return traversalState == TRAVERSAL.AVAILABLE_2.state ||
                traversalState == TRAVERSAL.AVAILABLE_3.state;
    }

    private boolean isDeadEnd(int cell) {
        return cell == TRAVERSAL.DEAD_END.state;
    }

    private boolean isExit(int cell) {
        return cell == TRAVERSAL.EXIT.state;
    }

    private boolean isExit(int i, int j) {
        return i == 0 && j == 0;
    }

    private boolean isAdjacentToNorthWall(int cx) {
        return cx % (x - 1) == 0;
    }

    private boolean isAdjacentToWestWall(int cx) {
        return cx == 0;
    }

    private boolean isAdjacentToEastWall(int cy) {
        return cy % (y - 1) == 0;
    }

    private boolean isAdjacentToSouthWall(int cy) {
        return cy == 0;
    }
}