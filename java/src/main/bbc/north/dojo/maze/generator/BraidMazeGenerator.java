package bbc.north.dojo.maze.generator;

import bbc.north.dojo.maze.InitialTraversal;
import bbc.north.dojo.maze.Intersection;
import bbc.north.dojo.maze.Traversal;
import bbc.north.dojo.maze.TraversalGraph;

import java.util.*;

public class BraidMazeGenerator extends DefaultMazeGenerator {

    Map<String, Intersection> toVisit = new LinkedHashMap<>();
    private int traversalCount;

    public BraidMazeGenerator(int x, int y) {
        super(x, y);
    }

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
    // 15 (N & S & E & W)

    @Override
    public int[][] generateMaze(int cx, int cy) throws Throwable {

        maze = recursiveBacktrack(cx, cy);
        // 1. Create traversal graph
        traversalGraph = new TraversalGraph(maze);

        // 2.Start at entrance (get the entrance cell)
        int entranceX = x - 1,
            entranceY = y - 1;

        int current = maze[entranceX][entranceY];
        traversalCount = 0;
        return traverse(new InitialTraversal(current, entranceX, entranceY), traversalCount);
    }

    private int[][] traverse(Traversal traversal, int traversalCount) throws Throwable {
        // Recursive follow Loop probably starts here
        // 3.If there is one option take it
        DIR availableDirection;
        int cx = traversal.toX(),
            cy = traversal.toY();

        int availableTraversals = traversalGraph.graph()[traversal.toX()][traversal.toY()];
        if (traversal.previous != null) { // we can't go back on ourselves
            availableTraversals -= traversal.previous.opposite.state;
        }
        int current = traversal.state;
        if (hasOneAvailableRoute(availableTraversals)) {
            availableDirection = DIR.toTraversalDirection(availableTraversals);

            // 7.Remove from the 'toVisit' queue
            String toVisitKey = toVisitKey(cx, cy);
            if (toVisit.containsKey(toVisitKey)) {
                toVisit.remove(toVisitKey);
            }

            // correct the traversal graph to remove one of the traversals
            // since we just traversed it

            traverseInNextDirection(cx, cy, traversalCount, availableDirection, current);
        } else if (hasMoreThanOneAvailableRoute(availableTraversals)) {
//            int availableDirections = listOfAvailableDirections(cx, cy);

            availableDirection = selectRandomDirection(DIR.toArray(availableTraversals));

            int nextDirectionState = traversalGraph.state(availableDirection.dx + cx, availableDirection.dy + cy);

            // recalculate the traversal value for this route
            // check if available direction has been traversed or not
            if (traversed(nextDirectionState)) {
                // if it has do nothing
                traverseToNextIntersection(traversalCount);
            } else {
                if (!toVisit.containsKey(toVisitKey(cx,cy))) {
                    toVisit.put(toVisitKey(cx, cy), new Intersection(cx, cy));
                }

                traverseInNextDirection(cx, cy, traversalCount, availableDirection, current);
            }
        } else if (isDeadEnd(availableTraversals) || traversed(availableTraversals)) {
            // 8.Are we at a dead end? i.e. there no options?
            // 9a.Yes - Remove a random wall but not a side wall
            // if cy == 0 -- south wall
            // if cx == 0 -- west wall
            // if cy + 1 % maze[0].length == 0 -- east wall
            // if cx + 1 % maze.length == 0 -- north wall
            System.out.println("Removing dead end");
            maze = removeRandomDeadEndWall(cx, cy, traversal);
            // mark the dead end as traversed (it's not a dead end any more
            traversalGraph.markTraversed(cx, cy);
        }
        traverseToNextIntersection(traversalCount);
        return maze;
    }

    private void traverseInNextDirection(int cx, int cy, int traversalCount, DIR availableDirection, int current) throws Throwable {
        traversalGraph.oneLessTraversal(cx, cy, availableDirection);
        if (traversalCount < x * y) {
            // re-traverse by following the next available direction
            int nx = cx + availableDirection.dx;
            int ny = cy + availableDirection.dy;

            current = maze[nx][ny];
            traversalCount = traversalCount + 1;
            traverse(new Traversal(current, availableDirection, nx, ny), traversalCount);
        }
    }

    private void traverseToNextIntersection(int traversalCount) throws Throwable {
        int current;
        if (toVisit.size() > 0) {
            Intersection nextIntersection = toVisit.get(toVisit.keySet().iterator().next()); // get the last intersection (always work from the back -- more efficient)
            int nx = 0, ny = 0;
            nx = nextIntersection.x;
            ny = nextIntersection.y;

            current = maze[nx][ny];
            traversalCount = traversalCount + 1;
            traverse(new Traversal(current, true, nextIntersection.x, nextIntersection.y),  traversalCount);
        }
    }

    private boolean traversed(int traversalState) {
        return traversalState == 15;
    }

    private String toVisitKey(int cx, int cy) {
        return String.valueOf(cx) + "," + String.valueOf(cy);
    }

    private int[][] removeRandomDeadEndWall(int cx, int cy, Traversal traversal) throws MazeGenerationFailureException {
        HashSet<DIR> availableWallsToRemove = listDeadEndInnerWallsToRemove(cx, cy, traversal);
        Collections.shuffle(Arrays.asList(availableWallsToRemove));
        DIR wallToRemove;
        Iterator<DIR> iter = availableWallsToRemove.iterator();
        while (iter.hasNext()) {
            wallToRemove = iter.next();
            maze[cx][cy] += wallToRemove.state;
            return maze;
        }

        return maze;
    }

    private int flipBits(int n, int k) {
        int mask = (1 << k) - 1;
        return ~n & mask;
    }

    private HashSet<DIR> listDeadEndInnerWallsToRemove(int cx, int cy, Traversal traversal) throws MazeGenerationFailureException {
        HashSet<DIR> innerWallsToRemove = new HashSet<>();
        innerWallsToRemove.add(DIR.N);
        innerWallsToRemove.add(DIR.E);
        innerWallsToRemove.add(DIR.W);
        innerWallsToRemove.add(DIR.S);

        // Remove the previous direction since we know there's no wall here
        innerWallsToRemove.remove(traversal.previous.opposite);

        if (isAdjacentToSouthWall(cy)) {
            innerWallsToRemove.remove(DIR.S);
        }
        if (isAdjacentToNorthWall(cy)) {
            innerWallsToRemove.remove(DIR.N);
        }
        if (isAdjacentToWestWall(cx)) {
            innerWallsToRemove.remove(DIR.W);
        }
        if (isAdjacentToEastWall(cx)) {
            innerWallsToRemove.remove(DIR.E);
        }

        return innerWallsToRemove;
    }

    private DIR selectRandomDirection(DIR[] available) {
        Collections.shuffle(Arrays.asList(available));
        return available[0];
    }

    private boolean hasOneAvailableRoute(int traversalState) {
        return traversalState == 1 || traversalState == 2 || traversalState == 4 || traversalState == 8;
    }

    private boolean hasMoreThanOneAvailableRoute(int traversalState) {
        return hasTwoAvailableRoutes(traversalState) || hasThreeAvailableRoutes(traversalState);
    }

    private boolean hasTwoAvailableRoutes(int traversalState) {
        return traversalState == 3 || traversalState == 5 || traversalState == 6 || traversalState  == 9 ||
                traversalState == 10 || traversalState == 12;
    }

    private boolean hasThreeAvailableRoutes(int traversalState) {
        return traversalState == 7 || traversalState == 11 || traversalState == 13 || traversalState == 14;
    }

    private boolean isDeadEnd(int availableTraversals) {
        return availableTraversals == 0;
    }

    private boolean isAdjacentToNorthWall(int cy) {
        return cy == 0;
    }

    private boolean isAdjacentToWestWall(int cx) {
        return cx == 0;
    }

    private boolean isAdjacentToEastWall(int cx) {
        return (cx + 1) % x == 0;
    }

    private boolean isAdjacentToSouthWall(int cy) {
        return (cy + 1) % y == 0;
    }
}