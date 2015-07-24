package bbc.north.dojo.maze.ui;

import bbc.north.dojo.maze.generator.Maze;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

public class MazeUI extends Application {

    private Maze maze;
    private Integer height = 5;
    private Integer width = 5;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BBC North Dojo: Solving a Maze");

        Group root = new Group();

        drawMazeView(root);

        primaryStage.setScene(new Scene(root, Color.WHITE));
        primaryStage.show();
    }

    private GraphicsContext initialiseGraphicsContext(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 600, 600);
        gc.setStroke(Color.GREEN);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        gc.setLineWidth(3);

        setBoxBlur(gc);
        return gc;
    }

    private void setBoxBlur(GraphicsContext gc) {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(1);
        blur.setHeight(1);
        blur.setIterations(1);
        gc.setEffect(blur);
    }

    private void removeBoxBlur(GraphicsContext gc) {
        gc.setEffect(null);
    }

    private void drawMazeView(Group root) {
        Canvas canvas = new Canvas(800, 800);
        GraphicsContext gc = initialiseGraphicsContext(canvas);

        GridPane.setConstraints(canvas, 0, 6);

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

        ObservableList<String> mazeGenerationTypes = FXCollections.observableArrayList(
            "Recursive BackTracker (Default)"
        );

        final ComboBox comboBox = new ComboBox(mazeGenerationTypes);

        VBox fieldsBox = new VBox();
        fieldsBox.getChildren().addAll(hbHeight, hbWidth, comboBox);

        // Create button that allows you to generate a new maze
        Button btn = new Button();
        btn.setText("Generate Random Maze");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Button clicked!");
                height = Integer.valueOf(heightTextField.getText());
                width = Integer.valueOf(widthTextField.getText());

                removeBoxBlur(gc);
                // clear old maze
                gc.clearRect(0, 0,
                        canvas.getHeight(),
                        canvas.getWidth());
                setBoxBlur(gc);

                maze = new Maze(height, width, "recursive-backtracker");

                drawMaze(gc);
            }
        });

        GridPane.setConstraints(btn, 0, 4);

        grid.getChildren().addAll(fieldsBox, btn, canvas);
        root.getChildren().add(grid);
    }

    void drawMaze(GraphicsContext gc) {
        int[][] maze = this.maze.representation();
        int xPos = 20,
            yPos = 20,
            length = 20,
            gap = 5;

        for (int i = 0; i < width; i++) {
            xPos = 20;
            // draw the north edge
            for (int j = 0; j < height; j++) {
                if ((maze[j][i] & 1) == 0) {
                    gc.strokeLine(xPos, yPos, xPos + length, yPos); // horizontal
                }
                xPos += length + gap;

                System.out.print((maze[j][i] & 1) == 0 ? "+---" : "+   ");
            }
            System.out.println("+");

            xPos = 20;
            // draw the west edge
            for (int j = 0; j < height; j++) {
                if ((maze[j][i] & 8) == 0) {
                    gc.strokeLine(xPos, yPos, xPos, yPos + length); // vertical
                }
                xPos += length + gap;

                System.out.print((maze[j][i] & 8) == 0 ? "|   " : "    ");
            }
            gc.strokeLine(xPos, yPos, xPos, yPos + length); // vertical
            System.out.println("|");
            yPos += length + gap;
        }
        // draw the bottom line

        xPos = 20; // reset x pos to western edge

        for (int j = 0; j < height; j++) {
            gc.strokeLine(xPos, yPos, xPos + length, yPos); // horizontal
            System.out.print("+---");
            xPos += length + gap;
        }
        System.out.println("+");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
