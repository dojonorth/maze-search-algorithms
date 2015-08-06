package bbc.north.dojo.maze.solver;

import bbc.north.dojo.maze.*;
import bbc.north.dojo.maze.generator.DefaultMazeGenerator;
import bbc.north.dojo.maze.generator.MazeGenerationFailureException;

import java.util.*;

public class MazeSolver {

    private final int[][] maze;
    private final int x;
    private final int y;

    private TraversalGraph traversalGraph;
    private List<Path> solution;
    private int traversalCount;
    private Map<String, Intersection> toVisit;

    public MazeSolver(int[][] maze) {
        this.maze = maze;
        solution = new ArrayList<>();
        traversalCount = 0;
        toVisit = new LinkedHashMap<>();
        x = this.maze.length;
        y = this.maze[0].length;
    }

    /**
     * Returns a path for the solution through the maze
     * @return
     */
    public List<Path> solve() throws Throwable {
        // 1. Create traversal graph
        traversalGraph = new TraversalGraph(maze);

        // 2.Start at entrance (get the entrance cell)
        int entranceX = 0,
            entranceY = 0;

        int current = maze[entranceX][entranceY];
        traversalCount = 0;
        int[][] braidMaze = traverse(new InitialTraversal(current, entranceX, entranceY), traversalCount);
        return solution;
    }

    private int[][] traverse(Traversal traversal, int traversalCount) throws Throwable {
        // Recursive follow Loop probably starts here
        // 3.If there is one option take it
        DefaultMazeGenerator.DIR availableDirection;
        int cx = traversal.toX(),
                cy = traversal.toY();

        int availableTraversals = traversalGraph.state(cx, cy);
        if (!isExit(cx, cy)) {
            if (hasOneAvailableRoute(availableTraversals)) {
                availableDirection = DefaultMazeGenerator.DIR.toTraversalDirection(availableTraversals);

                // 7.Remove from the 'toVisit' queue
                ifInToVisitQueueRemoveIt(cx, cy);

                // correct the traversal graph to remove one of the traversals
                // since we just traversed it

                traverseInNextDirection(cx, cy, traversalCount, availableDirection, availableTraversals);
            } else if (hasMoreThanOneAvailableRoute(availableTraversals)) {
    //            int availableDirections = listOfAvailableDirections(cx, cy);

                availableDirection = selectRandomDirection(availableTraversals);

                int nextDirectionState = traversalGraph.state(availableDirection.dx + cx, availableDirection.dy + cy);

                // recalculate the traversal value for this route
                // check if available direction has been traversed or not
                if (!traversed(nextDirectionState)) {
                    if (!toVisit.containsKey(toVisitKey(cx,cy))) {
                        toVisit.put(toVisitKey(cx, cy), new Intersection(cx, cy));
                    }

                    traverseInNextDirection(cx, cy, traversalCount, availableDirection, availableTraversals);
                }
            } else if (isDeadEnd(cx, cy)) {
                // 8.Are we at a dead end? i.e. there no options?
                // 9a.Yes - Remove a random wall but not a side wall
                // if cy == 0 -- south wall
                // if cx == 0 -- west wall
                // if cy + 1 % maze[0].length == 0 -- east wall
                // if cx + 1 % maze.length == 0 -- north wall
    //            System.out.println("Removing dead end");
    //            maze = removeRandomDeadEndWall(cx, cy, traversal);
                // mark the dead end as traversed (it's not a dead end any more)
                solution.add(new Path(cx,cy));
                traversalGraph.markTraversed(cx, cy);
            } else if (traversed(availableTraversals)) {
                ifInToVisitQueueRemoveIt(cx, cy);
            }
            traverseToNextIntersection(traversalCount);
        } else if (isExit(cx, cy)) {
            toVisit.clear();
            solution.add(new Path(cx,cy));
        }
        return maze;
    }

    private boolean isExit(int cx, int cy) {
        return (cx + 1) == maze.length && (cy + 1) == maze[0].length;
    }

    private void ifInToVisitQueueRemoveIt(int cx, int cy) {
        String toVisitKey = toVisitKey(cx, cy);
        if (toVisit.containsKey(toVisitKey)) {
            toVisit.remove(toVisitKey);
        }
    }

    private void traverseInNextDirection(int cx, int cy, int traversalCount, DefaultMazeGenerator.DIR availableDirection, int availableTraversals) throws Throwable {
        traversalGraph.oneLessTraversal(cx, cy, availableDirection);

        if (traversalCount <= x * y) {
            // re-traverse by following the next available direction
            int nx = cx + availableDirection.dx;
            int ny = cy + availableDirection.dy;

            try {
                availableTraversals = traversalGraph.state(nx, ny);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            traversalCount = traversalCount + 1;
            solution.add(new Path(cx,cy));
            traverse(new Traversal(availableTraversals, availableDirection, nx, ny), traversalCount);
        }
    }

    private void traverseToNextIntersection(int traversalCount) throws Throwable {
        int intersectionTraversals;
        if (toVisit.size() > 0) {
            Intersection nextIntersection = toVisit.get(toVisit.keySet().iterator().next()); // get the last intersection (always work from the back -- more efficient)
            int nx = nextIntersection.x;
            int ny = nextIntersection.y;

            intersectionTraversals = traversalGraph.state(nx, ny);

            traverse(new Traversal(intersectionTraversals, nx, ny),  traversalCount);
        }
    }

    private boolean traversed(int traversalState) {
        return traversalState == 0;
    }

    private String toVisitKey(int cx, int cy) {
        return String.valueOf(cx) + "," + String.valueOf(cy);
    }

    private HashSet<DefaultMazeGenerator.DIR> listAvailableDirectionsExcludingOuterWalls(int cx, int cy, Traversal traversal) throws MazeGenerationFailureException {
        HashSet<DefaultMazeGenerator.DIR> availableDirections = new HashSet<>();
        availableDirections.add(DefaultMazeGenerator.DIR.N);
        availableDirections.add(DefaultMazeGenerator.DIR.E);
        availableDirections.add(DefaultMazeGenerator.DIR.W);
        availableDirections.add(DefaultMazeGenerator.DIR.S);

        if (isAdjacentToSouthWall(cy)) {
            availableDirections.remove(DefaultMazeGenerator.DIR.S);
        }
        if (isAdjacentToNorthWall(cy)) {
            availableDirections.remove(DefaultMazeGenerator.DIR.N);
        }
        if (isAdjacentToWestWall(cx)) {
            availableDirections.remove(DefaultMazeGenerator.DIR.W);
        }
        if (isAdjacentToEastWall(cx)) {
            availableDirections.remove(DefaultMazeGenerator.DIR.E);
        }

        return availableDirections;
    }

    private HashSet<DefaultMazeGenerator.DIR> listAvailableInternalDirectionsExcludingOuterWallsAndPreviousTraversal(int cx, int cy, Traversal traversal) throws MazeGenerationFailureException {
        HashSet<DefaultMazeGenerator.DIR> availableDirections = listAvailableDirectionsExcludingOuterWalls(cx, cy, traversal);
        availableDirections.remove(traversal.previous.opposite);
        return availableDirections;
    }

    private DefaultMazeGenerator.DIR selectRandomDirection(int availableTraversals) {
        DefaultMazeGenerator.DIR[] availableDirections = DefaultMazeGenerator.DIR.toArray(availableTraversals);
        Collections.shuffle(Arrays.asList(availableDirections));
        return availableDirections[0];
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

    private boolean isDeadEnd(int cx, int cy) {
        int traversalState = maze[cx][cy];
        return traversalState == 1 || traversalState == 2 || traversalState == 4 || traversalState == 8;
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
