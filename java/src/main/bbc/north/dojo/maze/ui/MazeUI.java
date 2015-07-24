package bbc.north.dojo.maze.ui;

import bbc.north.dojo.maze.Maze;
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

    public static final String MAZE_ONE = "Maze 1 (Backtrack)";
    public static final String MAZE_TWO = "Maze 2 (Backtrack)";
    public static final String MAZE_THREE = "Maze 3 (Backtrack)";
    public static final String MAZE_FOUR = "Maze 4 (Backtrack)";
    public static final String MAZE_FIVE = "Maze 5 (Backtrack)";

    final static String DEFAULT_MAZE_TYPE = "Recursive BackTracker (Default)";
    final static String DEFAULT_PRE_GEN_MAZE_TYPE = "Custom";

    private Maze maze;
    private Integer height = 5;
    private Integer width = 5;

    final Label heightLabel = new Label("Height:");
    final TextField heightTextField = new TextField();

    final Label widthLabel = new Label("Label:");
    final TextField widthTextField = new TextField();

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

        HBox hbHeight = createLabel(heightLabel, heightTextField);
        GridPane.setConstraints(hbHeight, 0, 0);

        HBox hbWidth = createLabel(widthLabel, widthTextField);
        GridPane.setConstraints(hbWidth, 0, 2);

        final ComboBox mazeGenComboBox = addMazeGeneratorComboBox();
        final ComboBox<String> preGenComboBox = addPreGeneratedMazeTypes();

        VBox fieldsBox = new VBox();
        fieldsBox.getChildren().addAll(hbHeight, hbWidth);

        VBox dropdowns = new VBox();
        dropdowns.getChildren().addAll(mazeGenComboBox, preGenComboBox);

        VBox mazeOptions = new VBox();
        mazeOptions.getChildren().addAll(fieldsBox, dropdowns);

        // Create button that allows you to generate a new maze
        Button btn = new Button();
        btn.setText("Generate Random Maze");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Button clicked!");

                removeBoxBlur(gc);
                // clear old maze
                gc.clearRect(0, 0,
                        canvas.getHeight(),
                        canvas.getWidth());
                setBoxBlur(gc);

                if (preGenComboBox.getValue().toString().equals(DEFAULT_PRE_GEN_MAZE_TYPE)) {
                    height = Integer.valueOf(heightTextField.getText());
                    width = Integer.valueOf(widthTextField.getText());
                    maze = new Maze(height, width, "recursive-backtracker");
                } else if (preGenComboBox.getValue().toString().equals(MAZE_ONE)) {
                    maze = new Maze(Maze.mazeProblemOne);
                } else if (preGenComboBox.getValue().toString().equals(MAZE_TWO)) {
                    maze = new Maze(Maze.mazeProblemTwo);
                } else if (preGenComboBox.getValue().toString().equals(MAZE_THREE)) {
                    maze = new Maze(Maze.mazeProblemThree);
                } else if (preGenComboBox.getValue().toString().equals(MAZE_FOUR)) {
                    maze = new Maze(Maze.mazeProblemFour);
                } else if (preGenComboBox.getValue().toString().equals(MAZE_FIVE)) {
                    maze = new Maze(Maze.mazeProblemFive);
                }

                drawMaze(gc);
            }
        });

        GridPane.setConstraints(btn, 0, 4);

        grid.getChildren().addAll(mazeOptions, btn, canvas);
        root.getChildren().add(grid);
    }

    private HBox createLabel(Label label, TextField textField) {
        HBox hbHeight = new HBox();
        hbHeight.getChildren().addAll(label, textField);
        hbHeight.setSpacing(10);
        return hbHeight;
    }

    private ComboBox<String> addPreGeneratedMazeTypes() {

        ObservableList<String> preGeneratedMazes = FXCollections.observableArrayList(
                DEFAULT_PRE_GEN_MAZE_TYPE,
                MAZE_ONE, MAZE_TWO, MAZE_THREE, MAZE_FOUR, MAZE_FIVE
        );

        final ComboBox<String> preGenComboBox = new ComboBox(preGeneratedMazes);
        preGenComboBox.setValue(DEFAULT_PRE_GEN_MAZE_TYPE);
        return preGenComboBox;
    }

    private ComboBox addMazeGeneratorComboBox() {
        ObservableList<String> mazeGenerationTypes = FXCollections.observableArrayList(DEFAULT_MAZE_TYPE);

        final ComboBox mazeGenComboBox = new ComboBox(mazeGenerationTypes);
        mazeGenComboBox.setValue(DEFAULT_MAZE_TYPE);
        return mazeGenComboBox;
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
