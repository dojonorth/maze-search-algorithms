# North Dojo: Amazing Algorithms
This dojo is specifically designed to make participants think about how you might approach algorithm design for solving different types of Mazes. Also it's intended to encourage participants to think about how they might improve upon the design of their algorithms so that their algorithm finds a quicker path.

# Setup Instructions

## Java

### Setup
To get started you'll need:

  * Install of IntelliJ 14 Community Edition ([Download IntelliJ](https://www.jetbrains.com/idea/download/))
  * Install latest version of Java 8 ([Download jdk8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html))

Verify that java is installed with the following command in the terminal:

```
➜  ~  java -version
java version "1.8.0_11"
Java(TM) SE Runtime Environment (build 1.8.0_11-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.11-b03, mixed mode)
```

If you need assistance please let us know during the dojo.

#### IntelliJ project

* Clone the repo into a folder of your choice.
* Open IntelliJ and click "IntelliJ menu -> Preferences..."
* Search for plugins and install the JavaFX plugin
* Once installed, create a new Project using "File -> New Project"
* Select the "JavaFX Application" project making sure you select the root folder of the maze-search-algorithms repository
* Once the project is opened right click the root folder in the project view and select "Open Module Settings..."
* Make sure in the module settings window you have the Java 8 SDK added and the Java 8 SDK selected in the modules menu
* Exit and save your module options
* Right-click the java/src/main folder in the Project view and select "Mark Directory As -> Sources Root"
* Right-click the java/src/test folder in the Project view and select "Mark Directory As -> Test Sources Root"

## User Interface

The user interface allows you to randomly generate

### Starting the UI

Right-click the MazeUI class in the Project view and select "Run MazeUI.main()"

You should see the UI displayed after a few seconds

## Javascript

### Setup

Clone the repo and cd into the js folder.

Run:

``` npm install ```

To launch the UI run :

``` npm start ```

### The UI

The UI may launch in the background.

In the UI, you can generate mazes using a back track algorithm. Alternatively, select precanned mazes which feature perfect mazes generated with the backtrack algorith and also imperfect braided mazes. The precanned mazes are there to allow you to compare your solution's efficiency with others.

The solve button is hooked up to an empty method in the solution.js file. This is where your maze solving solution should be implemented.

### Aim

Your algorithm should generate and return an array of arrays containing [x,y] coordinates for each cell visited. You can then visualise your algorithm by pressing the solve button in the UI. The UI will present you with a score for your solution for the current maze. The aim is to get the number as low as possible.

## Data Structure

### Direction Enum
If you take a look under the hood you'll notice that at the core of the data structure is a DIR enum e.g.

```
        public static DIR[] toArray(int availableDirections) {
            if (availableDirections == 1) {
                return new DIR[]{DIR.N};
            } else if (availableDirections == 2) {
                return new DIR[]{DIR.S};
            } else if (availableDirections == 3) {
                return new DIR[]{DIR.N, DIR.S};
            } else if (availableDirections == 4) {
                return new DIR[]{DIR.E};
            } else if (availableDirections == 5) {
                return new DIR[]{DIR.E, DIR.N};
            } else if (availableDirections == 6) {
                return new DIR[]{DIR.E, DIR.S};
            } else if (availableDirections == 7) {
                return new DIR[]{DIR.N, DIR.S, DIR.E};
            } else if (availableDirections == 8) {
                return new DIR[]{DIR.W};
            } else if (availableDirections == 9) {
                return new DIR[]{DIR.W, DIR.N};
            } else if (availableDirections == 10) {
                return new DIR[]{DIR.W, DIR.S};
            } else if (availableDirections == 11) {
                return new DIR[]{DIR.W, DIR.S, DIR.N};
            } else if (availableDirections == 12) {
                return new DIR[]{DIR.W, DIR.E};
            } else if (availableDirections == 13) {
                return new DIR[]{DIR.N, DIR.E, DIR.W};
            } else if (availableDirections == 14) {
                return new DIR[]{DIR.S, DIR.E, DIR.W};
            }
            return new DIR[0];  //To change body of created methods use File | Settings | File Templates.
        }

        public static DIR toTraversalDirection(int direction) throws Exception {
            if (direction == 1) { // only available is north
                return DIR.N;
            } else if (direction == 2) { // only available is west
                return DIR.S;
            } else if (direction == 4) { // only available is east
                return DIR.E;
            } else if (direction == 8) { // only available is south
                return DIR.W;
            }
            throw new Exception("Invalid current state: current[" + direction + "]");
        }
    }
```

The DIR enum essentially represents the Direction of traversal as a 4 bit integer i.e. 1 = North, 2 = West, 4 = East and 8 = South. This is done this way because it makes performing bit arithmetic operations on an object extremely quick. For example, the position of walls for a cell where that only has one exit is simply a bitwise NOT of that direction e.g. Bitwise Not of N == S, E, W.

Representing data by using bit representations is a classic technique for increasing the speed of applications because we keep the application footprint slow and we make arithmetic operations fast (since we're just shifting bits around in memory). If we were to use objects instead, for example, we'd have to look up the references to those objects in the in-memory cache, which would be significantly slower than shifting bits in place.

## Expected output
The expected output for your Solver should be to calculate the path, so for every traversal you make you'll need to store it in either a Path object (in the Java exercise) containing x and y coordinates or represent it as a json object with x and y values representing the position of the traversal in the maze.

Your solver should calculate a legitimate unbroken path taken from Entrance (top left) to Exit (bottom right).





