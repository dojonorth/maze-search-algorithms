package bbc.north.dojo.maze;

import bbc.north.dojo.maze.generator.DefaultMazeGenerator;
import bbc.north.dojo.maze.generator.MazeGenerationFailureException;
import bbc.north.dojo.maze.generator.MazeGeneratorFactory;
import bbc.north.dojo.maze.viewer.MazeViewer;

/*
 * recursive backtracking algorithm
 * shamelessly borrowed from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
*/
public class Maze {

    private final int x;
    private final int y;
    private static int[][] maze;

    public static final int[][] mazeProblemOne = new int[][] {{2,5,2,3,7,3,1,6,7,3,3,5,6,3,5},{4,10,3,5,12,6,3,9,10,3,5,12,8,6,13},{12,6,5,12,12,14,3,1,6,3,9,10,3,9,12},{12,12,12,12,12,12,6,3,9,4,6,7,1,6,9},{14,9,10,9,12,12,12,6,5,12,12,10,5,12,4},{12,2,7,5,12,12,10,9,12,10,13,4,10,9,12},{12,6,9,12,12,12,2,5,10,5,10,11,3,3,13},{12,10,5,12,10,11,3,13,4,10,5,6,3,1,12},{12,6,9,10,5,4,6,9,12,6,9,12,6,3,9},{10,9,6,1,12,10,11,3,9,12,6,13,12,2,5},{6,7,9,6,9,6,3,3,3,9,8,12,12,6,13},{8,12,6,9,6,13,6,3,7,3,3,13,12,12,12},{6,9,10,5,12,12,12,4,10,3,5,8,12,12,8},{14,3,5,12,8,12,10,11,3,5,10,5,12,10,5},{10,1,10,11,3,9,2,3,3,11,1,10,11,3,9}};
    public static final int[][] mazeProblemTwo = new int[][] {{2,5,6,5,6,3,1,6,3,3,7,5,2,3,5},{4,10,9,12,14,3,3,9,6,5,12,10,3,3,9},{14,3,3,9,8,6,3,5,12,12,10,3,3,3,5},{12,6,3,3,5,12,4,10,9,10,3,3,3,3,13},{12,12,6,5,12,12,10,3,3,3,5,6,3,3,9},{12,10,9,12,10,9,6,5,6,3,13,10,3,3,5},{10,3,5,12,6,5,12,10,9,4,14,3,3,1,12},{4,6,9,12,12,10,9,6,3,11,13,6,3,3,9},{12,12,2,13,14,3,5,8,6,5,8,10,3,3,5},{12,10,3,9,12,4,12,6,9,12,6,5,6,3,9},{14,7,3,1,10,13,12,12,4,12,12,10,9,6,1},{12,10,3,7,1,12,10,11,9,12,12,2,3,11,5},{12,2,5,12,6,9,2,5,6,9,10,3,3,5,12},{10,5,12,12,12,6,3,13,12,6,7,1,6,9,12},{2,11,9,10,11,9,2,9,10,9,10,3,11,3,9}};
    public static final int[][] mazeProblemThree = new int[][] {{2,5,2,3,7,3,3,3,5,6,3,1,6,7,1},{4,10,3,5,12,2,5,6,9,10,3,3,9,14,5},{10,7,5,12,10,5,14,9,6,5,6,3,5,12,12},{6,9,8,10,3,9,12,6,13,12,10,5,12,12,12},{12,6,3,7,1,6,9,12,12,10,3,9,12,8,12},{12,12,4,10,5,10,5,8,14,3,5,2,11,3,13},{12,10,13,4,10,5,10,5,10,5,10,5,6,5,12},{14,1,12,10,5,14,1,12,6,9,4,10,9,12,12},{14,5,10,5,12,10,5,12,12,2,15,3,1,12,12},{8,12,6,9,10,5,12,12,10,3,9,6,3,9,12},{6,9,14,3,3,9,12,10,3,3,3,9,4,6,9},{10,5,12,6,3,3,13,6,3,3,1,6,11,9,4},{6,9,12,10,5,6,11,9,6,3,3,9,6,3,13},{14,5,12,4,12,10,1,6,9,6,5,2,13,4,12},{8,10,11,9,10,3,3,11,3,9,10,3,9,10,9}};
    public static final int[][] mazeProblemFour = new int[][] {{2,3,3,5,4,6,7,3,3,5,6,5,6,3,5},{6,3,1,12,12,12,10,3,5,10,9,12,10,5,12},{12,6,3,9,14,9,4,6,9,2,3,11,1,12,12},{14,9,2,3,11,5,12,10,3,5,6,3,3,9,12},{10,5,6,5,2,11,11,5,4,12,12,6,7,1,12},{4,10,9,10,3,5,6,9,12,10,9,8,12,6,13},{14,5,4,6,3,9,12,6,11,5,6,3,9,12,8},{12,10,9,12,4,6,9,10,5,12,14,3,5,10,5},{12,6,5,12,12,12,6,3,9,12,8,6,13,4,12},{14,9,10,9,12,12,10,3,5,10,5,12,10,13,12},{12,2,7,5,10,11,3,3,9,6,9,12,2,9,12},{10,3,9,10,3,3,5,4,6,11,3,9,6,3,13},{6,5,6,7,3,5,12,10,9,6,3,3,9,6,9},{12,10,9,10,5,8,12,6,5,10,5,6,5,10,5},{10,3,3,1,10,3,11,9,10,3,9,8,10,3,9}};
    public static final int[][] mazeProblemFive = new int[][] {{2,3,5,6,3,3,7,5,6,3,3,3,7,3,5},{6,5,12,12,6,3,9,8,10,5,4,6,9,4,12},{12,12,12,12,10,3,5,6,5,10,13,10,5,14,9},{12,10,9,10,5,4,10,9,14,1,12,4,12,10,5},{10,5,6,5,10,15,3,1,14,5,14,9,10,5,8},{6,9,12,10,5,8,6,5,8,10,9,6,5,14,5},{10,3,9,4,12,6,9,10,3,3,3,9,12,8,12},{6,3,7,13,12,10,7,5,2,7,7,1,10,3,13},{14,1,12,8,10,5,12,12,6,9,12,6,3,3,9},{8,6,13,6,3,9,12,12,12,2,13,12,6,3,5},{6,9,8,12,4,6,9,12,14,5,8,10,9,4,12},{12,6,3,9,10,9,6,9,8,10,7,5,6,11,9},{12,10,3,3,3,5,14,3,3,5,12,12,10,5,4},{14,1,6,5,6,9,10,3,1,12,12,10,1,10,13},{10,3,9,10,11,3,3,3,3,9,10,3,3,3,9}};

    public Maze(int dimension, String type) throws MazeGenerationFailureException {
        this.x = dimension;
        this.y = dimension;
        DefaultMazeGenerator generator = MazeGeneratorFactory.createInstance(dimension, y, type);
        maze = generator.generateMaze(0, 0);
    }

    public Maze(int[][] predefinedMaze) {
        this.maze = predefinedMaze;

        this.x = maze.length;
        this.y = maze[0].length;
    }

    public void display() {
        MazeViewer.display(this);
    }

    public int[][] representation() {
        return maze;
    }

    public static void main(String[] args) throws MazeGenerationFailureException {
        int x = args.length >= 1 ? (Integer.parseInt(args[0])) : 8;
        int y = args.length == 2 ? (Integer.parseInt(args[1])) : 8;
        Maze maze = new Maze(x, "recursive-backtracker");
        maze.display();
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}