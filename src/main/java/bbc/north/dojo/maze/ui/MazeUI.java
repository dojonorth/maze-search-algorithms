package bbc.north.dojo.maze.ui;

import bbc.north.dojo.maze.generator.MazeGenerator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MazeUI extends Application {

    private MazeGenerator generator;
    private Integer height;
    private Integer width;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BBC North Dojo: Solving a Maze");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        Label heightLabel = new Label("Height:");
        final TextField heightTextField = new TextField();
        HBox hbHeight = new HBox();
        hbHeight.getChildren().addAll(heightLabel, heightTextField);
        hbHeight.setSpacing(10);

        GridPane.setConstraints(hbHeight, 0, 0);

        Label widthLabel = new Label("Label:");
        final TextField widthTextField = new TextField();
        HBox hbWidth = new HBox();
        hbWidth.getChildren().addAll(widthLabel, widthTextField);
        hbWidth.setSpacing(10);

        GridPane.setConstraints(hbWidth, 0, 2);

        VBox fieldsBox = new VBox();
        fieldsBox.getChildren().addAll(hbHeight, hbWidth);

        // Create button that allows you to generate a new maze
        Button btn = new Button();
        btn.setText("Generate Random Maze");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                height = Integer.valueOf(heightTextField.getText());
                width = Integer.valueOf(widthTextField.getText());
                generator = new MazeGenerator(height, width);
                drawMaze();
            }
        });

        GridPane.setConstraints(btn, 0, 4);
        grid.getChildren().addAll(fieldsBox, btn);

        // Draw a grid below

        primaryStage.setScene(new Scene(grid, 600, 600, Color.BLACK));
        primaryStage.show();
    }

    void drawMaze() {
        int[][] maze = generator.maze();

        for (int i = 0; i < width; i++) {
            // draw the north edge
            for (int j = 0; j < height; j++) {

                System.out.print((maze[j][i] & 1) == 0 ? "+---" : "+   ");
            }
            System.out.println("+");
            // draw the west edge
            for (int j = 0; j < height; j++) {
                System.out.print((maze[j][i] & 8) == 0 ? "|   " : "    ");
            }
            System.out.println("|");
        }
        // draw the bottom line
        for (int j = 0; j < height; j++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }
}
