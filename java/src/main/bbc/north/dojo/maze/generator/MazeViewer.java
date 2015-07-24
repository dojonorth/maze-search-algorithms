package bbc.north.dojo.maze.generator;

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
}
