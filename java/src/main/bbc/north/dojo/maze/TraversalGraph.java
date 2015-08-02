package bbc.north.dojo.maze;

import bbc.north.dojo.maze.generator.DefaultMazeGenerator;

import java.util.Arrays;

/**
 * Created by jamesmurphy on 02/08/2015.
 */
public class TraversalGraph {
    private int[][] graph;

    public TraversalGraph(int[][] maze) {
        int [][] traversalGraph = new int[maze.length][];
        for(int i = 0; i < maze.length; i++)
        {
            int[] aMaze = maze[i];
            int   aLength = aMaze.length;
            traversalGraph[i] = new int[aLength];
            System.arraycopy(aMaze, 0, traversalGraph[i], 0, aLength);
        }

        this.graph = traversalGraph;
    }

    public int[][] graph() {
        return graph;
    }

    public void markTraversed(int cx, int cy) {
        graph[cx][cy] = 15;
    }

    public int state(int cx, int cy) {
        return graph[cx][cy];
    }

    public void oneLessTraversal(int cx, int cy, DefaultMazeGenerator.DIR directionTraversed) {
        graph[cx][cy] -= directionTraversed.state;
    }
}
