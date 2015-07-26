package bbc.north.dojo.maze.ui;

import bbc.north.dojo.maze.Maze;
import bbc.north.dojo.maze.viewer.MazeViewer;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

import static bbc.north.dojo.maze.generator.DefaultMazeGenerator.DIR;

public class MazeUI extends Application {

    public static final String MAZE_ONE = "Maze 1 (Backtrack)";
    public static final String MAZE_TWO = "Maze 2 (Backtrack)";
    public static final String MAZE_THREE = "Maze 3 (Backtrack)";
    public static final String MAZE_FOUR = "Maze 4 (Backtrack)";
    public static final String MAZE_FIVE = "Maze 5 (Backtrack)";

    public static final DIR[] MAZE_ONE_ROUTE =
            new DIR[]{ DIR.N, DIR.N, DIR.N, DIR.W, DIR.N };

    final static String DEFAULT_MAZE_TYPE = "Recursive BackTracker (Default)";
    final static String DEFAULT_PRE_GEN_MAZE_TYPE = "Custom";

    private Maze maze;
    private Integer colY = 5;
    private Integer colX = 5;

    final Label dimensionsLabel = new Label("Dimensions");
    final Label heightLabel = new Label("Height:");
    final TextField heightTextField = new TextField();

    final Label widthLabel = new Label("Width:");
    final TextField widthTextField = new TextField();

    public static final int CELL_LENGTH = 20;
    public static final int GAP = 5;

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
        gc.setStroke(Color.BLACK);
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
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = initialiseGraphicsContext(canvas);

        GridPane.setConstraints(canvas, 0, 4);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints labelConstraints = new ColumnConstraints();
        labelConstraints.setMaxWidth(100);
        grid.getColumnConstraints().add(labelConstraints);

        grid.add(dimensionsLabel, 0, 0);
        grid.add(heightLabel, 0, 1);
        grid.add(widthLabel, 0, 2);
        grid.add(heightTextField, 1, 1);
        grid.add(widthTextField, 1, 2);

        final ComboBox mazeGenComboBox = addMazeGeneratorComboBox();
        final ComboBox<String> preGenComboBox = addPreGeneratedMazeTypes();

        grid.add(mazeGenComboBox, 3, 1);
        grid.add(preGenComboBox, 3, 2);
//        VBox dropdowns = new VBox();
//        dropdowns.getChildren().addAll(mazeGenComboBox, preGenComboBox);
//
//        VBox mazeOptions = new VBox();
//        mazeOptions.getChildren().addAll(dropdowns);

        // Create button that allows you to generate a new maze
        Button btn = new Button();
        btn.setText("Generate Maze");
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

                if (!"".equals(heightTextField.getText()) &&
                    !"".equals(widthTextField.getText())) {

                    if (preGenComboBox.getValue().toString().equals(DEFAULT_PRE_GEN_MAZE_TYPE)) {
                            colY = Integer.valueOf(heightTextField.getText());
                            colX = Integer.valueOf(widthTextField.getText());
                            maze = new Maze(colY, colX, "recursive-backtracker");
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
            }
        });

        grid.add(btn, 0, 4);
        grid.add(canvas, 0, 5);
        root.getChildren().add(grid);
    }

    private HBox createLabel(Label label, TextField textField) {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(label, textField);
        hbox.setSpacing(10);
        return hbox;
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
        colX = this.maze.getX();
        colY = this.maze.getY();

        int xPos, yPos = 20;

        for (int i = 0; i < colX; i++) {
            xPos = 20;
            // draw the north edge
            for (int j = 0; j < colY; j++) {
                if ((maze[j][i] & 1) == 0) {
                    gc.strokeLine(xPos, yPos, xPos + CELL_LENGTH, yPos); // horizontal
                }
                xPos += CELL_LENGTH + GAP;
            }

            xPos = 20;
            // draw the west edge
            for (int j = 0; j < colY; j++) {
                if ((maze[j][i] & 8) == 0) {
                    gc.strokeLine(xPos, yPos, xPos, yPos + CELL_LENGTH); // vertical
                }
                xPos += CELL_LENGTH + GAP;

            }
            gc.strokeLine(xPos, yPos, xPos, yPos + CELL_LENGTH); // vertical
            yPos += CELL_LENGTH + GAP;
        }
        // draw the bottom line

        xPos = 20; // reset x pos to western edge

        for (int j = 0; j < colY; j++) {
            gc.strokeLine(xPos, yPos, xPos + CELL_LENGTH, yPos); // horizontal
            xPos += CELL_LENGTH + GAP;
        }

        gc.setFill(Color.RED);
        gc.fillOval(30, 30, 5, 5);

        gc.setFill(Color.GREEN);
        gc.fillOval(calculateXOffsetForEntrance(), calculateYOffsetForEntrance(), 5, 5);

        MazeViewer.display(this.maze);
    }

//    void renderRoute(GraphicsContext gc, DIR[] route) {
//        DoubleProperty routeXPos = new SimpleDoubleProperty();
//        DoubleProperty routeYPos = new SimpleDoubleProperty();
//
//        KeyFrame[] keyFrames = new KeyFrame[route.length];
//        for (int i = 0; i < route.length; i++) {
//            keyFrames[i] = new KeyFrame(
//                        Duration.millis(300),
//                        new KeyValue(routeXPos, calculateNextPos(route[i])),
//
//                    );
//        }
//
//        Timeline routeTimeline = new Timeline();
//
//    }

//    private Position calculateNextPos(DIR dir) {
//        if ()
//        return new Position()
//    }

    private int calculateYOffsetForEntrance() {
        return ((colY * (CELL_LENGTH + GAP)) + 15) - (CELL_LENGTH / 2);
    }

    private int calculateXOffsetForEntrance() {
        return ((colX * (CELL_LENGTH + GAP)) + 15) - (CELL_LENGTH / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
