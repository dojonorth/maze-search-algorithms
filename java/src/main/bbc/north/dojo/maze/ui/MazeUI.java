package bbc.north.dojo.maze.ui;

import bbc.north.dojo.maze.Maze;
import bbc.north.dojo.maze.Path;
import bbc.north.dojo.maze.solver.MazeSolver;
import bbc.north.dojo.maze.viewer.MazeViewer;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static bbc.north.dojo.maze.generator.DefaultMazeGenerator.DIR;

public class MazeUI extends Application {

    public static final String MAZE_ONE = "Maze 1 (Backtrack)";
    public static final String MAZE_TWO = "Maze 2 (Backtrack)";
    public static final String MAZE_THREE = "Maze 3 (Backtrack)";
    public static final String MAZE_FOUR = "Maze 4 (Backtrack)";
    public static final String MAZE_FIVE = "Maze 5 (Backtrack)";
    public static final String MAZE_SIX = "Maze 6 (Braid)";
    public static final String MAZE_SEVEN = "Maze 7 (Braid)";
    public static final String MAZE_EIGHT = "Maze 8 (Braid)";

    public static final DIR[] MAZE_ONE_ROUTE =
            new DIR[]{ DIR.N, DIR.N, DIR.N, DIR.W, DIR.N };

    public static final int CELL_LENGTH = 20;
    public static final int GAP = 5;

    final static String DEFAULT_PRE_GEN_MAZE_TYPE = "Custom";

    private Maze maze;
    private Group pathGroup = new Group();
    private Integer dimensions = 5;
    private Integer colX = 5;

    final Label dimensionsLabel = new Label("Dimensions");
    final TextField dimensionsTextField = new TextField();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BBC North Dojo: Solving a Maze");

        Group root = new Group();

        try {
            drawMazeView(root);
        } catch (Throwable e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

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
        Canvas canvas = new Canvas(800, 800);
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
        grid.add(dimensionsTextField, 0, 1);

        final ComboBox mazeGenComboBox = addMazeGeneratorComboBox();
        final ComboBox<String> preGenComboBox = addPreGeneratedMazeTypes();

        grid.add(mazeGenComboBox, 3, 1);
        grid.add(preGenComboBox, 3, 2);

        Circle entranceMarker = new Circle(calculateTopLeftCellX(), calculateTopLeftCellY(), 5, Color.web("blue", 0.5));
        root.getChildren().add(entranceMarker);

        Circle exitMarker = new Circle(calculateTopLeftCellX() + calculateXOffsetForExit(), calculateTopLeftCellY() + calculateYOffsetForExit(), 5, Color.web("red", 0.5));
        root.getChildren().add(exitMarker);

        // Create button that allows you to generate a new maze
        Button btn = new Button();
        btn.setText("Generate Maze");
        btn.setOnAction(event -> {
            System.out.println("Button clicked!");

            removeBoxBlur(gc);
            // clear old maze
            gc.clearRect(0, 0,
                    canvas.getHeight(),
                    canvas.getWidth());
            root.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();

            root.getChildren().remove(exitMarker);
            setBoxBlur(gc);

            if (preGenComboBox.getValue().toString().equals(DEFAULT_PRE_GEN_MAZE_TYPE)) {
                if (!"".equals(dimensionsTextField.getText())) {
                    dimensions = Integer.valueOf(dimensionsTextField.getText());
//                        colX = Integer.valueOf(widthTextField.getText());
                    try {
                        maze = new Maze(dimensions, mazeGenComboBox.getValue().toString());
                    } catch (Throwable e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            } else if (preGenComboBox.getValue().toString().equals(MAZE_ONE)) {
                maze = new Maze(Maze.BT_MAZE_PROBLEM_ONE);
            } else if (preGenComboBox.getValue().toString().equals(MAZE_TWO)) {
                maze = new Maze(Maze.BT_MAZE_PROBLEM_TWO);
            } else if (preGenComboBox.getValue().toString().equals(MAZE_THREE)) {
                maze = new Maze(Maze.BT_MAZE_PROBLEM_THREE);
            } else if (preGenComboBox.getValue().toString().equals(MAZE_FOUR)) {
                maze = new Maze(Maze.BT_MAZE_PROBLEM_FOUR);
            } else if (preGenComboBox.getValue().toString().equals(MAZE_FIVE)) {
                maze = new Maze(Maze.BT_MAZE_PROBLEM_FIVE);
            } else if (preGenComboBox.getValue().toString().equals(MAZE_SIX)) {
                maze = new Maze(Maze.BRAID_MAZE_PROBLEM_ONE);
            } else if (preGenComboBox.getValue().toString().equals(MAZE_SEVEN)) {
                maze = new Maze(Maze.BRAID_MAZE_PROBLEM_TWO);
            } else if (preGenComboBox.getValue().toString().equals(MAZE_EIGHT)) {
                maze = new Maze(Maze.BRAID_MAZE_PROBLEM_THREE);
            }

            drawMaze(gc);
            root.getChildren().add(pathGroup);
        });


        Button solveMazeBtn = new Button();
        solveMazeBtn.setText("Solve");
        solveMazeBtn.setOnAction(event -> {
            System.out.println("Solving maze...");

            MazeSolver solver = new MazeSolver(maze.representation());
            List<Path> solution;
            try {
                solution = solver.solve();
                animateRoute(solution, entranceMarker, gc);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

        grid.add(btn, 0, 4);
        grid.add(solveMazeBtn, 3, 4);
        grid.add(canvas, 0, 5);
        root.getChildren().add(grid);
    }

    private ComboBox<String> addPreGeneratedMazeTypes() {

        ObservableList<String> preGeneratedMazes = FXCollections.observableArrayList(
                DEFAULT_PRE_GEN_MAZE_TYPE,
                MAZE_ONE, MAZE_TWO, MAZE_THREE, MAZE_FOUR, MAZE_FIVE,
                MAZE_SIX, MAZE_SEVEN, MAZE_EIGHT
        );

        final ComboBox<String> preGenComboBox = new ComboBox(preGeneratedMazes);
        preGenComboBox.setValue(DEFAULT_PRE_GEN_MAZE_TYPE);
        return preGenComboBox;
    }

    private ComboBox addMazeGeneratorComboBox() {
        ObservableList<String> mazeGenerationTypes = FXCollections.observableArrayList(Maze.DEFAULT_MAZE_TYPE, Maze.BRAID_MAZE_TYPE);

        final ComboBox mazeGenComboBox = new ComboBox(mazeGenerationTypes);
        mazeGenComboBox.setValue(Maze.DEFAULT_MAZE_TYPE);
        return mazeGenComboBox;
    }

    void drawMaze(GraphicsContext gc) {
        int[][] maze = this.maze.representation();
        colX = this.maze.getX();
        dimensions = this.maze.getY();

        int xPos, yPos = 20;

        for (int i = 0; i < colX; i++) {
            xPos = 20;
            // draw the north edge
            for (int j = 0; j < dimensions; j++) {
                if ((maze[j][i] & 1) == 0) {
                    gc.strokeLine(xPos, yPos, xPos + CELL_LENGTH, yPos); // horizontal
                }
                xPos += CELL_LENGTH + GAP;
            }

            xPos = 20;
            // draw the west edge
            for (int j = 0; j < dimensions; j++) {
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

        for (int j = 0; j < dimensions; j++) {
            gc.strokeLine(xPos, yPos, xPos + CELL_LENGTH, yPos); // horizontal
            xPos += CELL_LENGTH + GAP;
        }

        MazeViewer.display(this.maze);
    }

    private List<Circle> generatePath(List<Path> route, Circle entranceMarker) {
        double entranceX = entranceMarker.getCenterX(),
            entranceY = entranceMarker.getCenterY();
        List<Circle> path = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            // create next circle in the path with Opacity 0 so we can animate each circle
            Circle nextInPath = new Circle(entranceX + calculateNextPosX(route.get(i).x), entranceY + calculateNextPosY(route.get(i).y), 5, Color.web("green", 0.5));
            path.add(nextInPath);
        }

        return path;
    }

    void animateRoute(List<Path> path, Circle entranceMarker, GraphicsContext gc) {
        double entranceX = entranceMarker.getCenterX();
        double entranceY = entranceMarker.getCenterY();

        int currentKeyFrameTimeInMs = 0,
            keyframeTimeInMs = 100;
        Timeline timeline = new Timeline();

        List<KeyFrame> keyFrames = new ArrayList<>();
        List<AnimationTimer> timers = new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {
            Path pathEntry = path.get(i);
            DoubleProperty opacity = new SimpleDoubleProperty();
//            Circle pathEntry = path.get(i);
            // animate Opacity 0% to 100% for each circle
            keyFrames.add(new KeyFrame(
                    Duration.millis(currentKeyFrameTimeInMs),
                    new KeyValue(opacity, 0)));
            keyFrames.add(new KeyFrame(
                    Duration.millis(currentKeyFrameTimeInMs + keyframeTimeInMs),
                    new KeyValue(opacity, 1)));

            timeline.setAutoReverse(true);
            timeline.setCycleCount(1);

            currentKeyFrameTimeInMs += keyframeTimeInMs;

            timers.add(new AnimationTimer() {
                @Override
                public void handle(long now) {
                    gc.setFill(Color.FORESTGREEN.deriveColor(0, 1, 1, opacity.get()));
                    gc.fillOval(
                            entranceX + calculateNextPosX(pathEntry.x) - 18,
                            entranceY + calculateNextPosY(pathEntry.y) - 173,
                            8,
                            8
                    );
                }
            });
        }

        timeline.getKeyFrames().addAll(keyFrames);

        for (int i = 0; i < path.size(); i++) {
            timers.get(i).start();
        }
        timeline.play();
    }

    private int calculateNextPosX(int x) {
        int shiftBy = (x * (CELL_LENGTH + 5)) + GAP;
        return shiftBy;
    }

    private int calculateNextPosY(int y) {
        int shiftBy = (y * (CELL_LENGTH + 5)) + GAP;
        return shiftBy;
    }

    private int calculateTopLeftCellX() {
        return 40;
    }

    private int calculateTopLeftCellY() {
        return 195;
    }

    private int calculateXOffsetForExit() {
        return (dimensions - 1) * (CELL_LENGTH + GAP);
    }

    private int calculateYOffsetForExit() {
        return (dimensions - 1) * (CELL_LENGTH + GAP);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
