package bbc.north.dojo.maze;

import bbc.north.dojo.maze.generator.DefaultMazeGenerator;

public class TraversalGraph {
    private int[][] graph;

    public TraversalGraph(int[][] maze) {
        this.graph = deepCopy(maze);
    }

    private int[][] deepCopy(int[][] originalArray) {
        int[][] newArray = new int[originalArray.length][];

        for (int x = 0; x < originalArray.length; x++) {
            newArray[x] = originalArray[x].clone();
        }

        return newArray;
    }

    public int[][] graph() {
        return graph;
    }

    public void markTraversed(int cx, int cy) {
        graph[cx][cy] = 15;
    }

    public int state(int cx, int cy) {
        try {
            int value = graph[cx][cy];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return graph[cx][cy];
    }

    public void oneLessTraversal(int cx, int cy, DefaultMazeGenerator.DIR directionTraversed) {
        if (graph[cx][cy] >= directionTraversed.state) {
            graph[cx][cy] -= directionTraversed.state;
        }
        int nx = directionTraversed.dx + cx;
        int ny = directionTraversed.dy + cy;
        if (graph[nx][ny] >= directionTraversed.opposite.state) {
            graph[nx][ny] -= directionTraversed.opposite.state;
        }
    }

    public void traversal(int cx, int cy, int traversalState) {
        graph[cx][cy] += traversalState;
    }
}
