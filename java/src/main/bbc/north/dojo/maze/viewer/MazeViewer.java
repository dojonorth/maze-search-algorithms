package bbc.north.dojo.maze.viewer;

import bbc.north.dojo.maze.Maze;

public class MazeViewer {

    public static void display(Maze maze) {
        for (int i = 0; i < maze.getY(); i++) {
            // draw the bbc.north edge
            for (int j = 0; j < maze.getX(); j++) {
                System.out.print((maze.representation()[j][i] & 1) == 0 ? "+---" : "+   ");
            }
            System.out.println("+");
            // draw the west edge
            for (int j = 0; j < maze.getX(); j++) {
                System.out.print((maze.representation()[j][i] & 8) == 0 ? "|   " : "    ");
            }
            System.out.println("|");
        }
        // draw the bottom line
        for (int j = 0; j < maze.getX(); j++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }

    public static String display(int[][] maze) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < maze[0].length; i++) {
            // draw the bbc.north edge
            for (int j = 0; j < maze.length; j++) {
                buf.append((maze[j][i] & 1) == 0 ? "+---" : "+   ");
            }
            buf.append("+\n");
            // draw the west edge
            for (int j = 0; j < maze.length; j++) {
                buf.append((maze[j][i] & 8) == 0 ? "|   " : "    ");
            }
            buf.append("|\n");
        }
        // draw the bottom line
        for (int j = 0; j < maze.length; j++) {
            buf.append("+---");
        }
        buf.append("+\n");
        return buf.toString();
    }
}
